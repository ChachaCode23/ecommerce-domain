package com.urbancollection.ecommerce.application.service;

import java.util.List;
import java.util.Optional;

import com.urbancollection.ecommerce.domain.base.OperationResult;
import com.urbancollection.ecommerce.domain.entity.catalogo.Cupon;
import com.urbancollection.ecommerce.domain.repository.CuponRepository;

/**
 * Servicio de aplicación para manejar la lógica relacionada con cupones.
 * Aquí se orquesta el acceso al Repository y se devuelven OperationResult
 * para indicar si las operaciones fueron exitosas o fallaron.
 * 
 */
public class CuponService implements ICuponService {

    private final CuponRepository cuponRepository;

    public CuponService(CuponRepository cuponRepository) {
        this.cuponRepository = cuponRepository;
    }

    @Override
    public List<Cupon> listar() {
        return cuponRepository.findAll();
    }

    @Override
    public Optional<Cupon> buscarPorId(Long id) {
        Cupon cupon = cuponRepository.findById(id);
        return Optional.ofNullable(cupon);
    }

    @Override
    public OperationResult crear(Cupon cupon) {
        if (cupon == null) return OperationResult.failure("Cupón inválido");
        
        cuponRepository.save(cupon);
        return OperationResult.success("Cupón creado correctamente");
    }

    @Override
    public OperationResult actualizar(Long id, Cupon cambios) {
        Cupon existente = cuponRepository.findById(id);
        if (existente == null) return OperationResult.failure("Cupón no encontrado");
        
        if (cambios == null) return OperationResult.failure("Cambios inválidos");

        // Asegura que el id que se guarda sea el del cupón que se está actualizando
        cambios.setId(id);
        cuponRepository.save(cambios);
        return OperationResult.success("Cupón actualizado correctamente");
    }

    @Override
    public OperationResult eliminar(Long id) {
        Cupon existente = cuponRepository.findById(id);
        if (existente == null) return OperationResult.failure("Cupón no encontrado");
        
        cuponRepository.deleteById(id);
        return OperationResult.success("Cupón eliminado correctamente");
    }
}