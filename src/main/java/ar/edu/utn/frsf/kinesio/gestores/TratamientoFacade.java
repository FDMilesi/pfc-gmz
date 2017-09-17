package ar.edu.utn.frsf.kinesio.gestores;

import ar.edu.utn.frsf.kinesio.entities.Paciente;
import ar.edu.utn.frsf.kinesio.entities.Tratamiento;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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
            this.setearSesionesRealizadas(tratamiento);
        }
        return lista;
    }

    private void setearSesionesRealizadas(Tratamiento tratamiento) {
        tratamiento.setSesionesRealizadas(sesionFacade.countSesionesByTratamientoQueCuentanTranscurridas(tratamiento));
    }

    public List<Tratamiento> getTratamientosEnCursoAndNoParticulares() {
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

    public boolean esOSIapos(Paciente paciente) {
        if (paciente.getObraSocial() == null) {
            return false;
        } else {
            return (paciente.getObraSocial().getId() == 35)
                    || (paciente.getObraSocial().getId() == 36)
                    || (paciente.getObraSocial().getId() == 37)
                    || (paciente.getObraSocial().getId() == 38)
                    || (paciente.getObraSocial().getId() == 39);
        }
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
    public boolean esValidaCantidadDeSesionesNoTranscurridas(Tratamiento tratamiento) {
        int cantidadSesionesNoTranscurridas = sesionFacade.countSesionesByTratamientoNoTranscurridas(tratamiento);

        return cantidadSesionesNoTranscurridas == 0;
    }

    public boolean esValidaCantidadSesionesCubiertas(Tratamiento tratamiento) {
        return calcularDeudaDeOrdenes(tratamiento, false) >= 0;
    }

    public boolean esValidaCantidadSesionesCubiertasAlFinalizar(Tratamiento tratamiento) {
        return calcularDeudaDeOrdenes(tratamiento, true) >= 0;
    }

    public boolean sonValidasTodasLasOrdenes(Tratamiento trat) {
        return ordenMedicaFacade.estanTodasLasOrdenesAutorizadas(ordenMedicaFacade.getOrdenesByTratamiento(trat));
    }

    public List<Tratamiento> getTratamientosByPacienteEnCurso(Paciente paciente) {
        return getEntityManager()
                .createNamedQuery("Tratamiento.findByPacienteEnCurso")
                .setParameter("paciente", paciente)
                .getResultList();
    }

    public boolean puedoEliminarTratamiento(Tratamiento selected) {
        return ((Number) getEntityManager().createNamedQuery("OrdenMedica.countByTratamiento")
                .setParameter("tratamiento", selected).getSingleResult()).intValue() == 0;
    }

    public List<Tratamiento> getTratamientosConSesionesAFavor(Tratamiento tratamiento) {
        return getEntityManager()
                .createNamedQuery("Tratamiento.tratmientosConSesionesAFavor")
                .setParameter("paciente", tratamiento.getPaciente())
                .setParameter("tipoDeTratamiento", tratamiento.getTipoDeTratamiento())
                .getResultList();
    }

    protected void setEm(EntityManager em) {
        this.em = em;
    }

    @Override
    public void create(Tratamiento tratamiento) {
        if (!tratamiento.getTipoDeTratamiento().isCubiertoPorObraSocial()) {
            tratamiento.setParticular(true);
        }
        super.create(tratamiento);
    }

    private int calcularDeudaDeOrdenes(Tratamiento tratamiento, boolean contarSoloSesionesRealizadas) {
        //Me fijo si uso sesiones a favor
        int sesionesAFavorUsadas = 0;
        if (tratamiento.getTratamientoAsociado() != null) {
            sesionesAFavorUsadas = (int) tratamiento.getTratamientoAsociado().getSesionesAFavor();
        }

        int cantSesiones = 0;
        if (contarSoloSesionesRealizadas) {
            //Cargo el numero de sesiones a las que asistio el paciente.
            if (tratamiento.getSesionesRealizadas() == 0) {
                this.setearSesionesRealizadas(tratamiento);
            }
            cantSesiones = tratamiento.getSesionesRealizadas();
        } else {
            cantSesiones = tratamiento.getCantidadDeSesiones();
        }

        //Busco las cant de sesiones en ordenes
        int sesionesEnOrdenes = (int) ordenMedicaFacade.sumatoriaSesionesDeOrdenes(tratamiento);

        //Calculo si sobran o faltan, o sale derecho
        int calculo = (sesionesEnOrdenes + sesionesAFavorUsadas) - cantSesiones;

        return calculo;
    }

    @Override
    public void edit(Tratamiento tratamiento) {
        if (!tratamiento.getTipoDeTratamiento().isCubiertoPorObraSocial()) {
            tratamiento.setParticular(true);
        }

        if (tratamiento.getTratamientoAsociado() != null) {
            tratamiento.getTratamientoAsociado().setSesionesAFavorUsadas(Boolean.TRUE);
            super.edit(tratamiento.getTratamientoAsociado());
        }

        if (!tratamiento.getParticular() && tratamiento.getFinalizado()) {

            int calculo = this.calcularDeudaDeOrdenes(tratamiento, true);

            //calculo las sesiones a favor y configuro
            //Me sobran sesiones cubiertas, es decir, quedan sesiones a favor
            if (calculo > 0) {
                tratamiento.setSesionesAFavor((short) calculo);
                tratamiento.setSesionesAFavorUsadas(Boolean.FALSE);
            }

            //Si salio derecho
            if (calculo == 0) {
                tratamiento.setSesionesAFavor((short) 0);
                tratamiento.setSesionesAFavorUsadas(Boolean.TRUE);
            }

            //Si da menor que cero lanzo excepcion, porque debio haberse validado antes de llega aqui
            if (calculo < 0) {
                tratamiento.setFinalizado(Boolean.FALSE);
                throw new EJBException("Se intento finalizar un tratamiento que no posee todas sus sesiones cubiertas");
            }

        }

        super.edit(tratamiento);
    }

}
