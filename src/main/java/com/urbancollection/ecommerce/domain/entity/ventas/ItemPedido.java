package com.urbancollection.ecommerce.domain.entity.ventas;

import com.urbancollection.ecommerce.domain.base.BaseEntity;
import com.urbancollection.ecommerce.domain.entity.catalogo.Producto;

import java.math.BigDecimal;

// Bean Validation (Jakarta)
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * Representa un item dentro de un pedido (producto + cantidad).
 */
public class ItemPedido extends BaseEntity {

    @NotNull(message = "El producto es obligatorio")
    private Producto producto;

    @Min(value = 1, message = "La cantidad debe ser al menos 1")
    private int cantidad;

    // No marcamos @NotNull porque normalmente el subtotal se calcula en el servicio.
    @DecimalMin(value = "0.00", message = "El subtotal no puede ser negativo")
    private BigDecimal subtotal;

    // No marcamos @NotNull: se vincula al crear el pedido en el servicio.
    private Pedido pedido;

    // Getters / Setters
    public Producto getProducto() { return producto; }
    public void setProducto(Producto producto) { this.producto = producto; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }

    public Pedido getPedido() { return pedido; }
    public void setPedido(Pedido pedido) { this.pedido = pedido; }
}
