package com.urbancollection.ecommerce.persistence.repositories;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.urbancollection.ecommerce.domain.entity.ventas.Pedido;

class PedidoRepositoryImplTest {

    private PedidoRepositoryImpl repo;

    @BeforeEach
    void setUp() {
        repo = new PedidoRepositoryImpl(); // impl real (in-memory)
    }

    @Test
    void save_y_findById_devuelve_el_mismo_pedido() {
        Pedido p = new Pedido();
        Pedido guardado = repo.save(p);
        Pedido recuperado = repo.findById(guardado.getId());

        assertNotNull(guardado.getId());
        assertEquals(guardado.getId(), recuperado.getId());
    }

    @Test
    void delete_porId_elimina_y_findById_devuelve_null() throws Exception {
        Long id = repo.save(new Pedido()).getId();

        // prueba varios nombres/firmas (Long/long)
        String[] names = { "deleteById", "delete", "removeById", "remove", "eliminarPorId" };
        boolean invoked = false;
        for (String n : names) {
            try {
                var m = repo.getClass().getMethod(n, Long.class);
                m.invoke(repo, id);
                invoked = true; break;
            } catch (NoSuchMethodException ignore) {
                try {
                    var m2 = repo.getClass().getMethod(n, long.class);
                    m2.invoke(repo, id.longValue());
                    invoked = true; break;
                } catch (NoSuchMethodException ignore2) { /* siguiente */ }
            }
        }
        assertTrue(invoked, "El repositorio no expone delete*/remove* con Long/long");

        assertNull(repo.findById(id));
    }

    @Test
    void findAll_devuelve_no_vacio_luego_de_un_save() {
        repo.save(new Pedido());
        assertFalse(repo.findAll().isEmpty());
    }
}
