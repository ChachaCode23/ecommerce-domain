package com.urbancollection.ecommerce.persistence.repositories;

import java.util.List;

import com.urbancollection.ecommerce.domain.entity.catalogo.Producto;
import com.urbancollection.ecommerce.domain.repository.ProductoRepository;
import com.urbancollection.ecommerce.persistence.context.InMemoryContext;

public class ProductoRepositoryImpl implements ProductoRepository {

    private final InMemoryContext context = InMemoryContext.getInstance();

    @Override public Producto findById(Long id) { return context.findProductoById(id); }
    @Override public List<Producto> findAll() { return context.getProductos(); }
    @Override public Producto save(Producto e) { return context.saveProducto(e); }
    @Override public void delete(Long id) { context.deleteProductoById(id); }

    @Override
    public Producto findByNombreIgnoreCase(String nombre) {
        if (nombre == null) return null;
        String n = nombre.trim().toLowerCase();
        return context.getProductos()
                .stream()
                .filter(p -> p.getNombre() != null && p.getNombre().trim().toLowerCase().equals(n))
                .findFirst()
                .orElse(null);
    }
}
