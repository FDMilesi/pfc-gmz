package ar.edu.utn.frsf.kinesio.gestores;

import ar.edu.utn.frsf.kinesio.entities.Agenda;
import ar.edu.utn.frsf.kinesio.entities.Sesion;
import ar.edu.utn.frsf.kinesio.entities.Tratamiento;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PostLoad;
import javax.persistence.PrePersist;

/**
 *
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

    public Sesion initSesionFromAgenda(Date date, Agenda agenda) {
        Sesion sesion = new Sesion();
        sesion.setAgenda(agenda);
        sesion.setFechaHoraInicio(date);
        sesion.setTranscurrida(false);
        sesion.setCuenta(true);
        return sesion;
    }

    public Sesion initSesionFromTratamiento(Tratamiento tratamiento) {
        Sesion sesion = new Sesion();
        sesion.setTratamiento(tratamiento);
        sesion.setTranscurrida(false);
        sesion.setCuenta(true);
        sesion.setNumeroDeSesion(this.getSiguienteNumeroDeSesion(tratamiento));
        return sesion;
    }

    public Short getSiguienteNumeroDeSesion(Tratamiento tratamiento) {
        return calcularSiguienteNumeroDeSesion(getSesionesByTratamiento(tratamiento));
    }

    /**
     * Dada una lista de sesiones calcula el número de sesión que le
     * correspondería a una sesión en caso de ser agregada a la lista. Para ello
     * toma el número de la última sesión en la lista y le adiciona 1 (uno).
     *
     * @param sesiones
     * @return: El número de sesión que tendría la siguiente sesión en la lista.
     */
    public Short calcularSiguienteNumeroDeSesion(List<Sesion> sesiones) {
        Short numeroUltimaSesion;
        if (sesiones.isEmpty()) {
            numeroUltimaSesion = (short) 0;
        } else {
            numeroUltimaSesion = sesiones.get(sesiones.size() - 1).getNumeroDeSesion();
        }

        return (short) (numeroUltimaSesion + 1);
    }

    public Sesion editAndReturn(Sesion sesion) {
        return getEntityManager().merge(sesion);
    }

    /**
     * Método ejecutado antes de que una sesión sea persistida
     *
     * @param sesion
     */
    @PrePersist
    void onPrePersist(Sesion s) {
        //Seteo la duración de la sesión creada en base al tipo de tratamiento
        s.setDuracion(s.getTratamiento().getTipoDeTratamiento().getDuracion());
        colorearSesiones(s);
    }

    @PostLoad
    void onPostLoad(Sesion s) {
        colorearSesiones(s);
    }

    public List<Sesion> getSesionesByTratamiento(Tratamiento tratamiento) {
        return getEntityManager().createNamedQuery("Sesion.findByTratamiento")
                .setParameter("tratamiento", tratamiento).getResultList();
    }

    public List<Sesion> getSesionesByAgenda(Agenda agenda) {
        return getEntityManager().createNamedQuery("Sesion.findByAgenda")
                .setParameter("agenda", agenda).getResultList();
    }

    private void colorearSesiones(Sesion s) {
        switch (s.getTratamiento().getTipoDeTratamiento().getId()) {
            case 1:
                s.setStyleClass("fisiokinesioterapia");
                break;
            case 2:
                s.setStyleClass("kinesioterapiaRespiratoria");
                break;
            case 3:
                s.setStyleClass("drenajeLinfatico");
                break;
            case 4:
                s.setStyleClass("rehabilitacionNeurologica");
                break;
            case 5:
                s.setStyleClass("esteticaElectrodos");
                break;
            case 6:
                s.setStyleClass("esteticaMasajes");
                break;
            case 7:
                s.setStyleClass("gimnasiaTerapeutica");
                break;
        }
    }
}
