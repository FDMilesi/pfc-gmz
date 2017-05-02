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
    /**
     * Atributos que sirven a los sesionControllers para saber si durante la
     * edición de una sesión se modificó la fecha de la misma. En caso de ser
     * así, debe resetearse el cambo 'cuenta' de la sesión si es que 'contaba'
     */
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

    public boolean getContaba() {
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

    /**
     * Antes de mostrar el dialog de edición de una sesión, guardo la fecha que
     * tenía y guarso el valor del boolean 'cuenta'. Esto me sirve para luego
     * decidir si resetear o no el boolean 'cuenta.
     */
    public void antesDeEditar() {
        fechaVieja = (Date) selected.getFechaHoraInicio().clone();
        contaba = selected.getCuenta();
    }

    /**
     * Resetea a TRUE el valor de 'cuenta' si la fecha de la sesión fué
     * modificada por el usuario
     */
    protected void resetearCuenta() {
        if (!selected.getFechaHoraInicio().equals(this.getFechaVieja())) {
            selected.setCuenta(Boolean.TRUE);
        }
    }

    public void calcularNumeroSesion() {
        selected.setNumeroDeSesion(this.getFacade().getNumeroDeSesion(selected.getTratamiento(), selected));
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
                if (cause != null && cause.getMessage() != null) {
                    msg = cause.getMessage();
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
