package com.urbancollection.ecommerce.persistence.repositories;

import java.util.List;

import com.urbancollection.ecommerce.domain.entity.catalogo.Producto;
import com.urbancollection.ecommerce.domain.repository.ProductoRepository;
import com.urbancollection.ecommerce.persistence.context.InMemoryContext;

/**
 * ProductoRepositoryImpl
 *
 * Implementación en memoria del repositorio de productos.
 * Esto NO habla con una base de datos real, sino con un "InMemoryContext"
 * que guarda todo en listas en RAM.
 *

 * - desarrolla temprano pruebas sin necesidad de montar SQL Server.
 * - Para poder probar la lógica de negocio sin depender de la BD.
 *
 * Métodos principales:
 *  - findById / findAll / save / delete -> CRUD básico usando el contexto en memoria.
 *  - findByNombreIgnoreCase -> busca un producto por nombre ignorando mayúsculas.
 */
public class ProductoRepositoryImpl implements ProductoRepository {

    // Usamos un singleton InMemoryContext que simula la "BD en memoria".
    private final InMemoryContext context = InMemoryContext.getInstance();

    @Override
    public Producto findById(Long id) {
        return context.findProductoById(id);
    }

    @Override
    public List<Producto> findAll() {
        return context.getProductos();
    }

    @Override
    public Producto save(Producto e) {
        return context.saveProducto(e);
    }

    @Override
    public void delete(Long id) {
        context.deleteProductoById(id);
    }

    /**
     * findByNombreIgnoreCase:
     * Busca el primer producto cuyo nombre coincida con el nombre dado
     * sin importar mayúsculas/minúsculas.
     *
     * Ejemplo:
     *   "gorra negra" == "GORRA NEGRA"
     *
     * Esto se usa para validar duplicados.
     */
    @Override
    public Producto findByNombreIgnoreCase(String nombre) {
        if (nombre == null) return null;
        String n = nombre.trim().toLowerCase();
        return context.getProductos()
                .stream()
                .filter(p ->
                        p.getNombre() != null &&
                        p.getNombre().trim().toLowerCase().equals(n)
                )
                .findFirst()
                .orElse(null);
    }
}
