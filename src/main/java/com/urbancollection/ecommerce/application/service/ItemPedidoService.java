package com.urbancollection.ecommerce.application.service;

import java.util.List;
import java.util.Optional;

import com.urbancollection.ecommerce.domain.base.OperationResult;
import com.urbancollection.ecommerce.domain.entity.ventas.ItemPedido;
import com.urbancollection.ecommerce.domain.repository.ItemPedidoRepository;

public class ItemPedidoService implements IItemPedidoService {

    private final ItemPedidoRepository repository;

    public ItemPedidoService(ItemPedidoRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<ItemPedido> listar() {
        return repository.findAll();
    }

    @Override
    public Optional<ItemPedido> buscarPorId(Long id) {
        return Optional.ofNullable(repository.findById(id));
    }

    @Override
    public OperationResult crear(ItemPedido item) {
        if (item == null) {
            return OperationResult.failure("Item inválido");
        }
        repository.save(item);
        return OperationResult.success("Creado");
    }

    @Override
    public OperationResult actualizar(Long id, ItemPedido cambios) {
        ItemPedido existente = repository.findById(id);
        if (existente == null) {
            return OperationResult.failure("No existe");
        }
        if (cambios == null) {
            return OperationResult.failure("Cambios inválidos");
        }

        // Merge simple: persistimos 'cambios' asegurando el id
        cambios.setId(id);
        repository.save(cambios);

        return OperationResult.success("Actualizado");
    }

    @Override
    public OperationResult eliminar(Long id) {
        ItemPedido existente = repository.findById(id);
        if (existente == null) {
            return OperationResult.failure("No existe");
        }
        // Si tu repo expone deleteById(Long), cambia esta línea por deleteById(id)
        repository.deleteById(id);
        return OperationResult.success("Eliminado");
    }
}
