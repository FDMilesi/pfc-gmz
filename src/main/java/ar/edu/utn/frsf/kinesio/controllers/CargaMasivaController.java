package ar.edu.utn.frsf.kinesio.controllers;

import ar.edu.utn.frsf.kinesio.controllers.util.JsfUtil;
import ar.edu.utn.frsf.kinesio.entities.Sesion;
import ar.edu.utn.frsf.kinesio.gestores.SesionFacade;
import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

@Named("cargaMasivaController")
@ViewScoped
public class CargaMasivaController implements Serializable {

    @EJB
    private SesionFacade ejbFacade;

    private int cantSesiones;
    private Set<String> diasSeleccionados;
    private Locale localidad;
    private DayOfWeek[] dias;
    private int[] arrayVecesDisponible;
    private Sesion selected;

    @PostConstruct
    public void init() {
        //Pasar esto a algún controller application scoped
        this.localidad = new Locale("es");
        this.dias = new DayOfWeek[5];
        System.arraycopy(DayOfWeek.values(), 0, this.dias, 0, 5);
    }

    public void validatorCheckBoxes(FacesContext facesContext, UIComponent componente, Object value) {
        //si no se seleccionó ningún dia
        if (((Set<String>) value).isEmpty()) {
            ((UIInput) componente).setValid(false);
            JsfUtil.addErrorMessage("Seleccione al menos un día");
        }
    }

    public void validatorVecesARepetir(FacesContext facesContext, UIComponent componente, Object valor) {
        if (this.arrayVecesDisponible.length == 0) {
            ((UIInput) componente).setValid(false);

            JsfUtil.addErrorMessage("No se pueden agregar sesiones. "
                    + String.format("El tratamiento posee un tope de %s sesiones", selected.getTratamiento().getCantidadDeSesiones()));
        }
    }

    public void guardarCargaMasiva() {
        if (cantSesiones > 0) {
            this.getFacade().cargaMasivaSesiones(selected, diasSeleccionados, cantSesiones);
            JsfUtil.addSuccessMessage("Sesiones creadas con éxito");
        } else {
            JsfUtil.addWarningMessage("No se ha creado ninguna sesión");
        }
    }

    public Sesion getSelected() {
        return selected;
    }

    public void setSelected(Sesion selected) {
        int vecesDisponible = selected.getTratamiento().getCantidadDeSesiones() - getFacade().countSesionesByTratamientoQueCuentan(selected.getTratamiento());
        if (vecesDisponible <= 0) {
            arrayVecesDisponible = new int[]{};
        } else {
            arrayVecesDisponible = new int[vecesDisponible];
            for (int i = 0; i < vecesDisponible; i++) {
                arrayVecesDisponible[i] = i + 1;
            }
        }
        this.selected = selected;
    }

    public int getCantSesiones() {
        return cantSesiones;
    }

    public void setCantSesiones(int cantSesiones) {
        this.cantSesiones = cantSesiones;
    }

    public DayOfWeek[] getDias() {
        return dias;
    }

    public String getItemLabel(int dia) {
        return DayOfWeek.of(dia).getDisplayName(TextStyle.FULL, localidad);
    }

    public Set<String> getDiasSeleccionados() {
        return diasSeleccionados;
    }

    public void setDiasSeleccionados(Set<String> diasSeleccionados) {
        this.diasSeleccionados = diasSeleccionados;
    }

    public int[] getArrayVecesDisponible() {
        return arrayVecesDisponible;
    }

    public void setArrayVecesDisponible(int[] arrayVecesDisponible) {
        this.arrayVecesDisponible = arrayVecesDisponible;
    }

    private SesionFacade getFacade() {
        return ejbFacade;
    }
}
