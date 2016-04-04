/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.edu.utn.frsf.kinesio.controllers;

import ar.edu.utn.frsf.kinesio.controllers.util.CreacionSesionEvento;
import ar.edu.utn.frsf.kinesio.controllers.util.EliminarSesionEvento;
import ar.edu.utn.frsf.kinesio.controllers.util.SesionCreada;
import ar.edu.utn.frsf.kinesio.controllers.util.SesionInicializada;
import ar.edu.utn.frsf.kinesio.controllers.util.VerSesionEvento;
import ar.edu.utn.frsf.kinesio.entities.Agenda;
import ar.edu.utn.frsf.kinesio.entities.Sesion;
import ar.edu.utn.frsf.kinesio.gestores.AgendaFacade;
import ar.edu.utn.frsf.kinesio.gestores.SesionFacade;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;

/**
 *
 * @author fer_0
 */
@Named(value = "agendaController")
@SessionScoped
public class AgendaController implements Serializable {

    @EJB
    AgendaFacade ejbFacade;
    @EJB
    SesionFacade sesionFacade;
    
    List<Agenda> items;
    
    Agenda agenda;

    /* Eventos para comunicar controladores */
    @Inject
    @SesionInicializada
    Event<CreacionSesionEvento> sesionInicializadaEvento;
    @Inject
    Event<VerSesionEvento> verSesionEvento;

    @PostConstruct
    protected void init() {
        agenda = getFacade().find(new Short("1"));
        agenda.setSesiones(sesionFacade.getSesionesByAgenda(agenda));
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

    public ScheduleModel getAgenda() {
        return agenda;
    }

    public void setAgenda(Agenda agenda) {
        this.agenda = agenda;
    }

    public void mostrarSesion(SelectEvent selectEvent) {
        Sesion sesion = (Sesion) agenda.getEvent(((ScheduleEvent) selectEvent.getObject()).getId());
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("sesion", sesion);
        verSesionEvento.fire(new VerSesionEvento());
    }

    public void prepareCreateSesion(SelectEvent selectEvent) {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("agenda", agenda);
        sesionInicializadaEvento.fire(new CreacionSesionEvento((Date) selectEvent.getObject()));
    }

    public void agregarSesion(@Observes @SesionCreada CreacionSesionEvento evento) {
        Sesion sesion = (Sesion) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sesion");
        agenda.addEvent(sesion);
    }

    public void eliminarSesion(@Observes EliminarSesionEvento evento) {
        agenda.deleteEvent(agenda.getEvent(evento.getIdSesionEliminada()));
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
