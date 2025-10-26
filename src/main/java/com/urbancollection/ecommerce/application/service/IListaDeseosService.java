package com.urbancollection.ecommerce.application.service;

import com.urbancollection.ecommerce.domain.base.OperationResult;
import com.urbancollection.ecommerce.domain.entity.ventas.ListaDeseos;

import java.util.List;

public interface IListaDeseosService {
    OperationResult agregar(Long usuarioId, Long productoId);
    OperationResult quitar(Long listaDeseosId);
    List<ListaDeseos> listarDeUsuario(Long usuarioId);
}
