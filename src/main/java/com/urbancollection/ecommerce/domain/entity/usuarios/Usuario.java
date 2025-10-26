package com.urbancollection.ecommerce.domain.entity.usuarios;

import com.urbancollection.ecommerce.domain.base.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Usuario
 *
 * Esta entidad representa a un usuario del sistema (cliente o admin).
 * Está mapeada a la tabla core.Usuario.
 *
 * - La PK real en la base es "usuario_id", no "id". Por eso usamos @AttributeOverride.
 * - El campo "correo" en el código se guarda en la columna "email" de la BD.
 * - La contraseña se guarda como hash (hash_password).
 * - El rol indica el tipo de usuario (por ejemplo "CUSTOMER" o "ADMIN").
 *
 * Restricciones:
 * - email es único (unique constraint + índice).
 * - nombre, email y rol son obligatorios.
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
    private String correo; // columna real = email

    @Size(max = 255)
    @Column(name = "hash_password", length = 255)
    private String contrasena; // columna real = hash_password

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
