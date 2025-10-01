package com.urbancollection.ecommerce.persistence.base;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import com.urbancollection.ecommerce.domain.base.BaseEntity;
import com.urbancollection.ecommerce.domain.repository.IBaseRepository;
import com.urbancollection.ecommerce.persistence.context.InMemoryContext;

/**
 * Esta clase representa un repositorio base en memoria que implementa las operaciones CRUD.
 * Es una clase abstracta que sirve como plantilla para los repositorios especificos.
 *
 * Funciona con un contexto en memoria (InMemoryContext) y un generador automatico de IDs.
 * 
 * Las clases hijas deben indicar en que "almacen" se guardaran los objetos.
 *
 * Metodos implementados:
 * - save(): guarda una entidad nueva o actualiza una existente.
 * - findById(): busca una entidad por su ID.
 * - delete(): elimina una entidad segun su ID.
 * - findAll(): devuelve una lista con todas las entidades almacenadas.
 */
public abstract class BaseRepository<T extends BaseEntity>
        implements IBaseRepository<T, Long> {

    // Contexto en memoria que actua como base de datos simulada
    private final InMemoryContext ctx = InMemoryContext.getInstance();

    // Generador de IDs automaticos (incrementa por cada registro nuevo)
    private final AtomicLong sequence = new AtomicLong(1L);

    /**
     * Metodo abstracto que las subclases deben implementar.
     * Debe devolver el mapa donde se almacenan las entidades de tipo T.
     */
    protected abstract Map<Long, T> store(InMemoryContext ctx);

    /**
     * Guarda una nueva entidad o actualiza una existente.
     * Si la entidad no tiene ID, se le asigna uno nuevo automaticamente.
     */
    @Override
    public T save(T entity) {
        if (entity.getId() == null) {
            entity.setId(sequence.getAndIncrement());
        }
        store(ctx).put(entity.getId(), entity);
        return entity;
    }

    /**
     * Busca una entidad por su ID.
     * Retorna null si el ID no existe.
     */
    @Override
    public T findById(Long id) {
        return store(ctx).get(id);
    }

    /**
     * Elimina una entidad del almacenamiento usando su ID.
     */
    @Override
    public void delete(Long id) {
        store(ctx).remove(id);
    }

    /**
     * Retorna una lista con todas las entidades almacenadas en memoria.
     */
    @Override
    public List<T> findAll() {
        return new ArrayList<>(store(ctx).values());
    }
}
