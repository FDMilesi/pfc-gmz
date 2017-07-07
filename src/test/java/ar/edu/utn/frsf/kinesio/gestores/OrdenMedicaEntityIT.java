package ar.edu.utn.frsf.kinesio.gestores;

import ar.edu.utn.frsf.kinesio.entities.ObraSocial;
import ar.edu.utn.frsf.kinesio.entities.OrdenMedica;
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

public class OrdenMedicaEntityIT {

    private OrdenMedicaFacade ordenMedicaFacade;
    private EntityTransaction transaction;

    private Validator validator;

    public OrdenMedicaEntityIT() {
    }

    @Before
    public void setUp() {
        ValidatorFactory vf = Validation.buildDefaultValidatorFactory();
        this.validator = vf.getValidator();
        this.ordenMedicaFacade = new OrdenMedicaFacade();
        this.ordenMedicaFacade.setEm(Persistence.createEntityManagerFactory("integration").createEntityManager());
        this.transaction = ordenMedicaFacade.getEntityManager().getTransaction();
    }

    @Test
    public void deberiaValidarYPersistirOrdenMedica() {
        ObraSocial obraSocial = new ObraSocial();
        obraSocial.setNombre("Una Obra Social");

        Paciente paciente = new Paciente();
        paciente.setFechaAlta(new Date());
        paciente.setNombre("Un");
        paciente.setApellido("Paciente");
        paciente.setDni("4566454");
        paciente.setNroAfiliadoOS("Numero afiliado");
        paciente.setObraSocial(obraSocial);

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

        OrdenMedica ordenMedica = new OrdenMedica();
        ordenMedica.setFechaCreacion(new Date());
        ordenMedica.setPresentadaAlCirculo(false);
        ordenMedica.setTratamiento(tratamiento);
        ordenMedica.setObraSocial(tratamiento.getPaciente().getObraSocial());
        ordenMedica.setNumeroAfiliadoPaciente(tratamiento.getPaciente().getNroAfiliadoOS());
        ordenMedica.setAutorizada(Boolean.FALSE);
        ordenMedica.setCantidadDeSesiones(new Short("5"));

        Set<ConstraintViolation<OrdenMedica>> violations = this.validator.validate(ordenMedica);

//        System.out.println(violations.toString());
        assertTrue(violations.isEmpty());

        this.transaction.begin();

        this.ordenMedicaFacade.getEntityManager().persist(obraSocial);
        this.ordenMedicaFacade.getEntityManager().persist(paciente);
        this.ordenMedicaFacade.getEntityManager().persist(tipoDeTratamiento);
        this.ordenMedicaFacade.getEntityManager().persist(tratamiento);

        this.ordenMedicaFacade.create(ordenMedica);

        List<OrdenMedica> listaOrdenes = ordenMedicaFacade.findAll();
        assertNotNull(listaOrdenes);
        assertEquals(listaOrdenes.size(), 1);

        this.transaction.rollback();
    }

    @Test
    public void deberiaSerInvalidoTratamientoNulo() {
        ObraSocial obraSocial = new ObraSocial();
        obraSocial.setNombre("Una Obra Social");

        OrdenMedica ordenMedica = new OrdenMedica();
        ordenMedica.setFechaCreacion(new Date());
        ordenMedica.setPresentadaAlCirculo(false);
        ordenMedica.setTratamiento(null);
        ordenMedica.setObraSocial(obraSocial);
        ordenMedica.setNumeroAfiliadoPaciente("1234");
        ordenMedica.setAutorizada(Boolean.FALSE);
        ordenMedica.setCantidadDeSesiones(new Short("5"));

        Set<ConstraintViolation<OrdenMedica>> violations = this.validator.validate(ordenMedica);

//        System.out.println(violations.toString());
        assertEquals(1, violations.size());
        assertEquals("tratamiento", violations.stream().findFirst().get().getPropertyPath().toString());
    }

    @Test
    public void deberiaSerInvalidoObraSocialNula() {
        Tratamiento tratamiento = new Tratamiento(null);
        tratamiento.setTipoDeTratamiento(null);

        OrdenMedica ordenMedica = new OrdenMedica();
        ordenMedica.setFechaCreacion(new Date());
        ordenMedica.setPresentadaAlCirculo(false);
        ordenMedica.setTratamiento(tratamiento);
        ordenMedica.setObraSocial(null);
        ordenMedica.setNumeroAfiliadoPaciente("1234");
        ordenMedica.setAutorizada(Boolean.FALSE);
        ordenMedica.setCantidadDeSesiones(new Short("5"));

        Set<ConstraintViolation<OrdenMedica>> violations = this.validator.validate(ordenMedica);

//        System.out.println(violations.toString());
        assertEquals(1, violations.size());
        assertEquals("obraSocial", violations.stream().findFirst().get().getPropertyPath().toString());
    }
}
