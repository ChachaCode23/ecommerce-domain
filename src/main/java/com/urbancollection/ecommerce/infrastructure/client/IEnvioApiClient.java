package com.urbancollection.ecommerce.infrastructure.client;

import com.urbancollection.ecommerce.domain.entity.logistica.Envio;

import java.util.List;
import java.util.Optional;

/**
 * IEnvioApiClient
 * 
 * Interfaz que define el contrato para el cliente HTTP que consume la API REST de Envíos.
 */
public interface IEnvioApiClient {

    /**
     * Obtiene la lista completa de envíos desde la API
     * @return Lista de envíos
     */
    List<Envio> listar();

    /**
     * Busca un envío por su ID
     * @param id ID del envío
     * @return Optional con el envío si existe
     */
    Optional<Envio> buscarPorId(Long id);

    /**
     * Crea un nuevo envío en la API
     * @param envio Envío a crear
     * @return Envío creado con su ID asignado
     */
    Envio crear(Envio envio);

    /**
     * Actualiza un envío existente
     * @param id ID del envío a actualizar
     * @param envio Datos actualizados del envío
     * @return Envío actualizado
     */
    Envio actualizar(Long id, Envio envio);

    /**
     * Elimina un envío por su ID
     * @param id ID del envío a eliminar
     */
    void eliminar(Long id);
}