package ar.edu.utn.frsf.kinesio.gestores;

import ar.edu.utn.frsf.kinesio.entities.ObraSocial;
import ar.edu.utn.frsf.kinesio.entities.TipoDeTratamiento;
import ar.edu.utn.frsf.kinesio.entities.OrdenMedica;
import ar.edu.utn.frsf.kinesio.entities.TipoTratamientoObraSocial;
import ar.edu.utn.frsf.kinesio.entities.TipoTratamientoObraSocialPK;
import ar.edu.utn.frsf.kinesio.entities.Tratamiento;
import ar.edu.utn.frsf.kinesio.entities.Paciente;
import ar.edu.utn.frsf.kinesio.gestores.util.FiltroOrdenMedica;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 */
@Stateless
public class OrdenMedicaFacade extends AbstractFacade<OrdenMedica> {

    @PersistenceContext(unitName = "ar.edu.utn.frsf_kinesio_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    @EJB
    ObraSocialFacade obraSocialFacade;
    @EJB
    private TipoTratamientoObraSocialFacade tipoTratamientoObraSocialFacade;

    public OrdenMedicaFacade() {
        super(OrdenMedica.class);
    }

    public OrdenMedica initOrden(Tratamiento tratamiento) {
        OrdenMedica orden = new OrdenMedica();
        orden.setFechaCreacion(new Date());
        orden.setPresentadaAlCirculo(false);
        orden.setTratamiento(tratamiento);
        this.setearObraSocial(orden, tratamiento);
        orden.setNumeroAfiliadoPaciente(tratamiento.getPaciente().getNroAfiliadoOS());
        orden.setAutorizada(Boolean.FALSE);

        //si no necesita autorizacion ya la doy de alta como autorizada
        if (!this.necesitaAutorizacion(tratamiento.getTipoDeTratamiento(), tratamiento.getPaciente().getObraSocial())) {
            orden.setAutorizada(Boolean.TRUE);
        }

        return orden;
    }

    private void setearObraSocial(OrdenMedica orden, Tratamiento tratamiento) {
        if (tratamiento.getAccidentetrabajo()) {
            ObraSocial iaposAccidenteTrabajo = this.obraSocialFacade.getObraSocialIAPOSAccidenteTrabajo();
            orden.setObraSocial(iaposAccidenteTrabajo);
        } else {
            orden.setObraSocial(tratamiento.getPaciente().getObraSocial());
        }
    }

    public boolean necesitaAutorizacion(TipoDeTratamiento tipoDeTratamiento, ObraSocial obraSocial) {
        TipoTratamientoObraSocial tipoTratamientoObraSocial = this.getTipoTratamientoObraSocialFacade()
                .find(new TipoTratamientoObraSocialPK(tipoDeTratamiento.getId(), obraSocial.getId()));

        //Si no existe la relacion entre la OS y el tipo de trata
        if (tipoTratamientoObraSocial == null) {
            return Boolean.TRUE;
        }

        return tipoTratamientoObraSocial.isRequiereAutorizacion();
    }

    public TipoTratamientoObraSocialFacade getTipoTratamientoObraSocialFacade() {
        return tipoTratamientoObraSocialFacade;
    }

    public List<OrdenMedica> getOrdenesByTratamiento(Tratamiento tratamiento) {
        return getEntityManager()
                .createNamedQuery("OrdenMedica.findByTratamiento")
                .setParameter("tratamiento", tratamiento)
                .getResultList();
    }

    public List<OrdenMedica> getOrdenesByFilters(FiltroOrdenMedica filtro) {
        String pqlQuery;

        pqlQuery = "SELECT o FROM OrdenMedica o WHERE 1=1 ";

        if (filtro.getAutorizada() != null) {
            pqlQuery += "and o.autorizada = :autorizada ";
        }

        if (filtro.getPresentada() != null) {
            pqlQuery += "and o.presentadaAlCirculo = :presentada ";
        }

        if (filtro.getStartDate() != null) {
            pqlQuery += "and o.fechaAutorizacion >= :startDate ";
        }

        if (filtro.getEndDate() != null) {
            pqlQuery += "and o.fechaAutorizacion <= :endDate ";
        }

        if (filtro.getObraSocial() != null) {
            pqlQuery += "and o.obraSocial = :obraSocial ";
        }

        if (filtro.getTipoDeTratamiento() != null) {
            pqlQuery += "and o.tratamiento.tipoDeTratamiento = :tipoDeTratamiento ";
        }

        //Caso especial: quiero excluir la OS, pero además excluir los tratamientos fisiokinesio
        if (filtro.getObraSocialExcluida() != null && filtro.getObraSocial() == null) {
            pqlQuery += "and o.id NOT IN "
                    + "(SELECT om.id "
                    + "FROM OrdenMedica om "
                    + "WHERE om.obraSocial = :obraSocialExcluida "
                    + "and om.tratamiento.tipoDeTratamiento = :tipoDeTratamientoExcluido) ";
        }

        //SETEO LOS PARAMETROS
        Query query = getEntityManager().createQuery(pqlQuery);

        if (filtro.getAutorizada() != null) {
            query.setParameter("autorizada", filtro.getAutorizada());
        }

        if (filtro.getPresentada() != null) {
            query.setParameter("presentada", filtro.getPresentada());
        }

        if (filtro.getStartDate() != null) {
            query.setParameter("startDate", filtro.getStartDate());
        }

        if (filtro.getEndDate() != null) {
            query.setParameter("endDate", filtro.getEndDate());
        }

        if (filtro.getObraSocial() != null) {
            query.setParameter("obraSocial", filtro.getObraSocial());
        }

        if (filtro.getTipoDeTratamiento() != null) {
            query.setParameter("tipoDeTratamiento", filtro.getTipoDeTratamiento());
        }

        if (filtro.getObraSocialExcluida() != null && filtro.getObraSocial() == null) {
            query.setParameter("obraSocialExcluida", filtro.getObraSocialExcluida());
            query.setParameter("tipoDeTratamientoExcluido", filtro.getTipoDeTratamientoExcluido());
        }

        return query.getResultList();
    }

    public Boolean esValidaCantidadDeSesionesDeOrdenes(Tratamiento tratamiento, Short valor) {
        return this.sumatoriaSesionesDeOrdenes(tratamiento) + valor <= tratamiento.getCantidadDeSesiones();
    }

    public Boolean estanTodasLasOrdenesAutorizadas(List<OrdenMedica> ordenes) {
        for (OrdenMedica o : ordenes) {
            if (o.getCodigoDeAutorizacion() == null) {
                return false;
            }
        }
        return true;
    }

    public OrdenMedica autorizarOrden(OrdenMedica ordenMedica) {
        //Si el codigo de autorización es nulo o vacio le seteo la fecha de autorización
        if (ordenMedica.getCodigoDeAutorizacion() != null && !(ordenMedica.getCodigoDeAutorizacion().isEmpty())) {
            ordenMedica.setFechaAutorizacion(new Date());
            ordenMedica.setAutorizada(Boolean.TRUE);
        }
        return ordenMedica;
    }

    public Short sumatoriaSesionesDeOrdenes(Tratamiento tratamiento) {
        boolean tieneOrdenes = ((Number) getEntityManager()
                .createQuery("SELECT COUNT(o) FROM OrdenMedica o WHERE o.tratamiento = :tratamiento")
                .setParameter("tratamiento", tratamiento).getSingleResult()).intValue() > 0;

        if (tieneOrdenes) {
            return ((Number) getEntityManager()
                    .createNamedQuery("OrdenMedica.sumaCantidadDeSesionesByTratamiento")
                    .setParameter("tratamiento", tratamiento)
                    .getSingleResult()).shortValue();
        } else {
            return (short) 0;
        }

    }
    
    /***
     * Este metodo no se esta siendo invocado por nadie (esta al pedo) <<<----------------------------------------------------
     * @param paciente
     * @return 
     */
    public int cantidadSesionesEnElAnio(Paciente paciente){
        Calendar fechaDesde = Calendar.getInstance(); 
        fechaDesde.set(Calendar.MONTH, 0);
        fechaDesde.set(Calendar.DAY_OF_MONTH, 1);
        fechaDesde.set(Calendar.HOUR_OF_DAY, 0);
        fechaDesde.set(Calendar.MINUTE, 0);
        fechaDesde.set(Calendar.SECOND, 0);
        fechaDesde.set(Calendar.MILLISECOND, 0);

        Object obj = getEntityManager()
                    .createNamedQuery("OrdenMedica.sumaCantidadSesionesEnElAnio")
                    .setParameter("paciente", paciente)
                    .setParameter("fechaAnioInicio", fechaDesde.getTime())
                    .setParameter("fechaAnioFin", new Date())
                    .getSingleResult();
        
        return ((Number)obj).intValue();
    }

    public void marcarOrdenesComoPresentadas(List<OrdenMedica> listaOrdenes) {
        for (OrdenMedica orden : listaOrdenes) {
            orden.setPresentadaAlCirculo(Boolean.TRUE);
            edit(orden);
        }
    }

    protected void setEm(EntityManager em) {
        this.em = em;
    }
    
    protected void setObraSocialFacade(ObraSocialFacade obraSocialFacade){
        this.obraSocialFacade = obraSocialFacade;
    }
}
