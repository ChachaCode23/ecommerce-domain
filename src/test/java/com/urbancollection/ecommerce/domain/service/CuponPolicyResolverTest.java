package com.urbancollection.ecommerce.domain.service;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Modifier;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class CuponPolicyResolverTest {

    @Test
    void deberiaSerUnaClaseConcretaNoAbstracta() {
        int modifiers = CuponPolicyResolver.class.getModifiers();
        assertFalse(Modifier.isAbstract(modifiers), "CuponPolicyResolver no debe ser abstracta");
        assertTrue(Modifier.isPublic(modifiers), "CuponPolicyResolver debería ser pública");
    }

    @Test
    void deberiaTenerAlMenosUnMetodoQueRetorneCuponPolicy() {
        boolean tieneMetodoQueRetornaPolicy = Arrays.stream(CuponPolicyResolver.class.getDeclaredMethods())
                .anyMatch(m -> m.getReturnType().equals(CuponPolicy.class));

        assertTrue(tieneMetodoQueRetornaPolicy,
                "CuponPolicyResolver debe exponer al menos un método que retorne CuponPolicy");
    }
}
