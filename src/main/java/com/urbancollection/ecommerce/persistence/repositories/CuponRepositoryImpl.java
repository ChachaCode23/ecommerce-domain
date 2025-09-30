package com.urbancollection.ecommerce.persistence.repositories;

import java.util.Map;

import com.urbancollection.ecommerce.domain.entity.catalogo.Cupon;
import com.urbancollection.ecommerce.domain.repository.CuponRepository;
import com.urbancollection.ecommerce.persistence.base.BaseRepository;
import com.urbancollection.ecommerce.persistence.context.InMemoryContext;

public class CuponRepositoryImpl
        extends BaseRepository<Cupon>
        implements CuponRepository {

    @Override
    protected Map<Long, Cupon> store(InMemoryContext ctx) {
        return ctx.cupones;
    }
}
