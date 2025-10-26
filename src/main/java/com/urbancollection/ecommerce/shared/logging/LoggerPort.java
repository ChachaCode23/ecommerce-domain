package com.urbancollection.ecommerce.shared.logging;

/**
 * Puerto de logging usado por la capa dominio.
 * La implementación concreta vive en ecommerce-api (Slf4jLoggerAdapter).
 */
public interface LoggerPort {

    void info(String message, Object... args);

    void warn(String message, Object... args);

    void error(String message, Object... args);

    void error(Throwable t, String message, Object... args);
}
