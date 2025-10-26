package com.urbancollection.ecommerce.persistence.repositories;

import com.urbancollection.ecommerce.domain.entity.ventas.ItemPedido;
import com.urbancollection.ecommerce.domain.repository.ItemPedidoRepository;

import java.util.List;

/**
 * Implementación in-memory / legacy.
 * Esta clase existe solo para cumplir la interfaz en el módulo domain.
 * La implementación REAL que usa Spring Data es ItemPedidoRepositoryJpaAdapter
 * en el módulo ecommerce-api.
 *
 * Ninguno de estos métodos debería llamarse en producción.
 */
public class ItemPedidoRepositoryImpl implements ItemPedidoRepository {

    @Override
    public ItemPedido save(ItemPedido itemPedido) {
        throw new UnsupportedOperationException("In-memory ItemPedidoRepositoryImpl no está soportado aquí.");
    }

    @Override
    public ItemPedido findById(Long id) {
        throw new UnsupportedOperationException("In-memory ItemPedidoRepositoryImpl no está soportado aquí.");
    }

    @Override
    public List<ItemPedido> findAll() {
        throw new UnsupportedOperationException("In-memory ItemPedidoRepositoryImpl no está soportado aquí.");
    }

    @Override
    public void deleteById(Long id) {
        throw new UnsupportedOperationException("In-memory ItemPedidoRepositoryImpl no está soportado aquí.");
    }

    // Helper extra que tu código usa en algunos sitios.
    // No forma parte del contrato de ItemPedidoRepository, pero lo dejamos
    // para que compile cualquier llamada que ya exista.
    public boolean existsByProductoId(Long productoId) {
        throw new UnsupportedOperationException("In-memory ItemPedidoRepositoryImpl no está soportado aquí.");
    }
}
