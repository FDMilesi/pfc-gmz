package ar.edu.utn.frsf.kinesio.gestores;

import ar.edu.utn.frsf.kinesio.entities.Agenda;
import ar.edu.utn.frsf.kinesio.entities.Sesion;
import ar.edu.utn.frsf.kinesio.entities.TipoDeTratamiento;
import ar.edu.utn.frsf.kinesio.entities.Tratamiento;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.mock;

public class SesionFacadeTest {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");
    private SesionFacade sesionFacade;
    List<Sesion> listaSesiones;
    EntityManager em = mock(EntityManager.class);

    public SesionFacadeTest() {
    }

    @Before
    public void setUp() {
        sesionFacade = new SesionFacade();
    }

    @Test
    public void debeRetornarNroSesionUno() {
        listaSesiones = Arrays.asList();
        Sesion sesion = new Sesion();
        sesion.setFechaHoraInicio(new Date());//Cualquier fecha
        Short numero = sesionFacade.getNumeroDeSesion(listaSesiones, sesion);
        assertEquals("Lista vacía debe dar 1", new Short((short) 1), numero);
    }

    @Test
    public void debeRetornarSegunElOrdenDeFecha() throws ParseException {
        Sesion sesion1 = new Sesion((short) 1);
        Sesion sesion2 = new Sesion((short) 2);
        Sesion sesion3 = new Sesion((short) 3);
        Sesion primeraSesion = new Sesion();
        Sesion sesionIntermedia = new Sesion();
        Sesion sesionAlFinal = new Sesion();

        sesion1.setFechaHoraInicio(DATE_FORMAT.parse("2/2/2017"));
        sesion2.setFechaHoraInicio(DATE_FORMAT.parse("4/2/2017"));
        sesion3.setFechaHoraInicio(DATE_FORMAT.parse("6/2/2017"));
        primeraSesion.setFechaHoraInicio(DATE_FORMAT.parse("1/2/2017"));
        sesionIntermedia.setFechaHoraInicio(DATE_FORMAT.parse("5/2/2017"));
        sesionAlFinal.setFechaHoraInicio(DATE_FORMAT.parse("7/2/2017"));

        listaSesiones = Arrays.asList(sesion1, sesion2, sesion3);//El orden importa
        assertEquals("Entre la 2 y la 3 me debe dar 3", new Short((short) 3), sesionFacade.getNumeroDeSesion(listaSesiones, sesionIntermedia));
        assertEquals("Antes que todas me debe dar 1", new Short((short) 1), sesionFacade.getNumeroDeSesion(listaSesiones, primeraSesion));
        assertEquals("Después que todas me debe dar 4", new Short((short) 4), sesionFacade.getNumeroDeSesion(listaSesiones, sesionAlFinal));

        assertEquals("Pisar la primera me debe dar 2", new Short((short) 2), sesionFacade.getNumeroDeSesion(listaSesiones, sesion1));
        assertEquals("Pisar la segunda me debe dar 3", new Short((short) 3), sesionFacade.getNumeroDeSesion(listaSesiones, sesion2));
        assertEquals("Pisar la tercera me debe dar 4", new Short((short) 4), sesionFacade.getNumeroDeSesion(listaSesiones, sesion3));
    }

    @Test
    public void debeInicializarOKDesdeAgenda() {
        Sesion sesion = sesionFacade.initSesionFromAgenda(new Date(), new Agenda(Short.MIN_VALUE));
        assertNotNull(sesion.getAgenda());
        assertNotNull(sesion.getFechaHoraInicio());
        assertFalse(sesion.getTranscurrida());
        assertTrue(sesion.getCuenta());
        assertNull(sesion.getTratamiento());
        assertNull(sesion.getNumeroDeSesion());
    }

    @Test
    public void debeInicializarOKDesdeTratamiento() {
        Sesion sesion = sesionFacade.initSesionFromTratamiento(new Tratamiento());
        assertNull(sesion.getAgenda());
        assertNull(sesion.getFechaHoraInicio());
        assertFalse(sesion.getTranscurrida());
        assertTrue(sesion.getCuenta());
        assertNotNull(sesion.getTratamiento());
        assertNull(sesion.getNumeroDeSesion());
    }

    @Test
    public void deberiaLlamarRecalculoNroSesionEdit() {
        Tratamiento tratamiento = new Tratamiento();
        TipoDeTratamiento tipoTrata = new TipoDeTratamiento((short) 1);
        tipoTrata.setDuracion(Short.MAX_VALUE);
        tratamiento.setTipoDeTratamiento(tipoTrata);
        tratamiento.setFinalizado(Boolean.FALSE);

        Sesion sesion = new Sesion();
        sesion.setCuenta(Boolean.FALSE);
        sesion.setTratamiento(tratamiento);

        when(em.merge(sesion)).thenReturn(sesion);

        //Mockeo un solo metodo del facade
        sesionFacade = spy(SesionFacade.class);
        doReturn(Arrays.asList()).when(sesionFacade).getSesionesByTratamiento(tratamiento);

        //Le seteo el mock del entitymanager
        sesionFacade.setEm(em);

        //Ejecuto la prueba
        sesionFacade.editAndReturn(sesion);
        verify(sesionFacade).recalcularNumerosDeSesion(anyObject());
    }

    @Test
    public void deberiaLlamarRecalculoNroSesionRemove() {
        Sesion sesion = new Sesion();

        when(em.merge(sesion)).thenReturn(sesion);

        //Mockeo un solo metodo del facade
        sesionFacade = spy(SesionFacade.class);
        doReturn(Arrays.asList()).when(sesionFacade).getSesionesByTratamiento(anyObject());

        //Le seteo el mock del entitymanager
        sesionFacade.setEm(em);

        //Ejecuto la prueba
        sesionFacade.remove(sesion);
        verify(sesionFacade).recalcularNumerosDeSesion(anyObject());
    }
}
