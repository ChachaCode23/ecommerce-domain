// com/urbancollection/ecommerce/domain/repository/UsuarioRepository.java
package com.urbancollection.ecommerce.domain.repository;

import com.urbancollection.ecommerce.domain.entity.usuarios.Usuario;

public interface UsuarioRepository extends IBaseRepository<Usuario, Long> {
    boolean existsByCorreoIgnoreCase(String correo);
    Usuario findByCorreoIgnoreCase(String correo); // útil si luego quieres traer el usuario
}
