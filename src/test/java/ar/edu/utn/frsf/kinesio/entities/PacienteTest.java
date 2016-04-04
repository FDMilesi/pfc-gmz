/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.edu.utn.frsf.kinesio.entities;

import java.text.ParseException;
import java.util.Date;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 */
public class PacienteTest {
    
    public PacienteTest() {
    }
    
    @Before
    public void setUp() {
    }

    @Test
    public void edadCalculadaDebeSer24() throws ParseException {
        Paciente paciente = new Paciente();
        paciente.setFechaDeNacimiento(new Date());
        assertEquals("Si comparo con el día de hoy debería tener 0 años", "0", paciente.getEdad());
    }
    
}
