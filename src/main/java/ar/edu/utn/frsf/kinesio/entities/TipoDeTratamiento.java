/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.edu.utn.frsf.kinesio.entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Fran
 */
@Entity
@Table(name = "tipodetratamiento")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TipoDeTratamiento.findAll", query = "SELECT t FROM TipoDeTratamiento t"),
    @NamedQuery(name = "TipoDeTratamiento.findById", query = "SELECT t FROM TipoDeTratamiento t WHERE t.id = :id"),
    @NamedQuery(name = "TipoDeTratamiento.findByNombre", query = "SELECT t FROM TipoDeTratamiento t WHERE t.nombre = :nombre"),
    @NamedQuery(name = "TipoDeTratamiento.findByDuracion", query = "SELECT t FROM TipoDeTratamiento t WHERE t.duracion = :duracion")})
public class TipoDeTratamiento implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Short id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "nombre")
    private String nombre;
    @Basic(optional = false)
    @NotNull
    @Column(name = "duracion")
    private short duracion;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "cubiertoporobrasocial")
    private boolean cubiertoPorObraSocial;
    
    @JoinColumn(name = "especialidadid", referencedColumnName = "id")
    @ManyToOne
    private Especialidad especialidadid;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoDeTratamiento")
    private List<TipoTratamientoObraSocial> tipoTratamientoObraSocialList;

    public TipoDeTratamiento() {
    }

    public TipoDeTratamiento(Short id) {
        this.id = id;
    }

    public TipoDeTratamiento(Short id, String nombre, short duracion, boolean cubiertoporobrasocial) {
        this.id = id;
        this.nombre = nombre;
        this.duracion = duracion;
        this.cubiertoPorObraSocial = cubiertoporobrasocial;
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

    public short getDuracion() {
        return duracion;
    }

    public void setDuracion(short duracion) {
        this.duracion = duracion;
    }

    public boolean isCubiertoPorObraSocial() {
        return cubiertoPorObraSocial;
    }

    public void setCubiertoPorObraSocial(boolean cubiertoPorObraSocial) {
        this.cubiertoPorObraSocial = cubiertoPorObraSocial;
    }

    public Especialidad getEspecialidadid() {
        return especialidadid;
    }

    public void setEspecialidadid(Especialidad especialidadid) {
        this.especialidadid = especialidadid;
    }

    @XmlTransient
    public List<TipoTratamientoObraSocial> getTipoTratamientoObraSocialList() {
        return tipoTratamientoObraSocialList;
    }

    public void setTipoTratamientoObraSocialList(List<TipoTratamientoObraSocial> tipoTratamientoObraSocialList) {
        this.tipoTratamientoObraSocialList = tipoTratamientoObraSocialList;
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
        if (!(object instanceof TipoDeTratamiento)) {
            return false;
        }
        TipoDeTratamiento other = (TipoDeTratamiento) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return nombre;
    }
    
}
