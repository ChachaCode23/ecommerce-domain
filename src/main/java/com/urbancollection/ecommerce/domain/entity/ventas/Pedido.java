package com.urbancollection.ecommerce.domain.entity.ventas;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.urbancollection.ecommerce.domain.base.BaseEntity;
import com.urbancollection.ecommerce.domain.entity.catalogo.Cupon;
import com.urbancollection.ecommerce.domain.entity.logistica.Direccion;
import com.urbancollection.ecommerce.domain.entity.logistica.Envio;
import com.urbancollection.ecommerce.domain.entity.usuarios.Usuario;
import com.urbancollection.ecommerce.domain.enums.EstadoDePedido;
import com.urbancollection.ecommerce.domain.enums.MetodoDePago;
import com.urbancollection.ecommerce.domain.service.CuponPolicy;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "Pedido", schema = "core")
// Sobrescribo el nombre de la columna "id" (de BaseEntity) para que en BD se llame "pedido_id"
@AttributeOverride(name = "id", column = @Column(name = "pedido_id"))
public class Pedido extends BaseEntity {

    // Relación muchos-a-uno con Usuario (un usuario puede tener muchos pedidos)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "usuario_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Usuario usuario;

    // Relación muchos-a-uno con Direccion (dirección de envío principal del pedido)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "direccion_envio_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Direccion direccionEntrega;

    // Relación muchos-a-uno con Cupon (cupón aplicado al pedido, si hay)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cupon_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Cupon cupon;

    // Estado actual del pedido (CREADO, PAGADO, ENVIADO, COMPLETADO, etc.)
    @Enumerated(EnumType.STRING)
    @Column(name = "estado", length = 20, nullable = false)
    private EstadoDePedido estado;

    // Método de pago usado para este pedido (TARJETA, PAYPAL, etc.)
    @Enumerated(EnumType.STRING)
    @Column(name = "metodo_pago", length = 20)
    private MetodoDePago metodoPago;
    
    // Subtotal del pedido (suma de items sin descuentos ni envío)
    @Column(name = "subtotal", precision = 12, scale = 2, nullable = false)
    private BigDecimal subtotal = BigDecimal.ZERO;

    // Monto total de descuento aplicado
    @Column(name = "descuento", precision = 12, scale = 2, nullable = false)
    private BigDecimal descuento = BigDecimal.ZERO;

    // Costo de envío del pedido
    @Column(name = "envio", precision = 12, scale = 2, nullable = false)
    private BigDecimal envio = BigDecimal.ZERO;

    // Total final del pedido (subtotal - descuento + envío)
    @Column(name = "total", precision = 12, scale = 2, nullable = false)
    private BigDecimal total = BigDecimal.ZERO;

    // Lista de items del pedido (detalle de productos)
    @OneToMany(
            mappedBy = "pedido",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    @JsonIgnoreProperties({"pedido"})
    private List<ItemPedido> items;

    // Lista de envíos asociados al pedido (historial de envíos)
    @OneToMany(
            mappedBy = "pedido",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    @JsonIgnoreProperties({"pedido"})
    private List<Envio> envios;

    // ===== Getters / Setters básicos =====
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public Direccion getDireccionEntrega() { return direccionEntrega; }
    public void setDireccionEntrega(Direccion direccionEntrega) { this.direccionEntrega = direccionEntrega; }

    public Cupon getCupon() { return cupon; }
    public void setCupon(Cupon cupon) { this.cupon = cupon; }

    public EstadoDePedido getEstado() { return estado; }
    public void setEstado(EstadoDePedido estado) { this.estado = estado; }

    public MetodoDePago getMetodoPago() { return metodoPago; }
    public void setMetodoPago(MetodoDePago metodoPago) { this.metodoPago = metodoPago; }
    
    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }

    public BigDecimal getDescuento() { return descuento; }
    public void setDescuento(BigDecimal descuento) { this.descuento = descuento; }

    public BigDecimal getEnvio() { return envio; }
    public void setEnvio(BigDecimal envio) { this.envio = envio; }

    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }

    // Devuelve la lista de items, inicializándola si viene null
    public List<ItemPedido> getItems() { 
        if (items == null) items = new ArrayList<>();
        return items; 
    }

    // Devuelve la lista de envíos, inicializándola si viene null
    public List<Envio> getEnvios() {
        if (envios == null) envios = new ArrayList<>();
        return envios;
    }

    public void setEnvios(List<Envio> envios) {
        this.envios = envios;
    }

    // Agrega un envío a la lista y setea la relación inversa
    public void agregarEnvio(Envio envio) {
        if (envio == null) throw new IllegalArgumentException("Envío no puede ser null");
        if (this.envios == null) this.envios = new ArrayList<>();
        this.envios.add(envio);
        envio.setPedido(this);
    }

    // Alias para agregar item (por si se usa addItem en lugar de agregarItem)
    public void addItem(ItemPedido item) { agregarItem(item); }

    // Agrega un item al pedido validando los datos mínimos
    public void agregarItem(@NotNull ItemPedido item) {
        if (item == null) throw new IllegalArgumentException("item requerido");
        if (item.getProducto() == null || item.getProducto().getId() == null)
            throw new IllegalArgumentException("producto requerido");
        if (item.getCantidad() <= 0)
            throw new IllegalArgumentException("cantidad debe ser > 0");

        BigDecimal precio = item.getProducto().getPrecio();
        if (precio == null)
            throw new IllegalArgumentException("precio del producto requerido");

        // Se setea el precio unitario del item según el producto
        item.setPrecioUnitario(precio);

        if (this.items == null) this.items = new ArrayList<>();
        this.items.add(item);
        // Relación inversa: el item sabe a qué pedido pertenece
        item.setPedido(this);
    }

    // Calcula totales usando descuento y envío en 0
    public Totales calcularTotales() {
        return calcularTotales(BigDecimal.ZERO, BigDecimal.ZERO);
    }

    // Calcula subtotal, descuento, envío y total del pedido
    public Totales calcularTotales(BigDecimal descuento, BigDecimal envio) {
        BigDecimal sub = BigDecimal.ZERO;
        if (this.items != null) {
            for (ItemPedido it : this.items) {
                if (it == null) continue;
                BigDecimal pu = it.getPrecioUnitario();
                if (pu == null)
                    throw new IllegalArgumentException("precioUnitario requerido en item");
                int qty = it.getCantidad();
                if (qty <= 0)
                    throw new IllegalArgumentException("cantidad de item debe ser > 0");
                // subtotal += precioUnitario * cantidad
                sub = sub.add(pu.multiply(BigDecimal.valueOf(qty)));
            }
        }
        // Normalizo descuento y envío a 0 si vienen null
        BigDecimal d = (descuento != null) ? descuento : BigDecimal.ZERO;
        BigDecimal e = (envio != null) ? envio : BigDecimal.ZERO;

        // totalCalc = subtotal - descuento
        BigDecimal totalCalc = sub.subtract(d);
        // El total no puede ser negativo
        if (totalCalc.compareTo(BigDecimal.ZERO) < 0) totalCalc = BigDecimal.ZERO;
        // Sumo el costo de envío
        totalCalc = totalCalc.add(e);

        // Actualizo los campos internos del pedido
        this.subtotal = sub;
        this.descuento = d;
        this.envio = e;
        this.total = totalCalc;

        // Devuelvo un objeto de solo lectura con los totales
        return new Totales(sub, d, e, totalCalc);
    }

    // Aplica un cupón a través de la policy correspondiente y devuelve el descuento calculado
    public BigDecimal aplicarCupon(CuponPolicy policy) {
        if (policy == null) throw new IllegalArgumentException("policy requerida");
        return policy.aplicaA(this) ? policy.descuento(this) : BigDecimal.ZERO;
    }

    // Lógica para registrar el pago de un pedido
    public void pagar(BigDecimal monto, MetodoDePago metodo) {
        if (monto == null || monto.compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("El monto debe ser mayor que 0.");
        if (metodo == null)
            throw new IllegalArgumentException("Método de pago requerido.");
        if (this.estado != EstadoDePedido.CREADO)
            throw new IllegalStateException("Solo se pueden pagar pedidos en estado CREADO.");
        if (this.total == null)
            throw new IllegalStateException("El pedido no tiene total calculado.");

        // El monto pagado no puede ser menor al total del pedido
        if (monto.compareTo(this.total) < 0)
            throw new IllegalArgumentException("El monto pagado es menor al total del pedido.");

        // Si todo está bien, se marca como PAGADO
        this.estado = EstadoDePedido.PAGADO;
    }

    // Lógica para marcar el pedido como despachado y crear un envío
    public void despachar(String tracking) {
        if (this.estado != EstadoDePedido.PAGADO)
            throw new IllegalStateException("Solo se pueden despachar pedidos PAGADOS.");
        if (tracking == null || tracking.isBlank())
            throw new IllegalArgumentException("El número de tracking es requerido.");

        // Crea un nuevo envío, le asigna el tracking y lo asocia al pedido
        Envio nuevoEnvio = new Envio();
        nuevoEnvio.setTracking(tracking);
        agregarEnvio(nuevoEnvio);

        // El pedido pasa a estado ENVIADO
        this.estado = EstadoDePedido.ENVIADO;
    }

    // Lógica para marcar el pedido como completado (entregado)
    public void completar() {
        if (this.estado != EstadoDePedido.ENVIADO)
            throw new IllegalStateException("Solo se pueden completar pedidos ENVIADOS.");
        this.estado = EstadoDePedido.COMPLETADO;
    }

    // Clase interna que representa un "snapshot" de los totales del pedido
    public static class Totales {
        private final BigDecimal subtotal;
        private final BigDecimal descuento;
        private final BigDecimal envio;
        private final BigDecimal total;

        public Totales(BigDecimal subtotal, BigDecimal descuento, BigDecimal envio, BigDecimal total) {
            this.subtotal = subtotal;
            this.descuento = descuento;
            this.envio = envio;
            this.total = total;
        }
        public BigDecimal getSubtotal() { return subtotal; }
        public BigDecimal getDescuento() { return descuento; }
        public BigDecimal getEnvio() { return envio; }
        public BigDecimal getTotal() { return total; }
    }

    // Devuelve el id de la dirección de envío (ayuda para templates / DTOs)
    public Long getDireccionEnvioId() {
        return direccionEntrega != null ? direccionEntrega.getId() : null;
    }

    // Alias para compatibilidad con templates que usan "direccionId"
    public Long getDireccionId() {
        return getDireccionEnvioId();
    }

    // Devuelve el id del usuario asociado al pedido
    public Long getUsuarioId() {
        return usuario != null ? usuario.getId() : null;
    }

    // Devuelve el id del cupón (como Integer) si existe
    public Integer getCuponId() {
        return cupon != null && cupon.getId() != null ? cupon.getId().intValue() : null;
    }

    // Setter de solo id de cupón (compatibilidad legacy)
    public void setCuponId(Integer cuponId) {
        // Este método es para compatibilidad legacy
        // En operaciones normales, usa setCupon(Cupon)
        if (cuponId == null) {
            this.cupon = null;
        } else {
            // Si solo tenemos el ID, creamos un objeto Cupon parcial con ese id
            Cupon c = new Cupon();
            c.setId(cuponId.longValue());
            this.cupon = c;
        }
    }

    // Suma la cantidad total de unidades entre todos los items del pedido
    public Integer getCantidadTotal() {
        if (items == null || items.isEmpty()) {
            return 0;
        }
        return items.stream()
                .mapToInt(ItemPedido::getCantidad)
                .sum();
    }

    // Método extraño / placeholder, parece un intento de imitar Optional.orElse
    public Pedido orElse(Object object) {
        return null;
    }
}
