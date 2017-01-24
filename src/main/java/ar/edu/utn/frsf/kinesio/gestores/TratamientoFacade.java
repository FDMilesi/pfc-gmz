package ar.edu.utn.frsf.kinesio.gestores;

import ar.edu.utn.frsf.kinesio.entities.Paciente;
import ar.edu.utn.frsf.kinesio.entities.Tratamiento;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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
    
    public List<Tratamiento> getTratamientosByPacienteContandoSesiones(Paciente paciente) {
        List<Tratamiento> lista = getEntityManager()
                .createNamedQuery("Tratamiento.findByPaciente")
                .setParameter("paciente", paciente).getResultList();
        for (Tratamiento tratamiento : lista) {
            tratamiento.setSesionesRealizadas(sesionFacade.countSesionesByTratamientoQueCuentanTranscurridas(tratamiento));
        }
        return lista;
    }
    
    public List<Tratamiento> getTratamientosEnCursoAndNoParticulares(){
        return getEntityManager()
                .createNamedQuery("Tratamiento.findByEnCursoAndNoParticular").getResultList();
    }
    
    public Tratamiento initTratamiento(Paciente paciente) {
        Tratamiento tratamiento = new Tratamiento(paciente);
        tratamiento.setFechaCreacion(new Date());
        tratamiento.setParticular(false);
        tratamiento.setFinalizado(Boolean.FALSE);
        return tratamiento;
    }
    
    public boolean esOSIapos(Paciente paciente){
        return (paciente.getObraSocial().getId() == 35) ||
                (paciente.getObraSocial().getId() == 36) ||
                (paciente.getObraSocial().getId() == 37) ||
                (paciente.getObraSocial().getId() == 38) ||
                (paciente.getObraSocial().getId() == 39);
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

    public List<Tratamiento> getTratamientosByPacienteEnCurso(Paciente paciente){
        return getEntityManager()
                .createNamedQuery("Tratamiento.findByPacienteEnCurso")
                .setParameter("paciente", paciente)
                .getResultList();
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

}
