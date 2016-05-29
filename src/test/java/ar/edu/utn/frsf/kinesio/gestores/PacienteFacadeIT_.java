/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.edu.utn.frsf.kinesio.gestores;

import ar.edu.utn.frsf.kinesio.entities.Paciente;
import java.util.List;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author fer_0
 */
public class PacienteFacadeIT_ {

    private PacienteFacade pacienteFacade;
    private EntityTransaction transaction;

    public PacienteFacadeIT_() {
    }

    @Before
    public void setUp() {
        this.pacienteFacade = new PacienteFacade();
        this.pacienteFacade.setEm(Persistence.createEntityManagerFactory("integration").createEntityManager());
        this.transaction = pacienteFacade.getEntityManager().getTransaction();
        
    }

    @Test
    public void testPacientePersistence() {
        Paciente paciente = pacienteFacade.initPaciente();
        paciente.setNombre("Ramon");
        paciente.setApellido("diaz");
        paciente.setDni("45236654");
        this.transaction.begin();
        this.pacienteFacade.create(paciente);
        this.transaction.commit();
        List<Paciente> listaPacientes = pacienteFacade.findAll();
        assertNotNull(listaPacientes);
        assertEquals(listaPacientes.size(), 1);
    }

}
