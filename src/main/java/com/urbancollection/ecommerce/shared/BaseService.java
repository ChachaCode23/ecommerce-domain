package com.urbancollection.ecommerce.shared;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * clase base para servicios
 * Contiene un Logger para registrar información y errores,
 * y un metodo de manejo de excepciones para evitar repetir codigo.
 */
public abstract class BaseService {

    //Logger para todas las clases que hereden de esta
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * Metodo centralizado para manejar errores.
     * este metodo nos muestra un mensaje claro y registrar el detalle del error.
     */
    protected void handleError(Exception e, String message) {
        // Muestra en consola un mensaje personalizado + el error real
        logger.error("❌ {} - Detalle: {}", message, e.getMessage());

    }
}
