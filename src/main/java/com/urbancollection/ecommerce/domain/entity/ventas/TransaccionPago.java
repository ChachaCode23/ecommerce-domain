package com.urbancollection.ecommerce.domain.entity.ventas;

import com.urbancollection.ecommerce.domain.base.BaseEntity;
import com.urbancollection.ecommerce.domain.enums.MetodoDePago;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

/**
 * Mapea a core.transaccion_pago
 * 
 */
@Entity
@Table(name = "transaccion_pago", schema = "core")
@AttributeOverride(name = "id", column = @Column(name = "transaccion_id"))
public class TransaccionPago extends BaseEntity {

    // Relación muchos a uno: varias transacciones pueden pertenecer a un mismo pedido.
    // Se carga de manera LAZY para no traer todo el pedido si no es necesario.
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "pedido_id", nullable = false)
    private Pedido pedido;

    // Método de pago utilizado (TARJETA, TRANSFERENCIA o PAYPAL).
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "metodo", length = 20, nullable = false)
    private MetodoDePago metodoDePago;

    // Estado de la transacción (PENDIENTE, APROBADO, RECHAZADO).
    // Hay un CHECK en base de datos que valida los valores.
    @NotNull
    @Column(name = "estado", length = 20, nullable = false)
    private String estado;

    // Monto total que se está pagando en esta transacción.
    @NotNull
    @DecimalMin("0.00")
    @Column(name = "monto", precision = 12, scale = 2, nullable = false)
    private BigDecimal monto;

    // Referencia externa del pago (por ejemplo, código del procesador de pagos).
    @Column(name = "referencia", length = 100)
    private String referencia;

    // Fecha y hora en que se creó la transacción en la base de datos.
    // Se llena automáticamente con sysutcdatetime() en la BD, por eso no se inserta ni actualiza desde Java.
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

    // Métodos generados automáticamente (aún no implementados).
    // Podrían usarse para setear el método de pago y la fecha de la transacción
    // en una lógica de negocio específica más adelante.
	public void setMetodo(MetodoDePago metodo) {
		// TODO Auto-generated method stub
		
	}
	public void setFecha(LocalDateTime now) {
		// TODO Auto-generated method stub
		
	}
}
