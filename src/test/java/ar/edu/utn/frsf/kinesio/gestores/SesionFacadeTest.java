/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.edu.utn.frsf.kinesio.gestores;

import ar.edu.utn.frsf.kinesio.entities.Sesion;
import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 */
public class SesionFacadeTest {

    private SesionFacade sesionFacade;
    List<Sesion> listaSesiones;

    public SesionFacadeTest() {
    }

    @Before
    public void setUp() {
        sesionFacade = new SesionFacade();
    }

    @Test
    public void debeRetornarUno() {
        listaSesiones = Arrays.asList();
        Short numero = sesionFacade.calcularSiguienteNumeroDeSesion(listaSesiones);
        assertEquals("Tratamiento sin sesiones debe comenzar por la sesión 1", new Short((short) 1), numero);
    }

    @Test
    public void debeRetornarTres() {
        listaSesiones = Arrays.asList(new Sesion((short) 2));
        assertEquals("Tratamiento con una sola sesión con nro 2 toma el numero siguiente", new Short((short) 3), sesionFacade.calcularSiguienteNumeroDeSesion(listaSesiones));
        listaSesiones = Arrays.asList(new Sesion((short) 1), new Sesion((short) 2));
        assertEquals("Tratamientos con sesión 1 y 2 continua por la 3", new Short((short) 3), sesionFacade.calcularSiguienteNumeroDeSesion(listaSesiones));
    }

}
