package com.urbancollection.ecommerce.domain.service;

import java.math.BigDecimal;
import java.math.RoundingMode;

import com.urbancollection.ecommerce.domain.entity.catalogo.Cupon;
import com.urbancollection.ecommerce.domain.enums.TipoDescuento;

/**
 * Devuelve la policy adecuada según el cupón.
 *  aquí NO validamos fechas/estado del cupón; solo elegimos la estrategia de cálculo.
 */
public final class CuponPolicyResolver {

    private CuponPolicyResolver() {}

    public static CuponPolicy from(Cupon cupon) {
        if (cupon == null || cupon.getTipo() == null) {
            return noop();
        }

        TipoDescuento tipo = cupon.getTipo();
        BigDecimal valor = cupon.getValorDescuento();

        if (tipo == TipoDescuento.PORCENTAJE) {
            // Acepta 0.10 (10%) o 10 (10) y normaliza a fracción.
            BigDecimal porcentaje = normalizePercent(valor);
            return new CuponPorcentajePolicy(porcentaje);
        }

        if (tipo == TipoDescuento.MONTO_FIJO) {
            BigDecimal monto = (valor != null) ? valor : BigDecimal.ZERO;
            return new CuponMontoFijoPolicy(monto.max(BigDecimal.ZERO));
        }

        // Si aparece un tipo no soportado, no aplica descuento
        return noop();
    }

    private static BigDecimal normalizePercent(BigDecimal v) {
        if (v == null) return BigDecimal.ZERO;
        // Si viene como 10 (10%), lo convertimos a 0.10
        if (v.compareTo(BigDecimal.ONE) > 0) {
            return v.divide(BigDecimal.valueOf(100), 6, RoundingMode.HALF_UP);
        }
        // Si ya viene como 0.10, lo dejamos
        return v;
    }

    private static CuponPolicy noop() {
        return new CuponPolicy() {
            @Override public boolean aplicaA(com.urbancollection.ecommerce.domain.entity.ventas.Pedido p) { return false; }
            @Override public BigDecimal descuento(com.urbancollection.ecommerce.domain.entity.ventas.Pedido p) { return BigDecimal.ZERO; }
        };
    }
}
