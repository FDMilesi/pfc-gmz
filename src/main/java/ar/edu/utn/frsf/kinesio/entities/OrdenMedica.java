/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.edu.utn.frsf.kinesio.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Nacho Gómez
 */
@Entity
@Table(name = "ordenmedica")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "OrdenMedica.findAll", query = "SELECT o FROM OrdenMedica o"),
    @NamedQuery(name = "OrdenMedica.findById", query = "SELECT o FROM OrdenMedica o WHERE o.id = :id"),
    @NamedQuery(name = "OrdenMedica.findByCantidaddesesiones", query = "SELECT o FROM OrdenMedica o WHERE o.cantidadDeSesiones = :cantidaddesesiones"),
    @NamedQuery(name = "OrdenMedica.findByCodigodeautorizacion", query = "SELECT o FROM OrdenMedica o WHERE o.codigoDeAutorizacion = :codigodeautorizacion"),
    @NamedQuery(name = "OrdenMedica.findByFechacreacion", query = "SELECT o FROM OrdenMedica o WHERE o.fechaCreacion = :fechacreacion"),
    @NamedQuery(name = "OrdenMedica.findByPresentadaalcirculo", query = "SELECT o FROM OrdenMedica o WHERE o.presentadaAlCirculo = :presentadaalcirculo"),
    @NamedQuery(name = "OrdenMedica.findByNumeroafiliadopaciente", query = "SELECT o FROM OrdenMedica o WHERE o.numeroAfiliadoPaciente = :numeroafiliadopaciente"),
    @NamedQuery(name = "OrdenMedica.findByTratamiento", query = "SELECT o FROM OrdenMedica o WHERE o.tratamiento = :tratamiento"),
    @NamedQuery(name = "OrdenMedica.findByAutorizadas", query = "SELECT o FROM OrdenMedica o WHERE o.codigoDeAutorizacion IS NOT NULL"),
    @NamedQuery(name = "OrdenMedica.findByNoAutorizadas", query = "SELECT o FROM OrdenMedica o WHERE o.codigoDeAutorizacion IS NULL"),
    @NamedQuery(name = "OrdenMedica.findByAutorizadasyPresentacion", query = "SELECT o FROM OrdenMedica o WHERE o.codigoDeAutorizacion IS NOT NULL and o.presentadaAlCirculo = :presentada"),
    @NamedQuery(name = "OrdenMedica.findByNoAutorizadasyPresentacion", query = "SELECT o FROM OrdenMedica o WHERE o.codigoDeAutorizacion IS NULL and o.presentadaAlCirculo = :presentada")})
public class OrdenMedica implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "cantidaddesesiones")
    @Min(1)
    @Max(99)
    private short cantidadDeSesiones;
    
    @Size(max = 50)
    @Column(name = "codigodeautorizacion")
    private String codigoDeAutorizacion;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "fechacreacion")
    @Temporal(TemporalType.DATE)
    private Date fechaCreacion;
    
    @Column(name = "presentadaalcirculo")
    private Boolean presentadaAlCirculo;
    
    @Size(max = 10)
    @Column(name = "numeroafiliadopaciente")
    private String numeroAfiliadoPaciente;
    
    @JoinColumn(name = "obrasocialid", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private ObraSocial obraSocial;
    
    @JoinColumn(name = "tratamientoid", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Tratamiento tratamiento;

    public OrdenMedica() {
    }

    public OrdenMedica(Integer id) {
        this.id = id;
    }

    public OrdenMedica(Integer id, short cantidaddesesiones, Date fechacreacion) {
        this.id = id;
        this.cantidadDeSesiones = cantidaddesesiones;
        this.fechaCreacion = fechacreacion;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public short getCantidadDeSesiones() {
        return cantidadDeSesiones;
    }

    public void setCantidadDeSesiones(short cantidadDeSesiones) {
        this.cantidadDeSesiones = cantidadDeSesiones;
    }

    public String getCodigoDeAutorizacion() {
        return codigoDeAutorizacion;
    }

    public void setCodigoDeAutorizacion(String codigoDeAutorizacion) {
        this.codigoDeAutorizacion = codigoDeAutorizacion;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Boolean getPresentadaAlCirculo() {
        return presentadaAlCirculo;
    }

    public void setPresentadaAlCirculo(Boolean presentadaAlCirculo) {
        this.presentadaAlCirculo = presentadaAlCirculo;
    }

    public String getNumeroAfiliadoPaciente() {
        return numeroAfiliadoPaciente;
    }

    public void setNumeroAfiliadoPaciente(String numeroAfiliadoPaciente) {
        this.numeroAfiliadoPaciente = numeroAfiliadoPaciente;
    }

    public ObraSocial getObraSocial() {
        return obraSocial;
    }

    public void setObraSocial(ObraSocial obraSocial) {
        this.obraSocial = obraSocial;
    }

    public Tratamiento getTratamiento() {
        return tratamiento;
    }

    public void setTratamiento(Tratamiento tratamiento) {
        this.tratamiento = tratamiento;
    }
    
    public boolean isAutorizada(){
        return (codigoDeAutorizacion != null);
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
        if (!(object instanceof OrdenMedica)) {
            return false;
        }
        OrdenMedica other = (OrdenMedica) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ar.edu.utn.frsf.kinesio.entities.OrdenMedica[ id=" + id + " ]";
    }
    
}
