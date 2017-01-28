/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.edu.utn.frsf.kinesio.gestores;

import ar.edu.utn.frsf.kinesio.entities.ObraSocial;
import java.time.YearMonth;
import java.util.Calendar;
import java.util.Date;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Fran
 */
@Stateless
public class EstadisticasFacade {

    @PersistenceContext(unitName = "ar.edu.utn.frsf_kinesio_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }

    public void setEm(EntityManager em) {
        this.em = em;
    }

    public int CantidadPacientesPorMesIapos(int mes, int anio, ObraSocial iapos) {

        int cantidadDiasMes = YearMonth.of(anio, mes + 1).lengthOfMonth();

        Calendar cDsd = Calendar.getInstance();
        cDsd.set(anio, mes, 1);

        Calendar cHst = Calendar.getInstance();
        cHst.set(anio, mes, cantidadDiasMes);

        Date fechaDesde = cDsd.getTime();
        Date fechaHasta = cHst.getTime();

        return ((Number) getEntityManager()
                .createQuery("SELECT COUNT(DISTINCT s.tratamiento.paciente) FROM Sesion s WHERE s.transcurrida = TRUE AND s.tratamiento.paciente.obraSocial = :obraSocial AND s.fechaHoraInicio BETWEEN :desde AND :hasta")
                .setParameter("desde", fechaDesde)
                .setParameter("hasta", fechaHasta)
                .setParameter("obraSocial", iapos).getSingleResult()).intValue();
    }

    private int CantidadPacientesPorMesTotal(int mes, int anio) {

        int cantidadDiasMes = YearMonth.of(anio, mes + 1).lengthOfMonth();

        Calendar cDsd = Calendar.getInstance();
        cDsd.set(anio, mes, 1);

        Calendar cHst = Calendar.getInstance();
        cHst.set(anio, mes, cantidadDiasMes);

        Date fechaDesde = cDsd.getTime();
        Date fechaHasta = cHst.getTime();

        return ((Number) getEntityManager()
                .createQuery("SELECT COUNT(DISTINCT s.tratamiento.paciente) FROM Sesion s WHERE s.transcurrida = TRUE AND s.fechaHoraInicio BETWEEN :desde AND :hasta")
                .setParameter("desde", fechaDesde)
                .setParameter("hasta", fechaHasta).getSingleResult()).intValue();
    }

    public int CantidadPacientesPorMesTodasMenosIapos(int mes, int anio, ObraSocial iapos) {
        return this.CantidadPacientesPorMesTotal(mes, anio) - this.CantidadPacientesPorMesIapos(mes, anio, iapos);
    }
}
