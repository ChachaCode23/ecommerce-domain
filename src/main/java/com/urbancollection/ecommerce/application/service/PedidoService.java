package com.urbancollection.ecommerce.application.service;

import com.urbancollection.ecommerce.shared.BaseService;
import com.urbancollection.ecommerce.shared.ValidationUtil;
import com.urbancollection.ecommerce.domain.base.OperationResult;

import com.urbancollection.ecommerce.domain.entity.catalogo.Producto;
import com.urbancollection.ecommerce.domain.entity.catalogo.Cupon;
import com.urbancollection.ecommerce.domain.entity.usuarios.Usuario;
import com.urbancollection.ecommerce.domain.entity.logistica.Direccion;
import com.urbancollection.ecommerce.domain.entity.logistica.Envio;
import com.urbancollection.ecommerce.domain.entity.ventas.ItemPedido;
import com.urbancollection.ecommerce.domain.entity.ventas.Pedido;
import com.urbancollection.ecommerce.domain.entity.ventas.TransaccionPago;

import com.urbancollection.ecommerce.domain.enums.EstadoDeEnvio;
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

import com.urbancollection.ecommerce.shared.notification.NotificationPort;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * PedidoService
 *
 * Servicio principal de la lógica de pedidos.
 * Aquí controlo todo el ciclo de vida del pedido:
 *  - creación del pedido
 *  - confirmación de pago
 *  - despacho (envío con tracking)
 *  - marcar como entregado
 *
 * También manejo reglas importantes:
 *  - validar usuario y dirección
 *  - validar stock y restarlo al pagar
 *  - aplicar cupón y calcular total
 *  - respetar los estados permitidos del pedido
 *  - registrar transacción de pago
 *  - mandar notificación al usuario
 */
public class PedidoService extends BaseService implements IPedidoService {

    private static final Logger logger = LoggerFactory.getLogger(PedidoService.class);

    private final UsuarioRepository usuarioRepository;
    private final DireccionRepository direccionRepository;
    private final ProductoRepository productoRepository;
    private final PedidoRepository pedidoRepository;
    private final CuponRepository cuponRepository;
    private final TransaccionPagoRepository transaccionPagoRepository;
    private final EnvioRepository envioRepository;
    private final NotificationPort notification;

    // ItemPedidoRepository se inyecta aunque ya no se guarde como campo,
    // por compatibilidad con la construcción actual. Confiamos en cascade
    // para guardar los ItemPedido cuando se guarda el Pedido.
    public PedidoService(UsuarioRepository usuarioRepository,
                         DireccionRepository direccionRepository,
                         ProductoRepository productoRepository,
                         PedidoRepository pedidoRepository,
                         ItemPedidoRepository itemPedidoRepository, // compat
                         CuponRepository cuponRepository,
                         TransaccionPagoRepository transaccionPagoRepository,
                         EnvioRepository envioRepository,
                         NotificationPort notification) {

        this.usuarioRepository = usuarioRepository;
        this.direccionRepository = direccionRepository;
        this.productoRepository = productoRepository;
        this.pedidoRepository = pedidoRepository;
        this.cuponRepository = cuponRepository;
        this.transaccionPagoRepository = transaccionPagoRepository;
        this.envioRepository = envioRepository;
        this.notification = notification;
    }

    // ===================== CONSULTAS =====================

    @Override
    public Pedido obtenerPorId(Long id) {
        return pedidoRepository.findById(id);
    }

    @Override
    public List<Pedido> listarTodos() {
        return pedidoRepository.findAll();
    }

    // ===================== CREAR PEDIDO =====================

    /**
     * crearPedido:
     * - Valida usuario, dirección y los items.
     * - Calcula el total.
     * - Aplica cupón si corresponde.
     * - Construye el Pedido con estado PENDIENTE_PAGO.
     * - Guarda el pedido y sus items (por cascade).
     */
    @Transactional
    public OperationResult crearPedido(Long usuarioId,
                                       Long direccionId,
                                       List<ItemPedido> items,
                                       Long cuponId) {
        try {
            // 1. validar usuario
            Usuario usuario = usuarioRepository.findById(usuarioId);
            if (usuario == null) return OperationResult.failure("Usuario no encontrado");

            // 2. validar direccion
            Direccion direccion = direccionRepository.findById(direccionId);
            if (direccion == null) return OperationResult.failure("Direccion no encontrada");

            // 3. validar items y calcular total
            if (items == null || items.isEmpty()) {
                return OperationResult.failure("El pedido debe contener al menos un item");
            }

            BigDecimal total = BigDecimal.ZERO;

            for (ItemPedido item : items) {
                if (item == null) return OperationResult.failure("Item invalido en el pedido");

                if (item.getCantidad() <= 0) {
                    return OperationResult.failure("Cada item debe tener cantidad > 0");
                }

                // validar producto asociado
                Producto producto = item.getProducto();
                if (producto == null)
                    return OperationResult.failure("El item no tiene producto asociado");

                Producto prodPersistido = productoRepository.findById(producto.getId());
                if (prodPersistido == null)
                    return OperationResult.failure("Producto no encontrado en item");

                // validar stock disponible
                if (prodPersistido.getStock() < item.getCantidad()) {
                    return OperationResult.failure("Stock insuficiente para el producto: " + prodPersistido.getNombre());
                }

                // usar el precio actual del producto
                BigDecimal precioUnitario = prodPersistido.getPrecio();
                item.setPrecioUnitario(precioUnitario);

                BigDecimal subtotalCalc = precioUnitario
                        .multiply(BigDecimal.valueOf(item.getCantidad()));

                total = total.add(subtotalCalc);

                // validación a nivel de entidad (por anotaciones)
                OperationResult iv = ValidationUtil.validate(item);
                if (!iv.isSuccess()) return iv;
            }

            // 4. aplicar cupón si viene cuponId
            BigDecimal totalConDescuento = total;
            if (cuponId != null) {
                Cupon cupon = cuponRepository.findById(cuponId);
                if (cupon == null) return OperationResult.failure("Cupon no encontrado");
                if (!cupon.isActivo()) return OperationResult.failure("Cupon inactivo");

                var ahora = java.time.LocalDateTime.now();

                if (cupon.getFechaInicio() != null && ahora.isBefore(cupon.getFechaInicio()))
                    return OperationResult.failure("Cupon aun no esta vigente");

                if (cupon.getFechaFin() != null && ahora.isAfter(cupon.getFechaFin()))
                    return OperationResult.failure("Cupon expirado");

                if (cupon.getMinimoCompra() != null && total.compareTo(cupon.getMinimoCompra()) < 0)
                    return OperationResult.failure("El total no alcanza el minimo para usar el cupon");

                // calculamos el descuento según el tipo del cupón
                BigDecimal descuento = BigDecimal.ZERO;
                switch (cupon.getTipo()) {
                    case PORCENTAJE ->
                            descuento = total.multiply(cupon.getValorDescuento()).divide(new BigDecimal("100"));
                    case MONTO_FIJO ->
                            descuento = cupon.getValorDescuento();
                }

                // respetar tope de descuento
                if (cupon.getTopeDescuento() != null && descuento.compareTo(cupon.getTopeDescuento()) > 0)
                    descuento = cupon.getTopeDescuento();

                totalConDescuento = total.subtract(descuento);
                if (totalConDescuento.compareTo(BigDecimal.ZERO) < 0) {
                    totalConDescuento = BigDecimal.ZERO;
                }
            }

            // 5. armar el Pedido en memoria
            Pedido pedido = new Pedido();
            pedido.setUsuario(usuario);
            pedido.setDireccionEntrega(direccion);
            pedido.setEstado(EstadoDePedido.PENDIENTE_PAGO); // estado inicial
            pedido.setTotal(totalConDescuento);

            // 6. vincular items con el pedido
            List<ItemPedido> listaItems = new ArrayList<>();
            for (ItemPedido item : items) {
                item.setPedido(pedido); // importante para que Hibernate guarde el FK pedido_id
                listaItems.add(item);
            }
            pedido.setItems(listaItems);

            // 7. validar el pedido completo
            OperationResult pv = ValidationUtil.validate(pedido);
            if (!pv.isSuccess()) return pv;

            // 8. guardar el pedido (y sus items por cascade)
            pedido = pedidoRepository.save(pedido);

            logger.info("Pedido creado id={} total={}", pedido.getId(), pedido.getTotal());
            return OperationResult.success("Pedido creado correctamente");

        } catch (Exception e) {
            System.err.println("Error al crear pedido: " + e.getMessage());
            e.printStackTrace();
            return OperationResult.failure("No se pudo crear el pedido");
        }
    }

    // ===================== CONFIRMAR PAGO =====================

    /**
     * confirmarPago:
     * - Valida que el pedido exista y esté en estado PENDIENTE_PAGO.
     * - Valida el monto pagado.
     * - Descuenta el stock real de cada producto.
     * - Registra la transacción de pago (APROBADO).
     * - Cambia el pedido a estado PAGADO.
     * - Notifica al usuario.
     */
    @Transactional
    @Override
    public OperationResult confirmarPago(Long pedidoId, MetodoDePago metodo, BigDecimal monto) {
        try {
            // 1. buscar pedido
            Pedido pedido = pedidoRepository.findById(pedidoId);
            if (pedido == null) return OperationResult.failure("Pedido no encontrado");

            // 2. validar estado del pedido
            if (pedido.getEstado() != EstadoDePedido.PENDIENTE_PAGO)
                return OperationResult.failure("Solo se puede pagar un pedido en estado PENDIENTE_PAGO");

            // 3. validar monto
            if (monto == null || monto.compareTo(BigDecimal.ZERO) <= 0)
                return OperationResult.failure("Monto de pago invalido");

            if (pedido.getTotal() == null || monto.compareTo(pedido.getTotal()) != 0)
                return OperationResult.failure("El monto debe ser igual al total del pedido");

            // 4. validar/bajar stock
            List<ItemPedido> itemsDelPedido = pedido.getItems();
            if (itemsDelPedido == null || itemsDelPedido.isEmpty())
                return OperationResult.failure("El pedido no tiene items para procesar el pago");

            for (ItemPedido item : itemsDelPedido) {
                if (item == null || item.getProducto() == null)
                    return OperationResult.failure("Item invalido o sin producto");

                Producto prod = productoRepository.findById(item.getProducto().getId());
                if (prod == null) return OperationResult.failure("Producto no encontrado en item");

                int nuevoStock = prod.getStock() - item.getCantidad();
                if (nuevoStock < 0)
                    return OperationResult.failure("Stock insuficiente para: " + prod.getNombre());

                prod.setStock(nuevoStock);
                productoRepository.save(prod);
            }

            // 5. crear registro de pago
            TransaccionPago tx = new TransaccionPago();
            tx.setPedido(pedido);
            tx.setMetodoDePago(metodo);
            tx.setMonto(monto);
            tx.setEstado("APROBADO"); // valor permitido por la BD
            tx.setReferencia(null);   // opcional

            OperationResult tv = ValidationUtil.validate(tx);
            if (!tv.isSuccess()) return tv;

            transaccionPagoRepository.save(tx);

            // 6. actualizar estado del pedido a PAGADO
            pedido.setEstado(EstadoDePedido.PAGADO);
            pedidoRepository.save(pedido);

            logger.info("Pago confirmado. Pedido={} Total={} Metodo={}", pedido.getId(), monto, metodo);

            // 7. notificación al usuario (best effort)
            try {
                String correo = (pedido.getUsuario() != null) ? pedido.getUsuario().getCorreo() : null;
                if (correo != null && !correo.isBlank()) {
                    notification.sendInfo(
                            correo,
                            "Tu pago fue confirmado. Pedido #" + pedido.getId()
                    );
                } else {
                    logger.warn("No se pudo enviar notificación: pedido {} sin correo de usuario", pedido.getId());
                }
            } catch (Exception ex) {
                logger.warn("Notificación falló para pedido {}: {}", pedido.getId(), ex.getMessage());
            }

            return OperationResult.success("Pago confirmado y stock actualizado");

        } catch (Exception e) {
            System.err.println("Error al confirmarPago: " + e.getMessage());
            e.printStackTrace();
            return OperationResult.failure("No se pudo confirmar el pago");
        }
    }

    // ===================== DESPACHAR PEDIDO =====================

    /**
     * despacharPedido:
     * - Solo se puede despachar un pedido que ya está PAGADO.
     * - Creo un registro Envio con tracking y estado EN_CAMINO.
     * - Cambio el pedido a estado ENVIADO.
     * - Evito despachar dos veces el mismo pedido (valido si ya tiene Envio).
     */
    @Transactional
    public OperationResult despacharPedido(Long pedidoId, String tracking) {
        try {
            Pedido pedido = pedidoRepository.findById(pedidoId);
            if (pedido == null) return OperationResult.failure("Pedido no encontrado");

            if (pedido.getEstado() != EstadoDePedido.PAGADO)
                return OperationResult.failure("Solo se puede despachar un pedido PAGADO");

            // validar que no lo hayamos despachado ya
            boolean yaDespachado = envioRepository.findAll().stream()
                    .anyMatch(e -> e.getPedido() != null && e.getPedido().getId().equals(pedidoId));
            if (yaDespachado)
                return OperationResult.failure("El pedido ya tiene un envio registrado");

            // crear Envio
            Envio envio = new Envio();
            envio.setPedido(pedido);
            envio.setTracking(tracking);
            envio.setEstado(EstadoDeEnvio.EN_CAMINO);

            OperationResult ev = ValidationUtil.validate(envio);
            if (!ev.isSuccess()) return ev;

            envioRepository.save(envio);

            // actualizar estado del pedido
            pedido.setEstado(EstadoDePedido.ENVIADO);
            pedidoRepository.save(pedido);

            logger.info("Pedido despachado id={} tracking={}", pedidoId, tracking);
            return OperationResult.success("Pedido despachado");
        } catch (Exception e) {
            System.err.println("Error al despachar pedido: " + e.getMessage());
            e.printStackTrace();
            return OperationResult.failure("No se pudo despachar el pedido");
        }
    }

    // ===================== MARCAR ENTREGADO =====================

    /**
     * marcarEntregado:
     * - Solo se puede completar un pedido que ya fue ENVIADO.
     * - Cambia el estado del pedido a COMPLETADO (estado final).
     */
    @Transactional
    public OperationResult marcarEntregado(Long pedidoId) {
        try {
            Pedido pedido = pedidoRepository.findById(pedidoId);
            if (pedido == null) return OperationResult.failure("Pedido no encontrado");

            if (pedido.getEstado() != EstadoDePedido.ENVIADO)
                return OperationResult.failure("Solo se puede completar si el pedido esta ENVIADO");

            // estado final
            pedido.setEstado(EstadoDePedido.COMPLETADO);
            pedidoRepository.save(pedido);

            logger.info("Pedido completado id={}", pedidoId);
            return OperationResult.success("Pedido marcado como COMPLETADO");
        } catch (Exception e) {
            System.err.println("Error al marcar completado: " + e.getMessage());
            e.printStackTrace();
            return OperationResult.failure("No se pudo marcar como COMPLETADO");
        }
    }
}
