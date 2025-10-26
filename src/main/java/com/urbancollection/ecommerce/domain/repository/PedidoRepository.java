package com.urbancollection.ecommerce.domain.repository;

import java.util.List;

import com.urbancollection.ecommerce.domain.entity.ventas.Pedido;

/**
 * PedidoRepository
 *
 * Interfaz del dominio para trabajar con pedidos.
 * Esto permite que la capa de negocio no dependa directamente de JPA.
 *
 * Métodos:
 *
 * findById(id):
 *   Busca un pedido por su id. Devuelve null si no existe.
 *
 * findAll():
 *   Devuelve todos los pedidos.
 *
 * save(pedido):
 *   Inserta o actualiza un pedido (junto con sus datos relevantes).
 *
 * delete(id):
 *   Borra un pedido por id.
 */
public interface PedidoRepository {

    Pedido findById(Long id);

    List<Pedido> findAll();

    Pedido save(Pedido pedido);

    void delete(Long id);
}
