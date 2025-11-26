package com.urbancollection.ecommerce.infrastructure.client;

import com.urbancollection.ecommerce.domain.entity.usuarios.Usuario;

import java.util.List;
import java.util.Optional;

/**
 * IUsuarioApiClient
 * 
 * Interfaz que define el contrato para el cliente HTTP que consume la API REST de Usuarios.
 */
public interface IUsuarioApiClient {

    /**
     * Obtiene la lista completa de usuarios desde la API
     * @return Lista de usuarios
     */
    List<Usuario> listar();

    /**
     * Busca un usuario por su ID
     * @param id ID del usuario
     * @return Optional con el usuario si existe
     */
    Optional<Usuario> buscarPorId(Long id);

    /**
     * Crea un nuevo usuario en la API
     * @param usuario Usuario a crear
     * @return Usuario creado con su ID asignado
     */
    Usuario crear(Usuario usuario);

    /**
     * Actualiza un usuario existente
     * @param id ID del usuario a actualizar
     * @param usuario Datos actualizados del usuario
     * @return Usuario actualizado
     */
    Usuario actualizar(Long id, Usuario usuario);

    /**
     * Elimina un usuario por su ID
     * @param id ID del usuario a eliminar
     */
    void eliminar(Long id);
}