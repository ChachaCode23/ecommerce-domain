package com.urbancollection.ecommerce.application.service;

import java.util.List;
import java.util.Optional;

import com.urbancollection.ecommerce.domain.base.OperationResult;
import com.urbancollection.ecommerce.domain.entity.logistica.Direccion;
import com.urbancollection.ecommerce.domain.repository.DireccionRepository;

// Servicio de aplicación para manejar la lógica de direcciones.
// Aquí se orquesta el acceso al repositorio y se devuelven OperationResult
// para indicar si las operaciones se realizaron bien o no.
public class DireccionService implements IDireccionService {

    // Repositorio del dominio para trabajar con la entidad Direccion.
    private final DireccionRepository direccionRepository;

    // El repositorio se recibe por constructor (lo inyecta Spring en la configuración).
    public DireccionService(DireccionRepository direccionRepository) {
        this.direccionRepository = direccionRepository;
    }

    @Override
    // Devuelve la lista completa de direcciones registradas.
    public List<Direccion> listar() {
        return direccionRepository.findAll();
    }

    @Override
    // Busca una dirección por su id y la envuelve en un Optional.
    public Optional<Direccion> buscarPorId(Long id) {
        return Optional.ofNullable(direccionRepository.findById(id));
    }

    @Override
    // Crea una nueva dirección.
    // Si la dirección viene null, devuelve un resultado de error.
    public OperationResult crear(Direccion direccion) {
        if (direccion == null) {
            return OperationResult.failure("Dirección inválida");
        }
        direccionRepository.save(direccion);
        return OperationResult.success("Creada");
    }

    @Override
    // Actualiza una dirección existente.
    // Primero valida que exista y que el objeto de cambios no sea null.
    public OperationResult actualizar(Long id, Direccion cambios) {
        Direccion existente = direccionRepository.findById(id);
        if (existente == null) {
            return OperationResult.failure("No existe");
        }
        if (cambios == null) {
            return OperationResult.failure("Cambios inválidos");
        }

        // Usamos el objeto 'cambios' y nos aseguramos de que tenga el id correcto.
        cambios.setId(id);
        direccionRepository.save(cambios);

        return OperationResult.success("Actualizada");
    }

    @Override
    // Elimina una dirección por su id.
    // Si no existe, devolvemos un OperationResult indicando que no se encontró.
    public OperationResult eliminar(Long id) {
        Direccion existente = direccionRepository.findById(id);
        if (existente == null) {
            return OperationResult.failure("No existe");
        }
        
        direccionRepository.deleteById(id);
        return OperationResult.success("Eliminada");
    }
}
