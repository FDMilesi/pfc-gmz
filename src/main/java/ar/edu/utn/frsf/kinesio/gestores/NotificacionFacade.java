package ar.edu.utn.frsf.kinesio.gestores;

import ar.edu.utn.frsf.kinesio.entities.Notificacion;
import ar.edu.utn.frsf.kinesio.entities.Tratamiento;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 */
@Stateless
public class NotificacionFacade extends AbstractFacade<Notificacion> {

    @PersistenceContext(unitName = "ar.edu.utn.frsf_kinesio_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    @EJB
    TratamientoFacade tratamientoFacade;

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    private static final Date invalidDate = new Date(0);

    private static Date[] fechasIAPOS = {
        fromString("20/01/2017"),
        fromString("22/02/2017"),
        fromString("21/03/2017"),
        fromString("20/04/2017"),
        fromString("22/05/2017"),
        fromString("21/06/2017"),
        fromString("20/07/2017"),
        fromString("22/08/2017"),
        fromString("20/09/2017"),
        fromString("23/10/2017"),
        fromString("21/11/2017"),
        fromString("20/12/2017"),};

    private static Date[] fechasOtrasOS = {
        fromString("01/02/2017"),
        fromString("01/03/2017"), //------------
        fromString("03/04/2017"),
        fromString("02/05/2017"),
        fromString("01/06/2017"),
        fromString("03/07/2017"),
        fromString("01/08/2017"),
        fromString("01/09/2017"),
        fromString("02/10/2017"),
        fromString("01/11/2017"),
        fromString("01/12/2017"),
        fromString("02/01/2018"),};

    public NotificacionFacade() {
        super(Notificacion.class);
    }

    private static Date fromString(String spec) {
        try {
            return dateFormat.parse(spec);
        } catch (ParseException dfe) {
            return invalidDate;
        }
    }

    public void NotificarEntregaPlanillaAlCirculo() {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.DATE, 5);  // dias con antelacion q avisa la notificacion        
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        Date dt = c.getTime();

        if (Arrays.asList(fechasOtrasOS).contains(dt)) { //esto nose si va a comparar bien
            String descripcion = "Recuerde entregar la planilla de las restantes obras sociales al círculo el día: " + dateFormat.format(dt);
            this.CrearNotificacion(descripcion);
        }
    }

    public void NotificarEntregaPlanillaIaposAlCirculo() {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.DATE, 5);  // dias con antelacion q avisa la notificacion
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        Date dt = c.getTime();

        if (Arrays.asList(fechasIAPOS).contains(dt)) {
            String descripcion = "Recuerde entregar la planilla de IAPOS al círculo el día: " + dateFormat.format(dt);
            this.CrearNotificacion(descripcion);
        }
    }

    public void NotificarSiPacienteDebeOrden() {
        List<String> nombresPacientes = new ArrayList<>();
        //recorro los tratamientos que estan en curso y q no son particulares
        for (Tratamiento item : tratamientoFacade.getTratamientosEnCursoAndNoParticulares()) {
            //veo que el tratamiento
            if (!tratamientoFacade.esValidaCantidadSesionesCubiertas(item)) {
                if (!nombresPacientes.contains(item.getPaciente().toString())) {
                    //creo la notificacion
                    String descripcion = "El paciente " + item.getPaciente() + " debe orden.";
                    this.CrearNotificacion(descripcion);
                    nombresPacientes.add(item.getPaciente().toString());
                }
            }
        }
    }

    private void CrearNotificacion(String descripcion) {
        Notificacion notificacion = new Notificacion();
        notificacion.setDescripcion(descripcion);
        notificacion.setFechayhora(new Date());

        this.create(notificacion);
    }
}
