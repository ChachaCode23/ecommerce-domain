package com.urbancollection.ecommerce.domain.entity.ventas;

import java.math.BigDecimal;

import com.urbancollection.ecommerce.domain.base.BaseEntity;
import com.urbancollection.ecommerce.domain.enums.MetodoDePago;

// Bean Validation (Jakarta)
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

/**
 * Registro de una transaccion de pago asociada a un Pedido.
 */
public class TransaccionPago extends BaseEntity {

    @NotNull(message = "El pedido es obligatorio")
    private Pedido pedido;

    @NotNull(message = "El metodo de pago es obligatorio")
    private MetodoDePago metodoDePago;

    @NotNull(message = "El monto es obligatorio")
    @DecimalMin(value = "0.01", message = "El monto debe ser mayor que 0")
    private BigDecimal monto;

    // Getters / Setters
    public Pedido getPedido() { return pedido; }
    public void setPedido(Pedido pedido) { this.pedido = pedido; }

    public MetodoDePago getMetodoDePago() { return metodoDePago; }
    public void setMetodoDePago(MetodoDePago metodoDePago) { this.metodoDePago = metodoDePago; }

    public BigDecimal getMonto() { return monto; }
    public void setMonto(BigDecimal monto) { this.monto = monto; }
}
