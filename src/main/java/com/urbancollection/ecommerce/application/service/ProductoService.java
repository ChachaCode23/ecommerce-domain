package com.urbancollection.ecommerce.application.service;

import com.urbancollection.ecommerce.shared.BaseService;   
import com.urbancollection.ecommerce.shared.ValidationUtil; 

import com.urbancollection.ecommerce.application.dto.ProductoDTO;     
import com.urbancollection.ecommerce.application.mapper.ProductoMapper; 

import com.urbancollection.ecommerce.domain.base.OperationResult; 
import com.urbancollection.ecommerce.domain.entity.catalogo.Producto; 
import com.urbancollection.ecommerce.domain.repository.ProductoRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Servicio de aplicación para gestionar Productos.
 * - Herencia: extiende BaseService para usar logger y manejo centralizado de errores.
 * - NO cambia las entidades: solo valida, mapea a DTO y llama al repositorio.
 */
public class ProductoService extends BaseService {

    // Dependencia a la capa de datos.
    private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    /** 
     * Convierte un Producto a DTO usando el Mapper.
     * - Responsabilidad: aislar la conversión en un único punto del servicio.
     * - try/catch: por si algo falla en el mapeo (evitar romper el flujo).
     */
    public ProductoDTO getModel(Producto producto) {
        try {
            return ProductoMapper.toDTO(producto); // Llama al mapper.
        } catch (Exception e) {
            handleError(e, "Error al generar el modelo DTO de Producto"); // Log + manejo de error centralizado.
            return null; // En caso de error, retorna null.
        }
    }

    //aqui se obtienen todos los productos en formato DTO.

    public List<ProductoDTO> listar() {
        try {
            List<Producto> productos = productoRepository.findAll(); // Consulta todos los productos.
            return productos.stream()
                    .map(ProductoMapper::toDTO) // Mapea cada entidad a DTO.
                    .collect(Collectors.toList());
        } catch (Exception e) {
            handleError(e, "Error al listar productos"); // Log + manejo de error.
            return java.util.Collections.emptyList(); // Fallback seguro.
        }
    }

    /**
     * Buscar un producto por ID y retornarlo como DTO.
     * - Nota: el repositorio devuelve Producto (no Optional), por eso aquí envolvemos manualmente.
     * - Retorna Optional.empty() si no existe.
     */
    public Optional<ProductoDTO> buscarPorId(Long id) {
        try {
            Producto producto = productoRepository.findById(id); // Busca por ID (puede ser null si no existe).
            if (producto == null) return Optional.empty();
            return Optional.of(ProductoMapper.toDTO(producto)); // Lo convertimos a DTO si existe.
        } catch (Exception e) {
            handleError(e, "Error al buscar producto por id");
            return Optional.empty(); // En error, también devuelve vacío.
        }
    }

    //Aqui se Crea un nuevo producto.

    public OperationResult crear(Producto nuevo) {
        try {
            normalizar(nuevo); // Limpieza previa (ej: quitar espacios en el nombre).

            // Valida el DTO (usando el mapper para construirlo desde la entidad).
            ProductoDTO dto = ProductoMapper.toDTO(nuevo);
            OperationResult dtoCheck = ValidationUtil.validate(dto);
            if (!dtoCheck.isSuccess()) return dtoCheck; // Si falla, cortar aquí.

            // Valida la Entidad.
            OperationResult entityCheck = ValidationUtil.validate(nuevo);
            if (!entityCheck.isSuccess()) return entityCheck;

            //Validaciones manuales de campos (extra a las anotaciones).
            OperationResult valid = validarCamposProducto(nuevo);
            if (!valid.isSuccess()) return valid;

            // Reglas de negocio para creación (ej: nombre único).
            OperationResult reglas = validarNegocioCrear(nuevo);
            if (!reglas.isSuccess()) return reglas;

            //Guarda en la BD.
            productoRepository.save(nuevo);
            logger.info("Producto creado: {}", nuevo.getNombre()); // Log de auditoría.
            return OperationResult.success("Producto creado correctamente");
        } catch (Exception e) {
            handleError(e, "Error al crear producto"); // Manejo centralizado de errores.
            return OperationResult.failure("No se pudo crear el producto");
        }
    }

    // Aqui Actualizamos un producto existente.

    public OperationResult actualizar(Long id, Producto cambios) {
        try {
            Producto existente = productoRepository.findById(id); //Carga desde BD.
            if (existente == null) return OperationResult.failure("Producto no encontrado");

            normalizar(cambios); //Limpieza de entradas.

            // Valida DTO (construido a partir de los cambios).
            ProductoDTO dtoCambios = ProductoMapper.toDTO(cambios);
            OperationResult dtoCheck = ValidationUtil.validate(dtoCambios);
            if (!dtoCheck.isSuccess()) return dtoCheck;

            //Valida Entidad (cambios completos sobre el objeto).
            OperationResult entityCheck = ValidationUtil.validate(cambios);
            if (!entityCheck.isSuccess()) return entityCheck;

            //Validaciones manuales + Reglas de negocio de actualización.
            OperationResult valid = validarCamposProducto(cambios);
            if (!valid.isSuccess()) return valid;

            OperationResult reglas = validarNegocioActualizar(existente, cambios);
            if (!reglas.isSuccess()) return reglas;

            //Aplica cambios puntuales (solo actualiza campos).
            existente.setNombre(cambios.getNombre());
            existente.setPrecio(cambios.getPrecio());
            existente.setStock(cambios.getStock());
            productoRepository.save(existente); // Guardar cambios en la BD.

            logger.info("Producto actualizado: {}", existente.getNombre());
            return OperationResult.success("Producto actualizado correctamente");
        } catch (Exception e) {
            handleError(e, "Error al actualizar producto");
            return OperationResult.failure("No se pudo actualizar el producto");
        }
    }

    /**
     * Elimina un producto por ID.
     * - Primero verifica existencia para poder responder con mensaje claro.
     * - Si existe, elimina y registra log.
     */
    public OperationResult eliminar(Long id) {
        try {
            Producto existente = productoRepository.findById(id); // Verifica si existe.
            if (existente == null) return OperationResult.failure("Producto no encontrado");
            productoRepository.delete(id); // Elimina en la BD.
            logger.info("Producto eliminado id={}", id); // Log de auditoría.
            return OperationResult.success("Producto eliminado correctamente");
        } catch (Exception e) {
            handleError(e, "Error al eliminar producto");
            return OperationResult.failure("No se pudo eliminar el producto");
        }
    }


    /**
     * Aqui hacemos validaciones manuales de los atributos basicos del producto.
     * - Nombre: requerido y no vacío.
     * - Precio: > 0.
     * - Stock: no negativo.
     * Devuelve OperationResult con "OK" si todo está bien.
     */
    private OperationResult validarCamposProducto(Producto p) {
        if (p == null) return OperationResult.failure("Producto requerido");
        if (p.getNombre() == null || p.getNombre().trim().isEmpty())
            return OperationResult.failure("El nombre es obligatorio");

        BigDecimal precio = p.getPrecio();
        if (precio == null || precio.compareTo(BigDecimal.ZERO) <= 0)
            return OperationResult.failure("El precio debe ser mayor que 0");

        if (p.getStock() < 0)
            return OperationResult.failure("El stock no puede ser negativo");

        return OperationResult.success("OK"); // Todo correcto.
    }


    // Validaciones de negocio
    /**
     * - Evitamos duplicados por nombre (case-insensitive).
     */
    private OperationResult validarNegocioCrear(Producto p) {
        boolean duplicado = productoRepository.findAll().stream()
                .anyMatch(x -> x.getNombre().equalsIgnoreCase(p.getNombre())); // Compara ignorando may/min.
        if (duplicado) return OperationResult.failure("Ya existe un producto con ese nombre");
        return OperationResult.success("OK");
    }

    /**
     * - Si el nombre cambio, volver a verificar duplicados por nombre.
     */
    private OperationResult validarNegocioActualizar(Producto existente, Producto cambios) {
        if (!existente.getNombre().equalsIgnoreCase(cambios.getNombre())) {
            boolean duplicado = productoRepository.findAll().stream()
                    .anyMatch(x -> x.getNombre().equalsIgnoreCase(cambios.getNombre()));
            if (duplicado) return OperationResult.failure("Ya existe otro producto con ese nombre");
        }
        return OperationResult.success("OK");
    }


    /**
     * - Limpia entradas tipicas antes de validar/guardar.
     * - Aquí aplicamos trim() al nombre para evitar espacios extra.
     */
    private void normalizar(Producto p) {
        if (p != null && p.getNombre() != null) {
            p.setNombre(p.getNombre().trim()); // Quita espacios al inicio/fin.
        }
    }
}
