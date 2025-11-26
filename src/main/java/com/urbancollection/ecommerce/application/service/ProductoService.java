package com.urbancollection.ecommerce.application.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.urbancollection.ecommerce.application.dto.ProductoDTO;
import com.urbancollection.ecommerce.domain.base.OperationResult;
import com.urbancollection.ecommerce.domain.entity.catalogo.Producto;
import com.urbancollection.ecommerce.domain.repository.ProductoRepository;
import com.urbancollection.ecommerce.shared.logging.LoggerPort;
import com.urbancollection.ecommerce.shared.tasks.TaskListPort;

/**
 * Servicio de aplicación para gestionar productos.
 * Implementa IProductoService para uso en controladores web y API REST.
 */
public class ProductoService implements IProductoService {

    private final ProductoRepository productoRepository;
    
    public ProductoService(ProductoRepository productoRepository,
                           LoggerPort loggerPort,
                           TaskListPort taskListPort) {
        this.productoRepository = productoRepository;
    }
    
    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    // ==================== MÉTODOS DE IProductoService ====================

    @Override
    public List<ProductoDTO> listar() {
        List<Producto> productos = productoRepository.findAll();
        return productos.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ProductoDTO> buscarPorId(Long id) {
        if (id == null) return Optional.empty();
        Producto producto = productoRepository.findById(id);
        return Optional.ofNullable(producto).map(this::toDTO);
    }

    @Override
    public OperationResult crear(Producto p) {
        try {
            if (p == null) {
                return OperationResult.failure("El producto es obligatorio");
            }

            if (p.getNombre() == null || p.getNombre().trim().isEmpty()) {
                return OperationResult.failure("El nombre del producto es obligatorio");
            }

            if (p.getPrecio() == null || p.getPrecio().compareTo(BigDecimal.ZERO) <= 0) {
                return OperationResult.failure("El precio debe ser mayor a 0");
            }

            productoRepository.save(p);
            return OperationResult.success("Producto creado correctamente");
            
        } catch (Exception e) {
            return OperationResult.failure("Error al crear el producto: " + e.getMessage());
        }
    }

    @Override
    public OperationResult actualizar(Long id, Producto cambios) {
        try {
            if (id == null) {
                return OperationResult.failure("El ID es obligatorio");
            }

            Producto existente = productoRepository.findById(id);
            if (existente == null) {
                return OperationResult.failure("Producto no encontrado");
            }

            if (cambios == null) {
                return OperationResult.failure("Los datos del producto son obligatorios");
            }

            // Actualizar campos
            if (cambios.getNombre() != null) {
                existente.setNombre(cambios.getNombre());
            }
            if (cambios.getDescripcion() != null) {
                existente.setDescripcion(cambios.getDescripcion());
            }
            if (cambios.getPrecio() != null) {
                existente.setPrecio(cambios.getPrecio());
            }
            // Stock es primitivo int, siempre actualizar
            existente.setStock(cambios.getStock());

            productoRepository.save(existente);
            return OperationResult.success("Producto actualizado correctamente");
            
        } catch (Exception e) {
            return OperationResult.failure("Error al actualizar el producto: " + e.getMessage());
        }
    }

    @Override
    public OperationResult eliminar(Long id) {
        try {
            if (id == null) {
                return OperationResult.failure("El ID es obligatorio");
            }

            Producto producto = productoRepository.findById(id);
            if (producto == null) {
                return OperationResult.failure("Producto no encontrado");
            }

            productoRepository.delete(id);
            return OperationResult.success("Producto eliminado correctamente");
            
        } catch (Exception e) {
            return OperationResult.failure("Error al eliminar el producto: " + e.getMessage());
        }
    }

    // ==================== MÉTODOS LEGACY (para compatibilidad) ====================

    /**
     * Crea un producto (método legacy para tests).
     */
    public Producto crearProducto(String nombre, String descripcion, BigDecimal precio, int stock) {
        Producto producto = new Producto();
        producto.setNombre(nombre);
        producto.setDescripcion(descripcion);
        producto.setPrecio(precio);
        producto.setStock(stock);
        return productoRepository.save(producto);
    }

    /**
     * Lista todos los productos (método legacy).
     */
    public List<Producto> listarProductos() {
        return productoRepository.findAll();
    }

    /**
     * Busca un producto por ID (método legacy).
     */
    public Producto obtenerProductoPorId(Long id) {
        if (id == null) return null;
        return productoRepository.findById(id);
    }

    /**
     * Actualiza el stock de un producto (método legacy).
     */
    public Producto actualizarStock(Long id, Integer nuevoStock) {
        if (id == null || nuevoStock == null) {
            return null;
        }

        Producto p = productoRepository.findById(id);
        if (p == null) {
            return null;
        }

        p.setStock(nuevoStock);
        return productoRepository.save(p);
    }

    /**
     * Elimina un producto por ID 
     */
    public boolean eliminarProducto(Long id) {
        if (id == null) return false;

        Producto p = productoRepository.findById(id);
        if (p == null) {
            return false;
        }

        productoRepository.delete(id);
        return true;
    }

    // ==================== MÉTODOS AUXILIARES ====================

    private ProductoDTO toDTO(Producto p) {
        ProductoDTO dto = new ProductoDTO();
        dto.setId(p.getId());
        dto.setNombre(p.getNombre());
        dto.setDescripcion(p.getDescripcion());
        dto.setPrecio(p.getPrecio());
        dto.setStock(p.getStock());
        return dto;
    }
}