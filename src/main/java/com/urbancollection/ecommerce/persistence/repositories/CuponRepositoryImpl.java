package com.urbancollection.ecommerce.persistence.repositories;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import com.urbancollection.ecommerce.domain.entity.catalogo.Cupon;
import com.urbancollection.ecommerce.domain.repository.CuponRepository;

public class CuponRepositoryImpl implements CuponRepository {

    private final Map<Long, Cupon> data = new HashMap<>();
    private final AtomicLong seq = new AtomicLong(0);

    @Override
    public Cupon save(Cupon cupon) {
        if (cupon.getId() == null) {
            cupon.setId(seq.incrementAndGet());
        }
        data.put(cupon.getId(), cupon);
        return cupon;
    }

    @Override
    public Cupon findById(Long id) {
        return data.get(id);
    }

    @Override
    public List<Cupon> findAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public void deleteById(Long id) {
        // 🔥 Aquí estaba el problema: antes no eliminaba del mapa
        data.remove(id);
    }
}
