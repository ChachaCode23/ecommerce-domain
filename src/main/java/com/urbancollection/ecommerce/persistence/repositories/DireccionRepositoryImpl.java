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
