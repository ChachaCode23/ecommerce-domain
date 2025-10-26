package com.urbancollection.ecommerce.domain.repository;

import java.util.List;

import com.urbancollection.ecommerce.domain.entity.catalogo.Producto;

public interface ProductoRepository {

    Producto findById(Long id);

    List<Producto> findAll();

    Producto save(Producto producto);

    void delete(Long id);

    // agregado para que compile ProductoService y cualquier código que lo use
    Producto findByNombreIgnoreCase(String nombre);
}
