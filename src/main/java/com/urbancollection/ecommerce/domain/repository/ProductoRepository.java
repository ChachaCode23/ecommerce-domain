package com.urbancollection.ecommerce.domain.repository;

import com.urbancollection.ecommerce.domain.entity.catalogo.Producto;

/**
 * Esta interfaz de efine las operaciones CRUD para la entidad Producto.
 * Extiende de IBaseRepository para heredar las operaciones
 * basicas de guardar, buscar, eliminar y listar.
 */
public interface ProductoRepository extends IBaseRepository<Producto, Long> {
    // Aqui se pueden agregar metodos especificos de ser necesario.
}