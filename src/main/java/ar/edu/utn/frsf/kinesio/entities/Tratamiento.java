/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.edu.utn.frsf.kinesio.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Fran
 */
@Entity
@Table(name = "tratamiento")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Tratamiento.findAll", query = "SELECT t FROM Tratamiento t"),
    @NamedQuery(name = "Tratamiento.findById", query = "SELECT t FROM Tratamiento t WHERE t.id = :id"),
    @NamedQuery(name = "Tratamiento.findByCantidaddesesiones", query = "SELECT t FROM Tratamiento t WHERE t.cantidaddesesiones = :cantidaddesesiones"),
    @NamedQuery(name = "Tratamiento.findByDiagnostico", query = "SELECT t FROM Tratamiento t WHERE t.diagnostico = :diagnostico"),
    @NamedQuery(name = "Tratamiento.findByParticular", query = "SELECT t FROM Tratamiento t WHERE t.particular = :particular"),
    @NamedQuery(name = "Tratamiento.findByFinalizado", query = "SELECT t FROM Tratamiento t WHERE t.finalizado = :finalizado"),
    @NamedQuery(name = "Tratamiento.findByObservaciones", query = "SELECT t FROM Tratamiento t WHERE t.observaciones = :observaciones"),
    @NamedQuery(name = "Tratamiento.findByFechacreacion", query = "SELECT t FROM Tratamiento t WHERE t.fechacreacion = :fechacreacion"),
    @NamedQuery(name = "Tratamiento.findBySesionesafavor", query = "SELECT t FROM Tratamiento t WHERE t.sesionesafavor = :sesionesafavor"),
    @NamedQuery(name = "Tratamiento.findBySesionesafavorusadas", query = "SELECT t FROM Tratamiento t WHERE t.sesionesafavorusadas = :sesionesafavorusadas"),
    @NamedQuery(name = "Tratamiento.findByFechaultimaautorizacion", query = "SELECT t FROM Tratamiento t WHERE t.fechaultimaautorizacion = :fechaultimaautorizacion")})
public class Tratamiento implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cantidaddesesiones")
    private short cantidaddesesiones;
    @Size(max = 100)
    @Column(name = "diagnostico")
    private String diagnostico;
    @Basic(optional = false)
    @NotNull
    @Column(name = "particular")
    private boolean particular;
    @Column(name = "finalizado")
    private Boolean finalizado;
    @Size(max = 2147483647)
    @Column(name = "observaciones")
    private String observaciones;
    @Column(name = "fechacreacion")
    @Temporal(TemporalType.DATE)
    private Date fechacreacion;
    @Column(name = "sesionesafavor")
    private Short sesionesafavor;
    @Column(name = "sesionesafavorusadas")
    private Boolean sesionesafavorusadas;
    @Column(name = "fechaultimaautorizacion")
    @Temporal(TemporalType.DATE)
    private Date fechaultimaautorizacion;
    @JoinColumn(name = "pacienteid", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Paciente pacienteid;
    @JoinColumn(name = "tipodetratamientoid", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private TipoDeTratamiento tipodetratamientoid;
    @OneToMany(mappedBy = "tratamientoasociadoid")
    private List<Tratamiento> tratamientoList;
    @JoinColumn(name = "tratamientoasociadoid", referencedColumnName = "id")
    @ManyToOne
    private Tratamiento tratamientoasociadoid;

    public Tratamiento() {
    }

    public Tratamiento(Integer id) {
        this.id = id;
    }

    public Tratamiento(Integer id, short cantidaddesesiones, boolean particular) {
        this.id = id;
        this.cantidaddesesiones = cantidaddesesiones;
        this.particular = particular;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public short getCantidaddesesiones() {
        return cantidaddesesiones;
    }

    public void setCantidaddesesiones(short cantidaddesesiones) {
        this.cantidaddesesiones = cantidaddesesiones;
    }

    public String getDiagnostico() {
        return diagnostico;
    }

    public void setDiagnostico(String diagnostico) {
        this.diagnostico = diagnostico;
    }

    public boolean getParticular() {
        return particular;
    }

    public void setParticular(boolean particular) {
        this.particular = particular;
    }

    public Boolean getFinalizado() {
        return finalizado;
    }

    public void setFinalizado(Boolean finalizado) {
        this.finalizado = finalizado;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Date getFechacreacion() {
        return fechacreacion;
    }

    public void setFechacreacion(Date fechacreacion) {
        this.fechacreacion = fechacreacion;
    }

    public Short getSesionesafavor() {
        return sesionesafavor;
    }

    public void setSesionesafavor(Short sesionesafavor) {
        this.sesionesafavor = sesionesafavor;
    }

    public Boolean getSesionesafavorusadas() {
        return sesionesafavorusadas;
    }

    public void setSesionesafavorusadas(Boolean sesionesafavorusadas) {
        this.sesionesafavorusadas = sesionesafavorusadas;
    }

    public Date getFechaultimaautorizacion() {
        return fechaultimaautorizacion;
    }

    public void setFechaultimaautorizacion(Date fechaultimaautorizacion) {
        this.fechaultimaautorizacion = fechaultimaautorizacion;
    }

    public Paciente getPacienteid() {
        return pacienteid;
    }

    public void setPacienteid(Paciente pacienteid) {
        this.pacienteid = pacienteid;
    }

    public TipoDeTratamiento getTipodetratamientoid() {
        return tipodetratamientoid;
    }

    public void setTipodetratamientoid(TipoDeTratamiento tipodetratamientoid) {
        this.tipodetratamientoid = tipodetratamientoid;
    }

    @XmlTransient
    public List<Tratamiento> getTratamientoList() {
        return tratamientoList;
    }

    public void setTratamientoList(List<Tratamiento> tratamientoList) {
        this.tratamientoList = tratamientoList;
    }

    public Tratamiento getTratamientoasociadoid() {
        return tratamientoasociadoid;
    }

    public void setTratamientoasociadoid(Tratamiento tratamientoasociadoid) {
        this.tratamientoasociadoid = tratamientoasociadoid;
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
        if (!(object instanceof Tratamiento)) {
            return false;
        }
        Tratamiento other = (Tratamiento) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ar.edu.utn.frsf.kinesio.entities.Tratamiento[ id=" + id + " ]";
    }
    
}
