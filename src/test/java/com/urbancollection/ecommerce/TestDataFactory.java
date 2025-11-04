package com.urbancollection.ecommerce;

import java.math.BigDecimal;

import com.urbancollection.ecommerce.domain.entity.catalogo.Producto;
import com.urbancollection.ecommerce.domain.entity.logistica.Envio;
import com.urbancollection.ecommerce.domain.entity.usuarios.Usuario;

public final class TestDataFactory {
    private TestDataFactory() {}

    public static Producto productoValido() {
        Producto p = new Producto();
        p.setNombre("Tennis Puma");
        p.setPrecio(BigDecimal.valueOf(100));
        p.setStock(10);
        return p;
    }

    // Lista de deseos (usuarios)
    public static com.urbancollection.ecommerce.domain.entity.usuarios.ListaDeseos listaDeseosVacia() {
        com.urbancollection.ecommerce.domain.entity.usuarios.ListaDeseos l =
                new com.urbancollection.ecommerce.domain.entity.usuarios.ListaDeseos();
        // si tu clase tiene setUsuarioId / setUsuario / setId, ajusta acá:
        // l.setUsuarioId(1L);
        return l;
    }

    // Lista de deseos (ventas)
    public static com.urbancollection.ecommerce.domain.entity.ventas.ListaDeseos listaDeseosVentasVacia() {
        com.urbancollection.ecommerce.domain.entity.ventas.ListaDeseos l =
                new com.urbancollection.ecommerce.domain.entity.ventas.ListaDeseos();
        // si tu clase requiere usuario/producto obligatorios, setéalos aquí:
        // Usuario u = usuarioValido();
        // Producto p = productoValido();
        // l.setUsuario(u);
        // l.setProducto(p);
        return l;
    }

    public static Usuario usuarioValido() {
        Usuario u = new Usuario();
        u.setNombre("User Test");
        // Campos típicamente requeridos por validaciones:
        u.setCorreo("user@test.com");
        u.setContrasena("123456");
        u.setRol("CLIENTE");
        return u;
    }
    
    public static Envio envioValido() {
        Envio e = new Envio();
        // e.setPedidoId(1L);
        // e.setTracking("TRACK-123");
        // e.setEstado(EstadoDeEnvio.PENDIENTE);
        return e;
    }

}
