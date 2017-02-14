package ar.edu.utn.frsf.kinesio.rest;

import ar.edu.utn.frsf.kinesio.entities.Sesion;
import ar.edu.utn.frsf.kinesio.gestores.SesionFacade;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import javax.ejb.EJB;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;

@Path("recordatorios")
@RequestScoped
public class RecordatoriosResource {

    @EJB
    private SesionFacade sesionFacade;

    private static final SimpleDateFormat FORMATO_FECHA_RECORDATORIO = new SimpleDateFormat("dd/MM");
    private static final SimpleDateFormat FORMATO_HORA_RECORDATORIO = new SimpleDateFormat("HH:mm");

    public static final String RECORDATORIO_WHATSAPP_PREFERENCE_KEY = "recordatorioWhatsApp";
    
    public RecordatoriosResource() {
    }

    /**
     * Retrieves representation of an instance of
     * ar.edu.utn.frsf.kinesio.rest.RecordatoriosResource
     *
     * @return an instance of ar.edu.utn.frsf.kinesio.rest.Recordatorio
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Recordatorio> getRecordatorios() {
        List<Recordatorio> lista = new ArrayList<>();
        lista.add(new Recordatorio("Fernando", "prueba"));
        return lista;
    }

    @GET
    @Path("{fechaDesde}/{fechaHasta}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Recordatorio> getRecordatorios(@PathParam("fechaDesde") String fechaDesde, @PathParam("fechaHasta") String fechaHasta) {

        SimpleDateFormat ISODateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        List<Recordatorio> listaRecordatorios = new ArrayList<>();

        try {
            Date fechaDesdeDate = ISODateFormat.parse(fechaDesde);
            Date fechaHastaDate = ISODateFormat.parse(fechaHasta);

            //El array contiene primero una sesion y luego un string con el nombre del contacto de google
            List<Object[]> sesiones = sesionFacade.getSesionesByRangoFechas(fechaDesdeDate, fechaHastaDate);

            String mensajeRecordatorio 
                    = Preferences.userNodeForPackage(this.getClass())
                            .get(RECORDATORIO_WHATSAPP_PREFERENCE_KEY, "%1$s: recuerde su turno con el Dr. Strange el día %2$s a las %3$s");
            
            for (Object[] itemSesion : sesiones) {
                listaRecordatorios.add(
                        new Recordatorio(
                                (String) itemSesion[1], //El nombre de contacto viene el la segunda posicion del array
                                this.generarMensajeRecordatorio((Sesion) itemSesion[0], mensajeRecordatorio))); //La sesion en la primera
            }

        } catch (ParseException pe) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Error al parsear las fechas", pe);
        }

        return listaRecordatorios;
    }

    /**
     * PUT method for updating or creating an instance of RecordatoriosResource
     *
     * @param content representation for the resource
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public void putJson(Recordatorio content) {
    }

    private String generarMensajeRecordatorio(Sesion sesion, String mensaje) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(String.format(mensaje,
                sesion.getTratamiento().getPaciente().toString(),
                FORMATO_FECHA_RECORDATORIO.format(sesion.getFechaHoraInicio()),
                FORMATO_HORA_RECORDATORIO.format(sesion.getFechaHoraInicio())));
        return stringBuilder.toString();
    }
}
