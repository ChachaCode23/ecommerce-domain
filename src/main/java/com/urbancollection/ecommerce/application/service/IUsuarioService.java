package com.urbancollection.ecommerce.application.service;

import com.urbancollection.ecommerce.domain.base.OperationResult;
import com.urbancollection.ecommerce.domain.entity.logistica.Direccion;
import com.urbancollection.ecommerce.domain.entity.usuarios.Usuario;

import java.util.List;
import java.util.Optional;

// Interfaz de servicio de aplicación para trabajar con usuarios.
// Aquí  defino qué operaciones debe implementar la capa de aplicación.
public interface IUsuarioService {

    // Devuelve la lista completa de usuarios registrados.
    List<Usuario> listar();

    // Busca un usuario por su id y lo devuelve dentro de un Optional.
    // Si no existe, el Optional viene vacío.
    Optional<Usuario> buscarPorId(Long id);

    // Crea un usuario sin manejar dirección en esta operación.
    // Devuelve un OperationResult para indicar si se creó correctamente o no.
    OperationResult crear(Usuario nuevo);
    
    // Crea un usuario junto con una dirección asociada.
    // Sirve cuando se quiere registrar todo de una vez (usuario + dirección).
    OperationResult crearConDireccion(Usuario nuevo, Direccion direccion);

    // Actualiza un usuario existente identificado por su id,
    // usando los datos que vienen en el objeto "cambios".
    OperationResult actualizar(Long id, Usuario cambios);

    // Elimina un usuario por su id.
    OperationResult eliminar(Long id);
}
