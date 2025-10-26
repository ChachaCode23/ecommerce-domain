package com.urbancollection.ecommerce.persistence.repositories;

import java.util.Collections;
import java.util.List;

import com.urbancollection.ecommerce.domain.entity.logistica.Direccion;
import com.urbancollection.ecommerce.domain.repository.DireccionRepository;

/**
 * Implementación dummy/legacy. Ya no se usa en runtime porque
 * la implementación real es DireccionRepositoryJpaAdapter (en infraestructura).
 * La dejamos solo para que el proyecto compile.
 */
public class DireccionRepositoryImpl implements DireccionRepository {

    @Override
    public Direccion findById(Long id) {
        throw new UnsupportedOperationException("DireccionRepositoryImpl ya no se usa");
    }

    @Override
    public List<Direccion> findAll() {
        return Collections.emptyList();
    }

    @Override
    public Direccion save(Direccion direccion) {
        throw new UnsupportedOperationException("DireccionRepositoryImpl ya no se usa");
    }

    @Override
    public void deleteById(Long id) {
        throw new UnsupportedOperationException("DireccionRepositoryImpl ya no se usa");
    }
}
