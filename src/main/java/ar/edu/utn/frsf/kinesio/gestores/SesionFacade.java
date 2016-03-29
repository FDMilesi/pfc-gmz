/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.edu.utn.frsf.kinesio.gestores;

import ar.edu.utn.frsf.kinesio.entities.Agenda;
import ar.edu.utn.frsf.kinesio.entities.Sesion;
import java.util.Date;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Fran
 */
@Stateless
public class SesionFacade extends AbstractFacade<Sesion> {

    @PersistenceContext(unitName = "ar.edu.utn.frsf_kinesio_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SesionFacade() {
        super(Sesion.class);
    }
    
    public Sesion initSesion(Date date, Agenda agenda){
        Sesion sesion = new Sesion();
        sesion.setAgenda(agenda);
        sesion.setFechaHoraInicio(date);
        return sesion;
    }

    public Sesion editAndReturn(Sesion entity) {
        return getEntityManager().merge(entity);
    }

}
