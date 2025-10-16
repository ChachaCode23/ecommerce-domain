package com.urbancollection.ecommerce.application.dto;

import java.math.BigDecimal;

/**
 * Clase de transferencia de datos (DTO) para Producto.
 * Esta clase nos muestra solo los campos necesarios para la vista o API.
 */
public class ProductoDTO {

    private Long id;
    private String nombre;
    private BigDecimal precio;

    //constructor que recibe los 3 datos que el DTO expone y los asigna a sus campos internos
    public ProductoDTO(Long id, String nombre, BigDecimal precio) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
    }

    //Getters
    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public BigDecimal getPrecio() {
        return precio;
    }
}
