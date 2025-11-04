package com.urbancollection.ecommerce.application.service;

import java.util.List;
import java.util.Optional;

import com.urbancollection.ecommerce.domain.base.OperationResult;
import com.urbancollection.ecommerce.domain.entity.logistica.Envio;
import com.urbancollection.ecommerce.domain.repository.EnvioRepository;

public class EnvioService implements IEnvioService {

    private final EnvioRepository envioRepository;

    public EnvioService(EnvioRepository envioRepository) {
        this.envioRepository = envioRepository;
    }

    @Override
    public List<Envio> listar() {
        return envioRepository.findAll();
    }

    @Override
    public Optional<Envio> buscarPorId(Long id) {
        return Optional.ofNullable(envioRepository.findById(id));
    }

    @Override
    public OperationResult crear(Envio envio) {
        if (envio == null) {
            return OperationResult.failure("Envío inválido");
        }
        envioRepository.save(envio);
        return OperationResult.success("Creado");
    }

    @Override
    public OperationResult actualizar(Long id, Envio cambios) {
        Envio existente = envioRepository.findById(id);
        if (existente == null) {
            return OperationResult.failure("No existe");
        }
        if (cambios == null) {
            return OperationResult.failure("Cambios inválidos");
        }

        // Merge simple: persistimos 'cambios' asegurando el id
        cambios.setId(id);
        envioRepository.save(cambios);

        return OperationResult.success("Actualizado");
    }

    @Override
    public OperationResult eliminar(Long id) {
        Envio existente = envioRepository.findById(id);
        if (existente == null) {
            return OperationResult.failure("No existe");
        }
        // Si tu repo expone deleteById(Long), cambia esta línea por deleteById(id)
        envioRepository.deleteById(id);
        return OperationResult.success("Eliminado");
    }
}
