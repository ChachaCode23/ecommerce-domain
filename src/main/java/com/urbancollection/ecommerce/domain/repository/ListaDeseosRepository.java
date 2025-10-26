package com.urbancollection.ecommerce.domain.repository;

import com.urbancollection.ecommerce.domain.entity.ventas.ListaDeseos;
import java.util.List;

public interface ListaDeseosRepository extends IBaseRepository<ListaDeseos, Long> {

    // Evitar duplicados: consultar por usuario+producto
    boolean existsByUsuarioIdAndProductoId(Long usuarioId, Long productoId);

    // Listar por usuario (útil para UI)
    List<ListaDeseos> findByUsuarioId(Long usuarioId);
}
