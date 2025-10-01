package com.urbancollection.ecommerce.domain.base;

/**
 * Clase base para todas las entidades del sistema.
 * Contiene un identificador único que será heredado por las demás clases del dominio.
 */
public abstract class BaseEntity {

    /**
     * Identificador unico de la entidad.
     */
    protected Long id;

    /**
     * Retorna el identificador unico de la entidad.
     */
    public Long getId() {
        return id;
    }

    /**
     * Asigna un identificador unico a la entidad.
     */
    public void setId(Long id) {
        this.id = id;
    }
}
