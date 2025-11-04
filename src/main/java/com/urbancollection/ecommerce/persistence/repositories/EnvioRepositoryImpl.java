package com.urbancollection.ecommerce.persistence.repositories;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import com.urbancollection.ecommerce.domain.entity.logistica.Envio;
import com.urbancollection.ecommerce.domain.repository.EnvioRepository;

public class EnvioRepositoryImpl implements EnvioRepository {

    private static final Map<Long, Envio> STORE = new ConcurrentHashMap<>();
    private static final AtomicLong SEQ = new AtomicLong(0);

    @Override
    public Envio save(Envio e) {
        if (e == null) return null;
        if (e.getId() == null) e.setId(SEQ.incrementAndGet());
        STORE.put(e.getId(), e);
        return e;
    }

    @Override
    public Envio findById(Long id) {
        return STORE.get(id);
    }

    @Override
    public List<Envio> findAll() {
        return new ArrayList<>(STORE.values());
    }

    // Soporte para ambas firmas usadas en tests/servicio
    public void delete(Long id) {
        STORE.remove(id);
    }

    public void deleteById(Long id) {
        STORE.remove(id);
    }

    // Útil para aislar pruebas, si lo necesitas
    public static void resetForTests() {
        STORE.clear();
        SEQ.set(0);
    }
}
