package com.urbancollection.ecommerce.application.service;

import com.urbancollection.ecommerce.domain.base.OperationResult;
import com.urbancollection.ecommerce.domain.entity.ventas.ItemPedido;
import com.urbancollection.ecommerce.domain.entity.ventas.Pedido;
import com.urbancollection.ecommerce.domain.enums.MetodoDePago;

import java.math.BigDecimal;
import java.util.List;

/**
 * IPedidoService
 *
 * Interfaz del servicio de pedidos. Aquí está la lógica de alto nivel
 * que el sistema necesita para manejar el ciclo de vida de un pedido.
 *
 * Responsabilidades principales:
 *
 * crearPedido(...)
 *   Crea un nuevo pedido a partir de:
 *   - usuario que compra
 *   - dirección de envío
 *   - lista de productos con cantidades
 *   - cupón opcional
 *   Devuelve OperationResult para decir si se pudo crear o no (y por qué).
 *
 * confirmarPago(...)
 *   Registra el pago del pedido. Aquí se valida el monto pagado,
 *   el método de pago y normalmente también se actualiza el stock real.
 *
 * ✅ NUEVO: marcarComoPagado(...)
 *   Marca el pedido como PAGADO sin procesar pago real (para testing/admin)
 *
 * despacharPedido(...)
 *   Marca el pedido como enviado y guarda el tracking del envío.
 *
 * marcarEntregado(...)
 *   Marca el pedido como completado / entregado al cliente.
 *
 * obtenerPorId(...) / listarTodos()
 *   Consultas para devolver pedidos ya creados.
 *   Lista pedidos PAGADOS sin envíos asociados (lógica movida del controlador)
 */
public interface IPedidoService {

    OperationResult crearPedido(Long usuarioId,
                                Long direccionId,
                                List<ItemPedido> items,
                                Long cuponId);

    OperationResult confirmarPago(Long pedidoId,
                                  MetodoDePago metodo,
                                  BigDecimal monto);

    // Marcar como pagado (sin procesar pago real)
    OperationResult marcarComoPagado(Long pedidoId);

    OperationResult despacharPedido(Long pedidoId, String tracking);

    OperationResult marcarEntregado(Long pedidoId);

    Pedido obtenerPorId(Long id);

    List<Pedido> listarTodos();

    /**
     * Listar pedidos disponibles para crear envío
     * Retorna pedidos que están en estado PAGADO y NO tienen envíos asociados.
     */
    List<Pedido> listarPedidosDisponiblesParaEnvio();

    /**
     *  Elimina un pedido por su ID
     */
    OperationResult eliminar(Long id);
}