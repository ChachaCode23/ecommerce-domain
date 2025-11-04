package com.urbancollection.ecommerce.application.service;

import java.util.List;
import java.util.Optional;

import com.urbancollection.ecommerce.domain.base.OperationResult;
import com.urbancollection.ecommerce.domain.entity.logistica.Direccion;

public interface IDireccionService {
    List<Direccion> listar();
    Optional<Direccion> buscarPorId(Long id);
    OperationResult crear(Direccion direccion);
    OperationResult actualizar(Long id, Direccion cambios);
    OperationResult eliminar(Long id);
}
