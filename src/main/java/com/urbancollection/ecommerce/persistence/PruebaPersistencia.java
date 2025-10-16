package com.urbancollection.ecommerce.persistence;

import java.math.BigDecimal;
import java.util.List;

import com.urbancollection.ecommerce.domain.entity.catalogo.Producto;
import com.urbancollection.ecommerce.persistence.repositories.ProductoRepositoryImpl;

public class PruebaPersistencia {

    public static void main(String[] args) {
        ProductoRepositoryImpl repo = new ProductoRepositoryImpl();

        // 1) CREAR
        Producto p = new Producto();
        p.setNombre("Gorra");
        p.setDescripcion("Gorra negra de 47 con logo bordado");
        p.setPrecio(new BigDecimal("950.00"));
        p.setStock(10);

        repo.save(p);

        // Asegura que el repositorio haya asignado un ID
        Long id = p.getId();
        if (id == null) {
            System.out.println("❌ ERROR: El repositorio no asignó ID al guardar el producto.");
            return;
        }
        System.out.println("✅ Creado producto id=" + id);

        // 2) BUSCAR POR ID (seguro)
        Producto buscado = repo.findById(id);
        if (buscado == null) {
            System.out.println("❌ Producto no encontrado por id=" + id);
            return;
        }
        System.out.println("🔎 Encontrado: " + buscado.getNombre() + " | $" + buscado.getPrecio());

        // 3) LISTAR
        List<Producto> lista = repo.findAll();
        System.out.println("📦 Total productos: " + lista.size());
        for (Producto it : lista) {
            System.out.println(" - id=" + it.getId() + " | " + it.getNombre() + " | $" + it.getPrecio());
        }

        // 4) ACTUALIZAR (ejemplo: cambiar precio y stock)
        buscado.setPrecio(new BigDecimal("899.00"));
        buscado.setStock(8);
        repo.save(buscado);

        Producto actualizado = repo.findById(id);
        System.out.println("♻️ Actualizado: id=" + actualizado.getId()
                + " | precio=" + actualizado.getPrecio()
                + " | stock=" + actualizado.getStock());

        // 5) ELIMINAR
        repo.delete(id);
        System.out.println("🗑️ Eliminado id=" + id + ". Total ahora: " + repo.findAll().size());
    }
}
