/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.edu.utn.frsf.kinesio.gestores;

import ar.edu.utn.frsf.kinesio.controllers.SesionController;
import ar.edu.utn.frsf.kinesio.entities.Agenda;
import ar.edu.utn.frsf.kinesio.entities.Sesion;
import ar.edu.utn.frsf.kinesio.entities.Tratamiento;
import java.util.Date;
import java.util.List;
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

    public Sesion initSesion(Date date, Agenda agenda) {
        Sesion sesion = new Sesion();
        sesion.setFechaHoraInicio(date);
        return sesion;
    }

    public Sesion initSesionFromTratamiento(Tratamiento tratamiento) {
        Sesion sesion = new Sesion();
        sesion.setTratamiento(tratamiento);
        sesion.setNumeroDeSesion(this.calcularNumeroDeSesion(sesion));
        return sesion;
    }

    public Short calcularNumeroDeSesion(Sesion sesion) {
        List<Sesion> sesiones = this.getSesionesByTratamiento(sesion.getTratamiento());
        Short numeroUltimaSesion;
        if (sesiones.isEmpty()) {
            numeroUltimaSesion = new Short("0");
        } else {
            numeroUltimaSesion = sesiones.get(sesiones.size() - 1).getNumeroDeSesion();
        }
        
        Short nuevo = (short) (numeroUltimaSesion + 1);
        return nuevo;
    }

    public Sesion editAndReturn(Sesion sesion) {
        sesion.setDuracion(sesion.getTratamiento().getTipoDeTratamiento().getDuracion());
        return getEntityManager().merge(sesion);
    }

    public List<Sesion> getSesionesByTratamiento(Tratamiento tratamiento) {
        return getEntityManager()
                .createNamedQuery("Sesion.findByTratamiento")
                .setParameter("tratamiento", tratamiento)
                .getResultList();
    }

    public List<Sesion> getSesionesByAgenda(Agenda agenda) {
        return getEntityManager()
                .createNamedQuery("Sesion.findByAgenda")
                .setParameter("agenda", agenda)
                .getResultList();
    }
}
