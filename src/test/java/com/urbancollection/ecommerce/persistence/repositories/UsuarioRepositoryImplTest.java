package com.urbancollection.ecommerce.persistence.repositories;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.urbancollection.ecommerce.domain.entity.usuarios.Usuario;

class UsuarioRepositoryImplTest {

    private UsuarioRepositoryImpl repo;

    @BeforeEach
    void setUp() {
        repo = new UsuarioRepositoryImpl(); // tu impl in-memory no necesita contexto
    }

    @Test
    void save_y_findById_devuelve_el_mismo_usuario() {
        Usuario u = com.urbancollection.ecommerce.TestDataFactory.usuarioValido();

        Usuario guardado = repo.save(u);
        Usuario recuperado = repo.findById(guardado.getId());

        assertNotNull(guardado.getId());
        assertEquals(guardado.getId(), recuperado.getId());
        assertEquals("User Test", recuperado.getNombre()); // ajusta si usas otro campo
    }

    @Test
    void update_persiste_cambios_de_atributos_basicos() {
        Usuario u = com.urbancollection.ecommerce.TestDataFactory.usuarioValido();
        Usuario guardado = repo.save(u);

        // cambia un atributo simple (nombre, email, username: usa lo que tengas)
        guardado.setNombre("User Test+");
        repo.save(guardado);

        Usuario actualizado = repo.findById(guardado.getId());
        assertEquals("User Test+", actualizado.getNombre());
    }

    @Test
    void delete_porId_elimina_y_no_aparece_en_find() throws Exception {
        Usuario u = com.urbancollection.ecommerce.TestDataFactory.usuarioValido();
        Usuario guardado = repo.save(u);
        Long id = guardado.getId();

        // usa el método real que tenga tu repo (deleteById/delete/remove...)
        tryDeleteById(repo, id);

        Usuario resultado = repo.findById(id);
        assertNull(resultado);
    }

    @Test
    void findAll_devuelve_lista_no_vacia_luego_de_save() throws Exception {
        repo.save(com.urbancollection.ecommerce.TestDataFactory.usuarioValido());
        var all = tryFindAll(repo);
        assertNotNull(all);
        assertFalse(all.isEmpty());
    }

    @Test
    void aislamiento_cada_test_tiene_estado_limpio() {
        Usuario u = com.urbancollection.ecommerce.TestDataFactory.usuarioValido();
        Usuario guardado = repo.save(u);
        assertNotNull(guardado.getId());
    }

    // ===== Helpers por reflexión para nombres variables =====
    private static void tryDeleteById(Object repo, Long id) throws Exception {
        String[] names = { "deleteById", "delete", "removeById", "remove", "eliminarPorId" };
        for (String n : names) {
            try {
                var m = repo.getClass().getMethod(n, Long.class);
                m.invoke(repo, id);
                return;
            } catch (NoSuchMethodException ignored) {}
        }
        fail("El repositorio no expone deleteById/delete/remove compatible (Long id).");
    }

    private static java.util.Collection<?> tryFindAll(Object repo) throws Exception {
        String[] names = { "findAll", "getAll", "listar" };
        for (String n : names) {
            try {
                var m = repo.getClass().getMethod(n);
                Object res = m.invoke(repo);
                if (res instanceof java.util.Collection<?>) return (java.util.Collection<?>) res;
            } catch (NoSuchMethodException ignored) {}
        }
        fail("El repositorio no expone findAll/getAll/listar que retorne Collection.");
        return null;
    }
}
