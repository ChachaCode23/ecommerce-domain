package com.urbancollection.ecommerce.domain.repository;

import java.util.List;

public interface IBaseRepository<T, ID> {

	T save(T entity);        // Guardar
    T findById(ID id);       // Buscar por id
    void delete(ID id);      // Eliminar
    List<T> findAll();       // Listar todos
}
