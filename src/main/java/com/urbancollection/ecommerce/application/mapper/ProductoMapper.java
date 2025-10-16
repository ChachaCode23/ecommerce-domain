package com.urbancollection.ecommerce.application.mapper;

import com.urbancollection.ecommerce.domain.entity.catalogo.Producto;
import com.urbancollection.ecommerce.application.dto.ProductoDTO;

public class ProductoMapper {

    public static ProductoDTO toDTO(Producto producto) {
        return new ProductoDTO(
            producto.getId(),       // debe devolver Long
            producto.getNombre(),   // String
            producto.getPrecio()    // BigDecimal
        );
    }
}
