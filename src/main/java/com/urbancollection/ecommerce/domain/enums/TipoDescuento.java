package com.urbancollection.ecommerce.domain.enums;

/**
 * TipoDescuento
 *
 * Enum para indicar qué tipo de descuento aplica un cupón:
 *
 * PORCENTAJE  -> ejemplo: 10 significa 10% de descuento.
 * MONTO_FIJO  -> ejemplo: 500.00 significa bajar $500 exactos.
 *
 * Esto se usa en Cupon para saber cómo calcular el descuento.
 */
public enum TipoDescuento {
    PORCENTAJE,
    MONTO_FIJO
}
