package ar.edu.utn.frsf.kinesio.gestores;

import ar.edu.utn.frsf.kinesio.entities.Paciente;
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

public class PacienteEntityIT {

    private PacienteFacade pacienteFacade;
    private EntityTransaction transaction;

    private Validator validator;

    public PacienteEntityIT() {
    }

    @Before
    public void setUp() {
        ValidatorFactory vf = Validation.buildDefaultValidatorFactory();
        this.validator = vf.getValidator();
        this.pacienteFacade = new PacienteFacade();
        this.pacienteFacade.setEm(Persistence.createEntityManagerFactory("integration").createEntityManager());
        this.transaction = pacienteFacade.getEntityManager().getTransaction();
    }

    @Test
    public void deberiaValidarYPersistirPaciente() {
        Paciente paciente = pacienteFacade.initPaciente();
        paciente.setNombre("Un");
        paciente.setApellido("Paciente");
        paciente.setDni("4566454");
        paciente.setId(1);

        Set<ConstraintViolation<Paciente>> violations = this.validator.validate(paciente);

//        System.out.println(violations.toString());

        assertTrue(violations.isEmpty());

        this.transaction.begin();
        this.pacienteFacade.create(paciente);

        List<Paciente> listaPacientes = pacienteFacade.findAll();
        assertNotNull(listaPacientes);
        assertEquals(listaPacientes.size(), 1);

        this.transaction.rollback();
    }
}
