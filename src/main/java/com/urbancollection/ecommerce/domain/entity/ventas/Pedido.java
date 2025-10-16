package com.urbancollection.ecommerce.domain.entity.ventas;

import com.urbancollection.ecommerce.domain.base.BaseEntity;
import com.urbancollection.ecommerce.domain.entity.usuarios.Usuario;
import com.urbancollection.ecommerce.domain.entity.logistica.Direccion;
import com.urbancollection.ecommerce.domain.enums.EstadoDePedido;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

// Bean Validation (Jakarta)
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class Pedido extends BaseEntity {

    @NotNull(message = "El usuario es obligatorio")
    private Usuario usuario;

    @NotNull(message = "La direccion de entrega es obligatoria")
    private Direccion direccionEntrega;

    @NotNull(message = "El estado del pedido es obligatorio")
    private EstadoDePedido estado;

    @NotNull(message = "El total es obligatorio")
    @DecimalMin(value = "0.00", message = "El total no puede ser negativo")
    private BigDecimal total;

    @NotNull(message = "La lista de items es obligatoria")
    @Size(min = 1, message = "El pedido debe contener al menos un item")
    @Valid
    private List<ItemPedido> items = new ArrayList<>();

    // Getters / Setters
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
    public void setItems(List<ItemPedido> items) {
        this.items = items;
    }
}
