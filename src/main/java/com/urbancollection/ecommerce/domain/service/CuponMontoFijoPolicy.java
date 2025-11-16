package com.urbancollection.ecommerce.domain.service;

import java.math.BigDecimal;
import com.urbancollection.ecommerce.domain.entity.ventas.ItemPedido;
import com.urbancollection.ecommerce.domain.entity.ventas.Pedido;

/** Descuento de monto fijo (ej. RD$ 300). */
public class CuponMontoFijoPolicy implements CuponPolicy {

    private final BigDecimal monto; // >= 0

    public CuponMontoFijoPolicy(BigDecimal monto) {
        if (monto == null || monto.signum() < 0)
            throw new IllegalArgumentException("monto debe ser >= 0");
        this.monto = monto;
    }

    @Override
    public boolean aplicaA(Pedido pedido) {
        return pedido != null && pedido.getItems() != null && !pedido.getItems().isEmpty();
    }

    @Override
    public BigDecimal descuento(Pedido pedido) {
        if (!aplicaA(pedido)) return BigDecimal.ZERO;
        BigDecimal subtotal = subtotal(pedido);
        // El descuento nunca supera el subtotal
        return monto.min(subtotal);
    }

    private BigDecimal subtotal(Pedido pedido) {
        BigDecimal sub = BigDecimal.ZERO;
        for (ItemPedido it : pedido.getItems()) {
            if (it == null) continue;
            BigDecimal pu = it.getPrecioUnitario();
            int qty = it.getCantidad();
            if (pu != null && qty > 0) {
                sub = sub.add(pu.multiply(java.math.BigDecimal.valueOf(qty)));
            }
        }
        return sub;
    }
}
