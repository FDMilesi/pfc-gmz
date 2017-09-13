package ar.edu.utn.frsf.kinesio.controllers;

import ar.edu.utn.frsf.kinesio.controllers.util.JsfUtil;
import ar.edu.utn.frsf.kinesio.gestores.GoogleContactsFacade;
import ar.edu.utn.frsf.kinesio.gestores.TipoTratamientoObraSocialFacade;
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
    int defaultTopeSesionesAnio;

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

    public int getDefaultTopeSesionesAnio() {
        if (defaultTopeSesionesAnio == 0) {
            defaultTopeSesionesAnio = Preferences.userNodeForPackage(TipoTratamientoObraSocialFacade.class)
                    .getInt(TipoTratamientoObraSocialFacade.DEFAULT_TOPE_SESIONES_ANIO_PREFERENCE_KEY, 25);
        }
        return defaultTopeSesionesAnio;
    }

    public void setDefaultTopeSesionesAnio(int defaultTopeSesionesAnio) {
        this.defaultTopeSesionesAnio = defaultTopeSesionesAnio;
    }

    public void guardarCambios() {
        Preferences preferencesRecordatorios = Preferences.userNodeForPackage(RecordatoriosResource.class);
        preferencesRecordatorios.put(RecordatoriosResource.RECORDATORIO_WHATSAPP_PREFERENCE_KEY, recordatorioWhatsApp);

        Preferences preferencesTipoTratamientoOS = Preferences.userNodeForPackage(TipoTratamientoObraSocialFacade.class);
        preferencesTipoTratamientoOS.putInt(TipoTratamientoObraSocialFacade.DEFAULT_TOPE_SESIONES_ANIO_PREFERENCE_KEY, defaultTopeSesionesAnio);
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
