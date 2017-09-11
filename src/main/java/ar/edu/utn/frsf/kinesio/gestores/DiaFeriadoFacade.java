package ar.edu.utn.frsf.kinesio.gestores;

import ar.edu.utn.frsf.kinesio.entities.DiaFeriado;
import java.util.Calendar;
import java.util.Date;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class DiaFeriadoFacade extends AbstractFacade<DiaFeriado> {

    public static final int ERROR_EL_DIA_POSEE_SESIONES = 1;

    @PersistenceContext(unitName = "ar.edu.utn.frsf_kinesio_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public DiaFeriadoFacade() {
        super(DiaFeriado.class);
    }

    public int validarCreacionFeriado(Date fecha) {
        //La fecha me llega ya con hh:mm:ss en cero
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fecha);
        calendar.add(Calendar.DATE, 1);
        Date fechaHasta = calendar.getTime();

        int cantSesiones = ((Number) getEntityManager().createNamedQuery("Sesion.countByRangoFechaHoraInicio")
                .setParameter("fechaDesde", fecha)
                .setParameter("fechaHasta", fechaHasta)
                .getSingleResult()).intValue();

        if (cantSesiones > 0) {
            return ERROR_EL_DIA_POSEE_SESIONES;
        }
        return 0;
    }
}
