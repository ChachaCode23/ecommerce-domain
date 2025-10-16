package com.urbancollection.ecommerce.domain.enums;

/**
 * Enum que representa los diferentes estados posibles de un Pedido.
 */
public enum EstadoDePedido {
    PENDIENTE,   // 🟡 Pedido creado pero no procesado aún
    PAGADO,      // 💳 Pagado pero no enviado
    ENVIADO,     // 📦 En camino
    ENTREGADO,   // ✅ Entregado al cliente
    CANCELADO    // ❌ Cancelado por el usuario o el sistema
}
