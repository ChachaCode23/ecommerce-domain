package com.urbancollection.ecommerce.application.service;

import java.util.List;
import java.util.Optional;

import com.urbancollection.ecommerce.domain.base.OperationResult;
import com.urbancollection.ecommerce.domain.entity.logistica.Envio;

// Interfaz del servicio de aplicación para trabajar con envíos.
// Aquí definins qué operaciones debe soportar la capa de aplicación
public interface IEnvioService {

    // Devuelve la lista completa de envíos registrados.
    List<Envio> listar();

    // Busca un envío por su id y lo devuelve envuelto en un Optional.
    // Si no existe, el Optional vendrá vacío.
    Optional<Envio> buscarPorId(Long id);

    // Crea un nuevo envío y devuelve un OperationResult indicando
    // si la operación fue exitosa o si ocurrió algún error.
    OperationResult crear(Envio envio);

    // Actualiza un envío existente identificado por su id
    // usando los datos del objeto "cambios".
    OperationResult actualizar(Long id, Envio cambios);

    // Elimina un envío por su id.
    OperationResult eliminar(Long id);
}
