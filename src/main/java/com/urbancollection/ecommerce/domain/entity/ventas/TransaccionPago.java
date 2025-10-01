package com.urbancollection.ecommerce.domain.entity.ventas;

import com.urbancollection.ecommerce.domain.base.BaseEntity;

/**
 * Esta clase representa el registro de un pago
 * realizado por un cliente dentro del sistema.
 *
 * Hereda de BaseEntity, lo que le proporciona un id unico.
 *
 * En una version completa podria incluir campos como:
 * - pedidoId: referencia al pedido asociado al pago
 * - monto: valor total de la transaccion
 * - metodoPago: tipo de pago utilizado (tarjeta, PayPal, etc.)
 * - fechaPago: fecha en la que se realizo el pago
 * - estado: indica si fue exitoso o fallido
 *
 * Esta entidad permite llevar control de los pagos
 * realizados por los usuarios en la plataforma.
 */
public class TransaccionPago extends BaseEntity {
    // Ejemplo de posibles campos:
    // private Long pedidoId;
    // private BigDecimal monto;
    // private String metodoPago;
    // private LocalDate fechaPago;
    // private String estado;
}
