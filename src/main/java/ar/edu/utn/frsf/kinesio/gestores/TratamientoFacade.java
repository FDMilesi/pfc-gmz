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
     * la cantidadDeSesiones sea un valor válido para dicho tratamiento. Para
     * eso compara el valor cantidadDeSesiones con dos cosas: con la cantidad de
     * sesiones QUE CUENTAN del tratamiento y con la cantidad de sesiones
     * cubiertas por ordenes.
     *
     * @param tratamiento
     * @param cantidadDeSesiones
     * @return
     */
    public boolean esValidaCantidadDeSesiones(Tratamiento tratamiento, Short cantidadDeSesiones) {
        boolean validaSegunSesiones;
        boolean validaSegunOrdenes = true;
        //valido segun las sesiones
        int cantidadSesionesQueCuentan = sesionFacade.countSesionesByTratamientoQueCuentan(tratamiento);
        validaSegunSesiones = cantidadSesionesQueCuentan <= cantidadDeSesiones.intValue();
        
        //Si el tratamiento es por obra social, valido las ordenes.
        if (!tratamiento.getParticular()) {
            validaSegunOrdenes
                    = Short.compare(cantidadDeSesiones, ordenMedicaFacade.sumatoriaSesionesDeOrdenes(tratamiento)) >= 0;
        }

        return validaSegunOrdenes && validaSegunSesiones;
    }
    
    //Valido que la cantidad de sesiones que no transcurrieron sea cero, es decir, que no haya sesiones pendientes.
    public boolean esValidaCantidadDeSesionesNoTranscurridas(Tratamiento tratamiento){
        int cantidadSesionesNoTranscurridas = sesionFacade.countSesionesByTratamientoNoTranscurridas(tratamiento);
        
        return cantidadSesionesNoTranscurridas == 0;
    }
    
    public boolean esValidaCantidadSesionesCubiertas(Tratamiento tratamiento){
        return Short.compare(tratamiento.getCantidadDeSesiones(), ordenMedicaFacade.sumatoriaSesionesDeOrdenes(tratamiento)) == 0;
    }
    
    public boolean sonValidasTodasLasOrdenes(Tratamiento trat){
        return ordenMedicaFacade.estanTodasLasOrdenesAutorizadas(ordenMedicaFacade.getOrdenesByTratamiento(trat));
    }

    /**
     * Método ejecutado antes de que un tratamiento sea persistido
     *
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
     *
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
