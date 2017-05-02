package ar.edu.utn.frsf.kinesio.gestores;

import ar.edu.utn.frsf.kinesio.entities.Sesion;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class SesionFacadeTest {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");
    private SesionFacade sesionFacade;
    List<Sesion> listaSesiones;

    public SesionFacadeTest() {
    }

    @Before
    public void setUp() {
        sesionFacade = new SesionFacade();
    }

    @Test
    public void debeRetornarUno() {
        listaSesiones = Arrays.asList();
        Sesion sesion = new Sesion();
        sesion.setFechaHoraInicio(new Date());//Cualquier fecha
        Short numero = sesionFacade.getNumeroDeSesion(listaSesiones, sesion);
        assertEquals("Lista vacía debe dar 1", new Short((short) 1), numero);
    }

    @Test
    public void debeRetornarSegunElOrdenDeFecha() {
        Sesion sesion1 = new Sesion((short) 1);
        Sesion sesion2 = new Sesion((short) 2);
        Sesion sesion3 = new Sesion((short) 3);
        Sesion primeraSesion = new Sesion();
        Sesion sesionIntermedia = new Sesion();
        Sesion sesionAlFinal = new Sesion();

        try {
            sesion1.setFechaHoraInicio(DATE_FORMAT.parse("2/2/2017"));
            sesion2.setFechaHoraInicio(DATE_FORMAT.parse("4/2/2017"));
            sesion3.setFechaHoraInicio(DATE_FORMAT.parse("6/2/2017"));
            primeraSesion.setFechaHoraInicio(DATE_FORMAT.parse("1/2/2017"));
            sesionIntermedia.setFechaHoraInicio(DATE_FORMAT.parse("5/2/2017"));
            sesionAlFinal.setFechaHoraInicio(DATE_FORMAT.parse("7/2/2017"));

        } catch (ParseException ex) {
            Logger.getLogger(SesionFacadeTest.class.getName()).log(Level.SEVERE, null, ex);
        }

        listaSesiones = Arrays.asList(sesion1, sesion2, sesion3);//El orden importa
        assertEquals("Entre la 2 y la 3 me debe dar 3", new Short((short) 3), sesionFacade.getNumeroDeSesion(listaSesiones, sesionIntermedia));
        assertEquals("Antes que todas me debe dar 1", new Short((short) 1), sesionFacade.getNumeroDeSesion(listaSesiones, primeraSesion));
        assertEquals("Después que todas me debe dar 4", new Short((short) 4), sesionFacade.getNumeroDeSesion(listaSesiones, sesionAlFinal));

        assertEquals("Pisar la primera me debe dar 2", new Short((short) 2), sesionFacade.getNumeroDeSesion(listaSesiones, sesion1));
        assertEquals("Pisar la segunda me debe dar 3", new Short((short) 3), sesionFacade.getNumeroDeSesion(listaSesiones, sesion2));
        assertEquals("Pisar la tercera me debe dar 4", new Short((short) 4), sesionFacade.getNumeroDeSesion(listaSesiones, sesion3));

    }    
}
