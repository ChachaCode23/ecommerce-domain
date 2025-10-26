package com.urbancollection.ecommerce.domain.repository;

import com.urbancollection.ecommerce.domain.entity.logistica.Direccion;
import java.util.List;

/**
 * DireccionRepository
 *
 * Interfaz del dominio para manejar direcciones de envío.
 * La capa de negocio habla con esto, no directamente con JPA.
 *
 * Métodos:
 * - findById(id): busca una dirección por su id (o null si no existe).
 * - findAll(): lista todas las direcciones guardadas.
 * - save(direccion): crea o actualiza una dirección.
 * - deleteById(id): elimina por id.
 */
public interface DireccionRepository {

    Direccion findById(Long id);

    List<Direccion> findAll();

    Direccion save(Direccion direccion);

    void deleteById(Long id);
}
