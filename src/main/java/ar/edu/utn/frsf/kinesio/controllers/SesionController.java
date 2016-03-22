package ar.edu.utn.frsf.kinesio.controllers;

import ar.edu.utn.frsf.kinesio.controllers.util.CreacionSesionEvento;
import ar.edu.utn.frsf.kinesio.entities.Sesion;
import ar.edu.utn.frsf.kinesio.controllers.util.JsfUtil;
import ar.edu.utn.frsf.kinesio.controllers.util.JsfUtil.PersistAction;
import ar.edu.utn.frsf.kinesio.controllers.util.SesionCreada;
import ar.edu.utn.frsf.kinesio.controllers.util.SesionInicializada;
import ar.edu.utn.frsf.kinesio.controllers.util.VerSesionEvento;
import ar.edu.utn.frsf.kinesio.entities.Agenda;
import ar.edu.utn.frsf.kinesio.gestores.SesionFacade;

import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;

@Named("sesionController")
@ViewScoped
public class SesionController implements Serializable {

    @EJB
    private ar.edu.utn.frsf.kinesio.gestores.SesionFacade ejbFacade;
    private List<Sesion> items = null;
    private Sesion selected;
    
    @Inject
    @SesionCreada
    Event<CreacionSesionEvento> sesionCreadaEvento;

    public void prepareCreate(@Observes @SesionInicializada CreacionSesionEvento evento) {
        Agenda agenda = (Agenda) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("agenda");
        selected = getFacade().initSesion(evento.getDate(), agenda);
    }
    
    public void verSesion(@Observes VerSesionEvento evento) {
        selected = (Sesion) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sesion");        
    }

    public SesionController() {
    }

    public Sesion getSelected() {
        return selected;
    }

    public void setSelected(Sesion selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private SesionFacade getFacade() {
        return ejbFacade;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("SesionCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("sesion", selected);
        sesionCreadaEvento.fire(new CreacionSesionEvento());
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("SesionUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("SesionDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Sesion> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
    }

    private void persist(PersistAction persistAction, String successMessage) {
        if (selected != null) {
            setEmbeddableKeys();
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

    public Sesion getSesion(java.lang.Integer id) {
        return getFacade().find(id);
    }

    public List<Sesion> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Sesion> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = Sesion.class)
    public static class SesionControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            SesionController controller = (SesionController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "sesionController");
            return controller.getSesion(getKey(value));
        }

        java.lang.Integer getKey(String value) {
            java.lang.Integer key;
            key = Integer.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Integer value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Sesion) {
                Sesion o = (Sesion) object;
                return getStringKey(o.getIdSesion());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Sesion.class.getName()});
                return null;
            }
        }

    }

}