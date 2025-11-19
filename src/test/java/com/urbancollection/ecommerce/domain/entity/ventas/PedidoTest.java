package com.urbancollection.ecommerce.domain.entity.ventas;

import com.urbancollection.ecommerce.domain.entity.catalogo.Producto;
import com.urbancollection.ecommerce.domain.entity.logistica.Envio;
import com.urbancollection.ecommerce.domain.enums.EstadoDePedido;
import com.urbancollection.ecommerce.domain.enums.MetodoDePago;
import com.urbancollection.ecommerce.domain.service.CuponPolicy;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PedidoTest {

    // ========== agregarItem ==========

    @Test
    void agregarItem_conItemValido_deberiaAgregarItemYConfigurarRelaciones() {
        Pedido pedido = new Pedido();

        ItemPedido item = mock(ItemPedido.class);
        Producto producto = mock(Producto.class);

        when(item.getProducto()).thenReturn(producto);
        when(producto.getId()).thenReturn(1L);
        when(item.getCantidad()).thenReturn(2);
        BigDecimal precio = new BigDecimal("100.00");
        when(producto.getPrecio()).thenReturn(precio);

        pedido.agregarItem(item);

        List<ItemPedido> items = pedido.getItems();
        assertEquals(1, items.size());
        assertSame(item, items.get(0));

        verify(item).setPrecioUnitario(precio);
        verify(item).setPedido(pedido);
    }

    @Test
    void agregarItem_conItemNull_deberiaLanzarIllegalArgumentException() {
        Pedido pedido = new Pedido();
        assertThrows(IllegalArgumentException.class, () -> pedido.agregarItem(null));
    }

    @Test
    void agregarItem_sinProducto_deberiaLanzarIllegalArgumentException() {
        Pedido pedido = new Pedido();
        ItemPedido item = mock(ItemPedido.class);
        when(item.getProducto()).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> pedido.agregarItem(item));
    }

    @Test
    void agregarItem_productoSinId_deberiaLanzarIllegalArgumentException() {
        Pedido pedido = new Pedido();
        ItemPedido item = mock(ItemPedido.class);
        Producto producto = mock(Producto.class);

        when(item.getProducto()).thenReturn(producto);
        when(producto.getId()).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> pedido.agregarItem(item));
    }

    @Test
    void agregarItem_cantidadNoPositiva_deberiaLanzarIllegalArgumentException() {
        Pedido pedido = new Pedido();
        ItemPedido item = mock(ItemPedido.class);
        Producto producto = mock(Producto.class);

        when(item.getProducto()).thenReturn(producto);
        when(producto.getId()).thenReturn(1L);
        when(item.getCantidad()).thenReturn(0);

        assertThrows(IllegalArgumentException.class, () -> pedido.agregarItem(item));
    }

    @Test
    void agregarItem_productoSinPrecio_deberiaLanzarIllegalArgumentException() {
        Pedido pedido = new Pedido();
        ItemPedido item = mock(ItemPedido.class);
        Producto producto = mock(Producto.class);

        when(item.getProducto()).thenReturn(producto);
        when(producto.getId()).thenReturn(1L);
        when(item.getCantidad()).thenReturn(1);
        when(producto.getPrecio()).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> pedido.agregarItem(item));
    }

    // ========== calcularTotales ==========

    @Test
    void calcularTotales_conItemsYDescuentoYEnvio_deberiaCalcularYGuardarCampos() {
        Pedido pedido = new Pedido();

        ItemPedido item1 = mock(ItemPedido.class);
        when(item1.getPrecioUnitario()).thenReturn(new BigDecimal("100.00"));
        when(item1.getCantidad()).thenReturn(2);

        ItemPedido item2 = mock(ItemPedido.class);
        when(item2.getPrecioUnitario()).thenReturn(new BigDecimal("50.00"));
        when(item2.getCantidad()).thenReturn(1);

        pedido.getItems().add(item1);
        pedido.getItems().add(item2);

        BigDecimal descuento = new BigDecimal("10.00");
        BigDecimal envio = new BigDecimal("15.00");

        Pedido.Totales totales = pedido.calcularTotales(descuento, envio);

        assertEquals(new BigDecimal("250.00"), totales.getSubtotal());
        assertEquals(new BigDecimal("10.00"), totales.getDescuento());
        assertEquals(new BigDecimal("15.00"), totales.getEnvio());
        assertEquals(new BigDecimal("255.00"), totales.getTotal());

        assertEquals(totales.getSubtotal(), pedido.getSubtotal());
        assertEquals(totales.getDescuento(), pedido.getDescuento());
        assertEquals(totales.getEnvio(), pedido.getEnvio());
        assertEquals(totales.getTotal(), pedido.getTotal());
    }

    @Test
    void calcularTotales_descuentoMayorQueSubtotal_noDebeDejarTotalNegativo() {
        Pedido pedido = new Pedido();

        ItemPedido item = mock(ItemPedido.class);
        when(item.getPrecioUnitario()).thenReturn(new BigDecimal("50.00"));
        when(item.getCantidad()).thenReturn(1);

        pedido.getItems().add(item);

        Pedido.Totales totales = pedido.calcularTotales(new BigDecimal("100.00"), BigDecimal.ZERO);

        assertEquals(new BigDecimal("50.00"), totales.getSubtotal());
        assertEquals(new BigDecimal("100.00"), totales.getDescuento());
        assertEquals(BigDecimal.ZERO, totales.getEnvio());
        assertEquals(BigDecimal.ZERO, totales.getTotal()); // piso en 0
    }

    @Test
    void calcularTotales_conDescuentoYEnvioNull_deberiaTratarlosComoCero() {
        Pedido pedido = new Pedido();

        ItemPedido item = mock(ItemPedido.class);
        when(item.getPrecioUnitario()).thenReturn(new BigDecimal("30.00"));
        when(item.getCantidad()).thenReturn(2);

        pedido.getItems().add(item);

        Pedido.Totales totales = pedido.calcularTotales(null, null);

        assertEquals(new BigDecimal("60.00"), totales.getSubtotal());
        assertEquals(BigDecimal.ZERO, totales.getDescuento());
        assertEquals(BigDecimal.ZERO, totales.getEnvio());
        assertEquals(new BigDecimal("60.00"), totales.getTotal());
    }

    // ========== aplicarCupon ==========

    @Test
    void aplicarCupon_conPolicyNull_deberiaLanzarIllegalArgumentException() {
        Pedido pedido = new Pedido();
        assertThrows(IllegalArgumentException.class, () -> pedido.aplicarCupon(null));
    }

    @Test
    void aplicarCupon_cuandoPolicyAplica_deberiaUsarDescuentoDePolicy() {
        Pedido pedido = new Pedido();
        CuponPolicy policy = mock(CuponPolicy.class);

        when(policy.aplicaA(pedido)).thenReturn(true);
        when(policy.descuento(pedido)).thenReturn(new BigDecimal("25.00"));

        BigDecimal result = pedido.aplicarCupon(policy);

        assertEquals(new BigDecimal("25.00"), result);
        verify(policy).aplicaA(pedido);
        verify(policy).descuento(pedido);
    }

    @Test
    void aplicarCupon_cuandoPolicyNoAplica_deberiaRetornarCero() {
        Pedido pedido = new Pedido();
        CuponPolicy policy = mock(CuponPolicy.class);

        when(policy.aplicaA(pedido)).thenReturn(false);

        BigDecimal result = pedido.aplicarCupon(policy);

        assertEquals(BigDecimal.ZERO, result);
        verify(policy).aplicaA(pedido);
        verify(policy, never()).descuento(pedido);
    }

    // ========== pagar ==========

    @Test
    void pagar_conDatosValidos_deberiaCambiarEstadoAPagado() {
        Pedido pedido = new Pedido();
        pedido.setEstado(EstadoDePedido.CREADO);
        pedido.setTotal(new BigDecimal("100.00"));

        BigDecimal monto = new BigDecimal("100.00");
        MetodoDePago metodo = MetodoDePago.values()[0];

        pedido.pagar(monto, metodo);

        assertEquals(EstadoDePedido.PAGADO, pedido.getEstado());
    }

    @Test
    void pagar_conMontoNullOZero_deberiaLanzarIllegalArgumentException() {
        Pedido pedido = new Pedido();
        pedido.setEstado(EstadoDePedido.CREADO);
        pedido.setTotal(new BigDecimal("100.00"));

        MetodoDePago metodo = MetodoDePago.values()[0];

        assertAll(
                () -> assertThrows(IllegalArgumentException.class, () -> pedido.pagar(null, metodo)),
                () -> assertThrows(IllegalArgumentException.class, () -> pedido.pagar(BigDecimal.ZERO, metodo))
        );
    }

    @Test
    void pagar_conMetodoNull_deberiaLanzarIllegalArgumentException() {
        Pedido pedido = new Pedido();
        pedido.setEstado(EstadoDePedido.CREADO);
        pedido.setTotal(new BigDecimal("100.00"));

        assertThrows(IllegalArgumentException.class,
                () -> pedido.pagar(new BigDecimal("100.00"), null));
    }

    @Test
    void pagar_conEstadoNoCreado_deberiaLanzarIllegalStateException() {
        Pedido pedido = new Pedido();
        pedido.setEstado(EstadoDePedido.PAGADO); // distinto de CREADO
        pedido.setTotal(new BigDecimal("100.00"));

        MetodoDePago metodo = MetodoDePago.values()[0];

        assertThrows(IllegalStateException.class,
                () -> pedido.pagar(new BigDecimal("100.00"), metodo));
    }

    @Test
    void pagar_sinTotalCalculado_deberiaLanzarIllegalStateException() {
        Pedido pedido = new Pedido();
        pedido.setEstado(EstadoDePedido.CREADO);
        pedido.setTotal(null);

        MetodoDePago metodo = MetodoDePago.values()[0];

        assertThrows(IllegalStateException.class,
                () -> pedido.pagar(new BigDecimal("100.00"), metodo));
    }

    @Test
    void pagar_conMontoMenorQueTotal_deberiaLanzarIllegalArgumentException() {
        Pedido pedido = new Pedido();
        pedido.setEstado(EstadoDePedido.CREADO);
        pedido.setTotal(new BigDecimal("100.00"));

        MetodoDePago metodo = MetodoDePago.values()[0];

        assertThrows(IllegalArgumentException.class,
                () -> pedido.pagar(new BigDecimal("50.00"), metodo));
    }

    // ========== despachar ==========

    @Test
    void despachar_conEstadoPagadoYTrackingValido_deberiaCrearEnvioYCambiarEstadoAEnviado() {
        Pedido pedido = new Pedido();
        pedido.setEstado(EstadoDePedido.PAGADO);

        pedido.despachar("TRACK-123");

        assertEquals(EstadoDePedido.ENVIADO, pedido.getEstado());
        List<Envio> envios = pedido.getEnvios();
        assertNotNull(envios);
        assertEquals(1, envios.size());
        assertNotNull(envios.get(0));
    }

    @Test
    void despachar_conEstadoNoPagado_deberiaLanzarIllegalStateException() {
        Pedido pedido = new Pedido();
        pedido.setEstado(EstadoDePedido.CREADO);

        assertThrows(IllegalStateException.class,
                () -> pedido.despachar("TRACK-123"));
    }

    @Test
    void despachar_conTrackingNullOBlanco_deberiaLanzarIllegalArgumentException() {
        Pedido pedido = new Pedido();
        pedido.setEstado(EstadoDePedido.PAGADO);

        assertAll(
                () -> assertThrows(IllegalArgumentException.class, () -> pedido.despachar(null)),
                () -> assertThrows(IllegalArgumentException.class, () -> pedido.despachar("   "))
        );
    }

    // ========== completar ==========

    @Test
    void completar_conEstadoEnviado_deberiaCambiarEstadoACompletado() {
        Pedido pedido = new Pedido();
        pedido.setEstado(EstadoDePedido.ENVIADO);

        pedido.completar();

        assertEquals(EstadoDePedido.COMPLETADO, pedido.getEstado());
    }

    @Test
    void completar_conEstadoDistintoEnviado_deberiaLanzarIllegalStateException() {
        Pedido pedido = new Pedido();
        pedido.setEstado(EstadoDePedido.PAGADO);

        assertThrows(IllegalStateException.class, pedido::completar);
    }
}
