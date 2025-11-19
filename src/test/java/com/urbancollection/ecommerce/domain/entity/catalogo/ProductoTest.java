package com.urbancollection.ecommerce.domain.entity.catalogo;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ProductoTest {

    @Test
    void constructorSinArgumentos_deberiaCrearInstanciaNoNula() {
        Producto producto = new Producto();
        assertNotNull(producto);
    }

    @Test
    void gettersYSetters_debenGuardarYRetornarValores() {
        Producto producto = new Producto();

        Long id = 10L;
        String nombre = "Camiseta Negra";
        String descripcion = "Algodón, talla M";
        BigDecimal precio = new BigDecimal("1299.99");
        int stock = 20;
        String sku = "SKU-123";
        Boolean activo = Boolean.FALSE;

        // setters
        producto.setNombre(nombre);
        producto.setDescripcion(descripcion);
        producto.setPrecio(precio);
        producto.setStock(stock);
        producto.setSku(sku);
        producto.setActivo(activo);

        // id se suele asignar por JPA, pero lo seteamos via reflexión solo para comprobar
        assertDoesNotThrow(() -> {
            Field idField = Producto.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(producto, id);
        });

        // getters
        assertEquals(nombre, producto.getNombre());
        assertEquals(descripcion, producto.getDescripcion());
        assertEquals(precio, producto.getPrecio());
        assertEquals(stock, producto.getStock());
        assertEquals(sku, producto.getSku());
        assertEquals(activo, producto.getActivo());

        // id por reflexión
        assertDoesNotThrow(() -> {
            Field idField = Producto.class.getDeclaredField("id");
            idField.setAccessible(true);
            assertEquals(id, idField.get(producto));
        });
    }

    @Test
    void clase_deberiaEstarAnotadaComoEntityYTableProductoCore() {
        Entity entity = Producto.class.getAnnotation(Entity.class);
        assertNotNull(entity, "Producto debe tener @Entity");

        Table table = Producto.class.getAnnotation(Table.class);
        assertNotNull(table, "Producto debe tener @Table");
        assertEquals("Producto", table.name());
        assertEquals("core", table.schema());
    }

    @Test
    void campoId_deberiaTenerIdGeneratedValueYColumnCorrecta() throws NoSuchFieldException {
        Field field = Producto.class.getDeclaredField("id");

        Id id = field.getAnnotation(Id.class);
        assertNotNull(id, "id debe tener @Id");

        GeneratedValue generatedValue = field.getAnnotation(GeneratedValue.class);
        assertNotNull(generatedValue, "id debe tener @GeneratedValue");
        assertEquals(GenerationType.IDENTITY, generatedValue.strategy());

        Column column = field.getAnnotation(Column.class);
        assertNotNull(column, "id debe tener @Column");
        assertEquals("producto_id", column.name());
    }

    @Test
    void campoNombre_deberiaTenerNotBlankYColumnCorrecta() throws NoSuchFieldException {
        Field field = Producto.class.getDeclaredField("nombre");

        NotBlank notBlank = field.getAnnotation(NotBlank.class);
        assertNotNull(notBlank, "nombre debe tener @NotBlank");

        Column column = field.getAnnotation(Column.class);
        assertNotNull(column, "nombre debe tener @Column");
        assertEquals("nombre", column.name());
        assertFalse(column.nullable());
        assertEquals(200, column.length());
    }

    @Test
    void campoDescripcion_deberiaTenerColumnConDefinitionCorrecta() throws NoSuchFieldException {
        Field field = Producto.class.getDeclaredField("descripcion");

        Column column = field.getAnnotation(Column.class);
        assertNotNull(column, "descripcion debe tener @Column");
        assertEquals("descripcion", column.name());
        assertEquals("NVARCHAR(MAX)", column.columnDefinition());
    }

    @Test
    void campoPrecio_deberiaTenerNotNullDecimalMinYColumnCorrecta() throws NoSuchFieldException {
        Field field = Producto.class.getDeclaredField("precio");

        NotNull notNull = field.getAnnotation(NotNull.class);
        assertNotNull(notNull, "precio debe tener @NotNull");

        DecimalMin decimalMin = field.getAnnotation(DecimalMin.class);
        assertNotNull(decimalMin, "precio debe tener @DecimalMin");
        assertEquals("0.00", decimalMin.value());

        Column column = field.getAnnotation(Column.class);
        assertNotNull(column, "precio debe tener @Column");
        assertEquals("precio", column.name());
        assertFalse(column.nullable());
        assertEquals(12, column.precision());
        assertEquals(2, column.scale());
    }

    @Test
    void campoStock_deberiaTenerMinYColumnCorrecta() throws NoSuchFieldException {
        Field field = Producto.class.getDeclaredField("stock");

        Min min = field.getAnnotation(Min.class);
        assertNotNull(min, "stock debe tener @Min");
        assertEquals(0L, min.value());

        Column column = field.getAnnotation(Column.class);
        assertNotNull(column, "stock debe tener @Column");
        assertEquals("stock", column.name());
        assertFalse(column.nullable());
    }

    @Test
    void campoSku_deberiaTenerColumnNoNulaYUnique() throws NoSuchFieldException {
        Field field = Producto.class.getDeclaredField("sku");

        Column column = field.getAnnotation(Column.class);
        assertNotNull(column, "sku debe tener @Column");
        assertEquals("sku", column.name());
        assertFalse(column.nullable());
        assertTrue(column.unique());
        assertEquals(50, column.length());
    }

    @Test
    void campoActivo_deberiaTenerColumnNoNula() throws NoSuchFieldException {
        Field field = Producto.class.getDeclaredField("activo");

        Column column = field.getAnnotation(Column.class);
        assertNotNull(column, "activo debe tener @Column");
        assertEquals("activo", column.name());
        assertFalse(column.nullable());
    }

    // ========== prePersist ==========

    @Test
    void prePersist_cuandoSkuNullYActivoNull_deberiaGenerarSkuYMarcarActivoTrue() throws Exception {
        Producto producto = new Producto();
        producto.setNombre("Camiseta Negra");

        // sku = null
        producto.setSku(null);
        // activo = null via reflexión (porque el campo tiene valor por defecto true)
        Field activoField = Producto.class.getDeclaredField("activo");
        activoField.setAccessible(true);
        activoField.set(producto, null);

        producto.prePersist();

        assertNotNull(producto.getSku());
        assertFalse(producto.getSku().isBlank());
        assertTrue(producto.getActivo(), "activo debe quedar en true cuando era null");
    }

    @Test
    void prePersist_cuandoSkuEnBlanco_deberiaGenerarNuevoSku() {
        Producto producto = new Producto();
        producto.setNombre("Gorra Roja");
        producto.setSku("   "); // blanco

        producto.prePersist();

        assertNotNull(producto.getSku());
        assertFalse(producto.getSku().isBlank());
        assertTrue(producto.getSku().contains("-"),
                "El SKU generado debería contener un guión (parte base + timestamp)");
    }

    @Test
    void prePersist_cuandoSkuYaExisteYActivoNoEsNull_noDebeSobrescribir() {
        Producto producto = new Producto();
        producto.setNombre("Zapatos");
        producto.setSku("EXISTENTE-123");
        producto.setActivo(Boolean.FALSE);

        producto.prePersist();

        assertEquals("EXISTENTE-123", producto.getSku(),
                "prePersist no debe cambiar un SKU ya asignado");
        assertFalse(producto.getActivo(),
                "prePersist no debe cambiar activo si ya tiene valor (aunque sea false)");
    }
}
