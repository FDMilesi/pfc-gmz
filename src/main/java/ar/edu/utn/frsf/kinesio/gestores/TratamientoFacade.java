/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.edu.utn.frsf.kinesio.gestores;

import ar.edu.utn.frsf.kinesio.entities.Paciente;
import ar.edu.utn.frsf.kinesio.entities.Tratamiento;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author fer_0
 */
@Stateless
public class TratamientoFacade extends AbstractFacade<Tratamiento> {

    @PersistenceContext(unitName = "ar.edu.utn.frsf_kinesio_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @EJB
    private SesionFacade sesionFacade;
    
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public TratamientoFacade() {
        super(Tratamiento.class);
    }

    public List<Tratamiento> getTratamientosByPaciente(Paciente paciente) {
        return getEntityManager()
                .createNamedQuery("Tratamiento.findByPaciente")
                .setParameter("paciente", paciente)
                .getResultList();
    }

    public Tratamiento initTratamiento(Paciente paciente) {
        Tratamiento tratamiento = new Tratamiento(paciente);
        tratamiento.setFechaCreacion(new Date());
        tratamiento.setParticular(false);
        return tratamiento;
    }
    
    public boolean esValidaCantidadDeSesiones(Tratamiento tratamiento, Short cantidadDeSesiones){
        return sesionFacade.getSesionesByTratamiento(tratamiento).size() <= cantidadDeSesiones.intValue();
    }
    
    @Override
    public void edit(Tratamiento tratamiento){
        if(!tratamiento.getTipoDeTratamiento().isCubiertoPorObraSocial()){
            tratamiento.setParticular(true);
        }
        super.edit(tratamiento);
    }
    
}
