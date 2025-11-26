package com.urbancollection.ecommerce.domain.entity.usuarios;

import com.urbancollection.ecommerce.domain.base.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

// Entidad JPA que representa a un usuario en el sistema.
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
// Sobrescribimos la columna "id" de BaseEntity para que en BD se llame "usuario_id".
@AttributeOverride(name = "id", column = @Column(name = "usuario_id"))
public class Usuario extends BaseEntity {

    // Nombre del usuario, obligatorio y con máximo de 150 caracteres.
    @NotBlank
    @Size(max = 150)
    @Column(name = "nombre", length = 150, nullable = false)
    private String nombre;

    // Correo electrónico del usuario, obligatorio, con formato de email y máximo 150 caracteres.
    @NotBlank
    @Email
    @Size(max = 150)
    @Column(name = "email", length = 150, nullable = false)
    private String correo;

    // Contraseña en formato hash, no obligatoria, hasta 255 caracteres.
    @Size(max = 255)
    @Column(name = "hash_password", length = 255)
    private String contrasena;

    // Rol del usuario (por ejemplo: ADMIN, CUSTOMER), obligatorio y con máximo 20 caracteres.
    @NotBlank
    @Size(max = 20)
    @Column(name = "rol", length = 20, nullable = false)
    private String rol;

    // Teléfono del usuario, opcional, máximo 20 caracteres.
    @Size(max = 20)
    @Column(name = "telefono", length = 20)
    private String telefono;

    // Indica si el usuario está activo o no. Por defecto true.
    @Column(name = "activo")
    private Boolean activo = true;

    // ------- Getters y Setters --------

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

    // Método auxiliar para los controladores que usan "email" en lugar de "correo".
    public void setEmail(String email) {
        this.correo = email;
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

    public String getTelefono() {
        return telefono;
    }
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Boolean getActivo() {
        return activo;
    }
    public void setActivo(Boolean activo) {
        this.activo = activo;
    }
}
