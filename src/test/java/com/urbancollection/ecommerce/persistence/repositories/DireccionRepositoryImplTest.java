package com.urbancollection.ecommerce.persistence.repositories;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.urbancollection.ecommerce.domain.entity.logistica.Direccion;

class DireccionRepositoryImplTest {

    private DireccionRepositoryImpl repo;

    @BeforeEach
    void setUp() {
        repo = new DireccionRepositoryImpl(); // implementación real (in-memory)
    }

    @Test
    void save_y_findById_devuelve_la_misma_entidad() {
        Direccion d = new Direccion();
        Direccion g = repo.save(d);
        Direccion r = repo.findById(g.getId());

        assertNotNull(g.getId());
        assertEquals(g.getId(), r.getId());
    }

    @Test
    void delete_porId_elimina_y_findById_devuelve_null() {
        Long id = repo.save(new Direccion()).getId();

        // La impl real expone ambos; usamos deleteById directo
        repo.deleteById(id);

        assertNull(repo.findById(id));
    }

    @Test
    void findAll_devuelve_no_vacio_luego_de_un_save() {
        repo.save(new Direccion());
        assertFalse(repo.findAll().isEmpty());
    }
}
