package ar.edu.utn.frsf.kinesio.controllers;

import ar.edu.utn.frsf.kinesio.gestores.OrdenMedicaFacade;
import ar.edu.utn.frsf.kinesio.controllers.util.JsfUtil;
import ar.edu.utn.frsf.kinesio.entities.OrdenMedica;
import ar.edu.utn.frsf.kinesio.entities.Tratamiento;
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
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

@Named("ordenMedicaController")
@ViewScoped
public class OrdenMedicaController implements Serializable {

    @EJB
    private OrdenMedicaFacade ejbFacade;
    private List<OrdenMedica> itemsTratamiento = null;
    private OrdenMedica selected;
    private Tratamiento tratamiento;

    public OrdenMedicaController() {

    }

    @PostConstruct
    public void init() {
        tratamiento = (Tratamiento) JsfUtil.getObjectFromRequestParameter(
                "tratamiento",
                FacesContext.getCurrentInstance().getApplication().createConverter(Tratamiento.class),
                null);
        itemsTratamiento = getFacade().getOrdenesByTratamiento(tratamiento);
    }

    //Getters y Setters
    private OrdenMedicaFacade getFacade() {
        return ejbFacade;
    }

    public OrdenMedica getSelected() {
        return selected;
    }

    public void setSelected(OrdenMedica selected) {
        this.selected = selected;
    }

    public void setItemsTratamiento(List<OrdenMedica> itemsTratamiento) {
        this.itemsTratamiento = itemsTratamiento;
    }

    public Tratamiento getTratamiento() {
        if (tratamiento == null) {
            tratamiento = (Tratamiento) JsfUtil.getObjectFromRequestParameter(
                    "tratamiento",
                    FacesContext.getCurrentInstance().getApplication().createConverter(Tratamiento.class),
                    null);
        }
        return tratamiento;
    }

    public void setTratamiento(Tratamiento tratamiento) {
        this.tratamiento = tratamiento;
    }

    public List<OrdenMedica> getItemsTratamiento() {
        if (itemsTratamiento == null) {
            itemsTratamiento = getFacade().getOrdenesByTratamiento(this.getTratamiento());
        }
        return itemsTratamiento;
    }

    //Validator del campo "Cantidad de sesiones a autorizar" en el Create de 
    //una orden médica.
    public void validarSesionesDeLaOrden(FacesContext facesContext, UIComponent componente, Object valor) {
        Short cantidadDeSesiones = (Short) valor;
        if (!getFacade().esValidaCantidadDeSesionesDeOrdenes(this.getTratamiento(), cantidadDeSesiones)) {
            ((UIInput) componente).setValid(false);
            JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Bundle").getString("CreateOrdenMedica_CantidadDeSesionesValidacion"));
        }
    }

    //Métodos de persistencia
    public OrdenMedica prepareCreate() {
        selected = getFacade().initOrden(this.getTratamiento());
        return selected;
    }

    public void create() {
        persist(JsfUtil.PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("OrdenMedicaCreated"));
        if (!JsfUtil.isValidationFailed()) {
            itemsTratamiento = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        this.getFacade().autorizarOrden(selected);
        persist(JsfUtil.PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("OrdenMedicaUpdated"));
    }

    public void destroy() {
        persist(JsfUtil.PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("OrdenMedicaDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            itemsTratamiento = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public OrdenMedica getOrdenMedica(java.lang.Integer id) {
        return getFacade().find(id);
    }

    private void persist(JsfUtil.PersistAction persistAction, String successMessage) {
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
