package com.urbancollection.ecommerce.domain.enums;

/**
 * Esta clase se utiliza para dar seguimiento al progreso de la entrega
 * de un pedido realizado por el cliente.
 *
 * Estados posibles:
 * - PENDIENTE: el envio aun no ha sido despachado.
 * - EN_TRANSITO: el paquete se encuentra en camino.
 * - ENTREGADO: el pedido ya fue recibido por el cliente.
 */
public enum EstadoDeEnvio {
    PENDIENTE,
    EN_TRANSITO,
    ENTREGADO
}
