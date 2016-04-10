package ar.edu.utn.frsf.kinesio.controllers;

import ar.edu.utn.frsf.kinesio.entities.Tratamiento;
import ar.edu.utn.frsf.kinesio.controllers.util.JsfUtil;
import ar.edu.utn.frsf.kinesio.controllers.util.JsfUtil.PersistAction;
import ar.edu.utn.frsf.kinesio.gestores.TratamientoFacade;

import java.io.Serializable;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.inject.Named;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;

@Named("editTratamientoController")
@ViewScoped
public class EditTratamientoController implements Serializable {

    @EJB
    private TratamientoFacade ejbFacade;
    private Tratamiento selected;

    @PostConstruct
    protected void init() {
        selected = (Tratamiento) FacesContext.getCurrentInstance()
                .getExternalContext()
                .getSessionMap()
                .get("tratamientoEdicion");
    }

    public EditTratamientoController() {
    }

    public void validarCantidadDeSesiones(FacesContext facesContext,
            UIComponent componente,
            Object valor) {
        Short cantidadDeSesiones = (Short) valor;
        if (!getFacade().esValidaCantidadDeSesiones(selected, cantidadDeSesiones)) {
            ((UIInput) componente).setValid(false);
            JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Bundle").getString("CreateTratamientoRangoCantidadDeSesiones"));
        }
    }

    public Tratamiento getSelected() {
        return selected;
    }

    public void setSelected(Tratamiento selected) {
        this.selected = selected;
    }

    private TratamientoFacade getFacade() {
        return ejbFacade;
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("TratamientoUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("TratamientoDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
        }
    }

    private void persist(PersistAction persistAction, String successMessage) {
        if (selected != null) {
            try {
                if (persistAction != PersistAction.DELETE) {
                    getFacade().edit(selected);
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

    public Tratamiento getTratamiento(java.lang.Integer id) {
        return getFacade().find(id);
    }

}
