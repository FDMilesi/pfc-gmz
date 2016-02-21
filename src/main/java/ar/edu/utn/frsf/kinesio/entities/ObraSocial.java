/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.edu.utn.frsf.kinesio.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Nacho Gómez
 */
@Entity
@Table(name = "obrasocial")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Obrasocial.findAll", query = "SELECT o FROM Obrasocial o"),
    @NamedQuery(name = "Obrasocial.findById", query = "SELECT o FROM Obrasocial o WHERE o.id = :id"),
    @NamedQuery(name = "Obrasocial.findByNombre", query = "SELECT o FROM Obrasocial o WHERE o.nombre = :nombre"),
    @NamedQuery(name = "Obrasocial.findByLinkautorizacion", query = "SELECT o FROM Obrasocial o WHERE o.linkautorizacion = :linkautorizacion")})
public class ObraSocial implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Short id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "nombre")
    private String nombre;
    @Size(max = 256)
    @Column(name = "linkautorizacion")
    private String linkautorizacion;

    public ObraSocial() {
    }

    public ObraSocial(Short id) {
        this.id = id;
    }

    public ObraSocial(Short id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public Short getId() {
        return id;
    }

    public void setId(Short id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getLinkautorizacion() {
        return linkautorizacion;
    }

    public void setLinkautorizacion(String linkautorizacion) {
        this.linkautorizacion = linkautorizacion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ObraSocial)) {
            return false;
        }
        ObraSocial other = (ObraSocial) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ar.edu.utn.frsf.kinesio.entities.Obrasocial[ id=" + id + " ]";
    }
    
}
