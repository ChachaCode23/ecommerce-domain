package com.urbancollection.ecommerce.persistence.repositories;

import java.util.Map;

import com.urbancollection.ecommerce.domain.entity.logistica.Direccion;
import com.urbancollection.ecommerce.domain.repository.DireccionRepository;
import com.urbancollection.ecommerce.persistence.base.BaseRepository;
import com.urbancollection.ecommerce.persistence.context.InMemoryContext;

/**
 * Gestiona las operaciones CRUD usando el contexto en memoria.
 * Implementacion en memoria del repositorio de Direccion.
 */
public class DireccionRepositoryImpl
        extends BaseRepository<Direccion>
        implements DireccionRepository {

    @Override
    protected Map<Long, Direccion> store(InMemoryContext ctx) {
        return ctx.direcciones;
    }
}
