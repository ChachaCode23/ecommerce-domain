package com.urbancollection.ecommerce.domain.repository;

import java.util.List;

import com.urbancollection.ecommerce.domain.entity.ventas.ItemPedido;

/**
 * ItemPedidoRepository
 *
 * Interfaz de acceso a datos para los items dentro de un pedido.
 * La capa de negocio usa esta interfaz sin saber si la implementación
 * final es JPA, SQL directo, etc.
 *
 * Métodos básicos:
 * - save(itemPedido): guarda o actualiza un item del pedido.
 * - findById(id): busca un item por id (devuelve null si no existe).
 * - findAll(): devuelve todos los items guardados.
 * - deleteById(id): elimina un itemPedido por su id.
 */
public interface ItemPedidoRepository {

    ItemPedido save(ItemPedido itemPedido);

    ItemPedido findById(Long id);

    List<ItemPedido> findAll();

    void deleteById(Long id);
}
