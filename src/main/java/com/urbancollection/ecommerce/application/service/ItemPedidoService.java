package com.urbancollection.ecommerce.application.service;

import java.util.List;
import java.util.Optional;

import com.urbancollection.ecommerce.domain.base.OperationResult;
import com.urbancollection.ecommerce.domain.entity.ventas.ItemPedido;
import com.urbancollection.ecommerce.domain.repository.ItemPedidoRepository;

// Servicio de aplicación para manejar los items de pedido.
// Aquí  conecto la lógica de la aplicación con el repositorio del dominio.
public class ItemPedidoService implements IItemPedidoService {

    // Repositorio para acceder a los items de pedido en la base de datos.
    private final ItemPedidoRepository repository;

    // El repositorio se recibe por constructor (lo inyecta la configuración de Spring).
    public ItemPedidoService(ItemPedidoRepository repository) {
        this.repository = repository;
    }

    @Override
    // Devuelve la lista completa de items de pedido.
    public List<ItemPedido> listar() {
        return repository.findAll();
    }

    @Override
    // Busca un item de pedido por su id y lo envuelve en un Optional.
    public Optional<ItemPedido> buscarPorId(Long id) {
        return Optional.ofNullable(repository.findById(id));
    }

    @Override
    // Crea un nuevo item de pedido, validando que el objeto no sea null.
    public OperationResult crear(ItemPedido item) {
        if (item == null) {
            return OperationResult.failure("Item inválido");
        }
        repository.save(item);
        return OperationResult.success("Creado");
    }

    @Override
    // Actualiza un item de pedido existente usando los datos del parámetro "cambios".
    public OperationResult actualizar(Long id, ItemPedido cambios) {
        ItemPedido existente = repository.findById(id);
        if (existente == null) {
            return OperationResult.failure("No existe");
        }
        if (cambios == null) {
            return OperationResult.failure("Cambios inválidos");
        }

        //persistimos 'cambios' asegurando el id
        cambios.setId(id);
        repository.save(cambios);

        return OperationResult.success("Actualizado");
    }

    @Override
    // Elimina un item de pedido por su id, solo si existe en la base de datos.
    public OperationResult eliminar(Long id) {
        ItemPedido existente = repository.findById(id);
        if (existente == null) {
            return OperationResult.failure("No existe");
        }
        
        repository.deleteById(id);
        return OperationResult.success("Eliminado");
    }
}
