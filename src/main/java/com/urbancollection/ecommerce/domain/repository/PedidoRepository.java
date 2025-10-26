package com.urbancollection.ecommerce.domain.repository;

import java.util.List;

import com.urbancollection.ecommerce.domain.entity.ventas.Pedido;

public interface PedidoRepository {

    Pedido findById(Long id);

    List<Pedido> findAll();

    Pedido save(Pedido pedido);

    void delete(Long id);
}
