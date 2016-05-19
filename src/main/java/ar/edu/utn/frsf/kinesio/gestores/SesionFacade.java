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
        sesion.setNumeroDeSesion(this.calcularNumeroDeSesion(sesion));
        return sesion;
    }

    /**
     * Dada una sesión, calcula el número de sesión que le corresponde según el
     * tratamiento al que pertenezca. Para ello busca la última sesión del
     * tratamiento al que pertenece la sesión recibida, se le extrae el número
     * de sesión, y se lo incrementa en 1 (uno).
     *
     * @param sesion: Sesión sin número de sesión, a la cual se le calculará
     * dicho número.
     * @return: El número de sesión que le corresponde a la sesión recibida como
     * parámetro.
     */
    public Short calcularNumeroDeSesion(Sesion sesion) {
        List<Sesion> sesiones = this.getSesionesByTratamiento(sesion.getTratamiento());
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
     * @param sesion
     */
    @PrePersist
    void onPrePersist(Sesion s) {
        //Seteo la duración de la sesión creada en base al tipo de tratamiento
        s.setDuracion(s.getTratamiento().getTipoDeTratamiento().getDuracion());
        colorearSesiones(s);
    }

    @PostLoad
    void onPostLoad(Sesion s){
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
    
    private void colorearSesiones(Sesion s){
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
