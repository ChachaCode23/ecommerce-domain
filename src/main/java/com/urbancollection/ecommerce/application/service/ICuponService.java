package com.urbancollection.ecommerce.application.service;

import java.util.List;
import java.util.Optional;

import com.urbancollection.ecommerce.domain.base.OperationResult;
import com.urbancollection.ecommerce.domain.entity.catalogo.Cupon;

public interface ICuponService {
    List<Cupon> listar();
    Optional<Cupon> buscarPorId(Long id);
    OperationResult crear(Cupon cupon);
    OperationResult actualizar(Long id, Cupon cambios);
    OperationResult eliminar(Long id);
}
