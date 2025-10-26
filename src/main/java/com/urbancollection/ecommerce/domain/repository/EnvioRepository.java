package com.urbancollection.ecommerce.domain.repository;

import com.urbancollection.ecommerce.domain.entity.logistica.Envio;
import java.util.List;

/**
 * EnvioRepository
 *
 * Este es el contrato (interfaz) para trabajar con envíos en la capa de dominio.
 * La idea es que la lógica de negocio (services) use esta interfaz,
 * sin importar si por debajo estamos usando JPA, SQL directo, etc.
 *
 * Métodos básicos:
 * - save(...)        -> guardar o actualizar un Envio
 * - findById(id)     -> buscar un Envio por su id (o null si no existe)
 * - findAll()        -> listar todos los envíos
 * - deleteById(id)   -> eliminar un envío por id
 */
public interface EnvioRepository {

    Envio save(Envio envio);

    Envio findById(Long id);

    List<Envio> findAll();

    void deleteById(Long id);
}
