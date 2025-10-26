package com.urbancollection.ecommerce.domain.enums;

// estos deben hacer match con el CHECK de core.Pedido.estado
public enum EstadoDePedido {
    CREADO,
    PENDIENTE_PAGO,
    PAGADO,
    EN_PREPARACION,
    ENVIADO,
    COMPLETADO,
    CANCELADO
}
