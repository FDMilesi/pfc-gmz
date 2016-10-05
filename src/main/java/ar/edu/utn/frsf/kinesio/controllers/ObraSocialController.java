package ar.edu.utn.frsf.kinesio.controllers;

import ar.edu.utn.frsf.kinesio.entities.ObraSocial;
import ar.edu.utn.frsf.kinesio.entities.TipoDeTratamiento;
import ar.edu.utn.frsf.kinesio.entities.TipoTratamientoObraSocial;
import ar.edu.utn.frsf.kinesio.entities.TipoTratamientoObraSocialPK;
import ar.edu.utn.frsf.kinesio.gestores.ObraSocialFacade;
import ar.edu.utn.frsf.kinesio.gestores.TipoTratamientoObraSocialFacade;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@Named(value = "obraSocialController")
@ApplicationScoped
public class ObraSocialController {

    @EJB
    private ObraSocialFacade ejbFacade;
    @EJB
    private TipoTratamientoObraSocialFacade tipoTratamientoObraSocialFacade;

    List<ObraSocial> items;

    //lista de OS para el autocomplete desde lista de ordenes. Necesito que este la opción: Todas excepto IAPOS
    List<ObraSocial> itemsMasObraSocialNOIapos;

    //Objeto usado para dar la opción en los combo de obtener todas las obras sociales excepto IAPOS
    //Es una obra social falsa, por eso el id -1. Creada a los fines de poder listarla en los combo.
    //requirió la modificacion del converter, para el caso de ID -1
    private final ObraSocial obraSocialNoIAPOS = new ObraSocial((short) -1, "Todas excepto IAPOS");

    @PostConstruct
    private void init() {
        items = getFacade().findAll();
        itemsMasObraSocialNOIapos = new ArrayList<>(items);
        itemsMasObraSocialNOIapos.add(obraSocialNoIAPOS);
    }

    public ObraSocialController() {
    }

    public String getCodigoDePrestacion(TipoDeTratamiento tipoDeTratamiento, ObraSocial obraSocial) {

        //Se puede eliminar este chequeo cdo la BD este consistente
        if (obraSocial == null) {
            return "Obra Social no seteada. Volver a crear la orden";
        }

        TipoTratamientoObraSocial tipoTratamientoObraSocial = this.getTipoTratamientoObraSocialFacade()
                .find(new TipoTratamientoObraSocialPK(tipoDeTratamiento.getId(), obraSocial.getId()));

        //Si no existe la relacion entre la OS y el tipo de trata
        if (tipoTratamientoObraSocial == null) {
            return tipoDeTratamiento.getNombre();
        }
        //Si no fue cargado ningún código a la relación
        if (tipoTratamientoObraSocial.getCodigoDePrestacion().isEmpty()
                || tipoTratamientoObraSocial.getCodigoDePrestacion() == null) {
            return tipoDeTratamiento.getNombre();
        }

        return tipoTratamientoObraSocial.getCodigoDePrestacion();
    }

    private ObraSocialFacade getFacade() {
        return ejbFacade;
    }

    public List<ObraSocial> autocompletar(String query) {
        return items.stream().filter(o -> o.getNombre().toLowerCase().startsWith(query.toLowerCase())).collect(Collectors.toList());
    }

    public List<ObraSocial> autocompletarDesdeListaOrdenes(String query) {
        return itemsMasObraSocialNOIapos.stream().filter(o -> o.getNombre().toLowerCase().startsWith(query.toLowerCase())).collect(Collectors.toList());
    }

    public TipoTratamientoObraSocialFacade getTipoTratamientoObraSocialFacade() {
        return tipoTratamientoObraSocialFacade;
    }

    public List<ObraSocial> getItemsAvailableSelectOne() {
        return items;
    }

    public ObraSocial getObraSocial(java.lang.Short id) {
        return getFacade().find(id);
    }

    public ObraSocial getObraSocialNoIAPOS() {
        return obraSocialNoIAPOS;
    }

    @FacesConverter(forClass = ObraSocial.class)
    public static class ObraSocialControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0 || !value.matches("\\d+")) {
                return null;
            }
            ObraSocialController controller = (ObraSocialController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "obraSocialController");
            if (getKey(value).equals((short) -1)) {
                return controller.getObraSocialNoIAPOS();
            }
            return controller.getObraSocial(getKey(value));
        }

        java.lang.Short getKey(String value) {
            java.lang.Short key;
            key = Short.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Short value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof ObraSocial) {
                ObraSocial o = (ObraSocial) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), ObraSocial.class.getName()});
                return null;
            }
        }

    }
}
