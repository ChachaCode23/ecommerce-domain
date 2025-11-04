package com.urbancollection.ecommerce.persistence.repositories;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.urbancollection.ecommerce.domain.entity.ventas.TransaccionPago;

class TransaccionPagoRepositoryImplTest {

    private TransaccionPagoRepositoryImpl repo;

    @BeforeEach
    void setUp() {
        repo = new TransaccionPagoRepositoryImpl();
    }

    @Test
    void save_y_findById_devuelve_la_misma_entidad() {
        TransaccionPago t = new TransaccionPago();
        var guardado = repo.save(t);
        var recuperado = repo.findById(guardado.getId());

        assertNotNull(guardado.getId());
        assertNotNull(recuperado);
        assertEquals(guardado.getId(), recuperado.getId());
    }

    @Test
    void findAll_devuelve_no_vacio_luego_de_un_save() {
        repo.save(new TransaccionPago());
        assertFalse(repo.findAll().isEmpty());
    }

    @Test
    void delete_porId_elimina_y_findById_devuelve_null() {
        Long id = repo.save(new TransaccionPago()).getId();

        // BaseRepository expone delete(id) en las impl in-memory
        repo.delete(id);

        assertNull(repo.findById(id), "Luego de eliminar, findById debe retornar null");
    }
}
