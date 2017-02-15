package ar.edu.utn.frsf.kinesio.controllers;

import ar.edu.utn.frsf.kinesio.controllers.util.JsfUtil;
import ar.edu.utn.frsf.kinesio.gestores.GoogleContactsFacade;
import ar.edu.utn.frsf.kinesio.rest.RecordatoriosResource;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

@Named("userPreferencesController")
@ViewScoped
public class UserPreferencesController implements Serializable {

    @EJB
    GoogleContactsFacade googleContactsFacade;

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

    public void sincronizarContactos() {
        try {
            String resultado = googleContactsFacade.sincronizarContactos();
            
            JsfUtil.addSuccessMessage(resultado);
            
        } catch (EJBException | GoogleContactsFacade.CredencialesNoEncontradasException ex) {
            Logger.getLogger(UserPreferencesController.class.getName()).log(Level.SEVERE, null, ex);
            JsfUtil.addErrorMessage("Ha ocurrido un error al sincronizar los contactos");
        }
    }

}
