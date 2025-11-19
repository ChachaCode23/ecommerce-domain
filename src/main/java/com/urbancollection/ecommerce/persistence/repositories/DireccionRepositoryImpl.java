package com.urbancollection.ecommerce.persistence.repositories;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import com.urbancollection.ecommerce.domain.entity.logistica.Direccion;
import com.urbancollection.ecommerce.domain.repository.DireccionRepository;

public class DireccionRepositoryImpl implements DireccionRepository {

    private static final Map<Long, Direccion> STORE = new ConcurrentHashMap<>();
    private static final AtomicLong SEQ = new AtomicLong(0);

    @Override
    public Direccion save(Direccion d) {
        if (d == null) return null;
        if (d.getId() == null) d.setId(SEQ.incrementAndGet());
        STORE.put(d.getId(), d);
        return d;
    }

    @Override
    public Direccion findById(Long id) {
        return STORE.get(id);
    }

    @Override
    public List<Direccion> findAll() {
        return new ArrayList<>(STORE.values());
    }

    //  Buscar dirección principal por usuario
    public Direccion findPrincipalByUsuarioId(Integer usuarioId) {
        if (usuarioId == null) return null;
        return STORE.values().stream()
                .filter(d -> d.getUsuarioId() != null && d.getUsuarioId().equals(usuarioId))
                .filter(d -> d.getEsPrincipal() != null && d.getEsPrincipal())
                .findFirst()
                .orElse(null);
    }

    //  Busca todas las direcciones de un usuario
    public List<Direccion> findByUsuarioId(Integer usuarioId) {
        if (usuarioId == null) return new ArrayList<>();
        return STORE.values().stream()
                .filter(d -> d.getUsuarioId() != null && d.getUsuarioId().equals(usuarioId))
                .toList();
    }

    public void delete(Long id) {
        STORE.remove(id);
    }

    public void deleteById(Long id) {
        STORE.remove(id);
    }

    public static void resetForTests() {
        STORE.clear();
        SEQ.set(0);
    }
}