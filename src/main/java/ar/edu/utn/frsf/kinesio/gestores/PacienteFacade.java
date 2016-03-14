/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.edu.utn.frsf.kinesio.gestores;

import ar.edu.utn.frsf.kinesio.entities.Paciente;
import java.util.Date;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author fer_0
 */
@Stateless
public class PacienteFacade extends AbstractFacade<Paciente> {

    @PersistenceContext(unitName = "ar.edu.utn.frsf_kinesio_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PacienteFacade() {
        super(Paciente.class);
    }
    
    public Paciente initPaciente(){
        Paciente paciente = new Paciente();
        paciente.setFechaAlta(new Date());
        return paciente;
    }
    
    /**
     * Crea un paciente. Luego de la creación del paciente realizo un 
     * {@code flush()} para que el paciente adquiera el {@code Id}, de modo que
     * los futuros usos del paciente puedan hacer correctamente el {@code merge}
     * @param paciente 
     */
    @Override
    public void create(Paciente paciente){
        super.create(paciente);
        getEntityManager().flush();
    }
}
