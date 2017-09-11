package ar.edu.utn.frsf.kinesio.gestores;

import ar.edu.utn.frsf.kinesio.entities.Paciente;
import ar.edu.utn.frsf.kinesio.entities.TipoDeTratamiento;
import ar.edu.utn.frsf.kinesio.entities.Tratamiento;
import java.util.Date;
import java.util.List;
import java.util.Set;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TratamientoEntityIT {

    private TratamientoFacade tratamientoFacade;
    private EntityTransaction transaction;

    private Validator validator;

    public TratamientoEntityIT() {
    }

    @Before
    public void setUp() {
        ValidatorFactory vf = Validation.buildDefaultValidatorFactory();
        this.validator = vf.getValidator();
        this.tratamientoFacade = new TratamientoFacade();
        this.tratamientoFacade.setEm(Persistence.createEntityManagerFactory("integration").createEntityManager());
        this.transaction = tratamientoFacade.getEntityManager().getTransaction();
    }

    @Test
    public void deberiaValidarYPersistirTratamiento() {
        Paciente paciente = new Paciente();
        paciente.setFechaAlta(new Date());
        paciente.setNombre("Un");
        paciente.setApellido("Paciente");
        paciente.setDni("4566454");

        TipoDeTratamiento tipoDeTratamiento = new TipoDeTratamiento(new Short("1"));
        tipoDeTratamiento.setNombre("Un tipo de tratamiento");
        tipoDeTratamiento.setDuracion(new Short("30"));
        tipoDeTratamiento.setCubiertoPorObraSocial(true);

        Tratamiento tratamiento = new Tratamiento(paciente);
        tratamiento.setFechaCreacion(new Date());
        tratamiento.setParticular(false);
        tratamiento.setFinalizado(Boolean.FALSE);
        tratamiento.setCantidadDeSesiones(new Short("10"));
        tratamiento.setTipoDeTratamiento(tipoDeTratamiento);
        tratamiento.setParticular(true);
        tratamiento.setAccidentetrabajo(Boolean.TRUE);

        Set<ConstraintViolation<Tratamiento>> violations = this.validator.validate(tratamiento);

//        System.out.println(violations.toString());
        assertTrue(violations.isEmpty());

        this.transaction.begin();

        this.tratamientoFacade.getEntityManager().persist(paciente);
        this.tratamientoFacade.getEntityManager().persist(tipoDeTratamiento);

        this.tratamientoFacade.create(tratamiento);

        List<Tratamiento> listaTratamientos = tratamientoFacade.findAll();
        assertNotNull(listaTratamientos);
        assertEquals(listaTratamientos.size(), 1);

        this.transaction.rollback();
    }

    @Test
    public void deberiaSerInvalidoPacienteNulo() {
        TipoDeTratamiento tipoDeTratamiento = new TipoDeTratamiento(new Short("1"));
        tipoDeTratamiento.setNombre("Un tipo de tratamiento");
        tipoDeTratamiento.setDuracion(new Short("30"));
        tipoDeTratamiento.setCubiertoPorObraSocial(true);

        //Seteo paciente null
        Tratamiento tratamiento = new Tratamiento(null);
        tratamiento.setFechaCreacion(new Date());
        tratamiento.setParticular(false);
        tratamiento.setFinalizado(Boolean.FALSE);
        tratamiento.setCantidadDeSesiones(new Short("10"));
        tratamiento.setTipoDeTratamiento(tipoDeTratamiento);
        tratamiento.setParticular(true);
        tratamiento.setAccidentetrabajo(Boolean.TRUE);

        Set<ConstraintViolation<Tratamiento>> violations = this.validator.validate(tratamiento);

//        System.out.println(violations.toString());
        assertEquals(1, violations.size());
        assertEquals("paciente", violations.stream().findFirst().get().getPropertyPath().toString());
    }

    @Test
    public void deberiaSerInvalidoTipoTratamientoNulo() {
        Paciente paciente = new Paciente();
        paciente.setFechaAlta(new Date());
        paciente.setNombre("Un");
        paciente.setApellido("Paciente");
        paciente.setDni("4566454");

        Tratamiento tratamiento = new Tratamiento(paciente);
        tratamiento.setFechaCreacion(new Date());
        tratamiento.setParticular(false);
        tratamiento.setFinalizado(Boolean.FALSE);
        tratamiento.setCantidadDeSesiones(new Short("10"));
        tratamiento.setTipoDeTratamiento(null);
        tratamiento.setParticular(true);
        tratamiento.setAccidentetrabajo(Boolean.TRUE);

        Set<ConstraintViolation<Tratamiento>> violations = this.validator.validate(tratamiento);

//        System.out.println(violations.toString());
        assertEquals(1, violations.size());
        assertEquals("tipoDeTratamiento", violations.stream().findFirst().get().getPropertyPath().toString());
    }
}
