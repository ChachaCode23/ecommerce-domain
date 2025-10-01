package com.urbancollection.ecommerce.domain.entity.usuarios;

import com.urbancollection.ecommerce.domain.base.BaseEntity;

/**
 * Esta clase representa a una persona registrada
 * dentro de la plataforma.
 *
 * Hereda de BaseEntity, por lo que ya posee un id unico.
 *
 * En una version completa podria incluir:
 * - nombre: nombre del usuario
 * - correo: email de contacto o login
 * - contrasena: clave para acceder al sistema
 * - rol: tipo de usuario (cliente, administrador, etc.)
 *
 * Esta entidad es esencial para manejar autenticacion y operaciones
 * relacionadas con los clientes dentro del sistema.
 */
public class Usuario extends BaseEntity {
    // Ejemplo de campos posibles:
    // private String nombre;
    // private String correo;
    // private String contrasena;
    // private String rol;

    // Se pueden agregar los getters y setters correspondientes.
}
