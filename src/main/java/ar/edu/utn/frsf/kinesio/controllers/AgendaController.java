package ar.edu.utn.frsf.kinesio.controllers;

import ar.edu.utn.frsf.kinesio.controllers.util.JsfUtil;
import ar.edu.utn.frsf.kinesio.entities.Agenda;
import ar.edu.utn.frsf.kinesio.entities.Sesion;
import ar.edu.utn.frsf.kinesio.gestores.AgendaFacade;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;

/**
 *
 */
@Named(value = "agendaController")
@ViewScoped
public class AgendaController implements Serializable {

    @EJB
    AgendaFacade ejbFacade;
    List<Agenda> items;
    Agenda selected;

    /* Eventos para comunicar controladores */
    @Inject
    Event<SesionInicializadaEvento> sesionInicializadaEvento;
    @Inject
    Event<VerSesionEvento> verSesionEvento;

    public AgendaController() {
    }

    //Getters y Setters
    private AgendaFacade getFacade() {
        return ejbFacade;
    }

    public List<Agenda> getItemsAvailableSelectOne() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
    }

    public Agenda getAgenda(Short id) {
        return getFacade().find(id);
    }

    public void setSelected(Agenda selected) {
        this.selected = selected;
    }

    public ScheduleModel getSelected() {
        if (selected == null) {
            selected = getFacade().getAgenda();
        }
        return selected;
    }

    //Métodos de negocio
    public void mostrarSesion(SelectEvent selectEvent) {
        //En este caso el scheduleEvent que devuelve la vista no contiene toda la info necesaria. Por eso es necesario 
        //buscarlo nuevamente en la lista de sesiones con el getEvent()
        String scheduleEventId = ((ScheduleEvent) selectEvent.getObject()).getId();
        Sesion sesion = (Sesion) getSelected().getEvent(scheduleEventId);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("sesion", sesion);
        verSesionEvento.fire(new VerSesionEvento());
    }

    public void prepareCreateSesion(SelectEvent selectEvent) {
        Date date = (Date) selectEvent.getObject();
        if (date.before(new Date())) {
            JsfUtil.addWarningMessage(ResourceBundle.getBundle("Bundle").getString("WarningSesionEnFechaAnterior"));
        }
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("agenda", selected);
        sesionInicializadaEvento.fire(new SesionInicializadaEvento(date));
    }

    public void agregarSesion(@Observes SesionController.SesionCreadaEvento evento) {
        Sesion sesion = (Sesion) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sesion");
        getSelected().addEvent(sesion);
    }

    public void eliminarSesion(@Observes SesionController.SesionEliminadaEvento evento) {
        getSelected().deleteEvent(getSelected().getEvent(evento.getIdSesionEliminada()));
    }

    //Eventos que lanza AgendaController
    public class SesionInicializadaEvento {

        private Date date;

        public SesionInicializadaEvento() {
        }

        public SesionInicializadaEvento(Date date) {
            this.date = date;
        }

        public Date getDate() {
            return date;
        }

        public void setDate(Date date) {
            this.date = date;
        }
    }

    public class VerSesionEvento {

        public VerSesionEvento() {

        }
    }

}
