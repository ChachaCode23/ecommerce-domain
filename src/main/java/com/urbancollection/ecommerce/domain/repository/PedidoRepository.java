package com.urbancollection.ecommerce.domain.repository;

import com.urbancollection.ecommerce.domain.entity.ventas.Pedido;

/**
 * Define las operaciones CRUD para la entidad Pedido.
 * Extiende de IBaseRepository para heredar las operaciones
 * básicas de guardar, buscar, eliminar y listar.
 */
public interface PedidoRepository extends IBaseRepository<Pedido, Long> {
    // Aqui puedo agregar metodos especificos de ser necesario
}
