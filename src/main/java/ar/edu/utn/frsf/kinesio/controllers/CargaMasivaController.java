package ar.edu.utn.frsf.kinesio.controllers;

import ar.edu.utn.frsf.kinesio.controllers.util.JsfUtil;
import ar.edu.utn.frsf.kinesio.entities.Sesion;
import ar.edu.utn.frsf.kinesio.gestores.SesionFacade;
import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
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
    private List<Sesion> sesionesCreadas;
    private Date horaLunes;
    private Date horaMartes;
    private Date horaMiercoles;
    private Date horaJueves;
    private Date horaViernes;

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
        if (this.getFacade().superaCantidadSesionesEnElAnioCargaMasiva(selected.getTratamiento(), (int)valor)) {
            ((UIInput) componente).setValid(false);
            JsfUtil.addErrorMessage("Se alcanzó el tope de sesiones en el año. Seleccione un numero menor de 'Veces a repetir'");        
        }
        
    }

    public void guardarCargaMasiva() {
        if (cantSesiones > 0) {
            Map<String, Date> diasYHorarios = new HashMap<>();
            for (String diaSeleccionado : diasSeleccionados) {
                diasYHorarios.put(diaSeleccionado, this.getHoraDelDiaSeleccionado(diaSeleccionado));
            }
            sesionesCreadas = this.getFacade().cargaMasivaSesiones(selected, diasYHorarios, cantSesiones);
            JsfUtil.addSuccessMessage("Sesiones creadas con éxito");
        } else {
            sesionesCreadas = new ArrayList<>();
            JsfUtil.addWarningMessage("No se ha creado ninguna sesión");
        }
    }

    private Date getHoraDelDiaSeleccionado(String dia) {
        switch (DayOfWeek.valueOf(dia)) {
            case MONDAY:
                return horaLunes;
            case TUESDAY:
                return horaMartes;
            case WEDNESDAY:
                return horaMiercoles;
            case THURSDAY:
                return horaJueves;
            case FRIDAY:
                return horaViernes;
        }
        return null;
    }

    public Sesion getSelected() {
        return selected;
    }

    public void setSelected(Sesion selected) {
        //Cargo las veces disponibles
        int vecesDisponible = selected.getTratamiento().getCantidadDeSesiones() - getFacade().countSesionesByTratamientoQueCuentan(selected.getTratamiento());
        if (vecesDisponible <= 0) {
            arrayVecesDisponible = new int[]{};
        } else {
            arrayVecesDisponible = new int[vecesDisponible];
            for (int i = 0; i < vecesDisponible; i++) {
                arrayVecesDisponible[i] = i + 1;
            }
        }
        //Seteo las horas por defecto
        horaLunes = (Date) selected.getFechaHoraInicio().clone();
        horaMartes = (Date) selected.getFechaHoraInicio().clone();
        horaMiercoles = (Date) selected.getFechaHoraInicio().clone();
        horaJueves = (Date) selected.getFechaHoraInicio().clone();
        horaViernes = (Date) selected.getFechaHoraInicio().clone();
        this.selected = selected;
    }

    public int getCantSesiones() {
        return cantSesiones;
    }

    public void setCantSesiones(int cantSesiones) {
        this.cantSesiones = cantSesiones;
    }

    public List<Sesion> getSesionesCreadas() {
        return sesionesCreadas;
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

    public Date getHoraLunes() {
        return horaLunes;
    }

    public void setHoraLunes(Date horaLunes) {
        this.horaLunes = horaLunes;
    }

    public Date getHoraMartes() {
        return horaMartes;
    }

    public void setHoraMartes(Date horaMartes) {
        this.horaMartes = horaMartes;
    }

    public Date getHoraMiercoles() {
        return horaMiercoles;
    }

    public void setHoraMiercoles(Date horaMiercoles) {
        this.horaMiercoles = horaMiercoles;
    }

    public Date getHoraJueves() {
        return horaJueves;
    }

    public void setHoraJueves(Date horaJueves) {
        this.horaJueves = horaJueves;
    }

    public Date getHoraViernes() {
        return horaViernes;
    }

    public void setHoraViernes(Date horaViernes) {
        this.horaViernes = horaViernes;
    }

}
