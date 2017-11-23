package ar.edu.utn.frsf.kinesio.controllers;

import ar.edu.utn.frsf.kinesio.controllers.util.SesionScheduleEventModel;
import ar.edu.utn.frsf.kinesio.entities.Agenda;
import ar.edu.utn.frsf.kinesio.gestores.SesionFacade;
import ar.edu.utn.frsf.kinesio.test.util.AgendaControllerRequestScoped;
import ar.edu.utn.frsf.kinesio.test.util.ContextMocker;
import ar.edu.utn.frsf.kinesio.test.util.AgendaSesionControllerRequestScoped;
import ar.edu.utn.frsf.kinesio.test.util.TestQualifier;
import ar.edu.utn.frsf.kinesio.entities.Sesion;
import ar.edu.utn.frsf.kinesio.test.util.ResourceBundleArquillianHelper;
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
import org.junit.runner.RunWith;
import org.primefaces.event.ScheduleEntryMoveEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.ScheduleEvent;

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
                .addClasses(AgendaControllerRequestScoped.class,
                        AgendaSesionControllerRequestScoped.class)
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @Test
    public void deberiaInicializarceSesionEnSesionController() {
        //Seteo el facade al sesion controller
        sesionController.setFacade(new SesionFacade());
        when(event.getObject()).thenReturn(new Date());
        agendaController.setSelected(new Agenda());

        //Ejecuto la prueba
        agendaController.prepareCreateSesion(event);
        //El agendaController debería comunicarse con el sesionController e 
        //inicializar una nueva sesion
        assertNotNull(sesionController.getSelected());
        assertNotNull(sesionController.getSelected().getAgenda());
    }

    @Test
    public void deberiaObtenerseLaMismaSesion() {
        Integer idSesion = 2;

        Agenda agenda = new Agenda();
        List<Sesion> sesiones = new ArrayList<>();
        Sesion sesion = new Sesion((short) 10);//Numero sesion 10
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
        assertEquals(sesionController.getSelected(), sesion);
    }

    @Test
    public void deberiaValidarYPersistirSesionOnMove() {
        ResourceBundleArquillianHelper.initBundle();

        //Agenda mock
        Agenda agenda = mock(Agenda.class);
        Sesion testSesion = new Sesion();
        testSesion.setIdSesion(1);
        testSesion.setFechaHoraInicio(new Date());
        
        SesionScheduleEventModel testEvent = new SesionScheduleEventModel(testSesion);
        
        when(agendaController.getEvent("1")).thenReturn(testEvent);

        //SesionFacade mock. Supongo que valida bien (retorna 0 de error code)
        SesionFacade sesionFacadeMock = mock(SesionFacade.class);
        when(sesionFacadeMock.validarFechaEdicionSesion(anyObject(), anyObject())).thenReturn(0);
        when(sesionFacadeMock.editAndReturn(anyObject())).thenReturn(testSesion);

        //Mockeo Evento de move
        ScheduleEntryMoveEvent moveEvent = mock(ScheduleEntryMoveEvent.class);
        when(moveEvent.getScheduleEvent()).thenReturn(testEvent);
        agendaController.setSesionFacade(sesionFacadeMock);
        agendaController.setSelected(agenda);
        sesionController.setFacade(sesionFacadeMock);

        //Ejecuto la prueba
        agendaController.moverSesion(moveEvent);

        verify(sesionFacadeMock).validarFechaEdicionSesion(anyObject(), anyObject());
        verify(sesionFacadeMock).editAndReturn(anyObject());
        assertNotNull(sesionController.getSelected());
    }

    @Test
    public void deberiaActualizarAgendaOnCreateSesion() {
        ResourceBundleArquillianHelper.initBundle();

        Sesion testSesion = new Sesion();
        testSesion.setIdSesion(1);
        SesionScheduleEventModel testEvent = new SesionScheduleEventModel(testSesion);
        
        //SesionFacade mock. Supongo que valida bien (retorna 0 de error code)
        SesionFacade sesionFacadeMock = mock(SesionFacade.class);
        when(sesionFacadeMock.editAndReturn(anyObject())).thenReturn(testSesion);

        sesionController.setFacade(sesionFacadeMock);
        sesionController.setSelected(testSesion);

        //Agenda mock
        Agenda agenda = mock(Agenda.class);
        doNothing().when(agendaController).addEvent(anyObject());
        agendaController.setSelected(agenda);

        //Ejecuto la prueba
        sesionController.createFromAgenda();
        verify(agendaController).addEvent(testEvent);
    }

    @Test
    public void deberiaActualizarAgendaOnUpdateSesion() {
        ResourceBundleArquillianHelper.initBundle();

        Sesion testSesion = new Sesion();
        testSesion.setIdSesion(2);
        testSesion.setCuenta(Boolean.TRUE);
        testSesion.setFechaHoraInicio(new Date());

        SesionScheduleEventModel testEvent = new SesionScheduleEventModel(testSesion);
        
        SesionFacade sesionFacadeMock = mock(SesionFacade.class);
        when(sesionFacadeMock.validarFechaEdicionSesion(anyObject(), anyObject())).thenReturn(0);
        when(sesionFacadeMock.editAndReturn(anyObject())).thenReturn(testSesion);

        sesionController.setFacade(sesionFacadeMock);
        sesionController.setSelected(testSesion);

        //Agenda mock
        Agenda agenda = mock(Agenda.class);
        doNothing().when(agendaController).updateEvent(anyObject());
        agendaController.setSelected(agenda);

        //Ejecuto la prueba
        sesionController.antesDeEditar();
        sesionController.updateFromAgenda();
        verify(agendaController).updateEvent(testEvent);
    }

    @Test
    public void deberiaActualizarAgendaOnRemoveSesion() {
        ResourceBundleArquillianHelper.initBundle();

        Sesion testSesion = new Sesion();
        testSesion.setIdSesion(3);
        testSesion.setTranscurrida(Boolean.FALSE);
        testSesion.setFechaHoraInicio(new Date());

        SesionScheduleEventModel testEvent = new SesionScheduleEventModel(testSesion);
        
        SesionFacade sesionFacadeMock = mock(SesionFacade.class);
        doNothing().when(sesionFacadeMock).remove(anyObject());
        sesionController.setFacade(sesionFacadeMock);

        sesionController.setSelected(testSesion);

        //Agenda mock
        Agenda agenda = mock(Agenda.class);
        when(agendaController.getEvent("3")).thenReturn(testEvent);
        when(agendaController.deleteEvent(anyObject())).thenReturn(Boolean.TRUE);
        agendaController.setSelected(agenda);

        //Ejecuto la prueba
        sesionController.destroyFromAgenda();
        verify(agendaController).deleteEvent(testEvent);
    }
}
