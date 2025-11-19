package com.urbancollection.ecommerce.application.service;

import java.util.List;
import java.util.Optional;

import com.urbancollection.ecommerce.domain.base.OperationResult;
import com.urbancollection.ecommerce.domain.entity.catalogo.Cupon;
import com.urbancollection.ecommerce.domain.repository.CuponRepository;

// Servicio de aplicación para manejar la lógica relacionada con cupones.
// Aquí se orquesta el acceso al repositorio y se devuelven OperationResult
// para indicar si las operaciones fueron exitosas o fallaron.
public class CuponService implements ICuponService {

    // Repositorio del dominio para acceder a la tabla de cupones.
    private final CuponRepository repository;

    // El repositorio se inyecta por constructor.
    public CuponService(CuponRepository repository) {
        this.repository = repository;
    }

    @Override
    // Devuelve la lista completa de cupones.
    public List<Cupon> listar() {
        return repository.findAll();
    }

    @Override
    // Busca un cupón por su id y lo envuelve en un Optional.
    public Optional<Cupon> buscarPorId(Long id) {
        return Optional.ofNullable(repository.findById(id));
    }

    @Override
    // Crea un nuevo cupón.
    // Si el cupón es null, devuelve un OperationResult de error.
    public OperationResult crear(Cupon cupon) {
        if (cupon == null) return OperationResult.failure("Cupón inválido");
        repository.save(cupon);
        return OperationResult.success("Creado");
    }

    @Override
    // Actualiza un cupón existente.
    // Primero verifica que exista y que los cambios no sean null.
    public OperationResult actualizar(Long id, Cupon cambios) {
        Cupon existente = repository.findById(id);
        if (existente == null) return OperationResult.failure("No existe");
        if (cambios == null) return OperationResult.failure("Cambios inválidos");

        // Asegura que el id que se guarda sea el del cupón que se está actualizando.
        cambios.setId(id);
        repository.save(cambios);
        return OperationResult.success("Actualizado");
    }

    @Override
    // Elimina un cupón por id.
    // Si no existe, devuelve un OperationResult indicando que no se encontró.
    public OperationResult eliminar(Long id) {
        Cupon existente = repository.findById(id);
        if (existente == null) return OperationResult.failure("No existe");
        
        repository.deleteById(id);
        return OperationResult.success("Eliminado");
    }
}
