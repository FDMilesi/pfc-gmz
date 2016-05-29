package ar.edu.utn.frsf.kinesio.controllers;

import ar.edu.utn.frsf.kinesio.entities.Tratamiento;
import ar.edu.utn.frsf.kinesio.controllers.util.JsfUtil;
import ar.edu.utn.frsf.kinesio.controllers.util.JsfUtil.PersistAction;
import ar.edu.utn.frsf.kinesio.entities.Paciente;
import ar.edu.utn.frsf.kinesio.gestores.TratamientoFacade;

import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.inject.Named;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.view.ViewScoped;

@Named("tratamientoController")
@ViewScoped
public class TratamientoController implements Serializable {

    private static final String EDIT_TRATAMIENTOS_PATH = "/protected/tratamiento/Edit.xhtml?faces-redirect=true";

    @EJB
    private TratamientoFacade ejbFacade;
    private List<Tratamiento> items = null;
    private Tratamiento selected;
    private Paciente paciente;

    @PostConstruct
    protected void init() {
        paciente = (Paciente) JsfUtil.getObjectFromRequestParameter("paciente", new PacienteController.PacienteControllerConverter(), null);
        items = getFacade().getTratamientosByPaciente(paciente);
    }

    public TratamientoController() {
    }

    public String prepararEditTratamiento() {
        return EDIT_TRATAMIENTOS_PATH;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
        items = null;
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

    public Tratamiento prepareCreate() {
        selected = getFacade().initTratamiento(paciente);
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("TratamientoCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("TratamientoUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("TratamientoDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Tratamiento> getItems() {
        if (items == null) {
            items = getFacade().getTratamientosByPaciente(paciente);
        }
        return items;
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

    public List<Tratamiento> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Tratamiento> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

}
