// com/urbancollection/ecommerce/persistence/repositories/UsuarioRepositoryImpl.java
package com.urbancollection.ecommerce.persistence.repositories;

import java.util.Map;

import com.urbancollection.ecommerce.domain.entity.usuarios.Usuario;
import com.urbancollection.ecommerce.domain.repository.UsuarioRepository;
import com.urbancollection.ecommerce.persistence.base.BaseRepository;
import com.urbancollection.ecommerce.persistence.context.InMemoryContext;

/**
 * UsuarioRepositoryImpl
 *
 * Implementación en memoria de UsuarioRepository.
 * Hereda de BaseRepository<Usuario> para tener CRUD genérico básico
 * (findById, findAll, save, delete...)
 * y además agrega consultas específicas por correo.
 *
 * Internamente usamos InMemoryContext como "BD falsa" en RAM.
 *
 * Reglas importantes:
 * - existsByCorreoIgnoreCase: se usa para validar que el correo no esté repetido.
 * - findByCorreoIgnoreCase: se usa para buscar usuarios por email sin importar mayúsculas.
 */
public class UsuarioRepositoryImpl extends BaseRepository<Usuario> implements UsuarioRepository {

    @Override
    protected Map<Long, Usuario> store(InMemoryContext ctx) {
        return ctx.getUsuariosStore();
    }

    @Override
    public boolean existsByCorreoIgnoreCase(String correo) {
        if (correo == null) return false;
        String n = correo.trim().toLowerCase();
        return store(InMemoryContext.getInstance()).values().stream()
                .map(Usuario::getCorreo)
                .filter(c -> c != null && !c.isBlank())
                .map(c -> c.trim().toLowerCase())
                .anyMatch(n::equals);
    }

    @Override
    public Usuario findByCorreoIgnoreCase(String correo) {
        if (correo == null) return null;
        String n = correo.trim().toLowerCase();
        return store(InMemoryContext.getInstance()).values().stream()
                .filter(u -> u.getCorreo() != null && u.getCorreo().trim().toLowerCase().equals(n))
                .findFirst().orElse(null);
    }
}
