package com.urbancollection.ecommerce.domain.repository;

import java.util.List;

/**
 * Esta interfaz define las operaciones básicas (CRUD)
 * que cualquier repositorio debe implementar.
 *
 * @param <T>  Tipo de la entidad
 * @param <ID> Tipo del identificador (por ejemplo, Long)
 */
public interface IBaseRepository<T, ID> {

    /**
     * Guarda una entidad en el repositorio.
     * @param entity Entidad a guardar
     * @return Entidad guardada
     */
    T save(T entity);

    /**
     * Busca una entidad por su identificador.
     * @param id Identificador único
     * @return Entidad encontrada o null si no existe
     */
    T findById(ID id);

    /**
     * Elimina una entidad por su identificador.
     * @param id Identificador único
     */
    void delete(ID id);

    /**
     * Lista todas las entidades almacenadas.
     * @return Lista de entidades
     */
    List<T> findAll();
}
