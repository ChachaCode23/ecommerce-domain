package com.urbancollection.ecommerce.application.service;

import java.math.BigDecimal;
import java.util.List;

import jakarta.transaction.Transactional;

import com.urbancollection.ecommerce.domain.base.OperationResult;
import com.urbancollection.ecommerce.domain.entity.catalogo.Producto;
import com.urbancollection.ecommerce.domain.repository.ProductoRepository;
import com.urbancollection.ecommerce.shared.BaseService;
import com.urbancollection.ecommerce.shared.logging.LoggerPort;
import com.urbancollection.ecommerce.shared.tasks.TaskListPort;

public class ProductoService extends BaseService {

    private final ProductoRepository productoRepository;
    private final LoggerPort logger;
    private final TaskListPort taskList;

    public ProductoService(
            ProductoRepository productoRepository,
            LoggerPort logger,
            TaskListPort taskList
    ) {
        this.productoRepository = productoRepository;
        this.logger = logger;
        this.taskList = taskList;
    }

    // ================== QUERIES ==================

    public List<Producto> listarProductos() {
        return productoRepository.findAll();
    }

    public Producto obtenerProductoPorId(Long id) {
        if (id == null || id <= 0) {
            return null;
        }
        return productoRepository.findById(id);
    }

    // ================== COMMANDS ==================

    /**
     * Crea un producto nuevo
     */
    @Transactional
    public Producto crearProducto(String nombre,
                                  String descripcion,
                                  BigDecimal precio,
                                  int stockInicial) {

        if (nombre == null || nombre.isBlank()) {
            logger.warn("crearProducto rechazado: nombre vacío");
            return null;
        }
        if (precio == null || precio.compareTo(BigDecimal.ZERO) < 0) {
            logger.warn("crearProducto rechazado: precio negativo {}", precio);
            return null;
        }
        if (stockInicial < 0) {
            logger.warn("crearProducto rechazado: stock negativo {}", stockInicial);
            return null;
        }

        Producto p = new Producto();
        p.setNombre(nombre);
        p.setDescripcion(descripcion);
        p.setPrecio(precio);
        p.setStock(stockInicial);

        // IMPORTANTE: en tu DB la columna sku es NOT NULL
        String generatedSku = (nombre.trim() + "-" + System.currentTimeMillis()).toUpperCase();
        p.setSku(generatedSku);

        Producto saved = productoRepository.save(p);

        logger.info("Producto creado id={} sku={}", saved.getId(), saved.getSku());

        if (saved.getStock() < 5) {
            taskList.enqueue(
                "REVISAR_STOCK",
                "Stock inicial bajo en producto " + saved.getId() + " (" + saved.getNombre() + ")"
            );
        }

        return saved;
    }

    /**
     * Actualiza el stock (setea a nuevoStock)
     */
    @Transactional
    public Producto actualizarStock(Long productoId, Integer nuevoStock) {
        if (productoId == null || productoId <= 0) {
            logger.warn("actualizarStock rechazado: productoId invalido {}", productoId);
            return null;
        }
        if (nuevoStock == null || nuevoStock < 0) {
            logger.warn("actualizarStock rechazado: stock invalido {}", nuevoStock);
            return null;
        }

        Producto existente = productoRepository.findById(productoId);
        if (existente == null) {
            logger.warn("actualizarStock: producto {} no encontrado", productoId);
            return null;
        }

        existente.setStock(nuevoStock);
        Producto actualizado = productoRepository.save(existente);

        logger.info("Stock actualizado producto={} stock={}", productoId, nuevoStock);

        if (nuevoStock < 5) {
            taskList.enqueue(
                "REVISAR_STOCK",
                "Stock crítico en producto " + productoId + " -> " + nuevoStock
            );
        }

        return actualizado;
    }

    /**
     * Borra un producto por ID
     */
    @Transactional
    public boolean eliminarProducto(Long productoId) {
        if (productoId == null || productoId <= 0) {
            logger.warn("eliminarProducto rechazado: productoId invalido {}", productoId);
            return false;
        }

        Producto existente = productoRepository.findById(productoId);
        if (existente == null) {
            logger.warn("eliminarProducto: producto {} no existe", productoId);
            return false;
        }

        // tu ProductoRepository debe tener algo tipo delete(Long id)
        productoRepository.delete(productoId);

        logger.info("Producto eliminado id={}", productoId);

        taskList.enqueue(
            "AUDITORIA_BAJA_PRODUCTO",
            "Se eliminó el producto " + productoId + " (" + existente.getNombre() + ")"
        );

        return true;
    }

    // ===== helpers que usa el controller para empaquetar respuesta =====

    public OperationResult toResult(Producto p) {
        if (p == null) {
            return OperationResult.failure("Operación inválida o producto no encontrado");
        }
        // IMPORTANTE: OperationResult.success(...) en tu proyecto
        // solo acepta mensaje (no acepta data extra).
        return OperationResult.success("ok");
    }

    public OperationResult toDeleteResult(boolean ok) {
        if (!ok) {
            return OperationResult.failure("No se pudo eliminar el producto");
        }
        return OperationResult.success("Producto eliminado");
    }
}
