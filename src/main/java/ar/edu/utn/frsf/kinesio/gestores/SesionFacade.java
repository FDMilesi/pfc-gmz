package ar.edu.utn.frsf.kinesio.gestores;

import ar.edu.utn.frsf.kinesio.entities.Agenda;
import ar.edu.utn.frsf.kinesio.entities.Sesion;
import ar.edu.utn.frsf.kinesio.entities.Tratamiento;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PostLoad;
import javax.persistence.PostUpdate;
import javax.persistence.PrePersist;

/**
 *
 */
@Stateless
public class SesionFacade extends AbstractFacade<Sesion> {

    @PersistenceContext(unitName = "ar.edu.utn.frsf_kinesio_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    private StringBuilder styleClassDeLaSesion;

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

    /**
     * Valida si puede agregarse a un tratamiento una sesión más. La validación
     * se hace típicamente ante la creación de una sesión, o a partir de la
     * modificación del campo {@code cuenta}.
     *
     * @param tratamiento
     * @return
     */
    public boolean puedoAgregarSesion(Tratamiento tratamiento) {
        short cantidadQueCuentanMasUno = (short) (this.countSesionesByTratamientoQueCuentan(tratamiento) + 1);
        //Si la cantiad de sesiones que cuentan más uno es menor o igual a 
        //la cantidad seteada en el tratamiento, retorno true
        return Short.compare(cantidadQueCuentanMasUno, tratamiento.getCantidadDeSesiones()) <= 0;
    }

    public Sesion editAndReturn(Sesion sesion) {
        //valido que la sesión no se esté agregando a un tratamiento finalizado
        if (sesion.getTratamiento().getFinalizado()) {
            throw new EJBException("Error: el tratamiento se encuentra finalizado");
        }
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
    }

    @PostLoad
    void onPostLoad(Sesion s) {
        s.setStartDate((Date) s.getFechaHoraInicio().clone());
        setSesionStyle(s);
    }

    @PostUpdate
    void onPostUpdate(Sesion s) {
        setSesionStyle(s);
    }

    public List<Sesion> getSesionesByTratamiento(Tratamiento tratamiento) {
        return getEntityManager().createNamedQuery("Sesion.findByTratamiento")
                .setParameter("tratamiento", tratamiento).getResultList();
    }

    public int countSesionesByTratamientoQueCuentan(Tratamiento tratamiento) {
        return ((Number) getEntityManager().createNamedQuery("Sesion.countByTratamientoQueCuentan")
                .setParameter("tratamiento", tratamiento).getSingleResult()).intValue();
    }

    public int countSesionesByTratamientoNoTranscurridas(Tratamiento tratamiento) {
        return ((Number) getEntityManager().createNamedQuery("Sesion.countByTratamientoNoTranscurridas")
                .setParameter("tratamiento", tratamiento).getSingleResult()).intValue();
    }

    public List<Sesion> getSesionesByAgenda(Agenda agenda) {
        return getEntityManager().createNamedQuery("Sesion.findByAgenda")
                .setParameter("agenda", agenda).getResultList();
    }

    private void setIconToSesion(Sesion s) {
        if (!s.getCuenta()) {
            styleClassDeLaSesion.append("sesionNoCuenta");
        } else if (s.getTranscurrida()) {
            styleClassDeLaSesion.append("sesionTranscurrida");
        }
    }

    private void colorearSesion(Sesion s) {
        switch (s.getTratamiento().getTipoDeTratamiento().getId()) {
            case 1:
                styleClassDeLaSesion.append("fisiokinesioterapia");
                break;
            case 2:
                styleClassDeLaSesion.append("kinesioterapiaRespiratoria");
                break;
            case 3:
                styleClassDeLaSesion.append("drenajeLinfatico");
                break;
            case 4:
                styleClassDeLaSesion.append("rehabilitacionNeurologica");
                break;
            case 5:
                styleClassDeLaSesion.append("esteticaElectrodos");
                break;
            case 6:
                styleClassDeLaSesion.append("esteticaMasajes");
                break;
            case 7:
                styleClassDeLaSesion.append("gimnasiaTerapeutica");
                break;
        }
    }

    private void setSesionStyle(Sesion s) {
        this.styleClassDeLaSesion = new StringBuilder();
        colorearSesion(s);
        styleClassDeLaSesion.append(" ");
        setIconToSesion(s);
        s.setStyleClass(styleClassDeLaSesion.toString());
    }

    public void marcarSesionesTranscurridas() {
        em.createQuery("UPDATE Sesion s SET s.transcurrida = TRUE WHERE s.transcurrida = FALSE and s.fechaHoraInicio < CURRENT_TIMESTAMP").executeUpdate();
    }

    public void cargaMasivaSesiones(Sesion sesionARepetir, boolean lun, boolean mar, boolean mie, boolean jue, boolean vie, int diasARepetir) {
        List<LocalDate> listaDeFechas = this.getFechasParaCargaMasiva(lun, mar, mie, jue, vie, diasARepetir);

        for (LocalDate fecha : listaDeFechas) {
            Sesion sesion = new Sesion();
            sesion.setAgenda(sesionARepetir.getAgenda());
            sesion.setFechaHoraInicio(this.localDateToDate(sesionARepetir.getFechaHoraInicio(), fecha));
            sesion.setTranscurrida(false);
            sesion.setCuenta(true);
            sesion.setTratamiento(sesionARepetir.getTratamiento());
            sesion.setNumeroDeSesion(this.getSiguienteNumeroDeSesion(sesionARepetir.getTratamiento()));
        }
    }

    //Fusiona la hora de la sesion a repetir con una fecha de la lista de fechas que me devolvio "getFechasParaCargaMasiva()"
    private Date localDateToDate(Date d, LocalDate ld) {
        java.util.Date date = java.sql.Date.valueOf(ld);
        date.setTime(d.getTime());
        return date;
    }

    private List<LocalDate> getFechasParaCargaMasiva(boolean lun, boolean mar, boolean mie, boolean jue, boolean vie, int diasARepetir) {
        int count = 0;
        int diasFuturos = 1;
        LocalDate diaFut;
        List<LocalDate> listaDeFechas = new ArrayList<>();

        while (count < diasARepetir) {
            diaFut = LocalDate.now().plusDays(diasFuturos);

            switch (diaFut.getDayOfWeek()) {
                case MONDAY:
                    if (lun) {
                        listaDeFechas.add(diaFut);
                        count++;
                    }
                    break;
                case TUESDAY:
                    if (mar) {
                        listaDeFechas.add(diaFut);
                        count++;
                    }
                    break;
                case WEDNESDAY:
                    if (mie) {
                        listaDeFechas.add(diaFut);
                        count++;
                    }
                    break;
                case THURSDAY:
                    if (jue) {
                        listaDeFechas.add(diaFut);
                        count++;
                    }
                    break;
                case FRIDAY:
                    if (vie) {
                        listaDeFechas.add(diaFut);
                        count++;
                    }
                    break;
            }
            diasFuturos++;
        }
        return listaDeFechas;
    }
}
