/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.edu.utn.frsf.kinesio.controllers;

import ar.edu.utn.frsf.kinesio.entities.Agenda;
import ar.edu.utn.frsf.kinesio.gestores.AgendaFacade;
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
@Named(value = "agendaController")
@ApplicationScoped
public class AgendaController implements Serializable {

    @EJB
    AgendaFacade ejbFacade;
    List<Agenda> items;

    @PostConstruct
    protected void init() {
        items = getFacade().findAll();
    }

    /**
     * Creates a new instance of AgendaController
     */
    public AgendaController() {
    }

    private AgendaFacade getFacade() {
        return ejbFacade;
    }

    public List<Agenda> getItemsAvailableSelectOne() {
        return items;
    }

    public Agenda getAgenda(java.lang.Short id) {
        return getFacade().find(id);
    }

    @FacesConverter(forClass = Agenda.class)
    public static class AgendaConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            AgendaController controller = (AgendaController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "agendaController");
            return controller.getAgenda(getKey(value));
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
            if (object instanceof Agenda) {
                Agenda o = (Agenda) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Agenda.class.getName()});
                return null;
            }
        }

    }
}
