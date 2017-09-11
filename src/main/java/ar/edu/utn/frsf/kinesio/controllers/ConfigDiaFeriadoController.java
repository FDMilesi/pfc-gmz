package ar.edu.utn.frsf.kinesio.controllers;

import ar.edu.utn.frsf.kinesio.controllers.util.JsfUtil.PersistAction;
import ar.edu.utn.frsf.kinesio.entities.DiaFeriado;
import ar.edu.utn.frsf.kinesio.gestores.DiaFeriadoFacade;
import ar.edu.utn.frsf.kinesio.controllers.util.JsfUtil;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

@Named("configDiaFeriadoController")
@ViewScoped
public class ConfigDiaFeriadoController implements Serializable {

    @EJB
    private DiaFeriadoFacade diaFeriadoFacade;

    List<DiaFeriado> listaFeriados;
    DiaFeriado currentDiaFeriado;

    public ConfigDiaFeriadoController() {
    }

    public void validarCreacionNuevoFeriado(FacesContext facesContext, UIComponent componente, Object value) {
        if (getFacade().validarCreacionFeriado((Date) value) == DiaFeriadoFacade.ERROR_EL_DIA_POSEE_SESIONES) {
            ((UIInput) componente).setValid(false);
            JsfUtil.addErrorMessage(ResourceBundle.getBundle("Bundle").getString("FeriadoCreateErrorExistenSesiones"));
        }
    }

    public List<DiaFeriado> getListaFeriados() {
        if (listaFeriados == null) {
            listaFeriados = getFacade().findAll();
        }
        return listaFeriados;
    }

    public void setListaFeriados(List<DiaFeriado> listaFeriados) {
        this.listaFeriados = listaFeriados;
    }

    public DiaFeriado getCurrentDiaFeriado() {
        return currentDiaFeriado;
    }

    public void setCurrentDiaFeriado(DiaFeriado currentDiaFeriado) {
        this.currentDiaFeriado = currentDiaFeriado;
    }

    private DiaFeriadoFacade getFacade() {
        return diaFeriadoFacade;
    }

    public DiaFeriado prepareCreate() {
        currentDiaFeriado = new DiaFeriado();
        return currentDiaFeriado;
    }

    public void create() {
        persist(PersistAction.CREATE, "Dia feriado cargado con éxito");
        if (!JsfUtil.isValidationFailed()) {
            listaFeriados = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void destroy() {
        persist(PersistAction.DELETE, "Dia feriado eliminado con éxito");
        currentDiaFeriado = null;
        listaFeriados = null;
    }

    private void persist(PersistAction persistAction, String successMessage) {
        if (currentDiaFeriado != null) {
            try {
                if (persistAction == PersistAction.CREATE) {
                    getFacade().create(currentDiaFeriado);
                } else if (persistAction == PersistAction.DELETE) {
                    getFacade().remove(currentDiaFeriado);
                }
                JsfUtil.addSuccessMessage(successMessage);

            } catch (EJBException ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));

            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            }
        } else {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Error es nuuuuul");
        }
    }

}
