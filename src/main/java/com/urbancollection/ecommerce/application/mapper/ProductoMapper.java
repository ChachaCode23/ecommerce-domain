package com.urbancollection.ecommerce.application.mapper;

import com.urbancollection.ecommerce.domain.entity.catalogo.Producto;
import com.urbancollection.ecommerce.application.dto.ProductoDTO;

// Clase de utilidad para convertir la entidad Producto a su DTO.
// La idea es no exponer directamente la entidad desde la capa de aplicación.
public class ProductoMapper {

    // Convierte un objeto Producto en un ProductoDTO.
    // Si el producto es null, devuelve null para evitar NullPointerException.
    public static ProductoDTO toDTO(Producto producto) {
        if (producto == null) {
            return null;
        }
        
        // Aquí construyo el DTO usando los campos principales del producto.
        return new ProductoDTO(
            producto.getId(),           // Long
            producto.getNombre(),       // String
            producto.getDescripcion(),  // String
            producto.getPrecio(),       // BigDecimal
            producto.getStock()         // Integer
        );
    }
}
