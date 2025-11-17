package com.urbancollection.ecommerce.domain.entity.ventas;

import com.urbancollection.ecommerce.domain.base.BaseEntity;
import com.urbancollection.ecommerce.domain.entity.logistica.Direccion;
import com.urbancollection.ecommerce.domain.entity.logistica.Envio;
import com.urbancollection.ecommerce.domain.entity.usuarios.Usuario;
import com.urbancollection.ecommerce.domain.enums.EstadoDePedido;
import com.urbancollection.ecommerce.domain.enums.MetodoDePago;
import com.urbancollection.ecommerce.domain.service.CuponPolicy;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Pedido", schema = "core")
@AttributeOverride(name = "id", column = @Column(name = "pedido_id"))
public class Pedido extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "direccion_envio_id")
    private Direccion direccionEntrega;

    @Column(name = "cupon_id")
    private Integer cuponId;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", length = 20, nullable = false)
    private EstadoDePedido estado;

    @Column(name = "total", precision = 12, scale = 2, nullable = false)
    private BigDecimal total = BigDecimal.ZERO;

    @OneToMany(
            mappedBy = "pedido",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private List<ItemPedido> items;

    @OneToOne(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private Envio envio;

    // ===== Getters / Setters =====
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public Direccion getDireccionEntrega() { return direccionEntrega; }
    public void setDireccionEntrega(Direccion direccionEntrega) { this.direccionEntrega = direccionEntrega; }

    public Integer getCuponId() { return cuponId; }
    public void setCuponId(Integer cuponId) { this.cuponId = cuponId; }

    public EstadoDePedido getEstado() { return estado; }
    public void setEstado(EstadoDePedido estado) { this.estado = estado; }

    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }

    public List<ItemPedido> getItems() { 
        if (items == null) items = new ArrayList<>();
        return items; 
    }

    public Envio getEnvio() { return envio; }
    public void setEnvio(Envio envio) { this.envio = envio; }

    // ===== Relación con items =====
    public void addItem(ItemPedido item) { agregarItem(item); }

    public void agregarItem(@NotNull ItemPedido item) {
        if (item == null) throw new IllegalArgumentException("item requerido");
        if (item.getProducto() == null || item.getProducto().getId() == null)
            throw new IllegalArgumentException("producto requerido");
        if (item.getCantidad() <= 0)
            throw new IllegalArgumentException("cantidad debe ser > 0");

        BigDecimal precio = item.getProducto().getPrecio();
        if (precio == null)
            throw new IllegalArgumentException("precio del producto requerido");

        item.setPrecioUnitario(precio);

        if (this.items == null) this.items = new ArrayList<>();
        this.items.add(item);
        item.setPedido(this);
    }

    // ===== Cálculo de totales =====
    public Totales calcularTotales() {
        return calcularTotales(BigDecimal.ZERO, BigDecimal.ZERO);
    }

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
                sub = sub.add(pu.multiply(BigDecimal.valueOf(qty)));
            }
        }
        BigDecimal d = (descuento != null) ? descuento : BigDecimal.ZERO;
        BigDecimal e = (envio != null) ? envio : BigDecimal.ZERO;

        BigDecimal totalCalc = sub.subtract(d);
        if (totalCalc.compareTo(BigDecimal.ZERO) < 0) totalCalc = BigDecimal.ZERO;
        totalCalc = totalCalc.add(e);
        this.total = totalCalc;

        return new Totales(sub, d, e, totalCalc);
    }

    /** 🔹 Aplica cupón usando la policy del dominio. */
    public BigDecimal aplicarCupon(CuponPolicy policy) {
        if (policy == null) throw new IllegalArgumentException("policy requerida");
        return policy.aplicaA(this) ? policy.descuento(this) : BigDecimal.ZERO;
    }

    /** 🔹 Pagar pedido (valida estado y monto, cambia a PAGADO). */
    public void pagar(BigDecimal monto, MetodoDePago metodo) {
        if (monto == null || monto.compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("El monto debe ser mayor que 0.");
        if (metodo == null)
            throw new IllegalArgumentException("Método de pago requerido.");
        if (this.estado != EstadoDePedido.CREADO)
            throw new IllegalStateException("Solo se pueden pagar pedidos en estado CREADO.");
        if (this.total == null)
            throw new IllegalStateException("El pedido no tiene total calculado.");

        if (monto.compareTo(this.total) < 0)
            throw new IllegalArgumentException("El monto pagado es menor al total del pedido.");

        this.estado = EstadoDePedido.PAGADO;
    }

    /** 🔹 Despachar pedido (valida estado PAGADO, crea/actualiza envío, cambia a ENVIADO). */
    public void despachar(String tracking) {
        if (this.estado != EstadoDePedido.PAGADO)
            throw new IllegalStateException("Solo se pueden despachar pedidos PAGADOS.");
        if (tracking == null || tracking.isBlank())
            throw new IllegalArgumentException("El número de tracking es requerido.");

        if (this.envio == null) {
            this.envio = new Envio();
            this.envio.setPedido(this);
        }
        this.envio.setTracking(tracking);

        this.estado = EstadoDePedido.ENVIADO;
    }

    /** 🔹 Completar pedido (valida estado ENVIADO, cambia a COMPLETADO). */
    public void completar() {
        if (this.estado != EstadoDePedido.ENVIADO)
            throw new IllegalStateException("Solo se pueden completar pedidos ENVIADOS.");
        this.estado = EstadoDePedido.COMPLETADO;
    }

    // ===== Clase interna para totales =====
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
}