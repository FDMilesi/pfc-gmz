package ar.edu.utn.frsf.kinesio.controllers;

import ar.edu.utn.frsf.kinesio.entities.Tratamiento;
import ar.edu.utn.frsf.kinesio.controllers.util.JsfUtil;
import ar.edu.utn.frsf.kinesio.controllers.util.JsfUtil.PersistAction;
import ar.edu.utn.frsf.kinesio.gestores.TratamientoFacade;

import java.io.Serializable;
import java.util.List;
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
    private List<Tratamiento> tratamientosConSesionesAFavor;
    
    @PostConstruct
    protected void init() {
        selected = (Tratamiento) JsfUtil.getObjectFromRequestParameter(
                "tratamiento",
                FacesContext.getCurrentInstance().getApplication().createConverter(Tratamiento.class),
                null);
        
        if (selected != null) {
            //Cargo los tratamientos que tienen sesiones a favor
            tratamientosConSesionesAFavor = getFacade().getTratamientosConSesionesAFavor(selected);

            //En caso que el paciente no tenga seteada la obra social y esté editando
            //un tratamiento no particular, emito un mensaje acerca de la creación de ordenes medicas.
            if (selected.getPaciente().getObraSocial() == null && !selected.getParticular()) {
                JsfUtil.addWarningMessage(ResourceBundle.getBundle("Bundle").getString("EditTratamiento_obraSocialNoSeteada"));
            }
        }
    }

    public EditTratamientoController() {
    }

    //Getters y Setters
    public Tratamiento getSelected() {
        return selected;
    }

    public void setSelected(Tratamiento selected) {
        this.selected = selected;
    }

    public List<Tratamiento> getTratamientosConSesionesAFavor() {
        return tratamientosConSesionesAFavor;
    }

    public void setTratamientosConSesionesAFavor(List<Tratamiento> tratamientosConSesionesAFavor) {
        this.tratamientosConSesionesAFavor = tratamientosConSesionesAFavor;
    }

    private TratamientoFacade getFacade() {
        return ejbFacade;
    }

    public void setEjbFacade(TratamientoFacade ejbFacade) {
        this.ejbFacade = ejbFacade;
    }

    public Tratamiento getTratamiento(java.lang.Integer id) {
        return getFacade().find(id);
    }
    
    //Métodos de negocio
    //Validator del campo Cantidad de sesiones de un Tratamiento.
    public void validarCantidadDeSesiones(FacesContext facesContext, UIComponent componente, Object valor) {
        if (valor != null) {
            Short cantidadDeSesiones = (Short) valor;
            if (!getFacade().esValidaCantidadDeSesiones(selected, cantidadDeSesiones)) {
                ((UIInput) componente).setValid(false);
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Bundle").getString("EditTratamiento_CantidadDeSesionesValidacion"));
            }
        }
    }

    public void validarFinalizacionTratamiento(FacesContext facesContext, UIComponent componente, Object valor) {
        if ((Boolean) valor) {
            if (!getFacade().sonValidasTodasLasOrdenes(selected)) {
                ((UIInput) componente).setValid(false);
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Bundle").getString("EditTratamiento_EstanTodasLasOrdenesAutorizadasValidacion"));
            }
            if (!getFacade().esValidaCantidadSesionesCubiertasAlFinalizar(selected)) {
                ((UIInput) componente).setValid(false);
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Bundle").getString("EditTratamiento_CantidadDeSesionesCubiertasValidacion"));
            }
            if (!getFacade().esValidaCantidadDeSesionesNoTranscurridas(selected)) {
                ((UIInput) componente).setValid(false);
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Bundle").getString("EditTratamiento_CantidadDeSesionesNoTranscurridasValidacion"));
            }
        }
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
    
    //seters para testing
    public void setFacade(TratamientoFacade tratamientoFacade) {
        this.ejbFacade = tratamientoFacade;
    }
}
