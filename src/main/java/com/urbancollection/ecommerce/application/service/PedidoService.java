package com.urbancollection.ecommerce.application.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.urbancollection.ecommerce.domain.base.OperationResult;
import com.urbancollection.ecommerce.domain.entity.catalogo.Cupon;
import com.urbancollection.ecommerce.domain.entity.catalogo.Producto;
import com.urbancollection.ecommerce.domain.entity.logistica.Direccion;
import com.urbancollection.ecommerce.domain.entity.usuarios.Usuario;
import com.urbancollection.ecommerce.domain.entity.ventas.ItemPedido;
import com.urbancollection.ecommerce.domain.entity.ventas.Pedido;
import com.urbancollection.ecommerce.domain.entity.ventas.TransaccionPago;
import com.urbancollection.ecommerce.domain.enums.EstadoDePedido;
import com.urbancollection.ecommerce.domain.enums.MetodoDePago;
import com.urbancollection.ecommerce.domain.repository.DireccionRepository;
import com.urbancollection.ecommerce.domain.repository.TransaccionPagoRepository;
import com.urbancollection.ecommerce.domain.service.CuponPolicy;
import com.urbancollection.ecommerce.domain.service.CuponPolicyResolver;
import com.urbancollection.ecommerce.domain.service.StockService;
import com.urbancollection.ecommerce.infrastructure.client.ICuponApiClient;
import com.urbancollection.ecommerce.infrastructure.client.IPedidoApiClient;
import com.urbancollection.ecommerce.infrastructure.client.IProductoApiClient;
import com.urbancollection.ecommerce.infrastructure.client.IUsuarioApiClient;
import com.urbancollection.ecommerce.shared.BaseService;
import com.urbancollection.ecommerce.shared.ValidationUtil;

import jakarta.transaction.Transactional;


public class PedidoService extends BaseService implements IPedidoService {

    // Cliente para obtener/consultar usuarios a través de la API REST
    private final IUsuarioApiClient usuarioApiClient;
    // Repositorio de direcciones 
    private final DireccionRepository direccionRepository;
    // Cliente para obtener productos por API
    private final IProductoApiClient productoApiClient;
    // Cliente para crear/consultar/actualizar pedidos por API
    private final IPedidoApiClient pedidoApiClient;
    // Cliente para consultar cupones por API
    private final ICuponApiClient cuponApiClient;
    // Repositorio para registrar transacciones de pago
    private final TransaccionPagoRepository transaccionPagoRepository;
    // Servicio de dominio que maneja validación y descuento de stock
    private final StockService stockService;

    // Constructor donde se inyectan todas las dependencias necesarias
    public PedidoService(IUsuarioApiClient usuarioApiClient,
                         DireccionRepository direccionRepository,
                         IProductoApiClient productoApiClient,
                         IPedidoApiClient pedidoApiClient,
                         ICuponApiClient cuponApiClient,
                         TransaccionPagoRepository transaccionPagoRepository,
                         StockService stockService) {

        this.usuarioApiClient = usuarioApiClient;
        this.direccionRepository = direccionRepository;
        this.productoApiClient = productoApiClient;
        this.pedidoApiClient = pedidoApiClient;
        this.cuponApiClient = cuponApiClient;
        this.transaccionPagoRepository = transaccionPagoRepository;
        this.stockService = stockService;
    }

    // ===================== CREAR PEDIDO =====================

    @Override
    @Transactional
    public OperationResult crearPedido(Long usuarioId,
                                       Long direccionId,
                                       List<ItemPedido> items,
                                       Long cuponId) {
        try {
            // Busca el usuario por API; si no existe, se corta el flujo
            Usuario usuario = usuarioApiClient.buscarPorId(usuarioId).orElse(null);
            if (usuario == null) return OperationResult.failure("Usuario no encontrado");

            // Busca la dirección de entrega; si no existe, se devuelve error
            Direccion direccion = direccionRepository.findById(direccionId);
            if (direccion == null) return OperationResult.failure("Dirección no encontrada");

            // Valida que el pedido tenga al menos un ítem
            if (items == null || items.isEmpty()) {
                return OperationResult.failure("El pedido debe contener al menos un item");
            }

            // Crea la entidad Pedido y setea usuario, dirección y estado inicial
            Pedido pedido = new Pedido();
            pedido.setUsuario(usuario);
            pedido.setDireccionEntrega(direccion);
            pedido.setEstado(EstadoDePedido.PENDIENTE_PAGO);

            // Recorre cada item recibido para validarlo y asociar el producto real
            for (ItemPedido item : items) {
                if (item == null) return OperationResult.failure("Item inválido");

                // Cada item debe tener cantidad mayor a 0
                if (item.getCantidad() <= 0) {
                    return OperationResult.failure("Cada item debe tener cantidad > 0");
                }

                // El item debe tener un producto con id válido
                if (item.getProducto() == null || item.getProducto().getId() == null) {
                    return OperationResult.failure("Item sin producto válido");
                }

                // Se busca el producto desde la API usando su id
                Producto p = productoApiClient.buscarPorId(item.getProducto().getId()).orElse(null);
                if (p == null) return OperationResult.failure("Producto no encontrado");

                // Se asigna el producto real al item
                item.setProducto(p);

                // Se agrega el item al pedido usando la lógica de dominio
                pedido.agregarItem(item);
            }

            // Valida el stock disponible antes de continuar
            try {
                stockService.validarDisponibilidad(pedido.getItems());
            } catch (IllegalStateException e) {
                // Si el servicio de stock detecta problema, se devuelve ese mensaje
                return OperationResult.failure(e.getMessage());
            }

            // Acumulador para el descuento calculado por el cupón
            BigDecimal descuento = BigDecimal.ZERO;

            // Si viene un cuponId se intenta aplicar el cupón
            if (cuponId != null) {
                // Busca el cupón por API
                Cupon cupon = cuponApiClient.buscarPorId(cuponId).orElse(null);
                if (cupon == null) return OperationResult.failure("Cupón no encontrado");
                if (!cupon.isActivo()) return OperationResult.failure("Cupón inactivo");

                // Verifica fechas de vigencia del cupón
                var ahora = java.time.LocalDateTime.now();

                if (cupon.getFechaInicio() != null && ahora.isBefore(cupon.getFechaInicio()))
                    return OperationResult.failure("Cupón aún no está vigente");

                if (cupon.getFechaFin() != null && ahora.isAfter(cupon.getFechaFin()))
                    return OperationResult.failure("Cupón expirado");

                // Calcula totales sin descuento para validar mínimo de compra
                Pedido.Totales previos = pedido.calcularTotales(BigDecimal.ZERO, BigDecimal.ZERO);

                if (cupon.getMinimoCompra() != null &&
                        previos.getSubtotal().compareTo(cupon.getMinimoCompra()) < 0) {
                    return OperationResult.failure("No alcanza el mínimo del cupón");
                }

                // Resuelve la política por porcentaje, monto fijo.
                CuponPolicy policy = CuponPolicyResolver.from(cupon);

                // Aplica el cupón al pedido y obtiene el descuento calculado
                descuento = pedido.aplicarCupon(policy);

                // Respeta el tope máximo de descuento si está definido
                if (cupon.getTopeDescuento() != null &&
                        descuento.compareTo(cupon.getTopeDescuento()) > 0) {
                    descuento = cupon.getTopeDescuento();
                }
                
                // Asocia el id del cupón al pedido
                pedido.setCuponId(cupon.getId().intValue());
            }

            // Calcula los totales finales del pedido usando el descuento
            pedido.calcularTotales(descuento, BigDecimal.ZERO);

            // Valida el pedido con Bean Validation 
            OperationResult val = ValidationUtil.validate(pedido);
            if (!val.isSuccess()) return val;

            // Envía el pedido a la API para que sea persistido
            pedidoApiClient.crear(pedido);

            // Si todo fue bien, se devuelve éxito
            return OperationResult.success("Pedido creado correctamente");

        } catch (Exception e) {
            // Cualquier error inesperado se captura aquí
            return OperationResult.failure("No se pudo crear el pedido");
        }
    }

    // ===================== CONFIRMAR PAGO =====================

    @Override
    @Transactional
    public OperationResult confirmarPago(Long pedidoId, MetodoDePago metodo, BigDecimal monto) {
        try {
            // Busca el pedido por su id a través de la API
            Pedido pedido = pedidoApiClient.buscarPorId(pedidoId).orElse(null);
            if (pedido == null) {
                return OperationResult.failure("Pedido no encontrado");
            }

            // Vuelve a validar disponibilidad de stock antes de confirmar el pago
            stockService.validarDisponibilidad(pedido.getItems());

            // Lógica de dominio: el propio pedido valida y cambia su estado según el pago
            pedido.pagar(monto, metodo);

            // Descuenta definitivamente el stock de los productos del pedido
            stockService.descontar(pedido.getItems());

            // Crea el registro de la transacción de pago
            TransaccionPago t = new TransaccionPago();
            t.setPedido(pedido);
            t.setMonto(monto);
            t.setMetodo(metodo);
            t.setFecha(java.time.LocalDateTime.now());

            // Guarda la transacción en la base de datos
            transaccionPagoRepository.save(t);

            // Actualiza el pedido ) vía API
            pedidoApiClient.actualizar(pedidoId, pedido);

            return OperationResult.success("Pago confirmado correctamente");

        } catch (IllegalArgumentException | IllegalStateException ex) {
            // Errores de negocio (parámetros incorrectos, estado inválido, etc.)
            return OperationResult.failure(ex.getMessage());
        } catch (Exception ex) {
            // Cualquier error inesperado
            return OperationResult.failure("Error inesperado al confirmar el pago");
        }
    }

    // =====================  MARCAR COMO PAGADO =====================

    @Override
    @Transactional
    public OperationResult marcarComoPagado(Long pedidoId) {
        try {
            // Recupera el pedido vía API
            Pedido pedido = pedidoApiClient.buscarPorId(pedidoId).orElse(null);
            if (pedido == null) {
                return OperationResult.failure("Pedido no encontrado");
            }

            // Solo se puede marcar como pagado si está en PENDIENTE_PAGO
            if (pedido.getEstado() != EstadoDePedido.PENDIENTE_PAGO) {
                return OperationResult.failure("Solo se pueden marcar como pagado pedidos en estado PENDIENTE_PAGO");
            }

            // Vuelve a validar stock disponible
            try {
                stockService.validarDisponibilidad(pedido.getItems());
            } catch (IllegalStateException e) {
                return OperationResult.failure(e.getMessage());
            }

            // Descuenta stock de forma definitiva
            stockService.descontar(pedido.getItems());

            // Cambia el estado del pedido a PAGADO
            pedido.setEstado(EstadoDePedido.PAGADO);
            // Persiste el cambio a través de la API
            pedidoApiClient.actualizar(pedidoId, pedido);

            return OperationResult.success("Pedido marcado como PAGADO y stock descontado");

        } catch (Exception ex) {
            // Error genérico al intentar marcar como pagado
            return OperationResult.failure("Error al marcar el pedido como pagado");
        }
    }

    // ===================== LISTAR / DETALLE =====================

    @Override
    public List<Pedido> listarTodos() {
        // Devuelve la lista de pedidos obtenida desde la API
        return pedidoApiClient.listar();
    }

    @Override
    public Pedido obtenerPorId(Long id) {
        // Intenta recuperar el pedido por id; si no existe, devuelve null
        return pedidoApiClient.buscarPorId(id).orElse(null);
    }


    @Override
    public List<Pedido> listarPedidosDisponiblesParaEnvio() {
        // Lista todos los pedidos y filtra solo:
        // - los que están PAGADOS
        // - los que no tienen envíos asociados
        return pedidoApiClient.listar().stream()
                .filter(p -> p.getEstado() == EstadoDePedido.PAGADO)
                .filter(p -> p.getEnvios() == null || p.getEnvios().isEmpty())
                .collect(Collectors.toList());
    }

    // ===================== DESPACHAR / ENTREGAR =====================

    @Override
    @Transactional
    public OperationResult despacharPedido(Long pedidoId, String tracking) {
        try {
            // Recupera el pedido a despachar
            Pedido pedido = pedidoApiClient.buscarPorId(pedidoId).orElse(null);
            if (pedido == null) {
                return OperationResult.failure("Pedido no encontrado");
            }

            // Usa la lógica de dominio para validar y marcar el pedido como despachado
            pedido.despachar(tracking);

            // Actualiza el pedido vía API con el nuevo estado y tracking
            pedidoApiClient.actualizar(pedidoId, pedido);

            return OperationResult.success("Pedido despachado correctamente");

        } catch (IllegalArgumentException | IllegalStateException ex) {
            // Errores de negocio (tracking inválido, estado inadecuado, etc.)
            return OperationResult.failure(ex.getMessage());
        } catch (Exception ex) {
            // Error inesperado al despachar
            return OperationResult.failure("No se pudo despachar el pedido");
        }
    }

    @Override
    @Transactional
    public OperationResult marcarEntregado(Long pedidoId) {
        try {
            // Recupera el pedido que se va a marcar como entregado
            Pedido pedido = pedidoApiClient.buscarPorId(pedidoId).orElse(null);
            if (pedido == null) {
                return OperationResult.failure("Pedido no encontrado");
            }

            // Lógica de dominio: pasa el pedido a COMPLETADO si el estado lo permite
            pedido.completar();

            // Persiste el cambio de estado a través de la API
            pedidoApiClient.actualizar(pedidoId, pedido);

            return OperationResult.success("Pedido completado correctamente");

        } catch (IllegalArgumentException | IllegalStateException ex) {
            // Errores de negocio al intentar completar el pedido
            return OperationResult.failure(ex.getMessage());
        } catch (Exception ex) {
            // Error inesperado al completar
            return OperationResult.failure("No se pudo completar el pedido");
        }
    }

    @Override
    public OperationResult eliminar(Long id) {
        try {
            // Verifica primero si el pedido existe a través del ApiClient
            Optional<Pedido> pedidoOpt = pedidoApiClient.buscarPorId(id);
            
            if (pedidoOpt.isEmpty()) {
                return OperationResult.failure("Pedido no encontrado");
            }

            // Si existe, delega al ApiClient la eliminación
            pedidoApiClient.eliminar(id);
            
            return OperationResult.success("Pedido eliminado correctamente");
            
        } catch (Exception ex) {
            // En caso de error, devuelve mensaje con el detalle
            return OperationResult.failure("No se pudo eliminar el pedido: " + ex.getMessage());
        }
    }

}
