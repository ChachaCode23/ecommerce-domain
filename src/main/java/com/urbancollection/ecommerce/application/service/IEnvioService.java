package com.urbancollection.ecommerce.application.service;

import java.util.List;
import java.util.Optional;

import com.urbancollection.ecommerce.domain.base.OperationResult;
import com.urbancollection.ecommerce.domain.entity.logistica.Envio;

public interface IEnvioService {
    List<Envio> listar();
    Optional<Envio> buscarPorId(Long id);
    OperationResult crear(Envio envio);
    OperationResult actualizar(Long id, Envio cambios);
    OperationResult eliminar(Long id);
}
