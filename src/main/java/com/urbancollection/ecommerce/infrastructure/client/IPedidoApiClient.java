package com.urbancollection.ecommerce.infrastructure.client;

import com.urbancollection.ecommerce.domain.entity.ventas.Pedido;

import java.util.List;
import java.util.Optional;

/**
 * IPedidoApiClient
 * 
 * Interfaz que define el contrato para el cliente HTTP que consume la API REST de Pedidos.
 */
public interface IPedidoApiClient {

    /**
     * Obtiene la lista completa de pedidos desde la API
     * @return Lista de pedidos
     */
    List<Pedido> listar();

    /**
     * Busca un pedido por su ID
     * @param id ID del pedido
     * @return Optional con el pedido si existe
     */
    Optional<Pedido> buscarPorId(Long id);

    /**
     * Crea un nuevo pedido en la API
     * @param pedido Pedido a crear
     * @return Pedido creado con su ID asignado
     */
    Pedido crear(Pedido pedido);

    /**
     * Actualiza un pedido existente
     * @param id ID del pedido a actualizar
     * @param pedido Datos actualizados del pedido
     * @return Pedido actualizado
     */
    Pedido actualizar(Long id, Pedido pedido);

    /**
     * Elimina un pedido por su ID
     * @param id ID del pedido a eliminar
     */
    void eliminar(Long id);
}