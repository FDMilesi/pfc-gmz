package ar.edu.utn.frsf.kinesio.gestores;

import ar.edu.utn.frsf.kinesio.entities.Agenda;
import ar.edu.utn.frsf.kinesio.entities.Sesion;
import ar.edu.utn.frsf.kinesio.entities.Tratamiento;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PostLoad;

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
        return this.puedoAgregarSesiones(tratamiento, 1);
    }

    /**
     * Me dice si puedo agregar o no una determinada cantidad de sesiones al
     * tratamiento pasado como parámetro. Se invoca típicamente desde la carga
     * masiva de sesiones. (Notar que es private, no usado desde afuera aún).
     *
     * @param tratamiento
     * @param cantSesiones cantidad de sesiones que quiero ver si se pueden
     * agregar al tratamiento
     * @return
     */
    private boolean puedoAgregarSesiones(Tratamiento tratamiento, int cantSesiones) {
        short cantidadQueCuentanMasUno = (short) (this.countSesionesByTratamientoQueCuentan(tratamiento) + cantSesiones);
        //Si la cantiad de sesiones que cuentan más la cantiadad que deseo agregar es menor o igual a 
        //la cantidad seteada en el tratamiento, retorno true
        return Short.compare(cantidadQueCuentanMasUno, tratamiento.getCantidadDeSesiones()) <= 0;
    }

    public Sesion editAndReturn(Sesion sesion) {
        //valido que la sesión no se esté agregando a un tratamiento finalizado
        if (sesion.getTratamiento().getFinalizado()) {
            throw new EJBException("Error: el tratamiento se encuentra finalizado");
        }
        sesion.setDuracion(sesion.getTratamiento().getTipoDeTratamiento().getDuracion());
        this.setSesionStyle(sesion);
        return getEntityManager().merge(sesion);
    }

    @PostLoad
    void onPostLoad(Sesion s) {
        s.setStartDate((Date) s.getFechaHoraInicio().clone());
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

    public int countSesionesByTratamientoQueCuentanTranscurridas(Tratamiento tratamiento) {
        return ((Number) getEntityManager().createNamedQuery("Sesion.countByTratamientoQueCuentanTranscurridas")
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

    //Métodos para la carga masiva
    
    public List<Sesion> cargaMasivaSesiones(Sesion sesionARepetir, Map<String, Date> diasYHorarios, int diasARepetir) {
        List<Sesion> sesionesCreadas = new ArrayList<>();
        LocalDate diaInicio;
        if(sesionARepetir.getFechaHoraInicio().before(new Date())){
            diaInicio = LocalDate.now();
        }else{
            diaInicio = sesionARepetir.getFechaHoraInicio().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        }
        List<Date> listaDeFechas = this.getFechasParaCargaMasiva(diasYHorarios, diasARepetir, diaInicio);
        int siguienteNumeroDeSesion = this.getSiguienteNumeroDeSesion(sesionARepetir.getTratamiento()).intValue();

        for (Date fecha : listaDeFechas) {
            Sesion sesion = new Sesion();
            sesion.setAgenda(sesionARepetir.getAgenda());
            sesion.setFechaHoraInicio(fecha);
            sesion.setTranscurrida(false);
            sesion.setCuenta(true);
            sesion.setTratamiento(sesionARepetir.getTratamiento());
            sesion.setNumeroDeSesion((short) siguienteNumeroDeSesion);
            sesion.setDuracion(sesionARepetir.getDuracion());
            this.setSesionStyle(sesion);
            this.getEntityManager().persist(sesion);
            sesionesCreadas.add(sesion);
            siguienteNumeroDeSesion++;
        }
        return sesionesCreadas;
    }

    private List<Date> getFechasParaCargaMasiva(Map<String, Date> diasYHorarios, int diasARepetir, LocalDate diaInicio) {
        int count = 0;
        int diasFuturos = 1;
        LocalDate diaFuturo;
        List<Date> listaDeFechas = new ArrayList<>();

        while (count < diasARepetir) {
            diaFuturo = diaInicio.plusDays(diasFuturos);
            if (diasYHorarios.containsKey(diaFuturo.getDayOfWeek().name())) {
                
                listaDeFechas
                        .add(this.calcularFechaSesionARepetir(diasYHorarios.get(diaFuturo.getDayOfWeek().name()), 
                                diaFuturo));
                count++;
            }
            diasFuturos++;
        }
        return listaDeFechas;
    }
    
    //Fusiona la hora de la sesion a repetir con la fecha en que debe repetirse la sesión
    private Date calcularFechaSesionARepetir(Date horaQueQuiero, LocalDate nuevaFecha) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(horaQueQuiero);
        int hora = cal.get(Calendar.HOUR_OF_DAY);
        int minutos = cal.get(Calendar.MINUTE);
        LocalDateTime ldt = nuevaFecha.atTime(hora, minutos);
        return Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
    }
}
