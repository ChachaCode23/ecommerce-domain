package com.urbancollection.ecommerce.domain.enums;

/**
 * Aqui defino los diferentes estados
 * que puede tener un pedido dentro del sistema.
 *
 * Esta clase sirve para controlar el ciclo de vida completo del pedido,
 * desde su creacion hasta su entrega o cancelacion.
 *
 * Estados posibles:
 * - CREADO: el pedido fue generado por el cliente.
 * - PAGADO: el pago del pedido fue confirmado.
 * - EN_PROCESO: el pedido se esta preparando.
 * - ENVIADO: el pedido fue despachado al cliente.
 * - ENTREGADO: el cliente ya recibio su pedido.
 * - CANCELADO: el pedido fue cancelado por el cliente o el sistema.
 */
public enum EstadoDePedido {
    CREADO,
    PAGADO,
    EN_PROCESO,
    ENVIADO,
    ENTREGADO,
    CANCELADO
}
