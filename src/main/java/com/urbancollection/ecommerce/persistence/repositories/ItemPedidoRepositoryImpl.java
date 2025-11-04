package com.urbancollection.ecommerce.persistence.repositories;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import com.urbancollection.ecommerce.domain.entity.ventas.ItemPedido;
import com.urbancollection.ecommerce.domain.repository.ItemPedidoRepository;

public class ItemPedidoRepositoryImpl implements ItemPedidoRepository {

    private static final Map<Long, ItemPedido> STORE = new ConcurrentHashMap<>();
    private static final AtomicLong SEQ = new AtomicLong(0);

    @Override
    public ItemPedido save(ItemPedido it) {
        if (it == null) return null;
        if (it.getId() == null) it.setId(SEQ.incrementAndGet());
        STORE.put(it.getId(), it);
        return it;
    }

    @Override
    public ItemPedido findById(Long id) {
        return STORE.get(id);
    }

    @Override
    public List<ItemPedido> findAll() {
        return new ArrayList<>(STORE.values());
    }

    // Soporta ambas variantes de borrado
    public void delete(Long id) {
        STORE.remove(id);
    }

    public void deleteById(Long id) {
        STORE.remove(id);
    }

    // Útil si quieres limpiar entre tests
    public static void resetForTests() {
        STORE.clear();
        SEQ.set(0);
    }
}
