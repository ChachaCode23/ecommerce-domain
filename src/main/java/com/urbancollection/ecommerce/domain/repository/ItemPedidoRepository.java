package com.urbancollection.ecommerce.domain.repository;

import java.util.List;

import com.urbancollection.ecommerce.domain.entity.ventas.ItemPedido;

public interface ItemPedidoRepository {

    ItemPedido save(ItemPedido itemPedido);

    ItemPedido findById(Long id);

    List<ItemPedido> findAll();

    void deleteById(Long id);
}
