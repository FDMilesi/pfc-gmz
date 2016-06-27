package ar.edu.utn.frsf.kinesio.controllers;

import ar.edu.utn.frsf.kinesio.entities.Sesion;
import ar.edu.utn.frsf.kinesio.controllers.util.JsfUtil;
import ar.edu.utn.frsf.kinesio.controllers.util.JsfUtil.PersistAction;
import ar.edu.utn.frsf.kinesio.entities.Agenda;

import java.io.Serializable;
import java.util.ResourceBundle;
import javax.inject.Named;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.UISelectBoolean;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;

@Named("agendaSesionController")
@ViewScoped
public class SesionController extends AbstractSesionController implements Serializable {

    /* Eventos para comunicar controladores */
    @Inject
    Event<SesionCreadaEvento> sesionCreadaEvento;
    @Inject
    Event<SesionModificadaEvento> sesionModificadaEvento;
    @Inject
    Event<SesionEliminadaEvento> sesionEliminadaEvento;

    public SesionController() {
    }

    //Métodos de negocio
    /**
     * Validador usado en la vista de modificar sesión. Verifica que al tildarse
     * el checkbox de cuenta, la cantidad de sesiones que cuentan no supere la
     * cantidad de sesiones seteadas en el tratamiento.
     *
     * @param facesContext
     * @param componente
     * @param checkBoxValue
     */
    public void puedoSetearCuentaTrue(FacesContext facesContext, UIComponent componente, Object checkBoxValue) {
        boolean cuenta = (boolean) checkBoxValue;
        //Si ahora cuenta y antes no contaba
        if (cuenta && !getContaba()) {
            if (!getFacade().puedoAgregarSesion(selected.getTratamiento())) {
                ((UISelectBoolean) componente).setValid(false);
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("Bundle").getString("EditSesion_validacionTopeDeSesiones"));
            }
        }
    }

    public void puedoCrearNuevaSesion(FacesContext facesContext, UIComponent componente, Object value) {
        if (selected.getTratamiento() != null && !getFacade().puedoAgregarSesion(selected.getTratamiento())) {
            ((UIInput) componente).setValid(false);
            JsfUtil.addErrorMessage(ResourceBundle.getBundle("Bundle").getString("EditSesion_validacionTopeDeSesiones"));
        }
    }

    public void editSesion(@Observes AgendaController.VerSesionEvento evento) {
        selected = (Sesion) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sesion");
        this.antesDeEditar();
    }

    public void onMoveFromAgenda(@Observes AgendaController.ModificarSesionEvento evento) {
        selected = (Sesion) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sesionUpdate");
        selected.setFechaHoraInicio(evento.getDate());
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("SesionUpdated"));
    }

    public void prepareCreateFromAgenda(@Observes AgendaController.SesionInicializadaEvento evento) {
        Agenda agendaDeSesion = (Agenda) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("agenda");
        selected = getFacade().initSesionFromAgenda(evento.getDate(), agendaDeSesion);
    }

    //Métodos de persistencia
    public void createFromAgenda() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("SesionCreated"));
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("sesion", selected);
        sesionCreadaEvento.fire(new SesionCreadaEvento());
    }

    public void updateFromAgenda() {
        this.resetearCuenta();
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("SesionUpdated"));
        //ahora se llama a setFechaHoraInicio para que setee starDate a la sesion, de modo que la Agenda se actualice
        selected.setFechaHoraInicio(selected.getFechaHoraInicio());
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("sesion", selected);
        sesionModificadaEvento.fire(new SesionModificadaEvento());
    }

    public void destroyFromAgenda() {
        if (!selected.getTranscurrida()) {
            persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("SesionDeleted"));
            String sesionId = selected.getId();
            if (!JsfUtil.isValidationFailed()) {
                selected = null; // Remove selection
                sesionEliminadaEvento.fire(new SesionEliminadaEvento(sesionId));
            }
        } else {
            JsfUtil.addErrorMessage("No se puede eliminar una sesion ya transcurrida");
        }

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
