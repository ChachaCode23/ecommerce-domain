package com.urbancollection.ecommerce.domain.entity.catalogo;

import java.math.BigDecimal;
import com.urbancollection.ecommerce.domain.base.BaseEntity;

/**
 * ESta clase  representa un artículo disponible
 * dentro del catalogo del sistema.
 *
 * Hereda de BaseEntity para obtener el identificador único (id)
 * y se complementa con los atributos propios del producto.
 *
 * Atributos:
 * - nombre: nombre del producto (por ejemplo, "Camiseta", "Gorra").
 * - descripcion: detalles adicionales sobre el producto.
 * - precio: costo del producto, representado con BigDecimal
 *   para manejar valores monetarios con precisión.
 *
 * tambien incluye los metodos getter y setter
 * para acceder y modificar sus atributos.
 */
public class Producto extends BaseEntity {
    private String nombre;
    private String descripcion;
    private BigDecimal precio;

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public BigDecimal getPrecio() { return precio; }
    public void setPrecio(BigDecimal precio) { this.precio = precio; }
}
