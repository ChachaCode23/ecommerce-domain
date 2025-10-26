package com.urbancollection.ecommerce.application.service;

import com.urbancollection.ecommerce.domain.base.OperationResult;
import com.urbancollection.ecommerce.domain.entity.ventas.ItemPedido;
import com.urbancollection.ecommerce.domain.entity.ventas.Pedido;
import com.urbancollection.ecommerce.domain.enums.MetodoDePago;

import java.math.BigDecimal;
import java.util.List;

public interface IPedidoService {
    OperationResult crearPedido(Long usuarioId, Long direccionId, List<ItemPedido> items, Long cuponId);
    OperationResult confirmarPago(Long pedidoId, MetodoDePago metodo, BigDecimal monto);
    OperationResult despacharPedido(Long pedidoId, String tracking);
    OperationResult marcarEntregado(Long pedidoId);
    
    Pedido obtenerPorId(Long id);
    List<Pedido> listarTodos();
}
