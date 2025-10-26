package com.urbancollection.ecommerce.domain.entity.catalogo;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

@Entity
@Table(name = "producto", schema = "core")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "producto_id")
    private Long id;

    @NotBlank
    @Column(name = "nombre", nullable = false, length = 255)
    private String nombre;

    @Column(name = "descripcion")
    private String descripcion;

    @NotNull
    @DecimalMin("0.00")
    @Column(name = "precio", nullable = false, precision = 19, scale = 2)
    private BigDecimal precio;

    @Min(0)
    @Column(name = "stock", nullable = false)
    private int stock;

    @Column(name = "sku", nullable = false, unique = true, length = 64)
    private String sku;

    // ===================== LIFECYCLE HOOK =====================
    // Antes de insertar, garantizamos SKU != null
    @PrePersist
    public void prePersist() {
        if (sku == null || sku.isBlank()) {
            this.sku = generarSkuInterno();
        }
    }

    // ===================== HELPERS =====================
    private String generarSkuInterno() {
        // Usa el nombre como base, limpia caracteres raros, corta a 6 chars
        String base = (nombre != null ? nombre : "PROD")
                .replaceAll("[^A-Za-z0-9]", "")
                .toUpperCase();

        if (base.length() > 6) {
            base = base.substring(0, 6);
        }

        // timestamp en base36 para hacerlo corto pero único-ish
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
}
