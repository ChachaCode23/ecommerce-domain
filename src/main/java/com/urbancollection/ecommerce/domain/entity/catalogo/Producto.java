package com.urbancollection.ecommerce.domain.entity.catalogo;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

/**
 * Producto
 *
 * Esta es la entidad que representa un producto del catálogo.
 * Está mapeada a la tabla core.producto en la base de datos.
 *
 * Campos principales:
 *  - id: PK autogenerada.
 *  - nombre: nombre comercial del producto (obligatorio).
 *  - descripcion: texto descriptivo (opcional).
 *  - precio: precio actual del producto (no puede ser negativo).
 *  - stock: cantidad disponible en inventario (no puede ser negativo).
 *  - sku: código interno único del producto.
 *
 * Validaciones:
 *  - @NotBlank en nombre → el producto no puede tener nombre vacío.
 *  - @NotNull / @DecimalMin en precio → el precio no puede ser null ni < 0.00.
 *  - @Min(0) en stock → no aceptamos stock negativo.
 *
 * SKU:
 *  - sku está marcado como NOT NULL y unique en la BD.
 *  - Antes de guardar (@PrePersist) si no tiene sku, se le genera uno automático.
 *    Así evitamos violar la restricción NOT NULL de la columna.
 */
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


    /**
     * @PrePersist:
     * Este método se ejecuta automáticamente ANTES de insertar el registro en DB.
     * Si el sku no fue seteado manualmente, aquí genero uno.
     *
     * Esto sirve como "última defensa" para que la fila no falle al insert
     * por tener sku NULL.
     */
    @PrePersist
    public void prePersist() {
        if (sku == null || sku.isBlank()) {
            this.sku = generarSkuInterno();
        }
    }


    /**
     * generarSkuInterno:
     * Genera un SKU a partir del nombre + timestamp.
     * - Limpia caracteres raros del nombre.
     * - Usa máximo 6 chars del nombre en mayúscula.
     * - Le pega un timestamp convertido a base36 para hacerlo corto y casi único.
     *
     * Ejemplo de SKU: "GORRA-LO6Z1F7"
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
}
