package com.urbancollection.ecommerce.domain.entity.ventas;

import com.urbancollection.ecommerce.domain.entity.catalogo.Producto;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ItemPedidoTest {

    @Test
    void constructorSinArgumentos_deberiaCrearInstanciaNoNula() {
        ItemPedido item = new ItemPedido();
        assertNotNull(item);
    }

    @Test
    void gettersYSetters_deberianGuardarYDevolverLosValores() {
        ItemPedido item = new ItemPedido();

        Producto producto = new Producto();
        Pedido pedido = new Pedido();
        int cantidad = 3;
        BigDecimal precio = new BigDecimal("199.99");

        item.setProducto(producto);
        item.setPedido(pedido);
        item.setCantidad(cantidad);
        item.setPrecioUnitario(precio);

        assertSame(producto, item.getProducto());
        assertSame(pedido, item.getPedido());
        assertEquals(cantidad, item.getCantidad());
        assertEquals(precio, item.getPrecioUnitario());
    }

    @Test
    void campoProducto_deberiaTenerManyToOneLazyYJoinColumnNoNulo() throws NoSuchFieldException {
        Field field = ItemPedido.class.getDeclaredField("producto");

        ManyToOne manyToOne = field.getAnnotation(ManyToOne.class);
        assertNotNull(manyToOne, "producto debe tener @ManyToOne");
        assertEquals(FetchType.LAZY, manyToOne.fetch());
        assertFalse(manyToOne.optional(), "producto no debe ser opcional");

        JoinColumn joinColumn = field.getAnnotation(JoinColumn.class);
        assertNotNull(joinColumn, "producto debe tener @JoinColumn");
        assertEquals("producto_id", joinColumn.name());
        assertFalse(joinColumn.nullable());
    }

    @Test
    void campoCantidad_deberiaTenerMinYColumnCorrectos() throws NoSuchFieldException {
        Field field = ItemPedido.class.getDeclaredField("cantidad");

        Min min = field.getAnnotation(Min.class);
        assertNotNull(min, "cantidad debe tener @Min");
        assertEquals(1L, min.value());

        Column column = field.getAnnotation(Column.class);
        assertNotNull(column, "cantidad debe tener @Column");
        assertEquals("cantidad", column.name());
        assertFalse(column.nullable());
    }

    @Test
    void campoPrecioUnitario_deberiaTenerNotNullDecimalMinYColumnCorrectos() throws NoSuchFieldException {
        Field field = ItemPedido.class.getDeclaredField("precioUnitario");

        NotNull notNull = field.getAnnotation(NotNull.class);
        assertNotNull(notNull, "precioUnitario debe tener @NotNull");

        DecimalMin decimalMin = field.getAnnotation(DecimalMin.class);
        assertNotNull(decimalMin, "precioUnitario debe tener @DecimalMin");
        assertEquals("0.00", decimalMin.value());

        Column column = field.getAnnotation(Column.class);
        assertNotNull(column, "precioUnitario debe tener @Column");
        assertEquals("precio_unitario", column.name());
        assertEquals(12, column.precision());
        assertEquals(2, column.scale());
        assertFalse(column.nullable());
    }

    @Test
    void campoPedido_deberiaTenerManyToOneLazyYJoinColumnNoNulo() throws NoSuchFieldException {
        Field field = ItemPedido.class.getDeclaredField("pedido");

        ManyToOne manyToOne = field.getAnnotation(ManyToOne.class);
        assertNotNull(manyToOne, "pedido debe tener @ManyToOne");
        assertEquals(FetchType.LAZY, manyToOne.fetch());
        assertFalse(manyToOne.optional(), "pedido no debe ser opcional");

        JoinColumn joinColumn = field.getAnnotation(JoinColumn.class);
        assertNotNull(joinColumn, "pedido debe tener @JoinColumn");
        assertEquals("pedido_id", joinColumn.name());
        assertFalse(joinColumn.nullable());
    }
}
