package com.urbancollection.ecommerce.application.service;

import java.util.List;
import java.util.Optional;

import com.urbancollection.ecommerce.domain.base.OperationResult;
import com.urbancollection.ecommerce.domain.entity.logistica.Envio;
import com.urbancollection.ecommerce.domain.entity.ventas.Pedido;
import com.urbancollection.ecommerce.domain.enums.EstadoDeEnvio;
import com.urbancollection.ecommerce.domain.enums.EstadoDePedido;
import com.urbancollection.ecommerce.domain.repository.EnvioRepository;
import com.urbancollection.ecommerce.domain.repository.PedidoRepository;

import jakarta.transaction.Transactional;

// Servicio de aplicación para manejar los envíos.
// Aquí se valida el estado del pedido y se actualiza tanto el envío
// como el pedido según las reglas de negocio.
public class EnvioService implements IEnvioService {

    // Repositorio para acceder y guardar envíos en la base de datos.
    private final EnvioRepository envioRepository;
    // Repositorio para consultar y actualizar pedidos relacionados con el envío.
    private final PedidoRepository pedidoRepository;

    // Constructor donde se inyectan los repositorios necesarios.
    public EnvioService(EnvioRepository envioRepository, PedidoRepository pedidoRepository) {
        this.envioRepository = envioRepository;
        this.pedidoRepository = pedidoRepository;
    }

    @Override
    // Devuelve la lista completa de envíos registrados.
    public List<Envio> listar() {
        return envioRepository.findAll();
    }

    @Override
    // Busca un envío por id y lo envuelve en un Optional.
    public Optional<Envio> buscarPorId(Long id) {
        return Optional.ofNullable(envioRepository.findById(id));
    }

    @Override
    @Transactional
    // Crea un nuevo envío y actualiza el estado del pedido si corresponde.
    public OperationResult crear(Envio envio) {
        if (envio == null) {
            return OperationResult.failure("Envío inválido");
        }
        
        // Validar que el pedido asociado esté PAGADO
        if (envio.getPedido() != null) {
            Pedido pedido = envio.getPedido();
            if (pedido.getEstado() != EstadoDePedido.PAGADO) {
                return OperationResult.failure("Solo se pueden crear envíos para pedidos PAGADOS");
            }
            
            // Cambiar pedido a ENVIADO al crear el envío
            pedido.setEstado(EstadoDePedido.ENVIADO);
            pedidoRepository.save(pedido);
        }
        
        envioRepository.save(envio);
        return OperationResult.success("Envío creado y pedido marcado como ENVIADO");
    }

    @Override
    @Transactional
    // Actualiza un envío existente y, si pasa a ENTREGADO, marca el pedido como COMPLETADO.
    public OperationResult actualizar(Long id, Envio cambios) {
        Envio existente = envioRepository.findById(id);
        if (existente == null) {
            return OperationResult.failure("No existe");
        }
        if (cambios == null) {
            return OperationResult.failure("Cambios inválidos");
        }

       
        // se actualiza el estado del pedido
        if (cambios.getEstado() == EstadoDeEnvio.ENTREGADO && 
            existente.getEstado() != EstadoDeEnvio.ENTREGADO) {
            
            Pedido pedido = existente.getPedido();
            if (pedido != null) {
                pedido.setEstado(EstadoDePedido.COMPLETADO);
                pedidoRepository.save(pedido);
            }
        }

        // Persistimos el objeto 'cambios' asegurando que tenga el id correcto.
        cambios.setId(id);
        envioRepository.save(cambios);

        return OperationResult.success("Envío actualizado" + 
            (cambios.getEstado() == EstadoDeEnvio.ENTREGADO ? " y pedido marcado como COMPLETADO" : ""));
    }

    @Override
    @Transactional
    // Elimina un envío si existe en la base de datos.
    public OperationResult eliminar(Long id) {
        Envio existente = envioRepository.findById(id);
        if (existente == null) {
            return OperationResult.failure("No existe");
        }
       
        envioRepository.deleteById(id);
        return OperationResult.success("Eliminado");
    }
}
