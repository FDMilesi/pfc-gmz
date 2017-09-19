package ar.edu.utn.frsf.kinesio.controllers;

import ar.edu.utn.frsf.kinesio.controllers.converters.TratamientoConverter;
import ar.edu.utn.frsf.kinesio.entities.ObraSocial;
import ar.edu.utn.frsf.kinesio.entities.Paciente;
import ar.edu.utn.frsf.kinesio.entities.Tratamiento;
import ar.edu.utn.frsf.kinesio.gestores.TratamientoFacade;
import ar.edu.utn.frsf.kinesio.test.util.ContextMocker;
import javax.el.ELResolver;
import javax.faces.context.FacesContext;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import static org.mockito.Mockito.*;

/**
 *
 */
public class EditTratamientoControllerTest {

    EditTratamientoController editTratamientoController;
    TratamientoController tratamientoController; //Lo necesita tratamientoConverter

    private final FacesContext context = ContextMocker.mockFacesContext();

    Paciente pacienteDePrueba = new Paciente();
    Tratamiento tratamientoDePrueba = new Tratamiento(pacienteDePrueba);

    TratamientoFacade tratamientoFacade = mock(TratamientoFacade.class);

    public EditTratamientoControllerTest() {
    }

    @Before
    public void init() {
        editTratamientoController = new EditTratamientoController();
        tratamientoController = new TratamientoController();
        tratamientoController.setFacade(tratamientoFacade);

        //Mock del context para proporcionar un converter cuando lo piden
        when(context.getApplication().createConverter(Tratamiento.class)).thenReturn(new TratamientoConverter());
        //Mock del context para que el converter encuentre el tratamiento controller
        //El tratamientoController es el encargado de llamar al facade
        ELResolver elResolver = mock(ELResolver.class);
        when(elResolver.getValue(context.getELContext(), null, "tratamientoController")).thenReturn(tratamientoController);
        when(context.getApplication().getELResolver()).thenReturn(elResolver);

        //Para testear la cantidad de mensajes puestos en el context
        ContextMocker.messageCount = 0;
    }

    //Test de parámetros
    @Test
    public void noDeberiaExplotarConParametroNull() {
        context.getExternalContext().getRequestParameterMap().put("tratamiento", null);

        editTratamientoController.init();
    }

    @Test
    public void noDeberiaExplotarConParametroIncorrecto() {
        context.getExternalContext().getRequestParameterMap().put("foo", "1");

        editTratamientoController.init();
    }

    @Test
    public void noDeberiaExplotarConParametroNoInt() {
        context.getExternalContext().getRequestParameterMap().put("tratamiento", "a4gfg");

        editTratamientoController.init();
    }

    //Específicos de editTratamientoController
    @Test
    public void deberiaInicializarseYNoAdvertir() {
        context.getExternalContext().getRequestParameterMap().put("tratamiento", "1");
        when(tratamientoFacade.find(1)).thenReturn(tratamientoDePrueba);
        when(tratamientoFacade.getTratamientosConSesionesAFavor(tratamientoDePrueba)).thenReturn(null);
        editTratamientoController.setFacade(tratamientoFacade);
        
        //Un tratamiento no particular y con el paciente con OS no deberia advertir
        //sobre la ausencia de la obra social
        tratamientoDePrueba.setParticular(false);
        pacienteDePrueba.setObraSocial(new ObraSocial());

        editTratamientoController.init();
        assertNotNull(editTratamientoController.getSelected());
        assertEquals(0, ContextMocker.messageCount);
    }

    @Test
    public void deberiaInicializarseYAdvertir() {
        context.getExternalContext().getRequestParameterMap().put("tratamiento", "1");
        when(tratamientoFacade.find(1)).thenReturn(tratamientoDePrueba);
        when(tratamientoFacade.getTratamientosConSesionesAFavor(tratamientoDePrueba)).thenReturn(null);
        editTratamientoController.setFacade(tratamientoFacade);

        //Un tratamiento no particular y con el paciente SIN OS deberia advertir
        tratamientoDePrueba.setParticular(false);
        pacienteDePrueba.setObraSocial(null);

        editTratamientoController.init();
        assertNotNull(editTratamientoController.getSelected());
        assertEquals(1, ContextMocker.messageCount);
    }
}
