package com.urbancollection.ecommerce.domain.repository;

import com.urbancollection.ecommerce.domain.entity.catalogo.Cupon;
import java.util.List;

/**
 * Puerto de dominio para Cupon.
 * No usar anotaciones de Spring aquí.
 */
public interface CuponRepository {

    Cupon save(Cupon cupon);

    Cupon findById(Long id);

    List<Cupon> findAll();

    void deleteById(Long id);
}
