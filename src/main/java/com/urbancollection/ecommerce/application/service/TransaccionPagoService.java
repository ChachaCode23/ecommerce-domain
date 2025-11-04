package com.urbancollection.ecommerce.application.service;

import java.util.List;
import java.util.Optional;
import com.urbancollection.ecommerce.domain.base.OperationResult;
import com.urbancollection.ecommerce.domain.entity.ventas.TransaccionPago;
import com.urbancollection.ecommerce.domain.repository.TransaccionPagoRepository;

public class TransaccionPagoService implements ITransaccionPagoService {

    private final TransaccionPagoRepository repository;

    public TransaccionPagoService(TransaccionPagoRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<TransaccionPago> listar() {
        return repository.findAll();
    }

    @Override
    public Optional<TransaccionPago> buscarPorId(Long id) {
        return Optional.ofNullable(repository.findById(id));
    }

    @Override
    public OperationResult crear(TransaccionPago t) {
        if (t == null) return OperationResult.failure("Inválido");
        repository.save(t);
        return OperationResult.success("Creado");
    }

    @Override
    public OperationResult actualizar(Long id, TransaccionPago cambios) {
        var existente = repository.findById(id);
        if (existente == null) return OperationResult.failure("No existe");
        if (cambios == null) return OperationResult.failure("Cambios inválidos");

        // merge simple: persiste 'cambios' con el id asegurado
        cambios.setId(id);
        repository.save(cambios);
        return OperationResult.success("Actualizado");
    }

    @Override
    public OperationResult eliminar(Long id) {
        var existente = repository.findById(id);
        if (existente == null) return OperationResult.failure("No existe");
        // si tu repo usa deleteById(Long), cámbialo aquí
        repository.delete(id);
        return OperationResult.success("Eliminado");
    }
}
