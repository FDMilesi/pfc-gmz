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
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PostLoad;

@Stateless
public class SesionFacade extends AbstractFacade<Sesion> {

    public static final int ERROR_EDIT_SESION_FECHA_ANTERIOR = 1;
    public static final int ERROR_EXCEDE_TOPE_SESIONES_TRATAMIENTO = 2;
    public static final int ERROR_FECHA_SESION_FERIADO = 3;
    public static final int ERROR_EXCEDE_TOPE_SESIONES_ANIO = 4;

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
//        sesion.setNumeroDeSesion(this.getSiguienteNumeroDeSesion(tratamiento));
        return sesion;
    }

    public Short getNumeroDeSesion(Tratamiento tratamiento, Sesion nuevaSesion) {
        return this.getNumeroDeSesion(getSesionesByTratamiento(tratamiento), nuevaSesion);
    }

    /**
     * Dada una lista de sesiones y una sesion que está en creación, calcula el
     * número de sesión que le correspondería a la nueva sesión en caso de
     * agregarse a la lista ordenadamente.
     *
     * @param sesiones
     * @param nuevaSesion
     * @return: El número de sesión que tendría la siguiente sesión en la lista.
     */
    public Short getNumeroDeSesion(List<Sesion> sesiones, Sesion nuevaSesion) {

        if (nuevaSesion.getFechaHoraInicio() == null) {
            return (short) 0;
        }

        int index = 0;
        for (Sesion sesion : sesiones) {
            //Si la nueva sesion se ubica antes de alguna de las ya existentes
            if (nuevaSesion.getFechaHoraInicio().compareTo(sesion.getFechaHoraInicio()) < 0) {
                //Retorno su mismo numero
                return sesion.getNumeroDeSesion();
            }
            index++;
        }

        //Si nunca entro en el if anterior, miro las otras cosas que pueden pasar
        //Si la lista esta vacía
        if (index == 0) {
            return (short) 1;
        }

        //Si termine el loop en el último elemento (el loop termina en indice ultimo + 1)
        if (index == sesiones.size()) {
            //Retorno el numero de la ultima + 1
            return (short) (sesiones.get(index - 1).getNumeroDeSesion().intValue() + 1);
        }

        //Sino retorno null porque algo raro hay
        return null;
    }

    public int validarCreacionSesion(Sesion sesion, Tratamiento tratamiento) {

        if (!puedoAgregarSesion(tratamiento)) {
            return ERROR_EXCEDE_TOPE_SESIONES_TRATAMIENTO;
        }

        if (esDiaFeriado(sesion.getFechaHoraInicio())) {
            return ERROR_FECHA_SESION_FERIADO;
        }

        return 0;
    }

    public int validarFechaEdicionSesion(Sesion sesion, Date nuevaFecha) {
        
        if (!nuevaFecha.equals(sesion.getFechaHoraInicio())) {
            if (nuevaFecha.before(new Date())) {
                return ERROR_EDIT_SESION_FECHA_ANTERIOR;
            }
        }
        
        if (esDiaFeriado(nuevaFecha)) {
            return ERROR_FECHA_SESION_FERIADO;
        }
        
        return 0;
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
        Sesion retorno = getEntityManager().merge(sesion);

        this.recalcularNumerosDeSesion(this.getSesionesByTratamiento(sesion.getTratamiento()));

        return retorno;
    }

    @Override
    public void remove(Sesion sesion) {
        super.remove(sesion);
        //Probar esto
        this.recalcularNumerosDeSesion(this.getSesionesByTratamiento(sesion.getTratamiento()));
    }

    private void recalcularNumerosDeSesion(List<Sesion> lista) {
        int i = 1;

        for (Sesion sesion : lista) {
            if (sesion.getNumeroDeSesion() == null) {//Si es null
                sesion.setNumeroDeSesion((short) i);
            } else if (sesion.getNumeroDeSesion() != (short) i) {//Si no es null pero es distintos
                sesion.setNumeroDeSesion((short) i);
            }
            i++;
        }
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

    public List<Object[]> getSesionesYContactoByRangoFechas(Date fechaDesde, Date fechaHasta) {
        return getEntityManager().createNamedQuery("Sesion.findByRangoFechas")
                .setParameter("fechaDesde", fechaDesde)
                .setParameter("fechaHasta", fechaHasta)
                .getResultList();
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
    //Crea y guarda las sesiones de carga masiva.
    public List<Sesion> cargaMasivaSesiones(Sesion sesionARepetir, Map<String, Date> diasYHorarios, int diasARepetir) {
        List<Sesion> sesionesCreadas = new ArrayList<>();
        LocalDate diaInicio;
        if (sesionARepetir.getFechaHoraInicio().before(new Date())) {
            diaInicio = LocalDate.now();
        } else {
            diaInicio = sesionARepetir.getFechaHoraInicio().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        }
        List<Date> listaDeFechas = this.getFechasParaCargaMasiva(diasYHorarios, diasARepetir, diaInicio);

        for (Date fecha : listaDeFechas) {
            Sesion sesion = new Sesion();
            sesion.setAgenda(sesionARepetir.getAgenda());
            sesion.setFechaHoraInicio(fecha);
            sesion.setTranscurrida(false);
            sesion.setCuenta(true);
            sesion.setTratamiento(sesionARepetir.getTratamiento());
            sesion.setDuracion(sesionARepetir.getDuracion());
            this.setSesionStyle(sesion);
            this.getEntityManager().persist(sesion);
            sesionesCreadas.add(sesion);
        }

        this.recalcularNumerosDeSesion(getSesionesByTratamiento(sesionARepetir.getTratamiento()));

        return sesionesCreadas;
    }

    /**
     * *
     * Retorna una lista con las fechas (dia y hora) en base a los datos
     * ingresados por el usuario para la caga masiva. Dada una lista de días de
     * la semana con un horario asociado (diasYHorarios), una cantidad de fechas
     * a obtener (diasARepetir) y un día de donde partir, va generarndo las
     * distintas fechas de las sesiones con el día y la hora solicitados por el
     * usuario
     *
     * @param diasYHorarios
     * @param diasARepetir
     * @param diaInicio
     * @return
     */
    private List<Date> getFechasParaCargaMasiva(Map<String, Date> diasYHorarios, int diasARepetir, LocalDate diaInicio) {
        int cantDiasAgregados = 0;
        int diasFuturos = 1;
        LocalDate diaFuturo;
        List<Date> listaDeFechas = new ArrayList<>();

        while (cantDiasAgregados < diasARepetir) {
            diaFuturo = diaInicio.plusDays(diasFuturos);
            
            //Si es un día de la semana de los seleccionados
            if (diasYHorarios.containsKey(diaFuturo.getDayOfWeek().name())) {
                
                //Cheuqeo si el dia es feriado. Tengo que pasar de LocalDate a Date
                if (esDiaFeriado(Date.from(diaFuturo.atStartOfDay(ZoneId.systemDefault()).toInstant()))){
                    //La logica es: saltear el dia si es feriado.
                    //Si se desea cambiar la logica ponerla aqui
                } else {
                    //Si no es feriado lo agrego y sumo uno a la cuenta de dias agregados
                    listaDeFechas
                        .add(this.calcularFechaSesionARepetir(diasYHorarios.get(diaFuturo.getDayOfWeek().name()),
                                diaFuturo));
                    cantDiasAgregados++;
                }
            }
            diasFuturos++;
        }
        return listaDeFechas;
    }

    /**
     * *
     * Setea a la nuevaFecha el horario horaQueQuiero. Se mantiene el día, mes y
     * año de nuevaFecha, pero se le coloca la hora y minutos de horaQueQuiero
     *
     * @param horaQueQuiero
     * @param nuevaFecha
     * @return
     */
    private Date calcularFechaSesionARepetir(Date horaQueQuiero, LocalDate nuevaFecha) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(horaQueQuiero);
        int hora = cal.get(Calendar.HOUR_OF_DAY);
        int minutos = cal.get(Calendar.MINUTE);
        LocalDateTime ldt = nuevaFecha.atTime(hora, minutos);
        return Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
    }

    private List<Date> listaFeriadosAsDateList() {
        return getEntityManager().createNamedQuery("Diaferiado.findAllDates").getResultList();
    }

    private boolean esDiaFeriado(Date date) {
        //Al date que me llega lo paso a calendar, le seteo el time a cero y 
        //luego veo si esta en lista de feriados
        Calendar calendarInstance = Calendar.getInstance();
        calendarInstance.setTime(date);
        calendarInstance.set(Calendar.HOUR_OF_DAY, 0);
        calendarInstance.set(Calendar.MINUTE, 0);
        calendarInstance.set(Calendar.SECOND, 0);
        calendarInstance.set(Calendar.MILLISECOND, 0);

        return this.listaFeriadosAsDateList().contains(calendarInstance.getTime());
    }
}
