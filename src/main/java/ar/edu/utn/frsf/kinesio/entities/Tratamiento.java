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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

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
    @NamedQuery(name = "Tratamiento.findByParticular", query = "SELECT t FROM Tratamiento t WHERE t.particular = :particular"),
    @NamedQuery(name = "Tratamiento.findByFinalizado", query = "SELECT t FROM Tratamiento t WHERE t.finalizado = :finalizado"),
    @NamedQuery(name = "Tratamiento.findByPaciente", query = "SELECT t FROM Tratamiento t WHERE t.paciente = :paciente")})
public class Tratamiento implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Basic(optional = false)
    @NotNull
    @Column(name = "cantidaddesesiones")
    private short cantidadDeSesiones;

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
    private Date fechaCreacion;

    @Column(name = "sesionesafavor")
    private Short sesionesAFavor;

    @Column(name = "sesionesafavorusadas")
    private Boolean sesionesAFavorUsadas;

    @Column(name = "fechaultimaautorizacion")
    @Temporal(TemporalType.DATE)
    private Date fechaUltimaAutorizacion;

    @JoinColumn(name = "pacienteid", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Paciente paciente;

    @JoinColumn(name = "tipodetratamientoid", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private TipoDeTratamiento tipoDeTratamiento;
    
    @OneToOne
    @JoinColumn(name = "tratamientoasociadoid")
    private Tratamiento tratamientoAsociado;

    public Tratamiento() {
    }

    public Tratamiento(Integer id, short cantidaddesesiones, boolean particular) {
        this.id = id;
        this.cantidadDeSesiones = cantidaddesesiones;
        this.particular = particular;
    }

    public Tratamiento(Paciente paciente) {
        this.paciente = paciente;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public short getCantidadDeSesiones() {
        return cantidadDeSesiones;
    }

    public void setCantidadDeSesiones(short cantidadDeSesiones) {
        this.cantidadDeSesiones = cantidadDeSesiones;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Short getSesionesAFavor() {
        return sesionesAFavor;
    }

    public void setSesionesAFavor(Short sesionesAFavor) {
        this.sesionesAFavor = sesionesAFavor;
    }

    public Boolean getSesionesAFavorUsadas() {
        return sesionesAFavorUsadas;
    }

    public void setSesionesAFavorUsadas(Boolean sesionesAFavorUsadas) {
        this.sesionesAFavorUsadas = sesionesAFavorUsadas;
    }

    public Date getFechaUltimaAutorizacion() {
        return fechaUltimaAutorizacion;
    }

    public void setFechaUltimaAutorizacion(Date fechaUltimaAutorizacion) {
        this.fechaUltimaAutorizacion = fechaUltimaAutorizacion;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public TipoDeTratamiento getTipoDeTratamiento() {
        return tipoDeTratamiento;
    }

    public void setTipoDeTratamiento(TipoDeTratamiento tipoDeTratamiento) {
        this.tipoDeTratamiento = tipoDeTratamiento;
    }

    public Tratamiento getTratamientoAsociado() {
        return tratamientoAsociado;
    }

    public void setTratamientoAsociado(Tratamiento tratamientoAsociado) {
        this.tratamientoAsociado = tratamientoAsociado;
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
        return tipoDeTratamiento + "-" + diagnostico;
    }

}
