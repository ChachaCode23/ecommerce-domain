package com.urbancollection.ecommerce.domain.entity.ventas;

import com.urbancollection.ecommerce.domain.base.BaseEntity;

/**
 * Esta clase representa cada producto individual
 * que forma parte de un pedido dentro de la plataforma.
 *
 * Hereda de BaseEntity, por lo que incluye un id unico.
 *
 * En una version completa podria tener:
 * - productoId: referencia al producto comprado
 * - cantidad: unidades del producto
 * - precioUnitario: precio por unidad al momento de la compra
 *
 * Esta entidad ayuda a desglosar los pedidos en sus componentes,
 * permitiendo calcular totales y manejar inventarios.
 */
public class ItemPedido extends BaseEntity {
    // Ejemplo de campos:
    // private Long productoId;
    // private int cantidad;
    // private BigDecimal precioUnitario;

    // Se pueden agregar getters y setters para estos campos.
}
