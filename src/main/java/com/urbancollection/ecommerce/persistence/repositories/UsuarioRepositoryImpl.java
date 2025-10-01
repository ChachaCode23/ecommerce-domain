package com.urbancollection.ecommerce.persistence.repositories;

import java.util.Map;

import com.urbancollection.ecommerce.domain.entity.usuarios.Usuario;
import com.urbancollection.ecommerce.domain.repository.UsuarioRepository;
import com.urbancollection.ecommerce.persistence.base.BaseRepository;
import com.urbancollection.ecommerce.persistence.context.InMemoryContext;

/**
 * Implementa en memoria del repositorio de Usuario.
 * Permite realizar operaciones CRUD simuladas usando un mapa en memoria.
 */
public class UsuarioRepositoryImpl
        extends BaseRepository<Usuario>
        implements UsuarioRepository {

    @Override
    protected Map<Long, Usuario> store(InMemoryContext ctx) {
        return ctx.usuarios;
    }
}
