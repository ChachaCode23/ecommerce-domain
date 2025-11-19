package com.urbancollection.ecommerce.domain.entity.logistica;

import com.urbancollection.ecommerce.domain.base.BaseEntity;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

class DireccionTest {

    @Test
    void constructorSinArgumentos_deberiaCrearInstanciaNoNula() {
        Direccion direccion = new Direccion();
        assertNotNull(direccion);
        assertTrue(direccion instanceof BaseEntity);
    }

    @Test
    void gettersYSetters_debenGuardarYRetornarValores() {
        Direccion d = new Direccion();

        Integer usuarioId = 10;
        String nombreContacto = "Juan Pérez";
        String linea1 = "Calle 1 #23";
        String linea2 = "Apto 4B";
        String ciudad = "Santo Domingo";
        String provincia = "Distrito Nacional";
        String codigoPostal = "10101";
        String pais = "República Dominicana";
        Boolean esPrincipal = true;

        d.setUsuarioId(usuarioId);
        d.setNombreContacto(nombreContacto);
        d.setLinea1(linea1);
        d.setLinea2(linea2);
        d.setCiudad(ciudad);
        d.setProvincia(provincia);
        d.setCodigoPostal(codigoPostal);
        d.setPais(pais);
        d.setEsPrincipal(esPrincipal);

        assertEquals(usuarioId, d.getUsuarioId());
        assertEquals(nombreContacto, d.getNombreContacto());
        assertEquals(linea1, d.getLinea1());
        assertEquals(linea2, d.getLinea2());
        assertEquals(ciudad, d.getCiudad());
        assertEquals(provincia, d.getProvincia());
        assertEquals(codigoPostal, d.getCodigoPostal());
        assertEquals(pais, d.getPais());
        assertEquals(esPrincipal, d.getEsPrincipal());
    }

    @Test
    void clase_deberiaEstarAnotadaComoEntityYTablaDireccionCore() {
        Entity entity = Direccion.class.getAnnotation(Entity.class);
        assertNotNull(entity, "Direccion debe tener @Entity");

        Table table = Direccion.class.getAnnotation(Table.class);
        assertNotNull(table, "Direccion debe tener @Table");
        assertEquals("Direccion", table.name());
        assertEquals("core", table.schema());

        AttributeOverride override = Direccion.class.getAnnotation(AttributeOverride.class);
        assertNotNull(override, "Direccion debe tener @AttributeOverride en id");
        assertEquals("id", override.name());
        assertEquals("direccion_id", override.column().name());
    }

    @Test
    void campoUsuarioId_deberiaTenerColumnUsuarioId() throws NoSuchFieldException {
        Field field = Direccion.class.getDeclaredField("usuarioId");
        Column column = field.getAnnotation(Column.class);
        assertNotNull(column);
        assertEquals("usuario_id", column.name());
    }

    @Test
    void campoNombreContacto_deberiaTenerColumnConLongitud() throws NoSuchFieldException {
        Field field = Direccion.class.getDeclaredField("nombreContacto");
        Column column = field.getAnnotation(Column.class);
        assertNotNull(column);
        assertEquals("nombre_contacto", column.name());
        assertEquals(150, column.length());
    }

    @Test
    void campoLinea1_deberiaSerNoNuloConLongitud() throws NoSuchFieldException {
        Field field = Direccion.class.getDeclaredField("linea1");
        Column column = field.getAnnotation(Column.class);
        assertNotNull(column);
        assertEquals("linea1", column.name());
        assertEquals(200, column.length());
        assertFalse(column.nullable());
    }

    @Test
    void campoLinea2_deberiaTenerLongitud() throws NoSuchFieldException {
        Field field = Direccion.class.getDeclaredField("linea2");
        Column column = field.getAnnotation(Column.class);
        assertNotNull(column);
        assertEquals("linea2", column.name());
        assertEquals(200, column.length());
    }

    @Test
    void campoCiudad_deberiaSerNoNuloConLongitud() throws NoSuchFieldException {
        Field field = Direccion.class.getDeclaredField("ciudad");
        Column column = field.getAnnotation(Column.class);
        assertNotNull(column);
        assertEquals("ciudad", column.name());
        assertEquals(100, column.length());
        assertFalse(column.nullable());
    }

    @Test
    void campoProvincia_deberiaTenerLongitud() throws NoSuchFieldException {
        Field field = Direccion.class.getDeclaredField("provincia");
        Column column = field.getAnnotation(Column.class);
        assertNotNull(column);
        assertEquals("provincia", column.name());
        assertEquals(100, column.length());
    }

    @Test
    void campoCodigoPostal_deberiaTenerLongitud() throws NoSuchFieldException {
        Field field = Direccion.class.getDeclaredField("codigoPostal");
        Column column = field.getAnnotation(Column.class);
        assertNotNull(column);
        assertEquals("codigo_postal", column.name());
        assertEquals(20, column.length());
    }

    @Test
    void campoPais_deberiaSerNoNuloConLongitud() throws NoSuchFieldException {
        Field field = Direccion.class.getDeclaredField("pais");
        Column column = field.getAnnotation(Column.class);
        assertNotNull(column);
        assertEquals("pais", column.name());
        assertEquals(100, column.length());
        assertFalse(column.nullable());
    }

    @Test
    void campoEsPrincipal_deberiaTenerColumnNoNula() throws NoSuchFieldException {
        Field field = Direccion.class.getDeclaredField("esPrincipal");
        Column column = field.getAnnotation(Column.class);
        assertNotNull(column);
        assertEquals("es_principal", column.name());
        assertFalse(column.nullable());
    }

    @Test
    void metodosLegacy_getCalleYSetCalle_debenMapearALinea1() {
        Direccion d = new Direccion();
        d.setLinea1("Linea 1 original");

        assertEquals("Linea 1 original", d.getCalle());

        d.setCalle("Calle Nueva");
        assertEquals("Calle Nueva", d.getLinea1());
        assertEquals("Calle Nueva", d.getCalle());
    }
}
