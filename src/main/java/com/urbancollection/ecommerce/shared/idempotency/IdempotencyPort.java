package com.urbancollection.ecommerce.shared.idempotency;

// Marca una clave de idempotencia como usada y responde si ya existía.
public interface IdempotencyPort {
    boolean wasSeen(String key);           // true si ya fue usada
    void remember(String key, long ttlMs); // TTL no persistente
}
