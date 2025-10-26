package com.urbancollection.ecommerce.domain.repository;

import java.util.List;

import com.urbancollection.ecommerce.domain.entity.catalogo.Producto;

/**
 * ProductoRepository
 *
 * Interfaz de acceso a datos para productos.
 * La capa de negocio (services) usa esta interfaz y no depende
 * directamente de cómo se guarda en la base de datos.
 *
 * Métodos principales:
 *
 * findById(id):
 *   Devuelve el producto con ese id o null si no existe.
 *
 * findAll():
 *   Lista todos los productos.
 *
 * save(producto):
 *   Crea o actualiza un producto.
 *
 * delete(id):
 *   Elimina un producto por id.
 *
 * findByNombreIgnoreCase(nombre):
 *   Busca por nombre sin importar mayúsculas/minúsculas.
 *   Útil para validar duplicados.
 */
public interface ProductoRepository {

    Producto findById(Long id);

    List<Producto> findAll();

    Producto save(Producto producto);

    void delete(Long id);

    Producto findByNombreIgnoreCase(String nombre);
}
