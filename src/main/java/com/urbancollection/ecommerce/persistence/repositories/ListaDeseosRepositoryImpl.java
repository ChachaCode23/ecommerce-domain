package com.urbancollection.ecommerce.persistence.repositories;

import com.urbancollection.ecommerce.domain.entity.ventas.ListaDeseos;
import com.urbancollection.ecommerce.domain.repository.ListaDeseosRepository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class ListaDeseosRepositoryImpl implements ListaDeseosRepository {

    private final Map<Long, ListaDeseos> data = new ConcurrentHashMap<>();
    private final AtomicLong seq = new AtomicLong(1);

    @Override
    public ListaDeseos findById(Long id) {
        return data.get(id);
    }

    @Override
    public List<ListaDeseos> findAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public ListaDeseos save(ListaDeseos entity) {
        if (entity.getId() == null) {
            entity.setId(seq.getAndIncrement());
        }
        data.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public void delete(Long id) {
        data.remove(id);
    }

    @Override
    public boolean existsByUsuarioIdAndProductoId(Long usuarioId, Long productoId) {
        if (usuarioId == null || productoId == null) return false;
        return data.values().stream().anyMatch(ld ->
                ld.getUsuario() != null && ld.getUsuario().getId() != null &&
                ld.getUsuario().getId().equals(usuarioId) &&
                ld.getProducto() != null && ld.getProducto().getId() != null &&
                ld.getProducto().getId().equals(productoId)
        );
    }

    @Override
    public List<ListaDeseos> findByUsuarioId(Long usuarioId) {
        if (usuarioId == null) return List.of();
        List<ListaDeseos> out = new ArrayList<>();
        for (var ld : data.values()) {
            if (ld.getUsuario() != null && usuarioId.equals(ld.getUsuario().getId())) {
                out.add(ld);
            }
        }
        return out;
    }
}
