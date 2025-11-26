package com.urbancollection.ecommerce.infrastructure.client;

import com.urbancollection.ecommerce.domain.entity.catalogo.Cupon;

import java.util.List;
import java.util.Optional;

/**
 * ICuponApiClient
 * 
 * Interfaz que define el contrato para el cliente HTTP que consume la API REST de Cupones.
 */
public interface ICuponApiClient {

    /**
     * Obtiene la lista completa de cupones desde la API
     * @return Lista de cupones
     */
    List<Cupon> listar();

    /**
     * Busca un cupón por su ID
     * @param id ID del cupón
     * @return Optional con el cupón si existe
     */
    Optional<Cupon> buscarPorId(Long id);

    /**
     * Crea un nuevo cupón en la API
     * @param cupon Cupón a crear
     * @return Cupón creado con su ID asignado
     */
    Cupon crear(Cupon cupon);

    /**
     * Actualiza un cupón existente
     * @param id ID del cupón a actualizar
     * @param cupon Datos actualizados del cupón
     * @return Cupón actualizado
     */
    Cupon actualizar(Long id, Cupon cupon);

    /**
     * Elimina un cupón por su ID
     * @param id ID del cupón a eliminar
     */
    void eliminar(Long id);
}