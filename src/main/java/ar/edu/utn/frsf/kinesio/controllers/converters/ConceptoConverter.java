package ar.edu.utn.frsf.kinesio.controllers.converters;

import ar.edu.utn.frsf.kinesio.controllers.CajaController;
import ar.edu.utn.frsf.kinesio.entities.Concepto;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter(forClass = Concepto.class)
public class ConceptoConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
        if (value == null || value.length() == 0) {
            return null;
        }
        CajaController controller = (CajaController) facesContext.getApplication().getELResolver().
                getValue(facesContext.getELContext(), null, "cajaController");
        return controller.getConcepto(getKey(value));
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
        if (object instanceof Concepto) {
            Concepto o = (Concepto) object;
            return getStringKey(o.getId());
        } else {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Concepto.class.getName()});
            return null;
        }
    }

}
