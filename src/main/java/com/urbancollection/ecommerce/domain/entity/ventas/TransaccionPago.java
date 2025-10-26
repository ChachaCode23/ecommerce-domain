package com.urbancollection.ecommerce.domain.entity.ventas;

import com.urbancollection.ecommerce.domain.base.BaseEntity;
import com.urbancollection.ecommerce.domain.enums.MetodoDePago;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

/**
 * Mapea a core.transaccion_pago
 */
@Entity
@Table(name = "transaccion_pago", schema = "core")
@AttributeOverride(name = "id", column = @Column(name = "transaccion_id"))
public class TransaccionPago extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "pedido_id", nullable = false)
    private Pedido pedido;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "metodo", length = 20, nullable = false)
    private MetodoDePago metodoDePago;

    // CHECK (estado IN ('PENDIENTE','APROBADO','RECHAZADO'))
    @NotNull
    @Column(name = "estado", length = 20, nullable = false)
    private String estado;

    @NotNull
    @DecimalMin("0.00")
    @Column(name = "monto", precision = 12, scale = 2, nullable = false)
    private BigDecimal monto;

    @Column(name = "referencia", length = 100)
    private String referencia;

    // default en DB = sysutcdatetime(), no se inserta desde Java
    @Column(name = "creado_en", insertable = false, updatable = false)
    private OffsetDateTime creadoEn;

    // ===== getters / setters =====
    public Pedido getPedido() { return pedido; }
    public void setPedido(Pedido pedido) { this.pedido = pedido; }

    public MetodoDePago getMetodoDePago() { return metodoDePago; }
    public void setMetodoDePago(MetodoDePago metodoDePago) { this.metodoDePago = metodoDePago; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public BigDecimal getMonto() { return monto; }
    public void setMonto(BigDecimal monto) { this.monto = monto; }

    public String getReferencia() { return referencia; }
    public void setReferencia(String referencia) { this.referencia = referencia; }

    public OffsetDateTime getCreadoEn() { return creadoEn; }
    public void setCreadoEn(OffsetDateTime creadoEn) { this.creadoEn = creadoEn; }
}
