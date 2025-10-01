package com.urbancollection.ecommerce.domain.repository;

import com.urbancollection.ecommerce.domain.entity.usuarios.Usuario;

/**
 * Esta interfaz define las operaciones CRUD basicas para la entidad Usuario.
 * Extiende de IBaseRepository para reutilizar los metodos genericos de
 * guardar, buscar, eliminar y listar.
 */
public interface UsuarioRepository extends IBaseRepository<Usuario, Long> {
    // Aqui se pueden agregar metodos especificos de Usuario si se necesitan
}
