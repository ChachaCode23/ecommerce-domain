package com.urbancollection.ecommerce.domain.entity.catalogo;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

/**
 * Producto
 *
 * Esta es la entidad que representa un producto del catálogo.
 * Está mapeada a la tabla core.Producto en SQL Server.
 *
 * Campos principales:
 *  - producto_id: PK autogenerada.
 *  - nombre: nombre comercial del producto (obligatorio).
 *  - descripcion: texto descriptivo (opcional).
 *  - precio: precio actual del producto (no puede ser negativo).
 *  - stock: cantidad disponible en inventario (no puede ser negativo).
 *  - sku: código interno único del producto.
 *  - activo: indica si el producto está activo en el catálogo.
 *
 * Validaciones:
 *  - @NotBlank en nombre → el producto no puede tener nombre vacío.
 *  - @NotNull / @DecimalMin en precio → el precio no puede ser null ni < 0.00.
 *  - @Min(0) en stock → no aceptamos stock negativo.
 *
 * SKU:
 *  - sku está marcado como NOT NULL y unique en la BD.
 *  - Antes de guardar (@PrePersist) si no tiene sku, se le genera uno automático.
 */
@Entity
@Table(name = "Producto", schema = "core")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "producto_id")
    private Long id;

    @NotBlank
    @Column(name = "nombre", nullable = false, length = 200)
    private String nombre;

    @Column(name = "descripcion", columnDefinition = "NVARCHAR(MAX)")
    private String descripcion;

    @NotNull
    @DecimalMin("0.00")
    @Column(name = "precio", nullable = false, precision = 12, scale = 2)
    private BigDecimal precio;

    @Min(0)
    @Column(name = "stock", nullable = false)
    private int stock;

    @Column(name = "sku", nullable = false, unique = true, length = 50)
    private String sku;

    @Column(name = "activo", nullable = false)
    private Boolean activo = true;

    /**
     * @PrePersist:
     * Este método se ejecuta automáticamente ANTES de insertar el registro en DB.
     * Si el sku no fue seteado manualmente, aquí genero uno.
     */
    @PrePersist
    public void prePersist() {
        if (sku == null || sku.isBlank()) {
            this.sku = generarSkuInterno();
        }
        if (activo == null) {
            this.activo = true;
        }
    }

    /**
     * generarSkuInterno:
     * Genera un SKU a partir del nombre + timestamp.
     */
    private String generarSkuInterno() {
        String base = (nombre != null ? nombre : "PROD")
                .replaceAll("[^A-Za-z0-9]", "")
                .toUpperCase();

        if (base.length() > 6) {
            base = base.substring(0, 6);
        }

        String ts36 = Long.toString(System.currentTimeMillis(), 36).toUpperCase();

        return base + "-" + ts36;
    }

    // ===================== GETTERS / SETTERS =====================
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BigDecimal getPrecio() {
        return precio;
    }
    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public int getStock() {
        return stock;
    }
    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getSku() {
        return sku;
    }
    public void setSku(String sku) {
        this.sku = sku;
    }

    public Boolean getActivo() {
        return activo;
    }
    public void setActivo(Boolean activo) {
        this.activo = activo;
    }
}