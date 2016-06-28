package ar.edu.utn.frsf.kinesio.controllers;

import ar.edu.utn.frsf.kinesio.entities.Sesion;
import ar.edu.utn.frsf.kinesio.controllers.util.JsfUtil;
import ar.edu.utn.frsf.kinesio.controllers.util.JsfUtil.PersistAction;
import ar.edu.utn.frsf.kinesio.entities.Tratamiento;

import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.UISelectBoolean;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;

@Named("listSesionController")
@ViewScoped
public class ListSesionController extends AbstractSesionController implements Serializable {

    private List<Sesion> items = null;
    private Tratamiento tratamientoEnEdicion;

    @PostConstruct
    public void init() {
        tratamientoEnEdicion = 
                (Tratamiento) JsfUtil.getObjectFromRequestParameter(
                    "tratamiento",
                    FacesContext.getCurrentInstance().getApplication().createConverter(Tratamiento.class),
                    null);
        if (tratamientoEnEdicion != null)
            items = getFacade().getSesionesByTratamiento(tratamientoEnEdicion);
    }

    public ListSesionController() {
    }

    //Getters y Setters
    public List<Sesion> getItems() {
        if (items == null)
            items = getFacade().getSesionesByTratamiento(tratamientoEnEdicion);
        return items;
    }

    public Tratamiento getTratamientoEnEdicion() {
        return tratamientoEnEdicion;
    }

    public void setTratamientoEnEdicion(Tratamiento tratamientoEnEdicion) {
        this.tratamientoEnEdicion = tratamientoEnEdicion;
    }

    /**
     * Validador usado en la vista de modificar sesión. Verifica que al tildarse
     * el checkbox de cuenta, la cantidad de sesiones que cuentan no supere la
     * cantidad de sesiones seteadas en el tratamiento.
     *
     * @param facesContext
     * @param componente
     * @param checkBoxValue
     */
    public void puedoSetearCuentaTrue(FacesContext facesContext, UIComponent componente, Object checkBoxValue) {
        boolean cuenta = (boolean) checkBoxValue;
        //Si ahora cuenta y antes no contaba
        if (cuenta && !getContaba()) {
            if (!getFacade().puedoAgregarSesion(this.getTratamientoEnEdicion())) {
                ((UISelectBoolean) componente).setValid(false);
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("Bundle").getString("EditSesion_validacionTopeDeSesiones"));
            }
        }
    }

    public void puedoCrearNuevaSesion(FacesContext facesContext, UIComponent componente, Object value) {
        if (!getFacade().puedoAgregarSesion(this.getTratamientoEnEdicion())) {
            ((UIInput) componente).setValid(false);
            JsfUtil.addErrorMessage(ResourceBundle.getBundle("Bundle").getString("EditSesion_validacionTopeDeSesiones"));
        }
    }

    //Comunicación entre controllers e inicializaciones de selected
    public Sesion prepareCreate(Tratamiento tratamiento) {
        selected = getFacade().initSesionFromTratamiento(tratamiento);
        return selected;
    }

    //Métodos de persistencia
    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("SesionCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        this.resetearCuenta();
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("SesionUpdated"));
    }

    public void destroy() {
        if (!selected.getTranscurrida()) {
            persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("SesionDeleted"));
            if (!JsfUtil.isValidationFailed()) {
                selected = null; // Remove selection
                items = null;    // Invalidate list of items to trigger re-query.
            }
        } else {
            JsfUtil.addErrorMessage("No se puede eliminar una sesion ya transcurrida");
        }

    }
}
