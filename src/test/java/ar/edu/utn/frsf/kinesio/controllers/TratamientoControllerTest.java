package ar.edu.utn.frsf.kinesio.controllers;

import ar.edu.utn.frsf.kinesio.controllers.converters.PacienteConverter;
import ar.edu.utn.frsf.kinesio.controllers.mocks.TestQualifier;
import ar.edu.utn.frsf.kinesio.controllers.mocks.TratamientoControllerHijo;
import ar.edu.utn.frsf.kinesio.entities.Paciente;
import ar.edu.utn.frsf.kinesio.entities.Tratamiento;
import ar.edu.utn.frsf.kinesio.gestores.TratamientoFacade;
import ar.edu.utn.frsf.kinesio.test.util.ContextMocker;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

/**
 *
 */
@RunWith(Arquillian.class)
public class TratamientoControllerTest {

    @Inject
    @TestQualifier
    TratamientoControllerHijo tratamientoController;

    private final FacesContext context;
    TratamientoFacade tratamientoFacade;

    public TratamientoControllerTest() {
        context = ContextMocker.mockFacesContext();
        context.getExternalContext().getRequestParameterMap().put("paciente", "1");
        PacienteConverter pc = mock(PacienteConverter.class);
        when(pc.getAsObject(context, null, "1")).thenReturn(new Paciente("Milesi", "Fernando", "36000060"));
        when(context.getApplication().createConverter(Paciente.class)).thenReturn(pc);

        tratamientoFacade = mock(TratamientoFacade.class);

    }

    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
                .addClasses(
                        TratamientoControllerHijo.class)
                .addAsResource("Bundle.properties")
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @Test
    public void deberiaCargarPacienteAlConstruirse() {
        assertNotNull(tratamientoController.getPaciente().getApellido());
    }

    @Test
    public void deberiaLlamarATratamientoFacade() {
        when(tratamientoFacade.initTratamiento((Paciente)anyObject())).thenReturn(new Tratamiento());
        tratamientoController.setFacade(tratamientoFacade);

        //Ejecuto la prueba
        tratamientoController.prepareCreate();
        verify(tratamientoFacade).initTratamiento((Paciente)anyObject());
    }

    @Test
    public void deberiaNoSerNullItems() throws NoSuchFieldException {
        tratamientoController.setFacade(tratamientoFacade);

        //Ejecuto la prueba
        assertNotNull(tratamientoController.getItems());
        verify(tratamientoFacade).getTratamientosByPaciente((Paciente) anyObject());
    }
}
