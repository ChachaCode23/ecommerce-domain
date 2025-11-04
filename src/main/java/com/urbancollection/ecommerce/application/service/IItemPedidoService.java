package com.urbancollection.ecommerce.application.service;

import java.util.List;
import java.util.Optional;

import com.urbancollection.ecommerce.domain.base.OperationResult;
import com.urbancollection.ecommerce.domain.entity.ventas.ItemPedido;

public interface IItemPedidoService {
    List<ItemPedido> listar();
    Optional<ItemPedido> buscarPorId(Long id);
    OperationResult crear(ItemPedido item);
    OperationResult actualizar(Long id, ItemPedido cambios);
    OperationResult eliminar(Long id);
}
