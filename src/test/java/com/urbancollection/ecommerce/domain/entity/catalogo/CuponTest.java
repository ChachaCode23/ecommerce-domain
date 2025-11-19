package com.urbancollection.ecommerce.domain.entity.catalogo;

import com.urbancollection.ecommerce.domain.enums.TipoDescuento;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class CuponTest {

    @Test
    void constructorSinArgumentos_deberiaCrearInstanciaNoNula() {
        Cupon cupon = new Cupon();
        assertNotNull(cupon);
    }

    @Test
    void gettersYSetters_debenGuardarYRetornarValores() {
        Cupon cupon = new Cupon();

        String codigo = "BLACKFRIDAY25";
        boolean activo = true;
        LocalDateTime inicio = LocalDateTime.now().minusDays(1);
        LocalDateTime fin = LocalDateTime.now().plusDays(1);
        BigDecimal minimo = new BigDecimal("1000.00");
        TipoDescuento tipo = TipoDescuento.values()[0];
        BigDecimal valor = new BigDecimal("250.00");
        BigDecimal tope = new BigDecimal("500.00");

        cupon.setCodigo(codigo);
        cupon.setActivo(activo);
        cupon.setFechaInicio(inicio);
        cupon.setFechaFin(fin);
        cupon.setMinimoCompra(minimo);
        cupon.setTipo(tipo);
        cupon.setValorDescuento(valor);
        cupon.setTopeDescuento(tope);

        assertEquals(codigo, cupon.getCodigo());
        assertEquals(activo, cupon.isActivo());
        assertEquals(inicio, cupon.getFechaInicio());
        assertEquals(fin, cupon.getFechaFin());
        assertEquals(minimo, cupon.getMinimoCompra());
        assertEquals(tipo, cupon.getTipo());
        assertEquals(valor, cupon.getValorDescuento());
        assertEquals(tope, cupon.getTopeDescuento());
    }

    @Test
    void clase_deberiaEstarAnotadaComoEntityYTableCupones() {
        Entity entity = Cupon.class.getAnnotation(Entity.class);
        assertNotNull(entity, "Cupon debe tener @Entity");

        Table table = Cupon.class.getAnnotation(Table.class);
        assertNotNull(table, "Cupon debe tener @Table");
        assertEquals("cupones", table.name());
    }

    @Test
    void campoCodigo_deberiaTenerValidacionesYColumnCorrectas() throws NoSuchFieldException {
        Field field = Cupon.class.getDeclaredField("codigo");

        NotBlank notBlank = field.getAnnotation(NotBlank.class);
        assertNotNull(notBlank, "codigo debe tener @NotBlank");

        Size size = field.getAnnotation(Size.class);
        assertNotNull(size, "codigo debe tener @Size");
        assertEquals(100, size.max());

        Column column = field.getAnnotation(Column.class);
        assertNotNull(column, "codigo debe tener @Column");
        assertEquals("codigo", column.name());
        assertEquals(100, column.length());
        assertFalse(column.nullable());
        assertTrue(column.unique());
    }

    @Test
    void campoActivo_deberiaTenerColumnNoNula() throws NoSuchFieldException {
        Field field = Cupon.class.getDeclaredField("activo");
        Column column = field.getAnnotation(Column.class);
        assertNotNull(column, "activo debe tener @Column");
        assertEquals("activo", column.name());
        assertFalse(column.nullable());
    }

    @Test
    void camposFechaInicioYFechaFin_debenTenerColumnCorrecta() throws NoSuchFieldException {
        Field inicio = Cupon.class.getDeclaredField("fechaInicio");
        Column colInicio = inicio.getAnnotation(Column.class);
        assertNotNull(colInicio, "fechaInicio debe tener @Column");
        assertEquals("fecha_inicio", colInicio.name());

        Field fin = Cupon.class.getDeclaredField("fechaFin");
        Column colFin = fin.getAnnotation(Column.class);
        assertNotNull(colFin, "fechaFin debe tener @Column");
        assertEquals("fecha_fin", colFin.name());
    }

    @Test
    void campoMinimoCompra_deberiaTenerDecimalMinYColumnCorrecta() throws NoSuchFieldException {
        Field field = Cupon.class.getDeclaredField("minimoCompra");

        DecimalMin decimalMin = field.getAnnotation(DecimalMin.class);
        assertNotNull(decimalMin, "minimoCompra debe tener @DecimalMin");
        assertEquals("0.00", decimalMin.value());

        Column column = field.getAnnotation(Column.class);
        assertNotNull(column, "minimoCompra debe tener @Column");
        assertEquals("minimo_compra", column.name());
        assertEquals(15, column.precision());
        assertEquals(2, column.scale());
    }

    @Test
    void campoTipo_deberiaTenerNotNullEnumeratedYColumnCorrecta() throws NoSuchFieldException {
        Field field = Cupon.class.getDeclaredField("tipo");

        NotNull notNull = field.getAnnotation(NotNull.class);
        assertNotNull(notNull, "tipo debe tener @NotNull");

        Enumerated enumerated = field.getAnnotation(Enumerated.class);
        assertNotNull(enumerated, "tipo debe tener @Enumerated");
        assertEquals(EnumType.STRING, enumerated.value());

        Column column = field.getAnnotation(Column.class);
        assertNotNull(column, "tipo debe tener @Column");
        assertEquals("tipo", column.name());
        assertEquals(20, column.length());
        assertFalse(column.nullable());
    }

    @Test
    void campoValorDescuento_deberiaTenerNotNullDecimalMinYColumnCorrecta() throws NoSuchFieldException {
        Field field = Cupon.class.getDeclaredField("valorDescuento");

        NotNull notNull = field.getAnnotation(NotNull.class);
        assertNotNull(notNull, "valorDescuento debe tener @NotNull");

        DecimalMin decimalMin = field.getAnnotation(DecimalMin.class);
        assertNotNull(decimalMin, "valorDescuento debe tener @DecimalMin");
        assertEquals("0.00", decimalMin.value());

        Column column = field.getAnnotation(Column.class);
        assertNotNull(column, "valorDescuento debe tener @Column");
        assertEquals("valor_descuento", column.name());
        assertEquals(15, column.precision());
        assertEquals(2, column.scale());
        assertFalse(column.nullable());
    }

    @Test
    void campoTopeDescuento_deberiaTenerDecimalMinYColumnCorrecta() throws NoSuchFieldException {
        Field field = Cupon.class.getDeclaredField("topeDescuento");

        DecimalMin decimalMin = field.getAnnotation(DecimalMin.class);
        assertNotNull(decimalMin, "topeDescuento debe tener @DecimalMin");
        assertEquals("0.00", decimalMin.value());

        Column column = field.getAnnotation(Column.class);
        assertNotNull(column, "topeDescuento debe tener @Column");
        assertEquals("tope_descuento", column.name());
        assertEquals(15, column.precision());
        assertEquals(2, column.scale());
    }
}
