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

/**
 * ProductoService
 *
 * Servicio que maneja la lógica de negocio de productos.
 * Aquí controlo:
 *  - crear productos nuevos
 *  - actualizar stock
 *  - eliminar productos
 *  - listar / buscar productos
 *
 * También registro eventos útiles:
 *  - logs (para auditoría)
 *  - tareas en background cuando hay stock bajo
 */
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

    /**
     * listarProductos:
     * Devuelve todos los productos de la base.
     */
    public List<Producto> listarProductos() {
        return productoRepository.findAll();
    }

    /**
     * obtenerProductoPorId:
     * Busca un producto por id. Si el id es inválido o no existe, retorna null.
     */
    public Producto obtenerProductoPorId(Long id) {
        if (id == null || id <= 0) {
            return null;
        }
        return productoRepository.findById(id);
    }

    // ================== COMMANDS ==================

    /**
     * crearProducto:
     * Crea un producto nuevo con nombre, descripción, precio y stock inicial.
     *
     * Validaciones básicas:
     *  - nombre no puede venir vacío
     *  - precio no puede ser negativo
     *  - stock inicial no puede ser negativo
     *
     * También genero un SKU automáticamente porque en la BD ese campo es NOT NULL.
     *
     * Después de guardar:
     *  - hago logger.info con los datos creados
     *  - si el stock quedó bajo (<5) encolo una tarea en taskList
     *    para que alguien revise inventario.
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

        // Genero un SKU simple basado en el nombre + timestamp.
        // Importante porque la columna sku es NOT NULL en DB.
        String generatedSku = (nombre.trim() + "-" + System.currentTimeMillis()).toUpperCase();
        p.setSku(generatedSku);

        Producto saved = productoRepository.save(p);

        logger.info("Producto creado id={} sku={}", saved.getId(), saved.getSku());

        // Si el stock ya nace bajito, creo una tarea de alerta.
        if (saved.getStock() < 5) {
            taskList.enqueue(
                "REVISAR_STOCK",
                "Stock inicial bajo en producto " + saved.getId() + " (" + saved.getNombre() + ")"
            );
        }

        return saved;
    }

    /**
     * actualizarStock:
     * Setea el stock de un producto a un nuevo valor.
     *
     * Reglas:
     *  - productoId debe ser válido
     *  - el producto debe existir
     *  - nuevoStock no puede ser negativo
     *
     * Luego de actualizar:
     *  - log de auditoría
     *  - si el stock quedó crítico (<5) encolo una tarea de revisión
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
     * eliminarProducto:
     * Elimina un producto por ID.
     *
     * Reglas:
     *  - si el ID es inválido → false
     *  - si el producto no existe → false
     *  - si existe → se borra y devuelvo true
     *
     * También registro en logs y mando una tarea de auditoría.
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

        productoRepository.delete(productoId);

        logger.info("Producto eliminado id={}", productoId);

        taskList.enqueue(
            "AUDITORIA_BAJA_PRODUCTO",
            "Se eliminó el producto " + productoId + " (" + existente.getNombre() + ")"
        );

        return true;
    }

    // ===== Helpers para el controller =====

    /**
     * toResult:
     * Convierte un Producto (o null) a un OperationResult genérico
     * que el controller puede devolver.
     */
    public OperationResult toResult(Producto p) {
        if (p == null) {
            return OperationResult.failure("Operación inválida o producto no encontrado");
        }
        return OperationResult.success("ok");
    }

    /**
     * toDeleteResult:
     * Convierte el boolean de eliminarProducto(...) a OperationResult.
     */
    public OperationResult toDeleteResult(boolean ok) {
        if (!ok) {
            return OperationResult.failure("No se pudo eliminar el producto");
        }
        return OperationResult.success("Producto eliminado");
    }
}
