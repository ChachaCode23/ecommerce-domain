package com.urbancollection.ecommerce.domain.entity.logistica;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.urbancollection.ecommerce.domain.base.BaseEntity;
import com.urbancollection.ecommerce.domain.entity.ventas.Pedido;
import com.urbancollection.ecommerce.domain.enums.EstadoDeEnvio;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

/**
 * Envio
 *
 * Representa el envío físico de un pedido.
 * Esta clase guarda:
 *  - a qué pedido pertenece el envío
 *  - el código de tracking que se le da al cliente
 *  - el estado actual del envío (por ejemplo EN_CAMINO)
 *
 * Validaciones:
 *  - pedido no puede ser null (siempre debe estar asociado a un pedido real)
 *  - tracking es obligatorio y único
 *  - estado es obligatorio y es un enum controlado (EstadoDeEnvio)
 *
 * Se guarda en la tabla "envios".
 */
@Entity
@Table(name = "envios")
public class Envio extends BaseEntity {

    @NotNull(message = "El pedido es obligatorio")
    @ManyToOne(optional = false)
    @JoinColumn(name = "pedido_id", nullable = false)
    @JsonIgnoreProperties({"items", "envio", "usuario", "direccionEntrega"})
    private Pedido pedido;

    @NotBlank(message = "El tracking es obligatorio")
    @Size(max = 100, message = "El tracking no puede exceder 100 caracteres")
    @Column(name = "tracking", length = 100, nullable = false, unique = true)
    private String tracking;

    @NotNull(message = "El estado de envio es obligatorio")
    @Enumerated(EnumType.STRING)
    @Column(name = "estado", length = 30, nullable = false)
    private EstadoDeEnvio estado;

    // ===== CAMPOS DE AUDITORÍA =====
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Envio() {}

    // Método que se ejecuta antes de persistir
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    // ===== GETTERS Y SETTERS =====
    
    public Pedido getPedido() { return pedido; }
    public void setPedido(Pedido pedido) { this.pedido = pedido; }

    public String getTracking() { return tracking; }
    public void setTracking(String tracking) { this.tracking = tracking; }

    public EstadoDeEnvio getEstado() { return estado; }
    public void setEstado(EstadoDeEnvio estado) { 
        this.estado = estado;
        this.updatedAt = LocalDateTime.now();
    }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    @Override
    public String toString() {
        return "Envio{" +
                "id=" + getId() +
                ", pedidoId=" + (pedido != null ? pedido.getId() : null) +
                ", tracking='" + tracking + '\'' +
                ", estado=" + estado +
                ", createdAt=" + createdAt +
                '}';
    }
}