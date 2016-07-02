package ar.edu.utn.frsf.kinesio.controllers.converters;

import ar.edu.utn.frsf.kinesio.controllers.TratamientoController;
import ar.edu.utn.frsf.kinesio.entities.Tratamiento;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter(forClass = Tratamiento.class)
public class TratamientoConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
        if (value == null || value.length() == 0) {
            return null;
        }
        TratamientoController controller = (TratamientoController) facesContext.getApplication().getELResolver().
                getValue(facesContext.getELContext(), null, "tratamientoController");
        return controller.getTratamiento(getKey(value));
    }

    java.lang.Integer getKey(String value) {
        java.lang.Integer key;
        try {
            key = Integer.valueOf(value);
        }catch (NumberFormatException e){
            key = Integer.MAX_VALUE;
        }
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
        if (object instanceof Tratamiento) {
            Tratamiento o = (Tratamiento) object;
            return getStringKey(o.getId());
        } else {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Tratamiento.class.getName()});
            return null;
        }
    }

}
