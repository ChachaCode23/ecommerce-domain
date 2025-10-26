package com.urbancollection.ecommerce.domain.entity.ventas;

import com.urbancollection.ecommerce.domain.base.BaseEntity;
import com.urbancollection.ecommerce.domain.entity.catalogo.Producto;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

/**
 * Mapea a core.ItemPedido
 * PK = item_pedido_id
 */
@Entity
@Table(name = "ItemPedido", schema = "core")
@AttributeOverride(name = "id", column = @Column(name = "item_pedido_id"))
public class ItemPedido extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    @Min(1)
    @Column(name = "cantidad", nullable = false)
    private int cantidad;

    @NotNull
    @DecimalMin("0.00")
    @Column(name = "precio_unitario", precision = 12, scale = 2, nullable = false)
    private BigDecimal precioUnitario;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "pedido_id", nullable = false)
    private Pedido pedido;

    // ===== getters / setters =====

    public Producto getProducto() {
        return producto;
    }
    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public int getCantidad() {
        return cantidad;
    }
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }
    public void setPrecioUnitario(BigDecimal precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public Pedido getPedido() {
        return pedido;
    }
    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }
}
