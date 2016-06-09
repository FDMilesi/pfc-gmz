package ar.edu.utn.frsf.kinesio.controllers;

import ar.edu.utn.frsf.kinesio.controllers.adaptadores.EditTratamientoControllerHijo;
import ar.edu.utn.frsf.kinesio.controllers.converters.TratamientoConverter;
import ar.edu.utn.frsf.kinesio.entities.ObraSocial;
import ar.edu.utn.frsf.kinesio.entities.Paciente;
import ar.edu.utn.frsf.kinesio.entities.Tratamiento;
import ar.edu.utn.frsf.kinesio.test.util.ContextMocker;
import javax.faces.context.FacesContext;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import static org.mockito.Mockito.*;

/**
 *
 */
public class EditTratamientoControllerTest {

    EditTratamientoControllerHijo editTratamientoController;

    private final FacesContext context = ContextMocker.mockFacesContext();
    ;
    Paciente pacienteDePrueba;
    Tratamiento tratamientoDePrueba;

    public EditTratamientoControllerTest() {
    }

    @Before
    public void init() {
        //Entities de prueba
        pacienteDePrueba = new Paciente();
        tratamientoDePrueba = new Tratamiento(pacienteDePrueba);

        //El converter retorna el tratamiento
        TratamientoConverter tc = mock(TratamientoConverter.class);
        when(tc.getAsObject(context, null, "1")).thenReturn(tratamientoDePrueba);
        //Seteo el converter al context
        when(context.getApplication().createConverter(Tratamiento.class)).thenReturn(tc);

        editTratamientoController = new EditTratamientoControllerHijo();
        ContextMocker.messageCount = 0;
    }

    @Test
    public void deberiaInicializarseYNoAdvertir() {
        context.getExternalContext().getRequestParameterMap().put("tratamiento", "1");

        //Un tratamiento no particular y con el paciente con OS no deberia advertir
        tratamientoDePrueba.setParticular(false);
        pacienteDePrueba.setObraSocial(new ObraSocial());
        editTratamientoController.init();
        assertNotNull(editTratamientoController.getSelected());
        assertEquals(0, ContextMocker.messageCount);
    }

    @Test
    public void deberiaInicializarseYAdvertir() {
        context.getExternalContext().getRequestParameterMap().put("tratamiento", "1");

        //Un tratamiento no particular y con el paciente SIN OS deberia advertir
        tratamientoDePrueba.setParticular(false);
        pacienteDePrueba.setObraSocial(null);
        editTratamientoController.init();
        assertNotNull(editTratamientoController.getSelected());
        assertEquals(1, ContextMocker.messageCount);
    }

    @Test
    public void noDeberiaExplotar() {//smoke test
        context.getExternalContext().getRequestParameterMap().put("tratamiento", null);
        editTratamientoController.init();
    }

}
