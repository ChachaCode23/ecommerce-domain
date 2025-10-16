package com.urbancollection.ecommerce.domain.base;

// BaseEntity (clase abstracta)

public abstract class BaseEntity {

    /**
     * Identificador único de la entidad.
     * - Representa la clave primaria en la base de datos.
     * - Debe ser estable y no cambiar arbitrariamente durante el ciclo de vida del objeto.
     */
    protected Long id;

    /**
     * Obtiene el identificador único de la entidad.
     * @return Long id (puede ser null si aún no se ha guardado en la BD).
     */
    public Long getId() {
        return id;
    }

    /**
     * Asigna el identificador único a la entidad.
     * @param id valor del identificador (normalmente asignado por la capa de persistencia).
     */
    public void setId(Long id) {
        this.id = id;
    }
}
