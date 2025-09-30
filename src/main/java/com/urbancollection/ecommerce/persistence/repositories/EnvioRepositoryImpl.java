package com.urbancollection.ecommerce.persistence.repositories;

import java.util.Map;

import com.urbancollection.ecommerce.domain.entity.logistica.Envio;
import com.urbancollection.ecommerce.domain.repository.EnvioRepository;
import com.urbancollection.ecommerce.persistence.base.BaseRepository;
import com.urbancollection.ecommerce.persistence.context.InMemoryContext;

public class EnvioRepositoryImpl
        extends BaseRepository<Envio>
        implements EnvioRepository {

    @Override
    protected Map<Long, Envio> store(InMemoryContext ctx) {
        return ctx.envios;
    }
}
