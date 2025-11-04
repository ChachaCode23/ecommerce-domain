package com.urbancollection.ecommerce.application.service;

import java.util.List;
import java.util.Optional;

import com.urbancollection.ecommerce.domain.base.OperationResult;
import com.urbancollection.ecommerce.domain.entity.logistica.Direccion;
import com.urbancollection.ecommerce.domain.repository.DireccionRepository;

public class DireccionService implements IDireccionService {

    private final DireccionRepository direccionRepository;

    public DireccionService(DireccionRepository direccionRepository) {
        this.direccionRepository = direccionRepository;
    }

    @Override
    public List<Direccion> listar() {
        return direccionRepository.findAll();
    }

    @Override
    public Optional<Direccion> buscarPorId(Long id) {
        return Optional.ofNullable(direccionRepository.findById(id));
    }

    @Override
    public OperationResult crear(Direccion direccion) {
        if (direccion == null) {
            return OperationResult.failure("Dirección inválida");
        }
        direccionRepository.save(direccion);
        return OperationResult.success("Creada");
    }

    @Override
    public OperationResult actualizar(Long id, Direccion cambios) {
        Direccion existente = direccionRepository.findById(id);
        if (existente == null) {
            return OperationResult.failure("No existe");
        }
        if (cambios == null) {
            return OperationResult.failure("Cambios inválidos");
        }

        // Merge simple: usamos el objeto 'cambios' y aseguramos el id
        cambios.setId(id);
        direccionRepository.save(cambios);

        return OperationResult.success("Actualizada");
    }

    @Override
    public OperationResult eliminar(Long id) {
        Direccion existente = direccionRepository.findById(id);
        if (existente == null) {
            return OperationResult.failure("No existe");
        }
        // Si tu repo expone deleteById(Long), cámbialo aquí:
        direccionRepository.deleteById(id);
        return OperationResult.success("Eliminada");
    }
}
