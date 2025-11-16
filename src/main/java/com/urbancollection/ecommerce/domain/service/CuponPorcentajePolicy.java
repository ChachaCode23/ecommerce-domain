package com.urbancollection.ecommerce.domain.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import com.urbancollection.ecommerce.domain.entity.ventas.ItemPedido;
import com.urbancollection.ecommerce.domain.entity.ventas.Pedido;

/** Descuento por porcentaje (ej. 10% = 0.10). */
public class CuponPorcentajePolicy implements CuponPolicy {

    private final BigDecimal porcentaje; // 0.00 .. 1.00

    public CuponPorcentajePolicy(BigDecimal porcentaje) {
        if (porcentaje == null || porcentaje.compareTo(BigDecimal.ZERO) < 0 || porcentaje.compareTo(BigDecimal.ONE) > 0)
            throw new IllegalArgumentException("porcentaje debe estar entre 0 y 1");
        this.porcentaje = porcentaje;
    }

    @Override
    public boolean aplicaA(Pedido pedido) {
        return pedido != null && pedido.getItems() != null && !pedido.getItems().isEmpty();
    }

    @Override
    public BigDecimal descuento(Pedido pedido) {
        if (!aplicaA(pedido)) return BigDecimal.ZERO;
        BigDecimal subtotal = subtotal(pedido);
        return subtotal.multiply(porcentaje).setScale(2, RoundingMode.HALF_UP);
    }

    // No usamos pedido.calcularTotales() para evitar efectos secundarios.
    private BigDecimal subtotal(Pedido pedido) {
        BigDecimal sub = BigDecimal.ZERO;
        for (ItemPedido it : pedido.getItems()) {
            if (it == null) continue;
            BigDecimal pu = it.getPrecioUnitario();
            int qty = it.getCantidad();
            if (pu != null && qty > 0) {
                sub = sub.add(pu.multiply(BigDecimal.valueOf(qty)));
            }
        }
        return sub;
    }
}
