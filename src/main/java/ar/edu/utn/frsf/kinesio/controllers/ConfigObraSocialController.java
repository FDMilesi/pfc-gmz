package ar.edu.utn.frsf.kinesio.controllers;

import ar.edu.utn.frsf.kinesio.entities.ObraSocial;
import ar.edu.utn.frsf.kinesio.entities.TipoDeTratamiento;
import ar.edu.utn.frsf.kinesio.entities.TipoTratamientoObraSocial;
import ar.edu.utn.frsf.kinesio.entities.TipoTratamientoObraSocialPK;
import ar.edu.utn.frsf.kinesio.gestores.TipoTratamientoObraSocialFacade;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

@Named("configObraSocialController")
@ViewScoped
public class ConfigObraSocialController implements Serializable {

    @EJB
    private TipoTratamientoObraSocialFacade tipoTratamientoObraSocialFacade;

    private ObraSocial currentObraSocial;
    private TipoDeTratamiento currentTipoDeTratamiento;
    private TipoTratamientoObraSocial currentTipoTratamientoObraSocial;
 
    public ConfigObraSocialController() {
    }

    public void buscarTipoTratamientoObraSocial() {
        if (currentObraSocial != null && currentTipoDeTratamiento != null){
            TipoTratamientoObraSocialPK key = new TipoTratamientoObraSocialPK(currentTipoDeTratamiento.getId(), currentObraSocial.getId());
            //Busco la relación (puede no existir y retornar null
           currentTipoTratamientoObraSocial = tipoTratamientoObraSocialFacade.find(key);
           //Si es null
           if(currentTipoTratamientoObraSocial == null){
               //Creo la relación para luego guardarla
               currentTipoTratamientoObraSocial = new TipoTratamientoObraSocial(key);
               currentTipoTratamientoObraSocial.setTopeSesionesAño((short)25);
           }
        }
    }

    public void updateTipoTratamientoObraSocial(){
        this.tipoTratamientoObraSocialFacade.edit(currentTipoTratamientoObraSocial);
    }
    
    public ObraSocial getCurrentObraSocial() {
        return currentObraSocial;
    }

    public void setCurrentObraSocial(ObraSocial currentObraSocial) {
        this.currentObraSocial = currentObraSocial;
    }

    public TipoDeTratamiento getCurrentTipoDeTratamiento() {
        return currentTipoDeTratamiento;
    }

    public void setCurrentTipoDeTratamiento(TipoDeTratamiento currentTipoDeTratamiento) {
        this.currentTipoDeTratamiento = currentTipoDeTratamiento;
    }

    public TipoTratamientoObraSocial getCurrentTipoTratamientoObraSocial() {
        return currentTipoTratamientoObraSocial;
    }

    public void setCurrentTipoTratamientoObraSocial(TipoTratamientoObraSocial currentTipoTratamientoObraSocial) {
        this.currentTipoTratamientoObraSocial = currentTipoTratamientoObraSocial;
    }

}
