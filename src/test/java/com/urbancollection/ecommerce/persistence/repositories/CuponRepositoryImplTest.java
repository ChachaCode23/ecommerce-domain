package com.urbancollection.ecommerce.persistence.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.urbancollection.ecommerce.domain.entity.catalogo.Cupon;

class CuponRepositoryImplTest {

    private CuponRepositoryImpl repo;

    @BeforeEach
    void setUp() {
        repo = new CuponRepositoryImpl();
    }

    @Test
    void save_y_findById_devuelve_la_misma_entidad() {
        Cupon c = new Cupon();
        Cupon guardado = repo.save(c);
        Cupon recuperado = repo.findById(guardado.getId());

        assertNotNull(guardado.getId());
        assertNotNull(recuperado);
        assertEquals(guardado.getId(), recuperado.getId());
    }

    @Test
    void findAll_devuelve_no_vacio_luego_de_un_save() {
        repo.save(new Cupon());
        assertFalse(repo.findAll().isEmpty());
    }

    @Test
    void deleteById_elimina_y_findById_devuelve_null() {
        Long id = repo.save(new Cupon()).getId();

        // tu repositorio expone deleteById(Long)
        repo.deleteById(id);

        assertNull(repo.findById(id), "Luego de eliminar, findById debe retornar null");
    }
}
