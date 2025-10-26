package com.urbancollection.ecommerce.application.service;

import com.urbancollection.ecommerce.domain.base.OperationResult;
import com.urbancollection.ecommerce.domain.entity.ventas.ListaDeseos;

import java.util.List;

/**
 * IListaDeseosService
 *
 * Esta es la interfaz del servicio que maneja la lista de deseos (wishlist).
 * La idea es exponer las operaciones que el caso de uso necesita,
 * sin meter detalles de base de datos ni de controladores aquí.
 *
 * Métodos:
 *
 * agregar(usuarioId, productoId):
 *   Agrega un producto a la lista de deseos de un usuario.
 *   Devuelve OperationResult para saber si fue éxito o hubo algún problema
 *   (por ejemplo, producto ya estaba en la lista).
 *
 * quitar(listaDeseosId):
 *   Quita un registro específico de la lista de deseos.
 *   También devuelve OperationResult para reportar éxito / error.
 *
 * listarDeUsuario(usuarioId):
 *   Devuelve todos los items en la lista de deseos de ese usuario.
 */
public interface IListaDeseosService {
    OperationResult agregar(Long usuarioId, Long productoId);
    OperationResult quitar(Long listaDeseosId);
    List<ListaDeseos> listarDeUsuario(Long usuarioId);
}
