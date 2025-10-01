package com.urbancollection.ecommerce.domain.enums;

/**
 * Esta clase representa las diferentes
 * formas de pago disponibles dentro del sistema.
 *
 * El objetivo de esta clase es estandarizar los metodos de pago
 * que los clientes pueden utilizar al realizar sus compras.
 *
 * Metodos disponibles:
 * - TARJETA: pago con tarjeta de credito o debito.
 * - PAYPAL: pago a traves de la plataforma PayPal.
 * - TRANSFERENCIA: pago mediante transferencia bancaria.
 */
public enum MetodoDePago {
    TARJETA,
    PAYPAL,
    TRANSFERENCIA
}
