/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.edu.utn.frsf.kinesio.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Fran
 */
@Embeddable
public class TipoTratamientoObraSocialPK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Column(name = "tipotratamientoid")
    private short tipotratamientoid;
    @Basic(optional = false)
    @NotNull
    @Column(name = "obrasocialid")
    private short obrasocialid;

    public TipoTratamientoObraSocialPK() {
    }

    public TipoTratamientoObraSocialPK(short tipotratamientoid, short obrasocialid) {
        this.tipotratamientoid = tipotratamientoid;
        this.obrasocialid = obrasocialid;
    }

    public short getTipotratamientoid() {
        return tipotratamientoid;
    }

    public void setTipotratamientoid(short tipotratamientoid) {
        this.tipotratamientoid = tipotratamientoid;
    }

    public short getObrasocialid() {
        return obrasocialid;
    }

    public void setObrasocialid(short obrasocialid) {
        this.obrasocialid = obrasocialid;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) tipotratamientoid;
        hash += (int) obrasocialid;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TipoTratamientoObraSocialPK)) {
            return false;
        }
        TipoTratamientoObraSocialPK other = (TipoTratamientoObraSocialPK) object;
        if (this.tipotratamientoid != other.tipotratamientoid) {
            return false;
        }
        if (this.obrasocialid != other.obrasocialid) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ar.edu.utn.frsf.kinesio.entities.TipoTratamientoObraSocialPK[ tipotratamientoid=" + tipotratamientoid + ", obrasocialid=" + obrasocialid + " ]";
    }
    
}
