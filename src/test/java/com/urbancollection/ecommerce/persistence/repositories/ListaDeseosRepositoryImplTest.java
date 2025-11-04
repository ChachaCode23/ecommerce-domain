package com.urbancollection.ecommerce.persistence.repositories;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.lang.reflect.Method;
import java.util.Collection;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.urbancollection.ecommerce.domain.entity.catalogo.Producto;
import com.urbancollection.ecommerce.domain.entity.usuarios.Usuario;
import com.urbancollection.ecommerce.domain.entity.ventas.ListaDeseos;

class ListaDeseosRepositoryImplTest {

    private ListaDeseosRepositoryImpl repo;

    @BeforeEach
    void setUp() {
        repo = new ListaDeseosRepositoryImpl();
    }

    @Test
    void save_y_findById_devuelve_la_misma_lista() {
        ListaDeseos lista = nuevaEntrada(1L, "User A", "Prod A", BigDecimal.valueOf(100), 5);

        ListaDeseos guardada = repo.save(lista);
        ListaDeseos recuperada = repo.findById(guardada.getId());

        assertNotNull(guardada.getId());
        assertEquals(guardada.getId(), recuperada.getId());
        assertNotNull(recuperada.getUsuario());
        assertNotNull(recuperada.getProducto());
        assertEquals("User A", recuperada.getUsuario().getNombre());
        assertEquals("Prod A", recuperada.getProducto().getNombre());
    }

    @Test
    void save_de_dos_entradas_distintas_genera_ids_distintos_y_ambas_se_recuperan() {
        ListaDeseos l1 = nuevaEntrada(1L, "User A", "Prod A", BigDecimal.valueOf(100), 5);
        ListaDeseos l2 = nuevaEntrada(2L, "User B", "Prod B", BigDecimal.valueOf(200), 8);

        ListaDeseos g1 = repo.save(l1);
        ListaDeseos g2 = repo.save(l2);

        assertNotNull(g1.getId());
        assertNotNull(g2.getId());
        assertNotEquals(g1.getId(), g2.getId());
        assertEquals("User A", repo.findById(g1.getId()).getUsuario().getNombre());
        assertEquals("User B", repo.findById(g2.getId()).getUsuario().getNombre());
    }

    @Test
    void update_persiste_cambios_en_la_lista() {
        ListaDeseos lista = nuevaEntrada(1L, "User A", "Prod A", BigDecimal.valueOf(100), 5);
        ListaDeseos guardada = repo.save(lista);

        Producto nuevoProducto = new Producto();
        nuevoProducto.setNombre("Prod A+");
        nuevoProducto.setPrecio(BigDecimal.valueOf(150));
        nuevoProducto.setStock(7);
        guardada.setProducto(nuevoProducto);

        repo.save(guardada);
        ListaDeseos actualizada = repo.findById(guardada.getId());

        assertEquals("Prod A+", actualizada.getProducto().getNombre());
        assertEquals(0, actualizada.getProducto().getPrecio().compareTo(BigDecimal.valueOf(150)));
        assertEquals(7, actualizada.getProducto().getStock());
    }

    @Test
    void delete_porId_elimina_y_no_aparece_en_find() throws Exception {
        ListaDeseos lista = nuevaEntrada(3L, "User C", "Prod C", BigDecimal.valueOf(300), 2);
        ListaDeseos guardada = repo.save(lista);
        Long id = guardada.getId();

        // usa el método real que tenga el repo (deleteById / delete / removeById / remove / eliminarPorId)
        reflectDeleteById(repo, id);

        ListaDeseos resultado = repo.findById(id);
        assertNull(resultado);
    }

    @Test
    void findAll_devuelve_lista_no_vacia_luego_de_save() throws Exception {
        repo.save(nuevaEntrada(1L, "User A", "Prod A", BigDecimal.valueOf(100), 5));
        Collection<?> all = reflectFindAll(repo);
        assertNotNull(all);
        assertFalse(all.isEmpty());
    }

    @Test
    void aislamiento_cada_test_tiene_estado_limpio() {
        ListaDeseos guardada = repo.save(nuevaEntrada(9L, "User Z", "Prod Z", BigDecimal.valueOf(90), 1));
        assertNotNull(guardada.getId());
    }

    // ===== Helpers =====

    private static ListaDeseos nuevaEntrada(Long usuarioId, String usuarioNombre,
                                            String prodNombre, BigDecimal precio, int stock) {
        Usuario u = new Usuario();
        u.setId(usuarioId);
        u.setNombre(usuarioNombre);

        Producto p = new Producto();
        p.setNombre(prodNombre);
        p.setPrecio(precio);
        p.setStock(stock);

        ListaDeseos l = new ListaDeseos();
        l.setUsuario(u);
        l.setProducto(p);
        return l;
    }

    private static void reflectDeleteById(Object repo, Long id) throws Exception {
        String[] names = { "deleteById", "delete", "removeById", "remove", "eliminarPorId" };
        for (String n : names) {
            try {
                Method m = repo.getClass().getMethod(n, Long.class);
                m.invoke(repo, id);
                return;
            } catch (NoSuchMethodException ignored) { }
        }
        fail("El repositorio no expone deleteById/delete/remove compatible (Long id).");
    }

    private static Collection<?> reflectFindAll(Object repo) throws Exception {
        String[] names = { "findAll", "getAll", "listar" };
        for (String n : names) {
            try {
                Method m = repo.getClass().getMethod(n);
                Object res = m.invoke(repo);
                if (res instanceof Collection<?>) return (Collection<?>) res;
            } catch (NoSuchMethodException ignored) { }
        }
        fail("El repositorio no expone findAll/getAll/listar que retorne Collection.");
        return null;
    }
}
