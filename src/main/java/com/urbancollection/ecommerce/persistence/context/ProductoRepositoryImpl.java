package com.urbancollection.ecommerce.persistence.context;

import java.util.Map;

import com.urbancollection.ecommerce.domain.entity.catalogo.Producto;
import com.urbancollection.ecommerce.domain.repository.ProductoRepository;
import com.urbancollection.ecommerce.persistence.base.BaseRepository;
import com.urbancollection.ecommerce.persistence.context.InMemoryContext;

/**
 * Implementación en memoria del repositorio de Producto.
 */
public class ProductoRepositoryImpl
        extends BaseRepository<Producto>
        implements ProductoRepository {

    @Override
    protected Map<Long, Producto> store(InMemoryContext ctx) {
        return ctx.productos;
    }
}
