/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.edu.utn.frsf.kinesio.controllers.converters;

import ar.edu.utn.frsf.kinesio.entities.OrdenMedica;
import ar.edu.utn.frsf.kinesio.controllers.OrdenMedicaController;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

/**
 *
 * @author Nacho Gómez
 */
public class OrdenMedicaConverter implements Converter {

    public Object getAsObject(FacesContext facesContext, UIComponent component, String string) {
        if (string == null || string.length() == 0) {
            return null;
        }
        Integer id = new Integer(string);
        OrdenMedicaController controller = (OrdenMedicaController) facesContext.getApplication().getELResolver().getValue(facesContext.getELContext(), null, "ordenMedica");
        return controller.getOrdenMedica(getKey(string));
    }

    java.lang.Integer getKey(String value) {
        java.lang.Integer key;
        try {
            key = Integer.valueOf(value);
        } catch (NumberFormatException e) {
            key = Integer.MAX_VALUE;
        }
        return key;
    }
    
    public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
        if (object == null) {
            return null;
        }
        if (object instanceof OrdenMedica) {
            OrdenMedica o = (OrdenMedica) object;
            return o.getId() == null ? "" : o.getId().toString();
        } else {
            throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: ar.edu.utn.frsf.kinesio.entities.OrdenMedica");
        }
    }
    
}
