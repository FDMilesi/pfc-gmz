package ar.edu.utn.frsf.kinesio.controllers;

import ar.edu.utn.frsf.kinesio.entities.Agenda;
import ar.edu.utn.frsf.kinesio.gestores.SesionFacade;
import ar.edu.utn.frsf.kinesio.controllers.adaptadores.AgendaControllerHijo;
import ar.edu.utn.frsf.kinesio.test.util.ContextMocker;
import ar.edu.utn.frsf.kinesio.controllers.adaptadores.SesionControllerHijo;
import ar.edu.utn.frsf.kinesio.controllers.adaptadores.TestQualifier;
import ar.edu.utn.frsf.kinesio.entities.Sesion;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.ScheduleEvent;

/**
 *
 */
@RunWith(Arquillian.class)
public class AgendaYSesionControllersIT {

    @Inject
    @TestQualifier
    AgendaController agendaController;
    @Inject
    @TestQualifier
    AgendaSesionController sesionController;

    private final FacesContext context;

    //Evento mockeado que simule el evento enviado desde el ScheduleModel
    SelectEvent event = mock(SelectEvent.class);

    //Constructor
    public AgendaYSesionControllersIT() {
        context = ContextMocker.mockFacesContext();
    }

    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
                .addClasses(
                        AgendaControllerHijo.class,
                        SesionControllerHijo.class)
                .addAsResource("Bundle.properties")
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @Test
    public void deberiaInicializarceSesionEnSesionController() {
        //Seteo el facade al sesion controller
        sesionController.setFacade(new SesionFacade());
        when(event.getObject()).thenReturn(new Date());
        //Ejecuto la prueba
        agendaController.prepareCreateSesion(event);
        //El agendaController debería comunicarse con el sesionController e 
        //inicializar una nueva sesion
        assertNotNull(sesionController.getSelected());
    }

    @Test
    public void deberiaObtenerseLaMismaSesion() {
        Integer idSesion = 2;
        
        Agenda agenda = new Agenda();
        List<Sesion> sesiones = new ArrayList<>();
        Sesion sesion = new Sesion((short) 10);
        sesion.setIdSesion(idSesion);
        sesion.setCuenta(true);
        sesion.setFechaHoraInicio(new Date());
        sesiones.add(sesion);
        agenda.setSesiones(sesiones);
        
        ScheduleEvent scheduleEvent = new DefaultScheduleEvent();
        scheduleEvent.setId(String.valueOf(idSesion));
        when(event.getObject()).thenReturn(scheduleEvent);
        
        agendaController.setSelected(agenda);
        //Ejecuto la prueba
        agendaController.mostrarSesion(event);
        
        assertNotNull(sesionController.getSelected());

        Sesion sesionRecibida = sesionController.getSelected();
        
        assertEquals(sesionRecibida.getIdSesion(), idSesion);

    }

}
