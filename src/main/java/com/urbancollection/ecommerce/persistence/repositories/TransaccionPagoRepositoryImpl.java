package com.urbancollection.ecommerce.persistence.repositories;

import java.util.Map;

import com.urbancollection.ecommerce.domain.entity.ventas.TransaccionPago;
import com.urbancollection.ecommerce.domain.repository.TransaccionPagoRepository;
import com.urbancollection.ecommerce.persistence.base.BaseRepository;
import com.urbancollection.ecommerce.persistence.context.InMemoryContext;

/**
 * Implementa en memoria del repositorio de TransaccionPago.
 * 
 * Permite almacenar, buscar, eliminar y listar transacciones de pago
 * usando una estructura en memoria, ideal para pruebas sin base de datos.
 */
public class TransaccionPagoRepositoryImpl 
        extends BaseRepository<TransaccionPago> 
        implements TransaccionPagoRepository {

    @Override
    protected Map<Long, TransaccionPago> store(InMemoryContext ctx) {
        return ctx.transacciones;
    }
}
