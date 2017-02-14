package ar.edu.utn.frsf.kinesio.controllers;

import ar.edu.utn.frsf.kinesio.controllers.util.JsfUtil;
import ar.edu.utn.frsf.kinesio.rest.RecordatoriosResource;
import java.io.Serializable;
import java.util.prefs.Preferences;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

@Named("userPreferencesController")
@ViewScoped
public class UserPreferencesController implements Serializable {

    String recordatorioWhatsApp;

    public UserPreferencesController() {
    }

    public String getRecordatorioWhatsApp() {
        if (recordatorioWhatsApp == null) {
            recordatorioWhatsApp = Preferences.userNodeForPackage(RecordatoriosResource.class)
                    .get(RecordatoriosResource.RECORDATORIO_WHATSAPP_PREFERENCE_KEY, "Mensaje por defecto");
        }
        return recordatorioWhatsApp;
    }

    public void setRecordatorioWhatsApp(String recordatorioWhatsApp) {
        this.recordatorioWhatsApp = recordatorioWhatsApp;
    }
    
    public void guardarCambios() {
        Preferences preferences = Preferences.userNodeForPackage(RecordatoriosResource.class);
        preferences.put(RecordatoriosResource.RECORDATORIO_WHATSAPP_PREFERENCE_KEY, recordatorioWhatsApp);
        JsfUtil.addSuccessMessage("Cambios guardados con éxito");
        //TODO: capturar excepciones
    }

}
