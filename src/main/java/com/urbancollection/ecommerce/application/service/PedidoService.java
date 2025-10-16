package com.urbancollection.ecommerce.application.service;

import com.urbancollection.ecommerce.shared.BaseService;          // importamos esta clase base que trae utilidades comunes (ej. logger, manejo de errores).
import com.urbancollection.ecommerce.shared.ValidationUtil;      // Utilidad para ejecutar Bean Validation (validaciones de campos/anotaciones).
import com.urbancollection.ecommerce.domain.base.OperationResult;// Objeto para estandarizar respuestas (success/failure + mensaje).

import com.urbancollection.ecommerce.domain.entity.catalogo.Producto;
import com.urbancollection.ecommerce.domain.entity.catalogo.Cupon;
import com.urbancollection.ecommerce.domain.entity.usuarios.Usuario;
import com.urbancollection.ecommerce.domain.entity.logistica.Direccion;
import com.urbancollection.ecommerce.domain.entity.logistica.Envio;
import com.urbancollection.ecommerce.domain.entity.ventas.ItemPedido;
import com.urbancollection.ecommerce.domain.entity.ventas.Pedido;
import com.urbancollection.ecommerce.domain.entity.ventas.TransaccionPago;

import com.urbancollection.ecommerce.domain.enums.EstadoDePedido;
import com.urbancollection.ecommerce.domain.enums.MetodoDePago;

import com.urbancollection.ecommerce.domain.repository.*;

import java.math.BigDecimal;
import java.util.List;

// Herencia: extiende BaseService (para usar logger y manejo centralizado de errores).

public class PedidoService extends BaseService {

    // Dependencias hacia la capa de persistencia (Repositorios).
    private final UsuarioRepository usuarioRepository;
    private final DireccionRepository direccionRepository;
    private final ProductoRepository productoRepository;
    private final PedidoRepository pedidoRepository;
    private final ItemPedidoRepository itemPedidoRepository;
    private final CuponRepository cuponRepository;
    private final TransaccionPagoRepository transaccionPagoRepository;
    private final EnvioRepository envioRepository;

    //en este constructor se reciben todos los repositorios necesarios para operar sobre usuarios, direcciones, productos, pedidos, etc.
     
    public PedidoService(UsuarioRepository usuarioRepository,
                         DireccionRepository direccionRepository,
                         ProductoRepository productoRepository,
                         PedidoRepository pedidoRepository,
                         ItemPedidoRepository itemPedidoRepository,
                         CuponRepository cuponRepository,
                         TransaccionPagoRepository transaccionPagoRepository,
                         EnvioRepository envioRepository) {
        this.usuarioRepository = usuarioRepository;
        this.direccionRepository = direccionRepository;
        this.productoRepository = productoRepository;
        this.pedidoRepository = pedidoRepository;
        this.itemPedidoRepository = itemPedidoRepository;
        this.cuponRepository = cuponRepository;
        this.transaccionPagoRepository = transaccionPagoRepository;
        this.envioRepository = envioRepository;
    }

 
   
    public OperationResult crearPedido(Long usuarioId,
                                       Long direccionId,
                                       List<ItemPedido> items,
                                       Long cuponId) {
        try {
            // Validaciones básicas de existencia
            Usuario usuario = usuarioRepository.findById(usuarioId);
            if (usuario == null) return OperationResult.failure("Usuario no encontrado");

            Direccion direccion = direccionRepository.findById(direccionId);
            if (direccion == null) return OperationResult.failure("Direccion no encontrada");

            if (items == null || items.isEmpty()) {
                return OperationResult.failure("El pedido debe contener al menos un item");
            }

            // Recorre items, valida cantidades, existencia, y calcula el total
            BigDecimal total = BigDecimal.ZERO;

            for (ItemPedido item : items) {
                if (item == null) return OperationResult.failure("Item invalido en el pedido");
                if (item.getCantidad() <= 0) {
                    return OperationResult.failure("Cada item debe tener cantidad > 0");
                }

                Producto producto = item.getProducto();
                if (producto == null) return OperationResult.failure("El item no tiene producto asociado");

                // Confirma el producto contra la BD (consistencia/cantidad real)
                Producto prodPersistido = productoRepository.findById(producto.getId());
                if (prodPersistido == null) return OperationResult.failure("Producto no encontrado en item");

                // Valida cantidad suficiente
                if (prodPersistido.getStock() < item.getCantidad()) {
                    return OperationResult.failure("Stock insuficiente para el producto: " + prodPersistido.getNombre());
                }

                // Calcular subtotal por item (precio * cantidad)
                BigDecimal subtotal = prodPersistido.getPrecio().multiply(BigDecimal.valueOf(item.getCantidad()));
                item.setSubtotal(subtotal);

                // Bean Validation del item.
                OperationResult iv = ValidationUtil.validate(item);
                if (!iv.isSuccess()) return iv;

                // Suma al total del pedido
                total = total.add(subtotal);
            }

            // Aplica cupon si existe (lógica pendiente por definir)
            if (cuponId != null) {
                Cupon cupon = cuponRepository.findById(cuponId);
                if (cupon == null) return OperationResult.failure("Cupon no encontrado");
                
            }

            // construye el pedido inicial (estado PENDIENTE)
            Pedido pedido = new Pedido();
            pedido.setUsuario(usuario);
            pedido.setDireccionEntrega(direccion);
            pedido.setEstado(EstadoDePedido.PENDIENTE);
            pedido.setTotal(total);
            // Importante: asocia items al pedido (referencia para validaciones y persistencia)
            pedido.setItems(items);

            // Validacion del pedido completo antes de persistir
            OperationResult pv = ValidationUtil.validate(pedido);
            if (!pv.isSuccess()) return pv;

            // guarda el pedido y luego guarda cada item con la referencia al pedido
            pedido = pedidoRepository.save(pedido);

            for (ItemPedido item : items) {
                item.setPedido(pedido);            
                itemPedidoRepository.save(item);   // guardar el item
            }

           
            logger.info("Pedido creado id={} total={}", pedido.getId(), total);

            return OperationResult.success("Pedido creado correctamente");
        } catch (Exception e) {
            // Manejo centralizado de errores (de BaseService)
            handleError(e, "Error al crear pedido");
            return OperationResult.failure("No se pudo crear el pedido");
        }
    }

   
    public OperationResult confirmarPago(Long pedidoId,
                                         MetodoDePago metodo,
                                         BigDecimal monto) {
        try {
            // Valida existencia y estado del pedido
            Pedido pedido = pedidoRepository.findById(pedidoId);
            if (pedido == null) return OperationResult.failure("Pedido no encontrado");

            if (pedido.getEstado() != EstadoDePedido.PENDIENTE) {
                return OperationResult.failure("Solo se puede pagar un pedido en estado PENDIENTE");
            }

            // Valida monto del pedido
            if (monto == null || monto.compareTo(BigDecimal.ZERO) <= 0) {
                return OperationResult.failure("Monto de pago invalido");
            }

            if (pedido.getTotal() == null || monto.compareTo(pedido.getTotal()) != 0) {
                return OperationResult.failure("El monto debe ser igual al total del pedido");
            }

            // Evita pagos duplicados (buscar si ya existe transacción asociada a este pedido)
            boolean yaPagado = transaccionPagoRepository.findAll().stream()
                    .anyMatch(t -> t.getPedido() != null && t.getPedido().getId().equals(pedidoId));
            if (yaPagado) return OperationResult.failure("El pedido ya fue pagado");

            // Valida items del pedido (no se puede pagar si no hay items)
            List<ItemPedido> items = pedido.getItems();
            if (items == null || items.isEmpty()) {
                return OperationResult.failure("El pedido no tiene items para procesar el pago");
            }

            // descuenta en stock de cada producto de forma segura (verificando existencia y no quedar en negativo)
            for (ItemPedido item : items) {
                if (item == null || item.getProducto() == null) {
                    return OperationResult.failure("Item invalido o sin producto");
                }
                Producto prod = productoRepository.findById(item.getProducto().getId());
                if (prod == null) return OperationResult.failure("Producto no encontrado en item");

                int nuevoStock = prod.getStock() - item.getCantidad();
                if (nuevoStock < 0) {
                    return OperationResult.failure("Stock insuficiente para: " + prod.getNombre());
                }
                prod.setStock(nuevoStock);
                productoRepository.save(prod);
            }

            // Registra la transacción de pago y valida
            TransaccionPago tx = new TransaccionPago();
            tx.setPedido(pedido);
            tx.setMetodoDePago(metodo);
            tx.setMonto(monto);

            OperationResult tv = ValidationUtil.validate(tx);
            if (!tv.isSuccess()) return tv;

            transaccionPagoRepository.save(tx);

            // Cambia estado del pedido a PAGADO
            pedido.setEstado(EstadoDePedido.PAGADO);
            pedidoRepository.save(pedido);

            
            logger.info("Pago confirmado. Pedido={} Total={} Metodo={}", pedido.getId(), monto, metodo);

            return OperationResult.success("Pago confirmado y stock actualizado");
        } catch (Exception e) {
            handleError(e, "Error al confirmar pago");
            return OperationResult.failure("No se pudo confirmar el pago");
        }
    }

   
     
    public OperationResult despacharPedido(Long pedidoId, String tracking) {
        try {
            // Valida pedido y estado
            Pedido pedido = pedidoRepository.findById(pedidoId);
            if (pedido == null) return OperationResult.failure("Pedido no encontrado");

            if (pedido.getEstado() != EstadoDePedido.PAGADO) {
                return OperationResult.failure("Solo se puede despachar un pedido PAGADO");
            }

            // Verifica si ya existe envío asociado
            boolean yaDespachado = envioRepository.findAll().stream()
                    .anyMatch(e -> e.getPedido() != null && e.getPedido().getId().equals(pedidoId));
            if (yaDespachado) {
                return OperationResult.failure("El pedido ya tiene un envio registrado");
            }

            // Crea y validar el envío
            Envio envio = new Envio();
            envio.setPedido(pedido);
            envio.setTracking(tracking);
            envio.setEstado(com.urbancollection.ecommerce.domain.enums.EstadoDeEnvio.EN_CAMINO);

            OperationResult ev = ValidationUtil.validate(envio);
            if (!ev.isSuccess()) return ev;

            envioRepository.save(envio);

            // Marca el pedido como ENVIADO
            pedido.setEstado(EstadoDePedido.ENVIADO);
            pedidoRepository.save(pedido);

            // Log de auditoría
            logger.info("Pedido despachado id={} tracking={}", pedidoId, tracking);

            return OperationResult.success("Pedido despachado");
        } catch (Exception e) {
            handleError(e, "Error al despachar pedido");
            return OperationResult.failure("No se pudo despachar el pedido");
        }
    }

   
    public OperationResult marcarEntregado(Long pedidoId) {
        try {
            // Valida el pedido
            Pedido pedido = pedidoRepository.findById(pedidoId);
            if (pedido == null) return OperationResult.failure("Pedido no encontrado");

            // Valida el estado actual
            if (pedido.getEstado() != EstadoDePedido.ENVIADO) {
                return OperationResult.failure("Solo se puede marcar ENTREGADO si el pedido esta ENVIADO");
            }

            // Actualiza estado y guarda
            pedido.setEstado(EstadoDePedido.ENTREGADO);
            pedidoRepository.save(pedido);

            // Log de auditoría
            logger.info("Pedido entregado id={}", pedidoId);

            return OperationResult.success("Pedido marcado como ENTREGADO");
        } catch (Exception e) {
            handleError(e, "Error al marcar entregado");
            return OperationResult.failure("No se pudo marcar como entregado");
        }
    }
}
