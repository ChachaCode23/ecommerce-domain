package com.urbancollection.ecommerce.application.service;

import java.util.List;
import java.util.Optional;

import com.urbancollection.ecommerce.domain.base.OperationResult;
import com.urbancollection.ecommerce.domain.entity.logistica.Direccion;

// Interfaz del servicio de aplicación para trabajar con direcciones.
// Aquí defino las operaciones que el servicio debe implementar.
public interface IDireccionService {

    // Devuelve la lista completa de direcciones registradas.
    List<Direccion> listar();

    // Busca una dirección por su id y la devuelve dentro de un Optional.
    // Si no existe, el Optional estará vacío.
    Optional<Direccion> buscarPorId(Long id);

    // Crea una nueva dirección.
    // Retorna un OperationResult indicando si la operación fue exitosa o no.
    OperationResult crear(Direccion direccion);

    // Actualiza una dirección existente identificada por su id,
    // usando los datos que vienen en el objeto "cambios".
    OperationResult actualizar(Long id, Direccion cambios);

    // Elimina una dirección por su id.
    OperationResult eliminar(Long id);
}
