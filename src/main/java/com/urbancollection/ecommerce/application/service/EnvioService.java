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

/**
 * Servicio de aplicación para manejar los envíos.
 * Aquí se valida el estado del pedido y se actualiza tanto el envío
 * como el pedido según las reglas de negocio.
 * 
 */
public class EnvioService implements IEnvioService {

    private final EnvioRepository envioRepository;
    private final PedidoRepository pedidoRepository;

    public EnvioService(EnvioRepository envioRepository, PedidoRepository pedidoRepository) {
        this.envioRepository = envioRepository;
        this.pedidoRepository = pedidoRepository;
    }

    @Override
    public List<Envio> listar() {
        return envioRepository.findAll();
    }

    @Override
    public Optional<Envio> buscarPorId(Long id) {
        Envio envio = envioRepository.findById(id);
        return Optional.ofNullable(envio);
    }

    @Override
    @Transactional
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
    public OperationResult actualizar(Long id, Envio cambios) {
        Envio existente = envioRepository.findById(id);
        if (existente == null) {
            return OperationResult.failure("Envío no encontrado");
        }
        
        if (cambios == null) {
            return OperationResult.failure("Cambios inválidos");
        }

        // Se actualiza el estado del pedido
        if (cambios.getEstado() == EstadoDeEnvio.ENTREGADO && 
            existente.getEstado() != EstadoDeEnvio.ENTREGADO) {
            
            Pedido pedido = existente.getPedido();
            if (pedido != null) {
                pedido.setEstado(EstadoDePedido.COMPLETADO);
                pedidoRepository.save(pedido);
            }
        }

        // Persistimos el objeto 'cambios' asegurando que tenga el id correcto
        cambios.setId(id);
        envioRepository.save(cambios);

        return OperationResult.success("Envío actualizado" + 
            (cambios.getEstado() == EstadoDeEnvio.ENTREGADO ? " y pedido marcado como COMPLETADO" : ""));
    }

    @Override
    @Transactional
    public OperationResult eliminar(Long id) {
        Envio existente = envioRepository.findById(id);
        if (existente == null) {
            return OperationResult.failure("Envío no encontrado");
        }
       
        envioRepository.deleteById(id);
        return OperationResult.success("Envío eliminado correctamente");
    }
}