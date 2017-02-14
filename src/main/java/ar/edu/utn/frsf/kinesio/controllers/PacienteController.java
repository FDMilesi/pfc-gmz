package ar.edu.utn.frsf.kinesio.controllers;

import ar.edu.utn.frsf.kinesio.entities.Paciente;
import ar.edu.utn.frsf.kinesio.controllers.util.JsfUtil;
import ar.edu.utn.frsf.kinesio.controllers.util.JsfUtil.PersistAction;
import ar.edu.utn.frsf.kinesio.entities.DatosDeContacto;
import ar.edu.utn.frsf.kinesio.gestores.ABMPacienteFacade;
import ar.edu.utn.frsf.kinesio.gestores.GoogleContactsFacade;
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
    private PacienteFacade ejbFacade;
    @EJB
    private ABMPacienteFacade abmPacienteFacade;

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

    private ABMPacienteFacade getABMPacienteFacade() {
        return abmPacienteFacade;
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

    public void navegarAListaTratamientos() throws IOException {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        context.redirect(context.getRequestContextPath() + "/faces" + TRATAMIENTOS_PATH + "&paciente=" + selected.getId());
    }

    public List<Paciente> autocompletar(String query) {
        return this.getFacade().getPacientesAutocompletar(query.toLowerCase());
    }

    public Paciente prepareCreate() {
        selected = getFacade().initPaciente();
        return selected;
    }

    public String createAndRedirect() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("PacienteCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
        return TRATAMIENTOS_PATH + "&paciente=" + selected.getId().toString();
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("PacienteCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("PacienteUpdated"));
    }

    public void destroy() {
        if (this.getFacade().puedoEliminarPaciente(selected)) {
            persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("PacienteDeleted"));
            selected = null;
            items = null;
        } else {
            JsfUtil.addWarningMessage(ResourceBundle.getBundle("/Bundle").getString("PacienteNoSePuedeEliminar"));
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
                        try {
                            String[] datosGoogleContact = getABMPacienteFacade().createGoogleContact(selected);
                            //0 -> nombre contacto, 1 -> id de google
                            getABMPacienteFacade().create(selected, datosGoogleContact[0], datosGoogleContact[1]);

                        } catch (GoogleContactsFacade.CredencialesNoEncontradasException ex) {
                            getFacade().create(selected);
                            Logger.getLogger(this.getClass().getName()).log(Level.WARNING, null, ex);
                            JsfUtil.addWarningMessage(ex.getMessage());
                        }
                        break;

                    case UPDATE:
                        DatosDeContacto datosDeContacto = getABMPacienteFacade().getDatosDeContacto(selected);

                        try {
                            if (datosDeContacto != null) {
                                String nuevoNombre = getABMPacienteFacade().editGoogleContact(selected, datosDeContacto.getIdgooglecontacts());
                                getABMPacienteFacade().edit(selected, nuevoNombre);
                            } else {
                                String[] datosGoogleContacto = getABMPacienteFacade().createGoogleContact(selected);
                                getABMPacienteFacade().edit(selected, datosGoogleContacto[0], datosGoogleContacto[1]);
                            }

                        } catch (GoogleContactsFacade.CredencialesNoEncontradasException ex) {
                            getFacade().edit(selected);
                            Logger.getLogger(this.getClass().getName()).log(Level.WARNING, null, ex);
                            JsfUtil.addWarningMessage(ex.getMessage());
                        }

                        break;

                    case DELETE:
                        String idGoogleContacts = getABMPacienteFacade().remove(selected);

                        try {
                            getABMPacienteFacade().removeGoogleContact(idGoogleContacts);

                        } catch (GoogleContactsFacade.CredencialesNoEncontradasException ex) {
                            Logger.getLogger(this.getClass().getName()).log(Level.WARNING, null, ex);
                            JsfUtil.addWarningMessage(ex.getMessage());
                        }
                        break;
                }
                JsfUtil.addSuccessMessage(successMessage);

            } catch (EJBException ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));

            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            }
        }
    }
}
