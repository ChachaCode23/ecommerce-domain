package com.urbancollection.ecommerce.persistence.repositories;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.urbancollection.ecommerce.TestDataFactory;

class ProductoRepositoryImplTest {

    private ProductoRepositoryImpl repo;

    @BeforeEach
    void setUp_limpiaContexto_con_resetForTests() {
        // La implementación no usa contexto por constructor; crea uno nuevo por test
        repo = new ProductoRepositoryImpl();
    }

    @Test
    void save_y_findById_devuelve_el_mismo_producto() {
        // Arrange
        var p = TestDataFactory.productoValido();

        // Act
        var guardado = repo.save(p);
        var recuperado = repo.findById(guardado.getId());

        // Assert
        Assertions.assertNotNull(guardado.getId());
        Assertions.assertEquals(guardado.getId(), recuperado.getId());
        Assertions.assertEquals("Tennis Puma", recuperado.getNombre());
        Assertions.assertEquals(0, recuperado.getPrecio().compareTo(guardado.getPrecio()));
        Assertions.assertEquals(10, recuperado.getStock());
    }

    @Test
    void update_persiste_cambios_de_precio_y_stock() {
        // TODO: crear producto, guardar, cambiar precio/stock, volver a guardar, verificar
    }

    @Test
    void delete_porId_elimina_y_no_aparece_en_find() {
        // TODO
    }

    @Test
    void findAll_devuelve_lista_no_vacia_luego_de_save() {
        // TODO
    }

    @Test
    void save_con_multiples_registros_incrementa_ids_secuenciales() {
        // TODO
    }

    @Test
    void aislamiento_cada_test_tiene_contexto_limpio() {
        // TODO
    }
}
