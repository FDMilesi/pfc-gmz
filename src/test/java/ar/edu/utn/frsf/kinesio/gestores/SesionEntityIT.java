package ar.edu.utn.frsf.kinesio.gestores;

import ar.edu.utn.frsf.kinesio.entities.Agenda;
import ar.edu.utn.frsf.kinesio.entities.Paciente;
import ar.edu.utn.frsf.kinesio.entities.Sesion;
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

public class SesionEntityIT {

    private SesionFacade sesionFacade;
    private EntityTransaction transaction;

    private Validator validator;

    public SesionEntityIT() {
    }

    @Before
    public void setUp() {
        ValidatorFactory vf = Validation.buildDefaultValidatorFactory();
        this.validator = vf.getValidator();
        this.sesionFacade = new SesionFacade();
        this.sesionFacade.setEm(Persistence.createEntityManagerFactory("integration").createEntityManager());
        this.transaction = sesionFacade.getEntityManager().getTransaction();
    }

    @Test
    public void deberiaValidarYPersistirSesion() {
        Agenda agenda = new Agenda();

        Paciente paciente = new Paciente();
        paciente.setFechaAlta(new Date());
        paciente.setNombre("Un");
        paciente.setApellido("Paciente");
        paciente.setDni("4566454");
        paciente.setNroAfiliadoOS("Numero afiliado");

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
        tratamiento.setAccidentetrabajo(Boolean.FALSE);

        Sesion sesion = new Sesion();
        sesion.setFechaHoraInicio(new Date());
        sesion.setTratamiento(tratamiento);
        sesion.setAgenda(agenda);

        Set<ConstraintViolation<Sesion>> violations = this.validator.validate(sesion);

//        System.out.println(violations.toString());
        assertTrue(violations.isEmpty());

        this.transaction.begin();

        this.sesionFacade.getEntityManager().persist(agenda);
        this.sesionFacade.getEntityManager().persist(paciente);
        this.sesionFacade.getEntityManager().persist(tipoDeTratamiento);
        this.sesionFacade.getEntityManager().persist(tratamiento);

        this.sesionFacade.create(sesion);

        List<Sesion> listaOrdenes = sesionFacade.findAll();
        assertNotNull(listaOrdenes);
        assertEquals(listaOrdenes.size(), 1);

        this.transaction.rollback();
    }

    @Test
    public void deberiaSerInvalidoTratamientoNulo() {
        Agenda agenda = new Agenda();

        Sesion sesion = new Sesion();
        sesion.setFechaHoraInicio(new Date());
        sesion.setTratamiento(null);
        sesion.setAgenda(agenda);

        Set<ConstraintViolation<Sesion>> violations = this.validator.validate(sesion);

//        System.out.println(violations.toString());
        assertEquals(1, violations.size());
        assertEquals("tratamiento", violations.stream().findFirst().get().getPropertyPath().toString());
    }

    @Test
    public void deberiaSerInvalidoAgendaNula() {
        Tratamiento tratamiento = new Tratamiento(null);
        tratamiento.setFechaCreacion(new Date());
        tratamiento.setParticular(false);
        tratamiento.setFinalizado(Boolean.FALSE);
        tratamiento.setCantidadDeSesiones(new Short("10"));
        tratamiento.setTipoDeTratamiento(null);
        tratamiento.setParticular(true);
        tratamiento.setAccidentetrabajo(Boolean.FALSE);

        Sesion sesion = new Sesion();
        sesion.setFechaHoraInicio(new Date());
        sesion.setTratamiento(tratamiento);
        sesion.setAgenda(null);

        Set<ConstraintViolation<Sesion>> violations = this.validator.validate(sesion);

//        System.out.println(violations.toString());
        assertEquals(1, violations.size());
        assertEquals("agenda", violations.stream().findFirst().get().getPropertyPath().toString());
    }
}
