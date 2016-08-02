package ar.edu.utn.frsf.kinesio.controllers;

import ar.edu.utn.frsf.kinesio.entities.Paciente;
import ar.edu.utn.frsf.kinesio.controllers.util.JsfUtil;
import ar.edu.utn.frsf.kinesio.controllers.util.JsfUtil.PersistAction;
import ar.edu.utn.frsf.kinesio.gestores.PacienteFacade;
import java.io.IOException;

import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

@Named("pacienteController")
@ViewScoped
public class PacienteController implements Serializable {

    private static final String TRATAMIENTOS_PATH = "/protected/tratamiento/List.xhtml?faces-redirect=true";
    @EJB
    private ar.edu.utn.frsf.kinesio.gestores.PacienteFacade ejbFacade;
    private List<Paciente> items = null;
    private Paciente selected;
    private List<Paciente> pacientesFiltrados;

    public PacienteController() {
    }

    //Getters y Setters
    public List<Paciente> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
    }

    public Paciente getSelected() {
        return selected;
    }

    public void setSelected(Paciente selected) {
        this.selected = selected;
    }

    public List<Paciente> getPacientesFiltrados() {
        return pacientesFiltrados;
    }

    public void setPacientesFiltrados(List<Paciente> pacientesFiltrados) {
        this.pacientesFiltrados = pacientesFiltrados;
    }

    private PacienteFacade getFacade() {
        return ejbFacade;
    }

    public Paciente getPaciente(java.lang.Integer id) {
        return getFacade().find(id);
    }

    public List<Paciente> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    //Métodos de negocio
    public String mostrarTratamientos() {
        return TRATAMIENTOS_PATH;
    }
    
    public void navegarAListaTratamientos() throws IOException{
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        context.redirect(context.getRequestContextPath()+"/faces"+TRATAMIENTOS_PATH+"&paciente="+selected.getId());
    }
    
    public List<Paciente> autocompletar(String query){
        return this.getFacade().getPacientesAutocompletar(query.toLowerCase());
    }

    public Paciente prepareCreate() {
        selected = getFacade().initPaciente();
        return selected;
    }

    public String create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("PacienteCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
        return TRATAMIENTOS_PATH + "&paciente=" + selected.getId().toString();
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("PacienteUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("PacienteDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void setEjbFacade(PacienteFacade ejbFacade) {
        this.ejbFacade = ejbFacade;
    }

    private void persist(PersistAction persistAction, String successMessage) {
        if (selected != null) {
            try {
                switch (persistAction) {
                    case CREATE:
                        getFacade().create(selected);
                        break;
                    case UPDATE:
                        getFacade().edit(selected);
                        break;
                    case DELETE:
                        getFacade().remove(selected);
                        break;
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
}
