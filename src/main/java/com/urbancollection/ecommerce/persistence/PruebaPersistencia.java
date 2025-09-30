package com.urbancollection.ecommerce.persistence;

import java.math.BigDecimal;
import java.util.List;

import com.urbancollection.ecommerce.domain.entity.catalogo.Producto;
import com.urbancollection.ecommerce.persistence.repositories.ProductoRepositoryImpl;

public class PruebaPersistencia {
    public static void main(String[] args) {

        ProductoRepositoryImpl repo = new ProductoRepositoryImpl();

        Producto p = new Producto();
        p.setNombre("Gorra");
        p.setDescripcion("Gorra negra de 47 con logo bordado");
        p.setPrecio(new BigDecimal("950.00")); // Precio en pesos dominicanos

        // Guardar
        repo.save(p);

        // Buscar
        Producto buscado = repo.findById(p.getId());
        System.out.println("Producto encontrado: " + buscado.getNombre());

        // Listar
        List<Producto> lista = repo.findAll();
        System.out.println("Total de productos: " + lista.size());

        // Eliminar
        repo.delete(p.getId());
        System.out.println("Producto eliminado. Total actual: " + repo.findAll().size());
    }
}
