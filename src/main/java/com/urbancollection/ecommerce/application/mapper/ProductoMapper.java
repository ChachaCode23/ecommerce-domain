package com.urbancollection.ecommerce.application.mapper;

import com.urbancollection.ecommerce.domain.entity.catalogo.Producto;
import com.urbancollection.ecommerce.application.dto.ProductoDTO;

public class ProductoMapper {

    public static ProductoDTO toDTO(Producto producto) {
        if (producto == null) {
            return null;
        }
        
        return new ProductoDTO(
            producto.getId(),           // Long
            producto.getNombre(),       // String
            producto.getDescripcion(),  // String
            producto.getPrecio(),       // BigDecimal
            producto.getStock()         // Integer
        );
    }
}