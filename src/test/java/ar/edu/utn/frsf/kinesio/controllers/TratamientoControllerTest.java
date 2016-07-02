package ar.edu.utn.frsf.kinesio.controllers;

import ar.edu.utn.frsf.kinesio.controllers.converters.PacienteConverter;
import ar.edu.utn.frsf.kinesio.entities.Paciente;
import ar.edu.utn.frsf.kinesio.entities.Tratamiento;
import ar.edu.utn.frsf.kinesio.gestores.PacienteFacade;
import ar.edu.utn.frsf.kinesio.gestores.TratamientoFacade;
import ar.edu.utn.frsf.kinesio.test.util.ContextMocker;
import java.util.ArrayList;
import javax.el.ELResolver;
import javax.faces.context.FacesContext;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

public class TratamientoControllerTest {

    TratamientoController tratamientoController;
    PacienteController pacienteController;

    private final FacesContext context = ContextMocker.mockFacesContext();

    TratamientoFacade tratamientoFacade = mock(TratamientoFacade.class);
    PacienteFacade pacienteFacade = mock(PacienteFacade.class);

    public TratamientoControllerTest() {
    }

    @Before
    public void init() {
        tratamientoController = new TratamientoController();
        tratamientoController.setFacade(tratamientoFacade);
        pacienteController = new PacienteController();
        pacienteController.setEjbFacade(pacienteFacade);

        //Mock del context para proporcionar un converter cuando lo piden
        when(context.getApplication().createConverter(Paciente.class)).thenReturn(new PacienteConverter());
        //Mock del context para que el converter encuentre el tratamiento controller
        //El tratamientoController es el encargado de llamar al facade
        ELResolver elResolver = mock(ELResolver.class);
        when(elResolver.getValue(context.getELContext(), null, "pacienteController")).thenReturn(pacienteController);
        when(context.getApplication().getELResolver()).thenReturn(elResolver);
    }

    //Test de parámetros
    @Test
    public void noDeberiaExplotarConParametroNull() {
        context.getExternalContext().getRequestParameterMap().put("paciente", null);
        tratamientoController.init();
    }

    @Test
    public void noDeberiaExplotarConParametroNoInt() {
        context.getExternalContext().getRequestParameterMap().put("paciente", "a4gfg");
        tratamientoController.init();
    }

    //Lista de items
    @Test
    public void deberiaCargarPacienteAlConstruirse() {
        Paciente pacienteDePrueba = new Paciente("ape", "nom", "3");
        context.getExternalContext().getRequestParameterMap().put("paciente", "1");
        when(pacienteFacade.find(1)).thenReturn(pacienteDePrueba);
        when(tratamientoFacade.getTratamientosByPaciente(pacienteDePrueba)).thenReturn(new ArrayList<Tratamiento>());

        tratamientoController.init();
        assertNotNull(tratamientoController.getPaciente().getApellido());

        assertNotNull(tratamientoController.getItems());//llama al facade
        verify(tratamientoFacade).getTratamientosByPaciente(pacienteDePrueba);
    }

    @Test
    public void deberiaNoExplotarAntePacienteNull() {
        context.getExternalContext().getRequestParameterMap().put("paciente", "1");
        when(pacienteFacade.find(1)).thenReturn(null);//No encuentra el paciente

        tratamientoController.init();

        assertNull(tratamientoController.getPaciente());
        assertEquals(tratamientoController.getItems().size(), 0);//llama al facade
        verify(tratamientoFacade).getTratamientosByPaciente(null);

    }

    @Test
    public void deberiaNoExplotarSiFacadeDevuelveNull() {
        assertNotNull(tratamientoController.getItems());
        verify(tratamientoFacade).getTratamientosByPaciente((Paciente) anyObject());
    }

    //Operaciones CRUD
    @Test
    public void deberiaLlamarATratamientoFacade() {
        when(tratamientoFacade.initTratamiento((Paciente) anyObject())).thenReturn(new Tratamiento());

        //Ejecuto la prueba
        tratamientoController.prepareCreate();
        verify(tratamientoFacade).initTratamiento((Paciente) anyObject());
    }
}
