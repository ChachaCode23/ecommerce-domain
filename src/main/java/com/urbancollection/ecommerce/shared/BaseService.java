package com.urbancollection.ecommerce.shared;

import com.urbancollection.ecommerce.shared.logging.LoggerPort;

/**
 * BaseService
 * - NO depende de SLF4J (solo de la abstracción LoggerPort).
 * - Las implementaciones concretas del logger se inyectan desde "infrastructure".
 */
public abstract class BaseService {

    protected LoggerPort logger;

    /** Permite inyectar el logger adapter (p. ej., Slf4jLoggerAdapter) desde Infrastructure. */
    public void setLogger(LoggerPort logger) {
        this.logger = logger;
    }

    /** Manejo centralizado de errores: registra y deja listo para extender. */
    protected void handleError(Exception e, String message) {
        if (logger != null) {
            logger.error("{}: {}", message, e.getMessage());
        }
    }
}
