package com.urbancollection.ecommerce.domain.entity.catalogo;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.urbancollection.ecommerce.domain.base.BaseEntity;
import com.urbancollection.ecommerce.domain.enums.TipoDescuento;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Cupon
 *
 * Entidad JPA que representa un cupón de descuento.
 * Esta info vive en la base de datos en la tabla "cupones".
 *
 * cupon guarda:
 * - codigo: el código que el usuario escribe (ej: "BLACKFRIDAY25").
 * - activo: si el cupón está habilitado o no.
 * - fechaInicio / fechaFin: rango de validez.
 * - minimoCompra: monto mínimo para poder aplicar el cupón.
 * - tipo: si el descuento es PORCENTAJE o MONTO_FIJO.
 * - valorDescuento: cuánto descuenta (porcentaje o monto).
 * - topeDescuento: límite máximo que puede descontar.
 *
 * Validaciones:
 * - El código es obligatorio y único.
 * - No permitimos valores negativos en dinero.
 * - tipo y valorDescuento son obligatorios.
 */
@Entity
@Table(name = "cupones")
public class Cupon extends BaseEntity {

    @NotBlank(message = "El codigo del cupon es obligatorio")
    @Size(max = 100, message = "El codigo no puede exceder 100 caracteres")
    @Column(name = "codigo", length = 100, nullable = false, unique = true)
    private String codigo;

    @Column(name = "activo", nullable = false)
    private boolean activo = true;

    @Column(name = "fecha_inicio")
    private LocalDateTime fechaInicio;

    @Column(name = "fecha_fin")
    private LocalDateTime fechaFin;

    @DecimalMin(value = "0.00", message = "El minimo de compra no puede ser negativo")
    @Column(name = "minimo_compra", precision = 15, scale = 2)
    private BigDecimal minimoCompra; // opcional, si se requiere gastar al menos X

    @NotNull(message = "El tipo de descuento es obligatorio")
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", length = 20, nullable = false)
    private TipoDescuento tipo; // PORCENTAJE o MONTO_FIJO

    @NotNull(message = "El valor de descuento es obligatorio")
    @DecimalMin(value = "0.00", message = "El valor de descuento no puede ser negativo")
    @Column(name = "valor_descuento", precision = 15, scale = 2, nullable = false)
    private BigDecimal valorDescuento; // ej: 10 (%), o 500.00 (monto fijo)

    @DecimalMin(value = "0.00", message = "El tope no puede ser negativo")
    @Column(name = "tope_descuento", precision = 15, scale = 2)
    private BigDecimal topeDescuento; // límite máximo de descuento (opcional)

    // Getters / Setters
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }

    public LocalDateTime getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDateTime fechaInicio) { this.fechaInicio = fechaInicio; }

    public LocalDateTime getFechaFin() { return fechaFin; }
    public void setFechaFin(LocalDateTime fechaFin) { this.fechaFin = fechaFin; }

    public BigDecimal getMinimoCompra() { return minimoCompra; }
    public void setMinimoCompra(BigDecimal minimoCompra) { this.minimoCompra = minimoCompra; }

    public TipoDescuento getTipo() { return tipo; }
    public void setTipo(TipoDescuento tipo) { this.tipo = tipo; }

    public BigDecimal getValorDescuento() { return valorDescuento; }
    public void setValorDescuento(BigDecimal valorDescuento) { this.valorDescuento = valorDescuento; }

    public BigDecimal getTopeDescuento() { return topeDescuento; }
    public void setTopeDescuento(BigDecimal topeDescuento) { this.topeDescuento = topeDescuento; }
}
