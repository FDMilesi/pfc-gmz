/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.edu.utn.frsf.kinesio.controllers;

import ar.edu.utn.frsf.kinesio.entities.ObraSocial;
import ar.edu.utn.frsf.kinesio.entities.TipoDeTratamiento;
import ar.edu.utn.frsf.kinesio.gestores.ObraSocialFacade;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author fer_0
 */
@Named(value = "obraSocialController")
@ApplicationScoped
public class ObraSocialController {

    @EJB
    private ObraSocialFacade ejbFacade;
    List<ObraSocial> items;

    /**
     * Creates a new instance of ObraSocialController
     */
    @PostConstruct
    private void init() {
        items = getFacade().findAll();
    }

    public ObraSocialController() {
    }

    private ObraSocialFacade getFacade() {
        return ejbFacade;
    }

    public List<ObraSocial> getItemsAvailableSelectOne() {
        return items;
    }

    public ObraSocial getObraSocial(java.lang.Short id) {
        return getFacade().find(id);
    }

    @FacesConverter(forClass = ObraSocial.class)
    public static class ObraSocialControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ObraSocialController controller = (ObraSocialController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "obraSocialController");
            return controller.getObraSocial(getKey(value));
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
            if (object instanceof ObraSocial) {
                ObraSocial o = (ObraSocial) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), ObraSocial.class.getName()});
                return null;
            }
        }

    }
}
