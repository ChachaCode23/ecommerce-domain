package com.urbancollection.ecommerce.persistence.repositories;

import java.util.Map;

import com.urbancollection.ecommerce.domain.entity.ventas.ItemPedido;
import com.urbancollection.ecommerce.domain.repository.ItemPedidoRepository;
import com.urbancollection.ecommerce.persistence.base.BaseRepository;
import com.urbancollection.ecommerce.persistence.context.InMemoryContext;

/**
 * Implementacin en memoria del repositorio de ItemPedido.
 * 
 * Permite almacenar, buscar, eliminar y listar items de pedido
 * usando una estructura en memoria.
 */
public class ItemPedidoRepositoryImpl extends BaseRepository<ItemPedido> implements ItemPedidoRepository {

    @Override
    protected Map<Long, ItemPedido> store(InMemoryContext ctx) {
        return ctx.itemsPedido;
    }
}
