package com.urbancollection.ecommerce.persistence.repositories;

import java.util.Map;

import com.urbancollection.ecommerce.domain.entity.catalogo.Producto;
import com.urbancollection.ecommerce.domain.repository.ProductoRepository;
import com.urbancollection.ecommerce.persistence.base.BaseRepository;
import com.urbancollection.ecommerce.persistence.context.InMemoryContext;

/**
 * Implementacion en memoria del repositorio de Producto.
 * Permite guardar, buscar, eliminar y listar productos
 * usando una estructura en memoria.
 */
public class ProductoRepositoryImpl
        extends BaseRepository<Producto>
        implements ProductoRepository {

    @Override
    protected Map<Long, Producto> store(InMemoryContext ctx) {
        return ctx.productos;
    }
}
