package com.urbancollection.ecommerce.persistence.repositories;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.urbancollection.ecommerce.domain.entity.logistica.Envio;

class EnvioRepositoryImplTest {

    private EnvioRepositoryImpl repo;

    @BeforeEach
    void setUp() {
        repo = new EnvioRepositoryImpl(); // implementación real (in-memory)
    }

    @Test
    void save_y_findById_devuelve_la_misma_entidad() {
        Envio e = new Envio();
        Envio guardado = repo.save(e);
        Envio recuperado = repo.findById(guardado.getId());

        assertNotNull(guardado.getId());
        assertEquals(guardado.getId(), recuperado.getId());
    }

    @Test
    void delete_porId_elimina_y_findById_devuelve_null() throws Exception {
        Long id = repo.save(new Envio()).getId();

        // intenta distintas firmas/nombres: (Long) y (long)
        String[] names = { "deleteById", "delete", "removeById", "remove", "eliminarPorId" };
        boolean invoked = false;

        for (String n : names) {
            try {
                // (Long)
                var m = repo.getClass().getMethod(n, Long.class);
                m.invoke(repo, id);
                invoked = true;
                break;
            } catch (NoSuchMethodException ignore) {
                try {
                    // (long)
                    var m2 = repo.getClass().getMethod(n, long.class);
                    m2.invoke(repo, id.longValue());
                    invoked = true;
                    break;
                } catch (NoSuchMethodException ignore2) { /* probar siguiente nombre */ }
            }
        }

        assertTrue(invoked, "El repositorio no expone delete/deleteById compatible con Long/long");

        assertNull(repo.findById(id), "Luego de eliminar, findById debe retornar null");
    }


    @Test
    void findAll_devuelve_no_vacio_luego_de_un_save() {
        repo.save(new Envio());
        assertFalse(repo.findAll().isEmpty());
    }
}
