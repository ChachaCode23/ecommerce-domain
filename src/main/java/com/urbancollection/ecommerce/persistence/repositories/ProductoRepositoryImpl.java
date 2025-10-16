package com.urbancollection.ecommerce.persistence.repositories;

import java.util.List;

import com.urbancollection.ecommerce.domain.entity.catalogo.Producto;
import com.urbancollection.ecommerce.domain.repository.ProductoRepository;
import com.urbancollection.ecommerce.persistence.context.InMemoryContext;

/**
 * Implementacion del repositorio de Producto usando InMemoryContext.
 * Notas:
 * - Para guardar/actualizar usamos context.saveProducto(..) → asigna ID si es nuevo.
 * - Para leer por id usamos context.findProductoById(..).
 * - Para listar usamos context.getProductos() (copia para lectura).
 * - Para borrar usamos context.deleteProductoById(..).
 */
public class ProductoRepositoryImpl implements ProductoRepository {

    private final InMemoryContext context = InMemoryContext.getInstance();

    @Override
    public Producto findById(Long id) {
        return context.findProductoById(id);
    }

    @Override
    public List<Producto> findAll() {
        // devuelve una copia de los productos (solo lectura)
        return context.getProductos();
    }

    @Override
    public Producto save(Producto entity) {
        // si entity.getId() es null, InMemoryContext asigna un ID
        return context.saveProducto(entity);
    }

    @Override
    public void delete(Long id) {
        context.deleteProductoById(id);
    }
}
