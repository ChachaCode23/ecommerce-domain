package com.urbancollection.ecommerce.domain.entity.usuarios;

import com.urbancollection.ecommerce.domain.base.BaseEntity;

/**
 *Esta Clase representa una lista de productos
 * que un usuario marca como favoritos o que desea comprar mas adelante.
 *
 * Hereda de BaseEntity, por lo que incluye un id unico.
 *
 * En una version completa podria contener campos como:
 * - idUsuario: identificador del usuario propietario de la lista
 * - productos: coleccion de productos agregados a la lista
 *
 * Esta entidad es util para mejorar la experiencia del cliente
 * dentro de la plataforma.
 */
public class ListaDeseos extends BaseEntity {
    // Ejemplo de campos posibles:
    // private Long idUsuario;
    // private List<Producto> productos;

    // Se pueden agregar los getters y setters correspondientes.
}
