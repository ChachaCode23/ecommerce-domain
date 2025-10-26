package com.urbancollection.ecommerce.application.service;

import com.urbancollection.ecommerce.application.dto.ProductoDTO;
import com.urbancollection.ecommerce.domain.base.OperationResult;
import com.urbancollection.ecommerce.domain.entity.catalogo.Producto;

import java.util.List;
import java.util.Optional;

public interface IProductoService {
    List<ProductoDTO> listar();
    Optional<ProductoDTO> buscarPorId(Long id);
    OperationResult crear(Producto p);
    OperationResult actualizar(Long id, Producto cambios);
    OperationResult eliminar(Long id);
}
