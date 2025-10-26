package com.urbancollection.ecommerce.domain.repository;

import com.urbancollection.ecommerce.domain.entity.logistica.Direccion;
import java.util.List;

public interface DireccionRepository {

    Direccion findById(Long id);

    List<Direccion> findAll();

    Direccion save(Direccion direccion);

    void deleteById(Long id);
}
