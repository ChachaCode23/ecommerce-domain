package com.urbancollection.ecommerce.domain.entity.usuarios;

import com.urbancollection.ecommerce.domain.base.BaseEntity; // <-- tu BaseEntity
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Mapea a core.Usuario
 * PK real = usuario_id
 */
@Entity
@Table(
        name = "Usuario",
        schema = "core",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "UQ__Usuario__Email",
                        columnNames = "email"
                )
        },
        indexes = {
                @Index(name = "IX_Usuario_Email", columnList = "email")
        }
)
@AttributeOverride(name = "id", column = @Column(name = "usuario_id"))
public class Usuario extends BaseEntity {

    @NotBlank
    @Size(max = 150)
    @Column(name = "nombre", length = 150, nullable = false)
    private String nombre;

    @NotBlank
    @Email
    @Size(max = 150)
    @Column(name = "email", length = 150, nullable = false)
    private String correo; // en DB la columna se llama email

    @Size(max = 255)
    @Column(name = "hash_password", length = 255)
    private String contrasena; // en DB es hash_password

    @NotBlank
    @Size(max = 20)
    @Column(name = "rol", length = 20, nullable = false)
    private String rol; // 'CUSTOMER' / 'ADMIN'

    // ===== getters / setters =====

    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }
    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContrasena() {
        return contrasena;
    }
    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getRol() {
        return rol;
    }
    public void setRol(String rol) {
        this.rol = rol;
    }
}
