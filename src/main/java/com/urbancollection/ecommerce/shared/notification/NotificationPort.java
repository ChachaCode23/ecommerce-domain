package com.urbancollection.ecommerce.shared.notification;

/**
 * Puerto de notificaciones (lado dominio).
 * Por ahora solo necesitamos mandar mensajes informativos.
 */
public interface NotificationPort {

    /**
     * Notificación informativa (ej: "tu pago fue confirmado").
     */
    void sendInfo(String to, String message);
}
