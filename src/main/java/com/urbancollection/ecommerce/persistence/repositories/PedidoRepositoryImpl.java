package com.urbancollection.ecommerce.persistence.repositories;

import java.util.Map;

import com.urbancollection.ecommerce.domain.entity.ventas.Pedido;
import com.urbancollection.ecommerce.domain.repository.PedidoRepository;
import com.urbancollection.ecommerce.persistence.base.BaseRepository;
import com.urbancollection.ecommerce.persistence.context.InMemoryContext;

/**
 * Implementación en memoria del repositorio de Pedido.
 * 
 * Permite almacenar, buscar, eliminar y listar pedidos
 * usando una estructura en memoria.
 */
public class PedidoRepositoryImpl extends BaseRepository<Pedido> implements PedidoRepository {

    @Override
    protected Map<Long, Pedido> store(InMemoryContext ctx) {
        return ctx.pedidos;
    }
}
