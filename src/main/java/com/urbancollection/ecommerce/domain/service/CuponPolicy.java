package com.urbancollection.ecommerce.domain.service;

import java.math.BigDecimal;
import com.urbancollection.ecommerce.domain.entity.ventas.Pedido;

/**
 * Política de cupón (reglas de descuento).
 * NO valida fechas ni estado del cupón, solo cálculo y aplicabilidad.
 */
public interface CuponPolicy {
    boolean aplicaA(Pedido pedido);
    BigDecimal descuento(Pedido pedido);
}
