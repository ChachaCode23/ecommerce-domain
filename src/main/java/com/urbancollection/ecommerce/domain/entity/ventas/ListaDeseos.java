package com.urbancollection.ecommerce.domain.entity.ventas;

import com.urbancollection.ecommerce.domain.base.BaseEntity;
import com.urbancollection.ecommerce.domain.entity.catalogo.Producto;
import com.urbancollection.ecommerce.domain.entity.usuarios.Usuario;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

/**
 * ListaDeseos
 *
 * Esta entidad representa un producto que un usuario guardó en su "wishlist"
 * (lista de deseos). Básicamente: "el usuario X marcó el producto Y como favorito".
 *
 * Tabla: lista_deseos
 *
 * Campos:
 * - usuario: quién guardó el producto.
 * - producto: qué producto guardó.
 *
 * Validaciones:
 * - Ambos son obligatorios (@NotNull).
 *
 */
@Entity
@Table(name = "lista_deseos")
public class ListaDeseos extends BaseEntity {

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public Producto getProducto() { return producto; }
    public void setProducto(Producto producto) { this.producto = producto; }
}
