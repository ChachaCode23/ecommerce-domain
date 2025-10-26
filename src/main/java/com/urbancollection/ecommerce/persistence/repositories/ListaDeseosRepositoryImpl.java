package com.urbancollection.ecommerce.persistence.repositories;

import com.urbancollection.ecommerce.domain.entity.ventas.ListaDeseos;
import com.urbancollection.ecommerce.domain.repository.ListaDeseosRepository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * ListaDeseosRepositoryImpl
 *
 * Implementación en memoria (sin base de datos real) de la interfaz
 * ListaDeseosRepository. Esto es útil para pruebas locales o ambientes
 * donde todavía no tenemos la parte JPA lista.
 *
 * Internamente usamos:
 * - ConcurrentHashMap<Long, ListaDeseos> para guardar los registros.
 * - AtomicLong seq para ir generando IDs simulando una PK autoincremental.
 *
 * También implementamos reglas básicas como:
 * - evitar duplicados en la wishlist del mismo usuario
 * - listar los deseos por usuario
 */
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
        // Si no tiene id, le asignamos uno nuevo simulando autoincrement.
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

    /**
     * existsByUsuarioIdAndProductoId:
     * Revisa si ya existe un registro con ese usuario y ese producto.
     * Esto sirve para no meter el mismo producto dos veces en la misma wishlist.
     */
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

    /**
     * findByUsuarioId:
     * Devuelve toda la lista de deseos de un usuario específico.
     */
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
