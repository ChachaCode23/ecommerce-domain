package com.urbancollection.ecommerce.persistence.repositories;

import java.util.Map;

import com.urbancollection.ecommerce.domain.entity.catalogo.Cupon;
import com.urbancollection.ecommerce.domain.repository.CuponRepository;
import com.urbancollection.ecommerce.persistence.base.BaseRepository;
import com.urbancollection.ecommerce.persistence.context.InMemoryContext;

/**
 * Esta clase maneja las operaciones CRUD usando el contexto en memoria.
 * Implementa en memoria del repositorio de cupones.
 */
public class CuponRepositoryImpl
        extends BaseRepository<Cupon>
        implements CuponRepository {

    @Override
    protected Map<Long, Cupon> store(InMemoryContext ctx) {
        return ctx.cupones;
    }
}
