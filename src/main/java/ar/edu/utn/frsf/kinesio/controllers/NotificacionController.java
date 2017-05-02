package ar.edu.utn.frsf.kinesio.controllers;

import ar.edu.utn.frsf.kinesio.controllers.util.JsfUtil;
import ar.edu.utn.frsf.kinesio.controllers.util.JsfUtil.PersistAction;
import ar.edu.utn.frsf.kinesio.entities.Notificacion;
import ar.edu.utn.frsf.kinesio.gestores.NotificacionFacade;
import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

@Named(value = "notificacionController")
@SessionScoped
public class NotificacionController implements Serializable {

    @EJB
    private NotificacionFacade ejbFacade;

    List<Notificacion> items;
    private Notificacion selected;

    public NotificacionController() {
    }
    
    public void pruebaJob(){
        this.getFacade().NotificarEntregaPlanillaAlCirculo();
        this.getFacade().NotificarEntregaPlanillaIaposAlCirculo();
        this.getFacade().NotificarSiPacienteDebeOrden();
    }

    @PostConstruct
    private void init() {
        items = getFacade().findAll();       
    }

    public List<Notificacion> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
    }
    
    public int getCantidadDeNotificaciones() {
        return this.getItems().size();
    }

    private NotificacionFacade getFacade() {
        return ejbFacade;
    }

    public void setSelected(Notificacion selected) {
        this.selected = selected;
    }

    public Notificacion getSelected() {
        return selected;
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("NotificacionDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
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
