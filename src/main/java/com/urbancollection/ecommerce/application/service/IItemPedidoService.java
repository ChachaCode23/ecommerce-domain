package com.urbancollection.ecommerce.application.service;

import java.util.List;
import java.util.Optional;

import com.urbancollection.ecommerce.domain.base.OperationResult;
import com.urbancollection.ecommerce.domain.entity.ventas.ItemPedido;

// Interfaz del servicio de aplicación para trabajar con los items de un pedido.
// Aquí defino las operaciones que deben implementarse en la capa de aplicación.
public interface IItemPedidoService {

    // Devuelve la lista completa de items de pedido.
    List<ItemPedido> listar();

    // Busca un item de pedido por su id y lo envuelve en un Optional.
    // Si no existe, el Optional vendrá vacío.
    Optional<ItemPedido> buscarPorId(Long id);

    // Crea un nuevo item de pedido.
    // Retorna un OperationResult para indicar si la operación fue exitosa o no.
    OperationResult crear(ItemPedido item);

    // Actualiza un item de pedido existente identificado por su id,
    // usando los datos que vienen en el objeto "cambios".
    OperationResult actualizar(Long id, ItemPedido cambios);

    // Elimina un item de pedido por su id.
    OperationResult eliminar(Long id);
}
