package com.urbancollection.ecommerce.persistence.context;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.urbancollection.ecommerce.domain.entity.catalogo.Producto;
import com.urbancollection.ecommerce.domain.entity.catalogo.Cupon;
import com.urbancollection.ecommerce.domain.entity.logistica.Direccion;
import com.urbancollection.ecommerce.domain.entity.logistica.Envio;
import com.urbancollection.ecommerce.domain.entity.usuarios.Usuario;
import com.urbancollection.ecommerce.domain.entity.usuarios.ListaDeseos;
import com.urbancollection.ecommerce.domain.entity.ventas.Pedido;
import com.urbancollection.ecommerce.domain.entity.ventas.ItemPedido;
import com.urbancollection.ecommerce.domain.entity.ventas.TransaccionPago;

public final class InMemoryContext {

    // Catálogo
    public final Map<Long, Producto> productos = new ConcurrentHashMap<>();
    public final Map<Long, Cupon> cupones = new ConcurrentHashMap<>();

    // Logística
    public final Map<Long, Direccion> direcciones = new ConcurrentHashMap<>();
    public final Map<Long, Envio> envios = new ConcurrentHashMap<>();

    // Usuarios
    public final Map<Long, Usuario> usuarios = new ConcurrentHashMap<>();
    public final Map<Long, ListaDeseos> listasDeseos = new ConcurrentHashMap<>();

    // Ventas
    public final Map<Long, Pedido> pedidos = new ConcurrentHashMap<>();
    public final Map<Long, ItemPedido> itemsPedido = new ConcurrentHashMap<>();
    public final Map<Long, TransaccionPago> transacciones = new ConcurrentHashMap<>();

    // Singleton
    private static final InMemoryContext INSTANCE = new InMemoryContext();
    private InMemoryContext() { }
    public static InMemoryContext getInstance() { return INSTANCE; }
}
