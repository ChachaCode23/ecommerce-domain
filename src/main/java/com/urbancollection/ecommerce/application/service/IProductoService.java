package com.urbancollection.ecommerce.application.service;

import com.urbancollection.ecommerce.application.dto.ProductoDTO;
import com.urbancollection.ecommerce.domain.base.OperationResult;
import com.urbancollection.ecommerce.domain.entity.catalogo.Producto;

import java.util.List;
import java.util.Optional;

/**
 * IProductoService
 *
 * Interfaz del servicio de productos. Define qué operaciones puede hacer
 * el caso de uso de catálogo sin hablar directo con controladores ni repositorios.
 *

 *   Devuelve todos los productos como DTO (lo que se expone hacia afuera).
 *
 *   Busca un producto específico. Uso Optional porque puede no existir.
 *
 *   Crea un producto nuevo. Devuelve OperationResult para saber si fue válido
 *   (por ejemplo, nombre duplicado, precio inválido, etc.).
 *
 *   Edita un producto existente aplicando los cambios permitidos.
 *   Borra un producto por su id.
 */
public interface IProductoService {
    List<ProductoDTO> listar();
    Optional<ProductoDTO> buscarPorId(Long id);
    OperationResult crear(Producto p);
    OperationResult actualizar(Long id, Producto cambios);
    OperationResult eliminar(Long id);
}
