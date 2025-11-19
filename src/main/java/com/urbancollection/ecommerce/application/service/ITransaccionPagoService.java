package com.urbancollection.ecommerce.application.service;

import java.util.List;
import java.util.Optional;
import com.urbancollection.ecommerce.domain.base.OperationResult;
import com.urbancollection.ecommerce.domain.entity.ventas.TransaccionPago;

// Interfaz del servicio de aplicación para trabajar con transacciones de pago.
// Aquí defino qué operaciones debe ofrecer la capa de aplicación.
public interface ITransaccionPagoService {

    // Devuelve la lista completa de transacciones de pago.
    List<TransaccionPago> listar();

    // Busca una transacción de pago por su id y la envuelve en un Optional.
    Optional<TransaccionPago> buscarPorId(Long id);

    // Crea una nueva transacción de pago.
    // Usa OperationResult para indicar si la operación fue exitosa o no.
    OperationResult crear(TransaccionPago t);

    // Actualiza una transacción de pago existente usando los datos de "cambios".
    OperationResult actualizar(Long id, TransaccionPago cambios);

    // Elimina una transacción de pago por su id.
    OperationResult eliminar(Long id);
}
