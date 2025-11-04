package com.urbancollection.ecommerce.application.service;

import java.util.List;
import java.util.Optional;

import com.urbancollection.ecommerce.domain.base.OperationResult;
import com.urbancollection.ecommerce.domain.entity.catalogo.Cupon;
import com.urbancollection.ecommerce.domain.repository.CuponRepository;

public class CuponService implements ICuponService {

    private final CuponRepository repository;

    public CuponService(CuponRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Cupon> listar() {
        return repository.findAll();
    }

    @Override
    public Optional<Cupon> buscarPorId(Long id) {
        return Optional.ofNullable(repository.findById(id));
    }

    @Override
    public OperationResult crear(Cupon cupon) {
        if (cupon == null) return OperationResult.failure("Cupón inválido");
        repository.save(cupon);
        return OperationResult.success("Creado");
    }

    @Override
    public OperationResult actualizar(Long id, Cupon cambios) {
        Cupon existente = repository.findById(id);
        if (existente == null) return OperationResult.failure("No existe");
        if (cambios == null) return OperationResult.failure("Cambios inválidos");

        // merge simple: persiste cambios con el id asegurado
        cambios.setId(id);
        repository.save(cambios);
        return OperationResult.success("Actualizado");
    }

    @Override
    public OperationResult eliminar(Long id) {
        Cupon existente = repository.findById(id);
        if (existente == null) return OperationResult.failure("No existe");
        // si tu repo expone deleteById(Long), cambia esta línea por deleteById(id)
        repository.deleteById(id);
        return OperationResult.success("Eliminado");
    }
}
