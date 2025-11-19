package com.urbancollection.ecommerce.domain.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;

import org.junit.jupiter.api.Test;

class StockServiceTest {

    @Test
    void debeSerUnaInterfazPublica() {
        assertTrue(StockService.class.isInterface(), "StockService debe ser una interfaz");
        assertTrue(Modifier.isPublic(StockService.class.getModifiers()),
                "StockService debe ser pública");
    }

    @Test
    void debeTenerMetodoValidarDisponibilidad_conListaDeItemPedidoYRetornoVoid() throws NoSuchMethodException {
        Method method = StockService.class.getMethod("validarDisponibilidad", List.class);

        assertNotNull(method);
        assertEquals(void.class, method.getReturnType(),
                "validarDisponibilidad debe retornar void");

        Class<?> paramType = method.getParameterTypes()[0];
        assertEquals(List.class, paramType, "El parámetro debe ser List");
    }

    @Test
    void debeTenerMetodoDescontar_conListaDeItemPedidoYRetornoVoid() throws NoSuchMethodException {
        Method method = StockService.class.getMethod("descontar", List.class);

        assertNotNull(method);
        assertEquals(void.class, method.getReturnType(),
                "descontar debe retornar void");

        Class<?> paramType = method.getParameterTypes()[0];
        assertEquals(List.class, paramType, "El parámetro debe ser List");
    }
}
