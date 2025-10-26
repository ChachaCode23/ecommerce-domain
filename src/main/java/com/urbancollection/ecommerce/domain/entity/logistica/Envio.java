package com.urbancollection.ecommerce.domain.entity.logistica;

import com.urbancollection.ecommerce.domain.base.BaseEntity;
import com.urbancollection.ecommerce.domain.entity.ventas.Pedido;
import com.urbancollection.ecommerce.domain.enums.EstadoDeEnvio;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

// JPA
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;

@Entity
@Table(name = "envios")
public class Envio extends BaseEntity {

    @NotNull(message = "El pedido es obligatorio")
    @ManyToOne(optional = false)
    @JoinColumn(name = "pedido_id", nullable = false)
    private Pedido pedido;

    @NotBlank(message = "El tracking es obligatorio")
    @Size(max = 100, message = "El tracking no puede exceder 100 caracteres")
    @Column(name = "tracking", length = 100, nullable = false, unique = true)
    private String tracking;

    @NotNull(message = "El estado de envio es obligatorio")
    @Enumerated(EnumType.STRING)
    @Column(name = "estado", length = 30, nullable = false)
    private EstadoDeEnvio estado;

    public Envio() {}

    public Pedido getPedido() { return pedido; }
    public void setPedido(Pedido pedido) { this.pedido = pedido; }

    public String getTracking() { return tracking; }
    public void setTracking(String tracking) { this.tracking = tracking; }

    public EstadoDeEnvio getEstado() { return estado; }
    public void setEstado(EstadoDeEnvio estado) { this.estado = estado; }

    @Override
    public String toString() {
        return "Envio{" +
                "id=" + getId() +
                ", pedidoId=" + (pedido != null ? pedido.getId() : null) +
                ", tracking='" + tracking + '\'' +
                ", estado=" + estado +
                '}';
    }
}
