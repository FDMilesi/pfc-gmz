/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.edu.utn.frsf.kinesio.entities;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Fran
 */
@Entity
@Table(name = "tipotratamientoobrasocial")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TipoTratamientoObraSocial.findAll", query = "SELECT t FROM TipoTratamientoObraSocial t"),
    @NamedQuery(name = "TipoTratamientoObraSocial.findByTipotratamientoid", query = "SELECT t FROM TipoTratamientoObraSocial t WHERE t.tipoTratamientoObraSocialPK.tipotratamientoid = :tipotratamientoid"),
    @NamedQuery(name = "TipoTratamientoObraSocial.findByObrasocialid", query = "SELECT t FROM TipoTratamientoObraSocial t WHERE t.tipoTratamientoObraSocialPK.obrasocialid = :obrasocialid"),
    @NamedQuery(name = "TipoTratamientoObraSocial.findByCodigodeprestacion", query = "SELECT t FROM TipoTratamientoObraSocial t WHERE t.codigodeprestacion = :codigodeprestacion"),
    @NamedQuery(name = "TipoTratamientoObraSocial.findByTopesesionesa\u00f1o", query = "SELECT t FROM TipoTratamientoObraSocial t WHERE t.topesesionesa\u00f1o = :topesesionesa\u00f1o")})
public class TipoTratamientoObraSocial implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected TipoTratamientoObraSocialPK tipoTratamientoObraSocialPK;
    @Size(max = 20)
    @Column(name = "codigodeprestacion")
    private String codigoDePrestacion;
    @Column(name = "topesesionesa\u00f1o")
    private Short topeSesionesAño;
    @JoinColumn(name = "obrasocialid", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private ObraSocial obraSocial;
    @JoinColumn(name = "tipotratamientoid", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private TipoDeTratamiento tipoDeTratamiento;

    public TipoTratamientoObraSocial() {
    }

    public TipoTratamientoObraSocial(TipoTratamientoObraSocialPK tipoTratamientoObraSocialPK) {
        this.tipoTratamientoObraSocialPK = tipoTratamientoObraSocialPK;
    }

    public TipoTratamientoObraSocial(short tipotratamientoid, short obrasocialid) {
        this.tipoTratamientoObraSocialPK = new TipoTratamientoObraSocialPK(tipotratamientoid, obrasocialid);
    }

    public TipoTratamientoObraSocialPK getTipoTratamientoObraSocialPK() {
        return tipoTratamientoObraSocialPK;
    }

    public void setTipoTratamientoObraSocialPK(TipoTratamientoObraSocialPK tipoTratamientoObraSocialPK) {
        this.tipoTratamientoObraSocialPK = tipoTratamientoObraSocialPK;
    }

    public String getCodigoDePrestacion() {
        return codigoDePrestacion;
    }

    public void setCodigoDePrestacion(String codigoDePrestacion) {
        this.codigoDePrestacion = codigoDePrestacion;
    }

    public Short getTopeSesionesAño() {
        return topeSesionesAño;
    }

    public void setTopeSesionesAño(Short topeSesionesAño) {
        this.topeSesionesAño = topeSesionesAño;
    }

    public ObraSocial getObraSocial() {
        return obraSocial;
    }

    public void setObraSocial(ObraSocial obraSocial) {
        this.obraSocial = obraSocial;
    }

    public TipoDeTratamiento getTipoDeTratamiento() {
        return tipoDeTratamiento;
    }

    public void setTipoDeTratamiento(TipoDeTratamiento tipoDeTratamiento) {
        this.tipoDeTratamiento = tipoDeTratamiento;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tipoTratamientoObraSocialPK != null ? tipoTratamientoObraSocialPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TipoTratamientoObraSocial)) {
            return false;
        }
        TipoTratamientoObraSocial other = (TipoTratamientoObraSocial) object;
        if ((this.tipoTratamientoObraSocialPK == null && other.tipoTratamientoObraSocialPK != null) || (this.tipoTratamientoObraSocialPK != null && !this.tipoTratamientoObraSocialPK.equals(other.tipoTratamientoObraSocialPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ar.edu.utn.frsf.kinesio.entities.TipoTratamientoObraSocial[ tipoTratamientoObraSocialPK=" + tipoTratamientoObraSocialPK + " ]";
    }
    
}
