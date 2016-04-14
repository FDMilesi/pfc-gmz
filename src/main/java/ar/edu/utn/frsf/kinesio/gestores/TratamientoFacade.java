/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.edu.utn.frsf.kinesio.gestores;

import ar.edu.utn.frsf.kinesio.entities.Paciente;
import ar.edu.utn.frsf.kinesio.entities.Sesion;
import ar.edu.utn.frsf.kinesio.entities.Tratamiento;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PostLoad;

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
    
    /**
     * Dado un valor cantidadDeSesiones, verifica que sea un valor válido para el tratamiento
     * recibido como parámetro. Para ello lo contrasta con la cantidad de sesiones del tratamiento.
     * @param tratamiento: tratamiento para el cual se chequea que la cantidadDeSesiones sea válida
     * @param cantidadDeSesiones: valor cuya validez es chequeada
     * @return: true en caso de ser válido el valor cantidadDeSesiones. Falso caso contrario.
     */
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
    
    /**
     * Método que escucha el evento PostLoad del ciclo de vida de la entidad Tratamiento.
     * @param tratamiento 
     */
    @PostLoad
    void onPostLoad(Tratamiento tratamiento){
        //Calculo el atributo sesionesRealizadas del tratamiento que se esta cargando.
        List<Sesion> sesiones = this.sesionFacade.getSesionesByTratamiento(tratamiento);
        int sesionesRealizadas = 0;
        for (Sesion s : sesiones) {
            if (s.getTranscurrida()){
                sesionesRealizadas ++;
            }
        }
        tratamiento.setSesionesRealizadas(sesionesRealizadas);
    }
    
}
