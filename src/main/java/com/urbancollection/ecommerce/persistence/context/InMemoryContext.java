package com.urbancollection.ecommerce.persistence.context;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import com.urbancollection.ecommerce.domain.entity.catalogo.Cupon;
import com.urbancollection.ecommerce.domain.entity.catalogo.Producto;
import com.urbancollection.ecommerce.domain.entity.logistica.Direccion;
import com.urbancollection.ecommerce.domain.entity.logistica.Envio;
import com.urbancollection.ecommerce.domain.entity.usuarios.ListaDeseos;
import com.urbancollection.ecommerce.domain.entity.usuarios.Usuario;
import com.urbancollection.ecommerce.domain.entity.ventas.ItemPedido;
import com.urbancollection.ecommerce.domain.entity.ventas.Pedido;
import com.urbancollection.ecommerce.domain.entity.ventas.TransaccionPago;

/**
 * Contexto en memoria (Singleton) que simula una base de datos.
 */
public final class InMemoryContext {

    // ====== “Tablas” en memoria ======
    // Catálogo
    public final Map<Long, Producto> productos = new ConcurrentHashMap<>();
    public final Map<Long, Cupon>    cupones   = new ConcurrentHashMap<>();

    // Logística
    public final Map<Long, Direccion> direcciones = new ConcurrentHashMap<>();
    public final Map<Long, Envio>     envios      = new ConcurrentHashMap<>();

    // Usuarios
    public final Map<Long, Usuario>     usuarios     = new ConcurrentHashMap<>();
    public final Map<Long, ListaDeseos> listasDeseos = new ConcurrentHashMap<>();
 // en com.urbancollection.ecommerce.persistence.context.InMemoryContext
    public Map<Long, Usuario> getUsuariosStore() {
        return this.usuarios; // el mapa interno donde guardas los usuarios
    }

 // com/urbancollection/ecommerce/persistence/context/InMemoryContext.java
 // ...
 private final java.util.concurrent.ConcurrentHashMap<Long, com.urbancollection.ecommerce.domain.entity.ventas.ItemPedido> itemsPedidoStore
         = new java.util.concurrent.ConcurrentHashMap<>();

 public Map<Long, com.urbancollection.ecommerce.domain.entity.ventas.ItemPedido> getItemsPedidoStore() {
     return itemsPedidoStore;
 }

 // Si necesitas lista para lecturas rápidas:
 public java.util.List<com.urbancollection.ecommerce.domain.entity.ventas.ItemPedido> getItemsPedido1() {
     return new java.util.ArrayList<>(itemsPedidoStore.values());
 }


    // Ventas
    public final Map<Long, Pedido>          pedidos       = new ConcurrentHashMap<>();
    public final Map<Long, ItemPedido>      itemsPedido   = new ConcurrentHashMap<>();
    public final Map<Long, TransaccionPago> transacciones = new ConcurrentHashMap<>();

    // ====== Secuencias (autoincrement) ======
    private final AtomicLong productoSeq      = new AtomicLong(1);
    private final AtomicLong cuponSeq         = new AtomicLong(1);
    private final AtomicLong direccionSeq     = new AtomicLong(1);
    private final AtomicLong envioSeq         = new AtomicLong(1);
    private final AtomicLong usuarioSeq       = new AtomicLong(1);
    private final AtomicLong listaDeseosSeq   = new AtomicLong(1);
    private final AtomicLong pedidoSeq        = new AtomicLong(1);
    private final AtomicLong itemPedidoSeq    = new AtomicLong(1);
    private final AtomicLong transaccionSeq   = new AtomicLong(1);

    public long nextProductoId()    { return productoSeq.getAndIncrement(); }
    public long nextCuponId()       { return cuponSeq.getAndIncrement(); }
    public long nextDireccionId()   { return direccionSeq.getAndIncrement(); }
    public long nextEnvioId()       { return envioSeq.getAndIncrement(); }
    public long nextUsuarioId()     { return usuarioSeq.getAndIncrement(); }
    public long nextListaDeseosId() { return listaDeseosSeq.getAndIncrement(); }
    public long nextPedidoId()      { return pedidoSeq.getAndIncrement(); }
    public long nextItemPedidoId()  { return itemPedidoSeq.getAndIncrement(); }
    public long nextTransaccionId() { return transaccionSeq.getAndIncrement(); }

    // ====== Singleton ======
    private static final InMemoryContext INSTANCE = new InMemoryContext();
    public InMemoryContext() {}
    public static InMemoryContext getInstance() { return INSTANCE; }

    // ====== Getters como LISTA (copias para lectura) ======
    public List<Producto> getProductos() {
        return new ArrayList<>(productos.values());
    }
    public List<Cupon> getCupones() { return new ArrayList<>(cupones.values()); }
    public List<Direccion> getDirecciones() { return new ArrayList<>(direcciones.values()); }
    public List<Envio> getEnvios() { return new ArrayList<>(envios.values()); }
    public List<Usuario> getUsuarios() { return new ArrayList<>(usuarios.values()); }
    public List<ListaDeseos> getListasDeseos() { return new ArrayList<>(listasDeseos.values()); }
    public List<Pedido> getPedidos() { return new ArrayList<>(pedidos.values()); }
    public List<ItemPedido> getItemsPedido() { return new ArrayList<>(itemsPedido.values()); }
    public List<TransaccionPago> getTransacciones() { return new ArrayList<>(transacciones.values()); }

    // ====== Helpers específicos para Producto (lectura/escritura “real” en el Map) ======
    public Producto saveProducto(Producto p) {
        if (p.getId() == null) {
            p.setId(nextProductoId());
        }
        productos.put(p.getId(), p);
        return p;
    }

    public Producto findProductoById(Long id) {
        return productos.get(id);
    }

    public void deleteProductoById(Long id) {
        productos.remove(id);
    }
    
 // Uso de pruebas: limpia el estado in-memory entre tests.
 // package-private (sin public) para no exponerlo fuera del módulo.
 @SuppressWarnings({ "rawtypes" })
 void resetForTests() {
     try {
         for (java.lang.reflect.Field f : this.getClass().getDeclaredFields()) {
             if (java.lang.reflect.Modifier.isStatic(f.getModifiers())) continue;
             f.setAccessible(true);
             Object value = f.get(this);

             if (value instanceof java.util.Map<?, ?>) {
                 ((java.util.Map) value).clear();
             } else if (value instanceof java.util.Collection<?>) {
                 ((java.util.Collection) value).clear();
             }
         }
     } catch (IllegalAccessException ignored) {
         // Solo para tests.
     }
 
    }
}
