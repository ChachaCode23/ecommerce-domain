package com.urbancollection.ecommerce.application.service;

import java.util.List;
import java.util.Optional;

import com.urbancollection.ecommerce.domain.base.OperationResult;
import com.urbancollection.ecommerce.domain.entity.catalogo.Cupon;

// Interfaz del servicio de aplicación para trabajar con cupones.
// Aquí defino qué operaciones debe ofrecer el servicio,
// la implementación real está en la clase CuponService.
public interface ICuponService {

    // Devuelve la lista completa de cupones registrados.
    List<Cupon> listar();

    // Busca un cupón por su id y lo envuelve en un Optional.
    // Si no existe, el Optional vendrá vacío.
    Optional<Cupon> buscarPorId(Long id);

    // Crea un nuevo cupón.
    // Usa OperationResult para indicar si la operación fue exitosa o si hubo errores.
    OperationResult crear(Cupon cupon);

    // Actualiza un cupón existente identificado por su id usando los datos de "cambios".
    OperationResult actualizar(Long id, Cupon cambios);

    // Elimina un cupón por su id.
    OperationResult eliminar(Long id);
}
