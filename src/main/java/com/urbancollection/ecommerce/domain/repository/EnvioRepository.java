package com.urbancollection.ecommerce.domain.repository;

import com.urbancollection.ecommerce.domain.entity.logistica.Envio;
import java.util.List;

public interface EnvioRepository {

    Envio save(Envio envio);

    Envio findById(Long id);

    List<Envio> findAll();

    void deleteById(Long id);
}
