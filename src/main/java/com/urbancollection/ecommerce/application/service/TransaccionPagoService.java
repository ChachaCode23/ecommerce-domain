package com.urbancollection.ecommerce.application.service;

import java.util.List;
import java.util.Optional;
import com.urbancollection.ecommerce.domain.base.OperationResult;
import com.urbancollection.ecommerce.domain.entity.ventas.TransaccionPago;
import com.urbancollection.ecommerce.domain.repository.TransaccionPagoRepository;

// Servicio de aplicación para manejar las transacciones de pago.
// Aquí conecto la lógica de la aplicación con el repositorio del dominio.
public class TransaccionPagoService implements ITransaccionPagoService {

    // Repositorio que se encarga de acceder a la tabla de transacciones de pago.
    private final TransaccionPagoRepository repository;

    // El repositorio se recibe por el constructor (lo inyecta Spring en la config).
    public TransaccionPagoService(TransaccionPagoRepository repository) {
        this.repository = repository;
    }

    @Override
    // Devuelve la lista completa de transacciones de pago.
    public List<TransaccionPago> listar() {
        return repository.findAll();
    }

    @Override
    // Busca una transacción de pago por id y la envuelve en un Optional.
    public Optional<TransaccionPago> buscarPorId(Long id) {
        return Optional.ofNullable(repository.findById(id));
    }

    @Override
    // Crea una nueva transacción de pago, validando que el objeto no sea null.
    public OperationResult crear(TransaccionPago t) {
        if (t == null) return OperationResult.failure("Inválido");
        repository.save(t);
        return OperationResult.success("Creado");
    }

    @Override
    // Actualiza una transacción de pago existente usando los datos de "cambios".
    public OperationResult actualizar(Long id, TransaccionPago cambios) {
        var existente = repository.findById(id);
        if (existente == null) return OperationResult.failure("No existe");
        if (cambios == null) return OperationResult.failure("Cambios inválidos");

        // persiste 'cambios' con el id asegurado
        cambios.setId(id);
        repository.save(cambios);
        return OperationResult.success("Actualizado");
    }

    @Override
    // Elimina una transacción de pago por su id, si existe.
    public OperationResult eliminar(Long id) {
        var existente = repository.findById(id);
        if (existente == null) return OperationResult.failure("No existe");
       
        repository.delete(id);
        return OperationResult.success("Eliminado");
    }
}
