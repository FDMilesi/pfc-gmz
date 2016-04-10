/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.edu.utn.frsf.kinesio.controllers;

import ar.edu.utn.frsf.kinesio.entities.ObraSocial;
import ar.edu.utn.frsf.kinesio.entities.TipoDeTratamiento;
import ar.edu.utn.frsf.kinesio.entities.TipoTratamientoObraSocial;
import ar.edu.utn.frsf.kinesio.gestores.TipoDeTratamientoFacade;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author fer_0
 */
@Named(value = "tipoDeTratamientoController")
@ApplicationScoped
public class TipoDeTratamientoController implements Serializable {

    @EJB
    TipoDeTratamientoFacade ejbFacade;
    List<TipoDeTratamiento> items;

    @PostConstruct
    protected void init() {
        items = getFacade().findAll();
    }

    /**
     * Creates a new instance of TipoDeTratamientoController
     */
    public TipoDeTratamientoController() {
    }

    private TipoDeTratamientoFacade getFacade() {
        return ejbFacade;
    }

    public List<TipoDeTratamiento> getItemsAvailableSelectOne() {
        return items;
    }

    public TipoDeTratamiento getTipoDeTratamiento(java.lang.Short id) {
        return getFacade().find(id);
    }

    public String getCodigoDePrestacion(TipoDeTratamiento tipoDeTratamiento,ObraSocial obraSocial){
        String returnVal = "";
        
        for(TipoDeTratamiento tt : items)
            if(tt.equals(tipoDeTratamiento))
                for(TipoTratamientoObraSocial ttos : tt.getTipoTratamientoObraSocialList())
                    if(ttos.getObraSocial().equals(obraSocial))
                        returnVal = ttos.getCodigoDePrestacion();
        
        return returnVal;
    }
    
    @FacesConverter(forClass = TipoDeTratamiento.class)
    public static class TratamientoControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            TipoDeTratamientoController controller = (TipoDeTratamientoController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "tipoDeTratamientoController");
            return controller.getTipoDeTratamiento(getKey(value));
        }

        java.lang.Short getKey(String value) {
            java.lang.Short key;
            key = Short.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Short value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof TipoDeTratamiento) {
                TipoDeTratamiento o = (TipoDeTratamiento) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), TipoDeTratamiento.class.getName()});
                return null;
            }
        }

    }
}
