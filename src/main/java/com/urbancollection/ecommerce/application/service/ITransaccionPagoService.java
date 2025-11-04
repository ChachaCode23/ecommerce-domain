package com.urbancollection.ecommerce.application.service;

import java.util.List;
import java.util.Optional;
import com.urbancollection.ecommerce.domain.base.OperationResult;
import com.urbancollection.ecommerce.domain.entity.ventas.TransaccionPago;

public interface ITransaccionPagoService {
    List<TransaccionPago> listar();
    Optional<TransaccionPago> buscarPorId(Long id);
    OperationResult crear(TransaccionPago t);
    OperationResult actualizar(Long id, TransaccionPago cambios);
    OperationResult eliminar(Long id);
}
