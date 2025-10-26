package com.urbancollection.ecommerce.application.service;

import com.urbancollection.ecommerce.domain.base.OperationResult;
import com.urbancollection.ecommerce.domain.entity.usuarios.Usuario;

import java.util.List;
import java.util.Optional;

/**
 * IUsuarioService
 *
 * Interfaz del servicio de usuarios.
 * Aquí defino lo que se puede hacer con usuarios a nivel de negocio,
 * sin meter detalles de controllers ni de base de datos.
 *
 * Métodos:
 *
 * listar():
 *   Devuelve la lista completa de usuarios.
 *
 * buscarPorId(id):
 *   Busca un usuario específico. Devuelve Optional porque puede no existir.
 *
 * crear(nuevo):
 *   Crea un usuario nuevo. Devuelve OperationResult para saber si fue válido
 *   (por ejemplo correo ya registrado).
 *
 * actualizar(id, cambios):
 *   Edita un usuario existente con los cambios que se permitan.
 *
 * eliminar(id):
 *   Elimina un usuario por id.
 */
public interface IUsuarioService {

    List<Usuario> listar();

    Optional<Usuario> buscarPorId(Long id);

    OperationResult crear(Usuario nuevo);

    OperationResult actualizar(Long id, Usuario cambios);

    OperationResult eliminar(Long id);
}
