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
import javax.persistence.PrePersist;

/**
 *
 */
@Stateless
public class TratamientoFacade extends AbstractFacade<Tratamiento> {

    @PersistenceContext(unitName = "ar.edu.utn.frsf_kinesio_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @EJB
    private SesionFacade sesionFacade;
    @EJB
    private OrdenMedicaFacade ordenMedicaFacade;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public void refresh(Tratamiento tratamiento){
        this.getEntityManager().refresh(getEntityManager().merge(tratamiento));
    }
    
    public Tratamiento editAndReturn(Tratamiento tratamiento){
        return getEntityManager().merge(tratamiento);
    }
    
    public TratamientoFacade() {
        super(Tratamiento.class);
    }

    public List<Tratamiento> getTratamientosByPaciente(Paciente paciente) {
        return getEntityManager()
                .createNamedQuery("Tratamiento.findByPaciente")
                .setParameter("paciente", paciente).getResultList();
    }

    public Tratamiento initTratamiento(Paciente paciente) {
        Tratamiento tratamiento = new Tratamiento(paciente);
        tratamiento.setFechaCreacion(new Date());
        tratamiento.setParticular(false);
        return tratamiento;
    }

    /**
     * Dada una entidad Tratamiento y un valor cantidadDeSesiones, verifica que
     * la cantidadDeSesiones sea un valor válido para dicho tratamiento,
     * comparando el valor con la cantidad de sesiones del tratamiento.
     * @param tratamiento
     * @param cantidadDeSesiones
     * @return 
     */
    public boolean esValidaCantidadDeSesiones(Tratamiento tratamiento, Short cantidadDeSesiones) {
        return sesionFacade.getSesionesByTratamiento(tratamiento).size() <= cantidadDeSesiones.intValue();
    }
    
    public boolean esValidaCantidadSesionesCubiertas(Tratamiento trat){
        return Short.compare(trat.getCantidadDeSesiones(), ordenMedicaFacade.sumatoriaSesionesDeOrdenes(ordenMedicaFacade.getOrdenesByTratamiento(trat))) == 0;
    }

    /**
     * Método ejecutado antes de que un tratamiento sea persistido
     * @param tratamiento 
     */
    @PrePersist
    void onPrePersist(Tratamiento tratamiento) {
        if (!tratamiento.getTipoDeTratamiento().isCubiertoPorObraSocial()) {
            tratamiento.setParticular(true);
        }
    }

    /**
     * Método ejecutado luego de obtener un tratamiento desde la base de datos
     * Tratamiento.
     * @param tratamiento
     */
    @PostLoad
    void onPostLoad(Tratamiento tratamiento) {
        //Calculo el atributo sesionesRealizadas del tratamiento que se esta cargando.
        List<Sesion> sesiones = this.sesionFacade.getSesionesByTratamiento(tratamiento);
        int sesionesRealizadas = 0;
        for (Sesion s : sesiones) {
            if (s.getTranscurrida()) {
                sesionesRealizadas++;
            }
        }
        tratamiento.setSesionesRealizadas(sesionesRealizadas);
    }

}
