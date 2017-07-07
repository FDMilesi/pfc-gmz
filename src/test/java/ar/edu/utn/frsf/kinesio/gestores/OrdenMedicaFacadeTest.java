package ar.edu.utn.frsf.kinesio.gestores;

import ar.edu.utn.frsf.kinesio.entities.ObraSocial;
import ar.edu.utn.frsf.kinesio.entities.OrdenMedica;
import ar.edu.utn.frsf.kinesio.entities.Paciente;
import ar.edu.utn.frsf.kinesio.entities.TipoDeTratamiento;
import ar.edu.utn.frsf.kinesio.entities.Tratamiento;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

public class OrdenMedicaFacadeTest {

    ObraSocialFacade obraSocialFacade;
    OrdenMedicaFacade ordenMedicaFacade;

    final Short idObraSocialPaciente = new Short("123");
    final Short idObraSocialAccidenteTranbajo = new Short("456");

    @Before
    public void init() {
        obraSocialFacade = spy(ObraSocialFacade.class);
        doReturn(new ObraSocial(idObraSocialAccidenteTranbajo)).when(obraSocialFacade).getObraSocialIAPOSAccidenteTrabajo();

        ordenMedicaFacade = spy(OrdenMedicaFacade.class);
        ordenMedicaFacade.setObraSocialFacade(obraSocialFacade);
        doReturn(true).when(ordenMedicaFacade).necesitaAutorizacion(anyObject(), anyObject());
    }

    @Test
    public void deberiaInicializarSinAccidenteTrabajo() {
        ObraSocial obraSocial = new ObraSocial();
        obraSocial.setId(idObraSocialPaciente);
        obraSocial.setNombre("Una Obra Social");

        Paciente paciente = new PacienteFacade().initPaciente();
        paciente.setNombre("Un");
        paciente.setApellido("Paciente");
        paciente.setDni("4566454");
        paciente.setObraSocial(obraSocial);

        TipoDeTratamiento tipoDeTratamiento = new TipoDeTratamiento(new Short("1"));
        tipoDeTratamiento.setNombre("Un tipo de tratamiento");
        tipoDeTratamiento.setDuracion(new Short("30"));
        tipoDeTratamiento.setCubiertoPorObraSocial(true);

        Tratamiento tratamiento = new TratamientoFacade().initTratamiento(paciente);
        tratamiento.setCantidadDeSesiones(new Short("10"));
        tratamiento.setTipoDeTratamiento(tipoDeTratamiento);
        tratamiento.setParticular(true);

        tratamiento.setAccidentetrabajo(Boolean.FALSE);
        OrdenMedica ordenMedica = ordenMedicaFacade.initOrden(tratamiento);

        //Se setea la obra social del paciente, ya que el tratamiento no es acc de trabajo
        Assert.assertEquals(idObraSocialPaciente, ordenMedica.getObraSocial().getId());
    }

    @Test
    public void deberiaInicializarConAccidenteTrabajo() {
        ObraSocial obraSocial = new ObraSocial();
        obraSocial.setId(idObraSocialPaciente);
        obraSocial.setNombre("Una Obra Social");

        Paciente paciente = new PacienteFacade().initPaciente();
        paciente.setNombre("Un");
        paciente.setApellido("Paciente");
        paciente.setDni("4566454");
        paciente.setObraSocial(obraSocial);

        TipoDeTratamiento tipoDeTratamiento = new TipoDeTratamiento(new Short("1"));
        tipoDeTratamiento.setNombre("Un tipo de tratamiento");
        tipoDeTratamiento.setDuracion(new Short("30"));
        tipoDeTratamiento.setCubiertoPorObraSocial(true);

        Tratamiento tratamiento = new TratamientoFacade().initTratamiento(paciente);
        tratamiento.setCantidadDeSesiones(new Short("10"));
        tratamiento.setTipoDeTratamiento(tipoDeTratamiento);
        tratamiento.setParticular(true);

        tratamiento.setAccidentetrabajo(Boolean.TRUE);
        OrdenMedica ordenMedica = ordenMedicaFacade.initOrden(tratamiento);

        //Se setea la OS de acc de trabajo, ya que el trata es acc de trabajo=true
        Assert.assertEquals(idObraSocialAccidenteTranbajo, ordenMedica.getObraSocial().getId());
    }

    @Test
    public void deberiaInicializarAutorizadaFalse() {
        doReturn(true).when(ordenMedicaFacade).necesitaAutorizacion(anyObject(), anyObject());

        ObraSocial obraSocial = new ObraSocial();
        obraSocial.setId(idObraSocialPaciente);
        obraSocial.setNombre("Una Obra Social");

        Paciente paciente = new PacienteFacade().initPaciente();
        paciente.setNombre("Un");
        paciente.setApellido("Paciente");
        paciente.setDni("4566454");
        paciente.setObraSocial(obraSocial);

        TipoDeTratamiento tipoDeTratamiento = new TipoDeTratamiento(new Short("1"));
        tipoDeTratamiento.setNombre("Un tipo de tratamiento");
        tipoDeTratamiento.setDuracion(new Short("30"));
        tipoDeTratamiento.setCubiertoPorObraSocial(true);

        Tratamiento tratamiento = new TratamientoFacade().initTratamiento(paciente);
        tratamiento.setCantidadDeSesiones(new Short("10"));
        tratamiento.setTipoDeTratamiento(tipoDeTratamiento);
        tratamiento.setParticular(true);

        tratamiento.setAccidentetrabajo(Boolean.TRUE);
        OrdenMedica ordenMedica = ordenMedicaFacade.initOrden(tratamiento);

        Assert.assertFalse(ordenMedica.getAutorizada());
    }

    @Test
    public void deberiaInicializarAutorizadaTrue() {
        doReturn(false).when(ordenMedicaFacade).necesitaAutorizacion(anyObject(), anyObject());

        ObraSocial obraSocial = new ObraSocial();
        obraSocial.setId(idObraSocialPaciente);
        obraSocial.setNombre("Una Obra Social");

        Paciente paciente = new PacienteFacade().initPaciente();
        paciente.setNombre("Un");
        paciente.setApellido("Paciente");
        paciente.setDni("4566454");
        paciente.setObraSocial(obraSocial);

        TipoDeTratamiento tipoDeTratamiento = new TipoDeTratamiento(new Short("1"));
        tipoDeTratamiento.setNombre("Un tipo de tratamiento");
        tipoDeTratamiento.setDuracion(new Short("30"));
        tipoDeTratamiento.setCubiertoPorObraSocial(true);

        Tratamiento tratamiento = new TratamientoFacade().initTratamiento(paciente);
        tratamiento.setCantidadDeSesiones(new Short("10"));
        tratamiento.setTipoDeTratamiento(tipoDeTratamiento);
        tratamiento.setParticular(true);

        tratamiento.setAccidentetrabajo(Boolean.TRUE);
        OrdenMedica ordenMedica = ordenMedicaFacade.initOrden(tratamiento);

        Assert.assertTrue(ordenMedica.getAutorizada());
    }
}
