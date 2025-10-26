package com.urbancollection.ecommerce.domain.entity.ventas;

import com.urbancollection.ecommerce.domain.base.BaseEntity;
import com.urbancollection.ecommerce.domain.entity.logistica.Direccion;
import com.urbancollection.ecommerce.domain.entity.usuarios.Usuario;
import com.urbancollection.ecommerce.domain.enums.EstadoDePedido;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Mapea a core.Pedido
 * PK real = pedido_id
 *
 * direccion_envio_id en DB -> direccionEntrega aquí
 */
@Entity
@Table(name = "Pedido", schema = "core")
@AttributeOverride(name = "id", column = @Column(name = "pedido_id"))
public class Pedido extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "direccion_envio_id")
    private Direccion direccionEntrega;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", length = 20, nullable = false)
    private EstadoDePedido estado;

    @NotNull
    @Column(name = "total", precision = 12, scale = 2, nullable = false)
    private BigDecimal total;

    @OneToMany(
            mappedBy = "pedido",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<ItemPedido> items = new ArrayList<>();

    // ========== helpers IMPORTANTES ==========

    // aseguramos la relación en ambos lados
    public void addItem(ItemPedido item) {
        if (this.items == null) {
            this.items = new ArrayList<>();
        }
        this.items.add(item);
        item.setPedido(this); // <- CLAVE para que pedido_id no sea NULL
    }

    public void removeItem(ItemPedido item) {
        if (this.items == null) return;
        this.items.remove(item);
        item.setPedido(null);
    }

    // setItems ahora fuerza el link pedido <-> item
    public void setItems(List<ItemPedido> items) {
        this.items = new ArrayList<>();
        if (items != null) {
            for (ItemPedido it : items) {
                addItem(it); // usa addItem(), así siempre setPedido(this)
            }
        }
    }

    // ========== getters / setters normales ==========

    public Usuario getUsuario() {
        return usuario;
    }
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Direccion getDireccionEntrega() {
        return direccionEntrega;
    }
    public void setDireccionEntrega(Direccion direccionEntrega) {
        this.direccionEntrega = direccionEntrega;
    }

    public EstadoDePedido getEstado() {
        return estado;
    }
    public void setEstado(EstadoDePedido estado) {
        this.estado = estado;
    }

    public BigDecimal getTotal() {
        return total;
    }
    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public List<ItemPedido> getItems() {
        return items;
    }
}
