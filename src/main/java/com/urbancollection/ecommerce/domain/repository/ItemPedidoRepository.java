package com.urbancollection.ecommerce.domain.repository;

import com.urbancollection.ecommerce.domain.entity.ventas.ItemPedido;

/** 
 * Esta interfaz define las operaciones CRUD para la entidad ItemPedido.
 * Extiende de IBaseRepository para heredar los métodos básicos
 * de guardar, buscar, eliminar y listar.
 */
public interface ItemPedidoRepository extends IBaseRepository<ItemPedido, Long> {
    // Aquí podria agregar métodos específicos de ser necesario.
}
