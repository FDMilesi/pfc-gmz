package ar.edu.utn.frsf.kinesio.controllers;

import ar.edu.utn.frsf.kinesio.controllers.util.JsfUtil;
import ar.edu.utn.frsf.kinesio.entities.Agenda;
import ar.edu.utn.frsf.kinesio.entities.Sesion;
import ar.edu.utn.frsf.kinesio.gestores.AgendaFacade;
import ar.edu.utn.frsf.kinesio.gestores.SesionFacade;
import javax.inject.Named;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import org.primefaces.event.ScheduleEntryMoveEvent;
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
    @EJB
    SesionFacade sesionFacade;

    List<Agenda> items;
    Agenda selected;

    /* Eventos para comunicar controladores */
    @Inject
    Event<SesionInicializadaEvento> sesionInicializadaEvento;
    @Inject
    Event<VerSesionEvento> verSesionEvento;
    @Inject
    Event<ModificarSesionEvento> modificarSesionEvento;

    private String horaActual;
    private String selectedTime;

    @PostConstruct
    public void init() {
        int horaActualInt = LocalDateTime.now().getHour();
        horaActual = horaActualInt + ":00:00";
    }

    public AgendaController() {
    }

    //Getters y Setters
    private AgendaFacade getFacade() {
        return ejbFacade;
    }

    protected void setSesionFacade(SesionFacade sesionFacade) {
        this.sesionFacade = sesionFacade;
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
    public String getHoraActual() {
        return this.horaActual;
    }

    public String getSelectedTime() {
        return selectedTime;
    }

    public void setSelectedTime(String selectedTime) {
        this.selectedTime = selectedTime;
    }
    
    public void mostrarSesion(SelectEvent selectEvent) {
        //En este caso el scheduleEvent que devuelve la vista no contiene toda la info necesaria. Por eso es necesario 
        //buscarlo nuevamente en la lista de sesiones con el getEvent()
        String scheduleEventId = ((ScheduleEvent) selectEvent.getObject()).getId();
        Sesion sesion = (Sesion) getSelected().getEvent(scheduleEventId);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("sesion", sesion);
        verSesionEvento.fire(new VerSesionEvento());
    }

    public void moverSesion(ScheduleEntryMoveEvent event) {
        String scheduleEventId = event.getScheduleEvent().getId();
        Sesion sesion = (Sesion) getSelected().getEvent(scheduleEventId);

        int resultadoValidacion = sesionFacade.validarFechaEdicionSesion(sesion, event.getScheduleEvent().getStartDate());

        switch (resultadoValidacion) {
            case SesionFacade.ERROR_EDIT_SESION_FECHA_ANTERIOR:
                //deshago los cambios del move y aviso al usuario
                //vuelvo a setear fechaYHoraInicio, generando que tambien se resetee startDate
                sesion.setFechaHoraInicio(sesion.getFechaHoraInicio());
                sesion.setEndDate(null);//fuerzo a recalcular endDate
                getSelected().updateEvent(sesion);
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("Bundle").getString("ErrorMoverSesionFechaAnterior"));
                break;
            case SesionFacade.ERROR_FECHA_SESION_FERIADO:
                //deshago los cambios del move y aviso al usuario
                sesion.setFechaHoraInicio(sesion.getFechaHoraInicio());
                sesion.setEndDate(null);//fuerzo a recalcular endDate
                getSelected().updateEvent(sesion);
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("Bundle").getString("ErrorCreacionSesionDiaFeriado"));
                break;
            case 0:
                //si todo esta OK, le comunico al sesionController para q guarde los cambios de fecha
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("sesionUpdate", sesion);
                modificarSesionEvento.fire(new ModificarSesionEvento(event.getScheduleEvent().getStartDate()));
                break;
        }
    }

    public void prepareCreateSesion(SelectEvent selectEvent) {
        Date date = (Date) selectEvent.getObject();
        if (date.before(new Date())) {
            JsfUtil.addWarningMessage(ResourceBundle.getBundle("Bundle").getString("WarningCreacionSesionEnFechaAnterior"));
        }
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("agenda", selected);
        sesionInicializadaEvento.fire(new SesionInicializadaEvento(date));
    }
    
    public void prepareCreateSesionAutomatedTest() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        if(selectedTime!=null){
            try {
                Date selectedTimeDate = sdf.parse(selectedTime);
                if (selectedTimeDate.before(new Date())) {
                    JsfUtil.addWarningMessage(ResourceBundle.getBundle("Bundle").getString("WarningSesionEnFechaAnterior"));
                }
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("agenda", selected);
                sesionInicializadaEvento.fire(new SesionInicializadaEvento(selectedTimeDate));
            } catch (ParseException ex) {
                Logger.getLogger(AgendaController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void agregarSesion(@Observes AgendaSesionController.SesionCreadaEvento evento) {
        Sesion sesion = (Sesion) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sesion");
        getSelected().addEvent(sesion);
    }

    public void modificarSesion(@Observes AgendaSesionController.SesionModificadaEvento evento) {
        Sesion sesion = (Sesion) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sesion");
        getSelected().updateEvent(sesion);
    }

    public void eliminarSesion(@Observes AgendaSesionController.SesionEliminadaEvento evento) {
        getSelected().deleteEvent(getSelected().getEvent(evento.getIdSesionEliminada()));
    }

    public void agregarSesiones(List<Sesion> sesionesAAgregar) {
        for (Sesion sesion : sesionesAAgregar) {
            this.getSelected().addEvent(sesion);
        }
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

    public class ModificarSesionEvento {

        private Date date;

        public ModificarSesionEvento() {
        }

        public ModificarSesionEvento(Date date) {
            this.date = date;
        }

        public Date getDate() {
            return date;
        }

        public void setDate(Date date) {
            this.date = date;
        }

    }

}
