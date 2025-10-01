package com.urbancollection.ecommerce.persistence;

import java.math.BigDecimal;
import java.util.List;

import com.urbancollection.ecommerce.domain.entity.catalogo.Producto;
import com.urbancollection.ecommerce.persistence.repositories.ProductoRepositoryImpl;

/**
 * Esta clasela cree para probar la funcionalidad de la capa de persistencia en memoria.
 * Permite realizar operaciones CRUD (crear, leer, listar y eliminar) sobre la entidad Producto.
 * 
 * Es una forma simple de validar que los repositorios funcionan correctamente
 * sin necesidad de usar una base de datos real.
 */
public class PruebaPersistencia {

    public static void main(String[] args) {

        // Se crea una instancia del repositorio de productos
        ProductoRepositoryImpl repo = new ProductoRepositoryImpl();

        // Se crea un nuevo producto
        Producto p = new Producto();
        p.setNombre("Gorra");
        p.setDescripcion("Gorra negra de 47 con logo bordado");
        p.setPrecio(new BigDecimal("950.00")); // Precio

        // Guardar el producto en memoria
        repo.save(p);

        // Buscar el producto por su ID
        Producto buscado = repo.findById(p.getId());
        System.out.println("Producto encontrado: " + buscado.getNombre());

        // Lista de todos los productos almacenados
        List<Producto> lista = repo.findAll();
        System.out.println("Total de productos: " + lista.size());

        // Elimina el producto por su ID
        repo.delete(p.getId());
        System.out.println("Producto eliminado. Total actual: " + repo.findAll().size());
    }
}
