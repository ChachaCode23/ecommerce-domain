package com.urbancollection.ecommerce.domain.entity.logistica;

import com.urbancollection.ecommerce.domain.base.BaseEntity;
import jakarta.persistence.*;

/**
 * Direccion
 *
 * Entidad que representa una dirección física de envío.
 * Está mapeada a la tabla core.Direccion en SQL Server.
 *
 *
 * Esta info se usa cuando el usuario hace un pedido y el sistema necesita saber
 * a dónde enviar el paquete.
 */
@Entity
@Table(name = "Direccion", schema = "core")
@AttributeOverride(name = "id", column = @Column(name = "direccion_id"))
public class Direccion extends BaseEntity {

    @Column(name = "usuario_id")
    private Integer usuarioId;

    @Column(name = "nombre_contacto", length = 150)
    private String nombreContacto;

    @Column(name = "linea1", length = 200, nullable = false)
    private String linea1;

    @Column(name = "linea2", length = 200)
    private String linea2;

    @Column(name = "ciudad", length = 100, nullable = false)
    private String ciudad;

    @Column(name = "provincia", length = 100)
    private String provincia;

    @Column(name = "codigo_postal", length = 20)
    private String codigoPostal;

    @Column(name = "pais", length = 100, nullable = false)
    private String pais;

    @Column(name = "es_principal", nullable = false)
    private Boolean esPrincipal = false;

    // ===== Getters & Setters =====
    public Integer getUsuarioId() {
        return usuarioId;
    }
    public void setUsuarioId(Integer usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getNombreContacto() {
        return nombreContacto;
    }
    public void setNombreContacto(String nombreContacto) {
        this.nombreContacto = nombreContacto;
    }

    public String getLinea1() {
        return linea1;
    }
    public void setLinea1(String linea1) {
        this.linea1 = linea1;
    }

    public String getLinea2() {
        return linea2;
    }
    public void setLinea2(String linea2) {
        this.linea2 = linea2;
    }

    public String getCiudad() {
        return ciudad;
    }
    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getProvincia() {
        return provincia;
    }
    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getCodigoPostal() {
        return codigoPostal;
    }
    public void setCodigoPostal(String codigoPostal) {
        this.codigoPostal = codigoPostal;
    }

    public String getPais() {
        return pais;
    }
    public void setPais(String pais) {
        this.pais = pais;
    }

    public Boolean getEsPrincipal() {
        return esPrincipal;
    }
    public void setEsPrincipal(Boolean esPrincipal) {
        this.esPrincipal = esPrincipal;
    }

    // Métodos legacy para compatibilidad
    public String getCalle() {
        return linea1;
    }
    public void setCalle(String calle) {
        this.linea1 = calle;
    }
}