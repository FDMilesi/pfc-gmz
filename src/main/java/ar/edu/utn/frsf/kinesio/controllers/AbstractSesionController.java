package ar.edu.utn.frsf.kinesio.controllers;

import ar.edu.utn.frsf.kinesio.entities.Sesion;
import ar.edu.utn.frsf.kinesio.controllers.util.JsfUtil;
import ar.edu.utn.frsf.kinesio.controllers.util.JsfUtil.PersistAction;
import ar.edu.utn.frsf.kinesio.gestores.SesionFacade;

import java.io.Serializable;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;

public abstract class AbstractSesionController implements Serializable {

    @EJB
    private SesionFacade ejbFacade;
    protected Sesion selected;
    private Date fechaVieja;
    private boolean contaba;

    public AbstractSesionController() {
    }

    //Getters y Setters
    public Sesion getSelected() {
        return selected;
    }

    public void setSelected(Sesion selected) {
        this.selected = selected;
    }

    protected SesionFacade getFacade() {
        return ejbFacade;
    }

    public Sesion getSesion(Integer id) {
        return getFacade().find(id);
    }

    public Date getFechaVieja() {
        return fechaVieja;
    }
    
    public boolean getContaba(){
        return contaba;
    }

    //Métodos de negocio
    //No se puede reprogramar una sesion a una fecha anterior a la actual.
    public void validarFecha(FacesContext facesContext, UIComponent componente, Object valor) {
        if (!valor.equals(selected.getFechaHoraInicio())) {
            Date fechaModificada = (Date) valor;
            if (fechaModificada.before(new Date())) {
                ((UIInput) componente).setValid(false);
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Bundle").getString("EditSesion_fechaReprogramadaValidacion"));
            }
        }
    }

    public void antesDeEditar(){
        fechaVieja = (Date) selected.getFechaHoraInicio().clone();
        contaba = selected.getCuenta();
    }
    
    protected void resetearCuenta() {
        if (!selected.getFechaHoraInicio().equals(this.getFechaVieja())) {
            selected.setCuenta(Boolean.TRUE);
        }
    }

    public void calcularNumeroSesion() {
        selected.setNumeroDeSesion(this.getFacade().getSiguienteNumeroDeSesion(selected.getTratamiento()));
    }

    protected void persist(PersistAction persistAction, String successMessage) {
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

}
