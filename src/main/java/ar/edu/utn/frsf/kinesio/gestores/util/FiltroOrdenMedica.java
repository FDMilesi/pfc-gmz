package ar.edu.utn.frsf.kinesio.gestores.util;

import ar.edu.utn.frsf.kinesio.entities.ObraSocial;
import ar.edu.utn.frsf.kinesio.entities.TipoDeTratamiento;
import java.util.Date;

public class FiltroOrdenMedica {

    Boolean autorizada;
    Boolean presentada;
    ObraSocial obraSocial;
    Date startDate;
    Date endDate;
    ObraSocial obraSocialExcluida;
    TipoDeTratamiento tipoDeTratamiento;
    TipoDeTratamiento tipoDeTratamientoExcluido;

    public Boolean getAutorizada() {
        return autorizada;
    }

    public void setAutorizada(Boolean autorizada) {
        this.autorizada = autorizada;
    }

    public Boolean getPresentada() {
        return presentada;
    }

    public void setPresentada(Boolean presentada) {
        this.presentada = presentada;
    }

    public ObraSocial getObraSocial() {
        return obraSocial;
    }

    public void setObraSocial(ObraSocial obraSocial) {
        this.obraSocial = obraSocial;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public ObraSocial getObraSocialExcluida() {
        return obraSocialExcluida;
    }

    public void setObraSocialExcluida(ObraSocial obraSocialExcluida) {
        this.obraSocialExcluida = obraSocialExcluida;
    }

    public TipoDeTratamiento getTipoDeTratamiento() {
        return tipoDeTratamiento;
    }

    public void setTipoDeTratamiento(TipoDeTratamiento tipoDeTratamiento) {
        this.tipoDeTratamiento = tipoDeTratamiento;
    }

    public TipoDeTratamiento getTipoDeTratamientoExcluido() {
        return tipoDeTratamientoExcluido;
    }

    public void setTipoDeTratamientoExcluido(TipoDeTratamiento tipoDeTratamientoExcluido) {
        this.tipoDeTratamientoExcluido = tipoDeTratamientoExcluido;
    }
    
}
