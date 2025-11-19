package com.urbancollection.ecommerce.domain.service;

import com.urbancollection.ecommerce.domain.entity.ventas.Pedido;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class CuponPolicyTest {

    @Test
    void debeSerUnaInterfaz() {
        assertTrue(CuponPolicy.class.isInterface(),
                "CuponPolicy debe ser una interfaz");
    }

    @Test
    void debeTenerMetodoAplicaA_conPedidoYRetornarBoolean() throws NoSuchMethodException {
        Method aplicaA = CuponPolicy.class.getMethod("aplicaA", Pedido.class);

        assertNotNull(aplicaA);
        assertEquals(boolean.class, aplicaA.getReturnType(),
                "aplicaA debe retornar boolean");
    }

    @Test
    void debeTenerMetodoDescuento_conPedidoYRetornarBigDecimal() throws NoSuchMethodException {
        Method descuento = CuponPolicy.class.getMethod("descuento", Pedido.class);

        assertNotNull(descuento);
        assertEquals(BigDecimal.class, descuento.getReturnType(),
                "descuento debe retornar BigDecimal");
    }
}
