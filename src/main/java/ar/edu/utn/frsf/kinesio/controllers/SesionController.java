package ar.edu.utn.frsf.kinesio.controllers;

import ar.edu.utn.frsf.kinesio.controllers.converters.TratamientoConverter;
import ar.edu.utn.frsf.kinesio.entities.Sesion;
import ar.edu.utn.frsf.kinesio.controllers.util.JsfUtil;
import ar.edu.utn.frsf.kinesio.controllers.util.JsfUtil.PersistAction;
import ar.edu.utn.frsf.kinesio.entities.Agenda;
import ar.edu.utn.frsf.kinesio.entities.Tratamiento;
import ar.edu.utn.frsf.kinesio.gestores.SesionFacade;

import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.inject.Named;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;

@Named("sesionController")
@ViewScoped
public class SesionController implements Serializable {

    private static TratamientoConverter tratamientoConverter;

    @EJB
    private SesionFacade ejbFacade;
    private List<Sesion> items = null;
    private Sesion selected;
    private Tratamiento tratamientoEnEdicion;

    /* Eventos para comunicar controladores */
    @Inject
    Event<SesionCreadaEvento> sesionCreadaEvento;
    @Inject
    Event<SesionModificadaEvento> sesionModificadaEvento;
    @Inject
    Event<SesionEliminadaEvento> sesionEliminadaEvento;

    public SesionController() {
    }

    //Métodos static
    public static TratamientoConverter getTratamientoConverter() {
        if (tratamientoConverter == null) {
            tratamientoConverter = new TratamientoConverter();
        }
        return tratamientoConverter;
    }

    public static void setTratamientoConverter(TratamientoConverter tratamientoConverter) {
        SesionController.tratamientoConverter = tratamientoConverter;
    }

    //Getters y Setters
    public Sesion getSelected() {
        return selected;
    }

    public void setSelected(Sesion selected) {
        this.selected = selected;
    }

    private SesionFacade getFacade() {
        return ejbFacade;
    }

    public List<Sesion> getItems() {
        if (items == null) {
            items = getFacade().getSesionesByTratamiento(this.getTratamientoEnEdicion());
        }
        return items;
    }

    public Sesion getSesion(Integer id) {
        return getFacade().find(id);
    }

    private Tratamiento getTratamientoEnEdicion() {
        if (tratamientoEnEdicion == null) {
            tratamientoEnEdicion = (Tratamiento) JsfUtil.getObjectFromRequestParameter("tratamiento", getTratamientoConverter(), null);
        }
        return tratamientoEnEdicion;
    }

    //Métodos de negocio
    public void calcularNumeroSesion() {
        selected.setNumeroDeSesion(this.getFacade().getSiguienteNumeroDeSesion(selected.getTratamiento()));
    }

    public void verSesion(@Observes AgendaController.VerSesionEvento evento) {
        selected = (Sesion) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sesion");
    }
    
    public void onMoveFromAgenda(@Observes AgendaController.ModificarSesionEvento evento){
        selected = (Sesion) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sesion");
        this.update();
    }

    public void prepareCreateFromAgenda(@Observes AgendaController.SesionInicializadaEvento evento) {
        Agenda agendaDeSesion = (Agenda) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("agenda");
        selected = getFacade().initSesionFromAgenda(evento.getDate(), agendaDeSesion);
    }

    public Sesion prepareCreateFromTratamiento(Tratamiento tratamiento) {
        selected = getFacade().initSesionFromTratamiento(tratamiento);
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("SesionCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void createFromAgenda() {
        this.create();
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("sesion", selected);
        sesionCreadaEvento.fire(new SesionCreadaEvento());
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("SesionUpdated"));
    }
    
    public void updateFromAgenda(){
        this.update();
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("sesion", selected);
        sesionModificadaEvento.fire(new SesionModificadaEvento());
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("SesionDeleted"));
        String sesionId = selected.getId();
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
        sesionEliminadaEvento.fire(new SesionEliminadaEvento(sesionId));
    }

    private void persist(PersistAction persistAction, String successMessage) {
        if (selected != null) {
            try {
                if (persistAction != PersistAction.DELETE) {
                    selected = getFacade().editAndReturn(selected);
                } else {
                    getFacade().remove(selected);
                }
                JsfUtil.addSuccessMessage(successMessage);
            } catch (EJBException ex) {
                String msg = "";
                Throwable cause = ex.getCause();
                if (cause != null) {
                    msg = cause.getLocalizedMessage();
                }
                if (msg.length() > 0) {
                    JsfUtil.addErrorMessage(msg);
                } else {
                    JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
                }
            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            }
        }
    }

    //seters para testing
    public void setFacade(SesionFacade sesionFacade) {
        this.ejbFacade = sesionFacade;
    }

    //Eventos que lanza SesionController
    public class SesionCreadaEvento {

        public SesionCreadaEvento() {
        }
    }
    
    public class SesionModificadaEvento {

        public SesionModificadaEvento() {
        }
    }

    public class SesionEliminadaEvento {

        private String idSesionEliminada;

        public SesionEliminadaEvento() {

        }

        public SesionEliminadaEvento(String idSesionEliminada) {
            this.idSesionEliminada = idSesionEliminada;
        }

        public String getIdSesionEliminada() {
            return idSesionEliminada;
        }

        public void setIdSesionEliminada(String idSesionEliminada) {
            this.idSesionEliminada = idSesionEliminada;
        }
    }
}
