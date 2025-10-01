package com.urbancollection.ecommerce.domain.entity.ventas;

import com.urbancollection.ecommerce.domain.base.BaseEntity;

/**
 * Esta clase representa una orden de compra
 * realizada por un usuario dentro del sistema.
 *
 * Hereda de BaseEntity, por lo que incluye un id unico.
 *
 * En una version completa podria incluir:
 * - usuarioId: referencia al cliente que realiza la compra
 * - fechaCreacion: momento en que se realiza el pedido
 * - total: monto total de la compra
 * - estado: indica si esta en proceso, enviado o completado
 *
 * Esta entidad permite gestionar las ordenes de los clientes
 * y mantener el control de las transacciones dentro del sistema.
 */
public class Pedido extends BaseEntity {
    // Ejemplo de campos:
    // private Long usuarioId;
    // private LocalDate fechaCreacion;
    // private BigDecimal total;
    // private String estado;
}
