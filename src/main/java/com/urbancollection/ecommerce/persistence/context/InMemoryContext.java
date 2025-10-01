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

/**
 * Esta clase representa un "contexto en memoria" que actua como una base de datos simulada.
 * Cada entidad del dominio tiene su propio mapa donde se almacenan los datos.
 *
 * Esta clase usa el patron Singleton para garantizar que solo exista una instancia del contexto
 * en toda la aplicacion.
 *
 * Se utilizo en los repositorios para guardar, buscar, eliminar y listar los datos en memoria.
 */
public final class InMemoryContext {


    // Seccion de Catalogo
    public final Map<Long, Producto> productos = new ConcurrentHashMap<>();
    public final Map<Long, Cupon> cupones = new ConcurrentHashMap<>();

    // Seccion de Logistica
    public final Map<Long, Direccion> direcciones = new ConcurrentHashMap<>();
    public final Map<Long, Envio> envios = new ConcurrentHashMap<>();

    // Seccion de Usuarios
    public final Map<Long, Usuario> usuarios = new ConcurrentHashMap<>();
    public final Map<Long, ListaDeseos> listasDeseos = new ConcurrentHashMap<>();

    // Seccion de Ventas
    public final Map<Long, Pedido> pedidos = new ConcurrentHashMap<>();
    public final Map<Long, ItemPedido> itemsPedido = new ConcurrentHashMap<>();
    public final Map<Long, TransaccionPago> transacciones = new ConcurrentHashMap<>();

    // Patron Singleton
    // Se crea una unica instancia de InMemoryContext
    private static final InMemoryContext INSTANCE = new InMemoryContext();

    //Constructor privado para evitar que se creen nuevas instancias desde fuera.
    private InMemoryContext() { }

    // Metodo estatico para obtener la unica instancia disponible de InMemoryContext.
    public static InMemoryContext getInstance() {
        return INSTANCE;
    }
}
