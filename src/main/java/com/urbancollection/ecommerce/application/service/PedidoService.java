package com.urbancollection.ecommerce.application.service;

import java.math.BigDecimal;
import java.util.List;

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
import com.urbancollection.ecommerce.domain.repository.CuponRepository;
import com.urbancollection.ecommerce.domain.repository.DireccionRepository;
import com.urbancollection.ecommerce.domain.repository.EnvioRepository;
import com.urbancollection.ecommerce.domain.repository.ItemPedidoRepository;
import com.urbancollection.ecommerce.domain.repository.PedidoRepository;
import com.urbancollection.ecommerce.domain.repository.ProductoRepository;
import com.urbancollection.ecommerce.domain.repository.TransaccionPagoRepository;
import com.urbancollection.ecommerce.domain.repository.UsuarioRepository;
import com.urbancollection.ecommerce.domain.service.CuponPolicy;
import com.urbancollection.ecommerce.domain.service.CuponPolicyResolver;
import com.urbancollection.ecommerce.domain.service.StockService;
import com.urbancollection.ecommerce.shared.BaseService;
import com.urbancollection.ecommerce.shared.ValidationUtil;

import jakarta.transaction.Transactional;

public class PedidoService extends BaseService implements IPedidoService {

    private final UsuarioRepository usuarioRepository;
    private final DireccionRepository direccionRepository;
    private final ProductoRepository productoRepository;
    private final PedidoRepository pedidoRepository;
    private final ItemPedidoRepository itemPedidoRepository;
    private final CuponRepository cuponRepository;
    private final TransaccionPagoRepository transaccionPagoRepository;
    private final EnvioRepository envioRepository;
    private final StockService stockService;

    public PedidoService(UsuarioRepository usuarioRepository,
                         DireccionRepository direccionRepository,
                         ProductoRepository productoRepository,
                         PedidoRepository pedidoRepository,
                         ItemPedidoRepository itemPedidoRepository,
                         CuponRepository cuponRepository,
                         TransaccionPagoRepository transaccionPagoRepository,
                         EnvioRepository envioRepository,
                         StockService stockService) {

        this.usuarioRepository = usuarioRepository;
        this.direccionRepository = direccionRepository;
        this.productoRepository = productoRepository;
        this.pedidoRepository = pedidoRepository;
        this.itemPedidoRepository = itemPedidoRepository;
        this.cuponRepository = cuponRepository;
        this.transaccionPagoRepository = transaccionPagoRepository;
        this.envioRepository = envioRepository;
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
            Usuario usuario = usuarioRepository.findById(usuarioId);
            if (usuario == null) return OperationResult.failure("Usuario no encontrado");

            Direccion direccion = direccionRepository.findById(direccionId);
            if (direccion == null) return OperationResult.failure("Dirección no encontrada");

            if (items == null || items.isEmpty()) {
                return OperationResult.failure("El pedido debe contener al menos un item");
            }

            Pedido pedido = new Pedido();
            pedido.setUsuario(usuario);
            pedido.setDireccionEntrega(direccion);
            pedido.setEstado(EstadoDePedido.PENDIENTE_PAGO);

            for (ItemPedido item : items) {
                if (item == null) return OperationResult.failure("Item inválido");

                if (item.getCantidad() <= 0) {
                    return OperationResult.failure("Cada item debe tener cantidad > 0");
                }

                if (item.getProducto() == null || item.getProducto().getId() == null) {
                    return OperationResult.failure("Item sin producto válido");
                }

                Producto p = productoRepository.findById(item.getProducto().getId());
                if (p == null) return OperationResult.failure("Producto no encontrado");

                item.setProducto(p);

                pedido.agregarItem(item);
            }

            BigDecimal descuento = BigDecimal.ZERO;

            if (cuponId != null) {
                Cupon cupon = cuponRepository.findById(cuponId);
                if (cupon == null) return OperationResult.failure("Cupón no encontrado");
                if (!cupon.isActivo()) return OperationResult.failure("Cupón inactivo");

                var ahora = java.time.LocalDateTime.now();

                if (cupon.getFechaInicio() != null && ahora.isBefore(cupon.getFechaInicio()))
                    return OperationResult.failure("Cupón aún no está vigente");

                if (cupon.getFechaFin() != null && ahora.isAfter(cupon.getFechaFin()))
                    return OperationResult.failure("Cupón expirado");

                Pedido.Totales previos = pedido.calcularTotales(BigDecimal.ZERO, BigDecimal.ZERO);

                if (cupon.getMinimoCompra() != null &&
                        previos.getSubtotal().compareTo(cupon.getMinimoCompra()) < 0) {
                    return OperationResult.failure("No alcanza el mínimo del cupón");
                }

                CuponPolicy policy = CuponPolicyResolver.from(cupon);

                descuento = pedido.aplicarCupon(policy);

                if (cupon.getTopeDescuento() != null &&
                        descuento.compareTo(cupon.getTopeDescuento()) > 0) {
                    descuento = cupon.getTopeDescuento();
                }
            }

            pedido.calcularTotales(descuento, BigDecimal.ZERO);

            OperationResult val = ValidationUtil.validate(pedido);
            if (!val.isSuccess()) return val;

            pedidoRepository.save(pedido);

            return OperationResult.success("Pedido creado correctamente");

        } catch (Exception e) {
            return OperationResult.failure("No se pudo crear el pedido");
        }
    }

    // ===================== CONFIRMAR PAGO =====================

    @Override
    @Transactional
    public OperationResult confirmarPago(Long pedidoId, MetodoDePago metodo, BigDecimal monto) {
        try {
            // 1) Cargar pedido
            Pedido pedido = pedidoRepository.findById(pedidoId);
            if (pedido == null) {
                return OperationResult.failure("Pedido no encontrado");
            }

            // 2) Validar stock
            stockService.validarDisponibilidad(pedido.getItems());

            // 3) Cambiar estado a PAGADO (dominio valida monto/estado)
            pedido.pagar(monto, metodo);

            // 4) Descontar stock
            stockService.descontar(pedido.getItems());

            // 5) Registrar transacción
            TransaccionPago t = new TransaccionPago();
            t.setPedido(pedido);
            t.setMonto(monto);
            t.setMetodo(metodo);
            t.setFecha(java.time.LocalDateTime.now());

            transaccionPagoRepository.save(t);

            // 6) Guardar pedido
            pedidoRepository.save(pedido);

            return OperationResult.success("Pago confirmado correctamente");

        } catch (IllegalArgumentException | IllegalStateException ex) {
            return OperationResult.failure(ex.getMessage());
        } catch (Exception ex) {
            return OperationResult.failure("Error inesperado al confirmar el pago");
        }
    }

    // ===================== LISTAR / DETALLE =====================

    @Override
    public List<Pedido> listarTodos() {
        return pedidoRepository.findAll();
    }

    @Override
    public Pedido obtenerPorId(Long id) {
        // Si no existe, devuelve null; el controller se encarga del 404 JSON
        return pedidoRepository.findById(id);
    }

    // ===================== DESPACHAR / ENTREGAR =====================

    @Override
    @Transactional
    public OperationResult despacharPedido(Long pedidoId, String tracking) {
        try {
            // 1) Cargar pedido
            Pedido pedido = pedidoRepository.findById(pedidoId);
            if (pedido == null) {
                return OperationResult.failure("Pedido no encontrado");
            }

            // 2) Llamar la lógica de dominio
            pedido.despachar(tracking);

            // 3) Guardar
            pedidoRepository.save(pedido);

            return OperationResult.success("Pedido despachado correctamente");

        } catch (IllegalArgumentException | IllegalStateException ex) {
            return OperationResult.failure(ex.getMessage());
        } catch (Exception ex) {
            return OperationResult.failure("No se pudo despachar el pedido");
        }
    }

    @Override
    @Transactional
    public OperationResult marcarEntregado(Long pedidoId) {
        try {
            // 1) Cargar pedido
            Pedido pedido = pedidoRepository.findById(pedidoId);
            if (pedido == null) {
                return OperationResult.failure("Pedido no encontrado");
            }

            // 2) Ejecutar lógica de dominio
            pedido.completar();

            // 3) Guardar
            pedidoRepository.save(pedido);

            return OperationResult.success("Pedido completado correctamente");

        } catch (IllegalArgumentException | IllegalStateException ex) {
            return OperationResult.failure(ex.getMessage());
        } catch (Exception ex) {
            return OperationResult.failure("No se pudo completar el pedido");
        }
    }

}
