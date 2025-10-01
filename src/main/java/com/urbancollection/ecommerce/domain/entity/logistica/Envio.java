package com.urbancollection.ecommerce.domain.entity.logistica;

import com.urbancollection.ecommerce.domain.base.BaseEntity;

/**
 * Esta clase representa la informacion relacionada
 * con el proceso de entrega de un pedido en el sistema.
 *
 * Hereda de BaseEntity, por lo que incluye un id unico.
 *
 * En una version completa podria contener campos como:
 * - fechaEnvio: fecha en que se envio el pedido
 * - estado: estado actual del envio (ej. En camino, Entregado)
 * - direccion: referencia a la direccion de entrega
 *
 * Esta entidad permite gestionar y registrar los envios
 * realizados a los clientes.
 */
public class Envio extends BaseEntity {
    // Ejemplo de campos posibles:
    // private Date fechaEnvio;
    // private String estado;
    // private Direccion direccion;

    // Se pueden agregar los getters y setters correspondientes.
}
