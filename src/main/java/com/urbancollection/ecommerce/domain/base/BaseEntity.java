package com.urbancollection.ecommerce.domain.base;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;

/**
 * BaseEntity (clase abstracta)
 * Marcada como @MappedSuperclass para que las entidades hijas hereden el campo id
 * y sus anotaciones JPA, sin crear una tabla propia para BaseEntity.
 */
@MappedSuperclass
public abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id") // opcional, fija el nombre de la columna
    protected Long id;

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }
}
