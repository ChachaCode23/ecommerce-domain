package com.urbancollection.ecommerce.infrastructure.client;

import com.urbancollection.ecommerce.domain.entity.catalogo.Producto;

import java.util.List;
import java.util.Optional;

/**
 * IProductoApiClient
 * 
 * Interfaz que define el contrato para el cliente HTTP que consume la API REST de Productos.
 */
public interface IProductoApiClient {

    /**
     * Obtiene la lista completa de productos desde la API
     * @return Lista de productos
     */
    List<Producto> listar();

    /**
     * Busca un producto por su ID
     * @param id ID del producto
     * @return Optional con el producto si existe
     */
    Optional<Producto> buscarPorId(Long id);

    /**
     * Crea un nuevo producto en la API
     * @param producto Producto a crear
     * @return Producto creado con su ID asignado
     */
    Producto crear(Producto producto);

    /**
     * Actualiza un producto existente
     * @param id ID del producto a actualizar
     * @param producto Datos actualizados del producto
     * @return Producto actualizado
     */
    Producto actualizar(Long id, Producto producto);

    /**
     * Elimina un producto por su ID
     * @param id ID del producto a eliminar
     */
    void eliminar(Long id);
}