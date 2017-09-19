package ar.edu.utn.frsf.kinesio.controllers;

import ar.edu.utn.frsf.kinesio.entities.Tratamiento;
import ar.edu.utn.frsf.kinesio.entities.Paciente;
import ar.edu.utn.frsf.kinesio.controllers.util.JsfUtil;
import ar.edu.utn.frsf.kinesio.controllers.util.JsfUtil.PersistAction;
import ar.edu.utn.frsf.kinesio.entities.Estudio;
import ar.edu.utn.frsf.kinesio.gestores.TratamientoFacade;
import ar.edu.utn.frsf.kinesio.gestores.util.Configuracion;
import java.io.File;
import java.io.IOException;

import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.context.ExternalContext;
import javax.inject.Named;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;

@Named("tratamientoController")
@ViewScoped
public class TratamientoController implements Serializable {

    private static final String EDIT_TRATAMIENTOS_PATH
            = "/protected/editTratamiento/EditTratamiento.xhtml?faces-redirect=true";
    private static final String ADJUNTAR_ESTUDIOS_PATH
            = "/protected/tratamiento/AdjuntarEstudios.xhtml?faces-redirect=true";

    @EJB
    private TratamientoFacade ejbFacade;
    private List<Tratamiento> items = null;
    private Tratamiento selected;
    private Paciente paciente;

    @PostConstruct
    protected void init() {
        paciente = (Paciente) JsfUtil.getObjectFromRequestParameter(
                "paciente",
                FacesContext.getCurrentInstance().getApplication().createConverter(Paciente.class),
                null);
    }

    public TratamientoController() {
    }

    //Getters and Setters
    public Tratamiento getTratamiento(java.lang.Integer id) {
        return getFacade().find(id);
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

    //Métodos de negocio    
    public boolean esObraSocialIapos() {
        return this.getFacade().esOSIapos(paciente);
    }

    public List<Tratamiento> getItems() {
        if (items == null) {
            items = getFacade().getTratamientosByPacienteContandoSesiones(paciente);
        }
        return items;
    }

    public List<Tratamiento> getTratamientosByPacienteEnCurso() {
        return getFacade().getTratamientosByPacienteEnCurso(paciente);
    }

    public String prepararEditTratamiento() {
        return EDIT_TRATAMIENTOS_PATH;
    }
    
    public void navegarAEditTratamiento() throws IOException {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        context.redirect(context.getRequestContextPath() + "/faces" + EDIT_TRATAMIENTOS_PATH + "&tratamiento=" + selected.getId());
    }

    public Tratamiento prepareCreate() {
        selected = getFacade().initTratamiento(paciente);
        return selected;
    }

    public void prepararConsentimiento(Tratamiento tratamiento) {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("tratamientoParaConsentimiento", tratamiento);
    }

    public String redirectToConsentimiento() {
        return "/protected/tratamiento/ConsentimientoInformado.xhtml?faces-redirect-true";
    }
    
    public void prepararAdjuntarTratamientos(Tratamiento tratamiento) {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("tratamientoParaAdjuntarEstudios", tratamiento);
    }

    public String redirectToAdjuntarTratamientos(){
        return ADJUNTAR_ESTUDIOS_PATH;
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
    
    private void deleteEstudiosFromFileSystem(List<Estudio> estudioList){
        
        String baseDirectory = Configuracion.getInstance().getEstudiosProperties().getProperty("deltagestion.estudios.path")
                    + Configuracion.getInstance().getEstudiosProperties().getProperty("deltagestion.estudios.folder");
        
        for(Estudio e : estudioList){
            try {
                Files.delete(Paths.get(baseDirectory + File.separatorChar + e.getNombrearchivo()));
            } catch (IOException ex) {
                Logger.getLogger(TratamientoController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void destroy() {
        if (this.getFacade().puedoEliminarTratamiento(selected)) {
            persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("TratamientoDeleted"));
            deleteEstudiosFromFileSystem(selected.getEstudioList());
            selected = null;
            items = null;
        } else {
            JsfUtil.addWarningMessage(ResourceBundle.getBundle("/Bundle").getString("TratamientoNoSePuedeEliminar"));
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
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));

            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            }
        }
    }

    //seters para testing
    public void setFacade(TratamientoFacade tratamientoFacade) {
        this.ejbFacade = tratamientoFacade;
    }
}
