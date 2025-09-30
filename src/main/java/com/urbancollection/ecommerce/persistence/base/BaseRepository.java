package com.urbancollection.ecommerce.persistence.base;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import com.urbancollection.ecommerce.domain.base.BaseEntity;
import com.urbancollection.ecommerce.domain.repository.IBaseRepository;
import com.urbancollection.ecommerce.persistence.context.InMemoryContext;

/**
 * Repositorio base en memoria que implementa el contrato:
 *   T save(T entity)
 *   T findById(Long id)
 *   void delete(Long id)
 *   List<T> findAll()
 *
 * Nota: aquí fijamos el ID como Long para aprovechar el autogenerador.
 */
public abstract class BaseRepository<T extends BaseEntity>
        implements IBaseRepository<T, Long> {

    private final InMemoryContext ctx = InMemoryContext.getInstance();
    private final AtomicLong sequence = new AtomicLong(1L);

    /** Subclases deben devolver el "almacén" (tabla en memoria) donde guardan T. */
    protected abstract Map<Long, T> store(InMemoryContext ctx);

    @Override
    public T save(T entity) {
        if (entity.getId() == null) {
            entity.setId(sequence.getAndIncrement());
        }
        store(ctx).put(entity.getId(), entity);
        return entity;
    }

    @Override
    public T findById(Long id) {
        return store(ctx).get(id); // retorna null si no existe, acorde a tu interfaz
    }

    @Override
    public void delete(Long id) {
        store(ctx).remove(id);
    }

    @Override
    public List<T> findAll() {
        return new ArrayList<>(store(ctx).values());
    }
}
