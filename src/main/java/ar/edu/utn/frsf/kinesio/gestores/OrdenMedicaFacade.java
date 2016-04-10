/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.edu.utn.frsf.kinesio.gestores;

import ar.edu.utn.frsf.kinesio.entities.ObraSocial;
import ar.edu.utn.frsf.kinesio.entities.OrdenMedica;
import ar.edu.utn.frsf.kinesio.entities.Tratamiento;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Nacho Gómez
 */
@Stateless
public class OrdenMedicaFacade extends AbstractFacade<OrdenMedica> {

    @PersistenceContext(unitName = "ar.edu.utn.frsf_kinesio_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public OrdenMedicaFacade() {
        super(OrdenMedica.class);
    }
    
    public OrdenMedica initOrden(){
        OrdenMedica orden = new OrdenMedica();
        orden.setFechaCreacion(new Date());
        orden.setPresentadaAlCirculo(false);
        return orden;
    }
    
    @Override
    public void create(OrdenMedica orden){
        super.create(orden);
        getEntityManager().flush();
    }
    
    public List<OrdenMedica> getOrdenesByTratamiento(Tratamiento tratamiento) {
        return getEntityManager()
                .createNamedQuery("OrdenMedica.findByTratamiento")
                .setParameter("tratamiento", tratamiento)
                .getResultList();
    }
    
    public List<OrdenMedica> getOrdenesByFilters(Boolean autorizada,Boolean presentada,ObraSocial obraSocial,Date startDate,Date endDate){
        boolean autorizadaSet = false;
        boolean presentadaSet = false;
        boolean obraSocialSet = false;
        boolean startDateSet = false;
        boolean endDateSet = false;
        String pqlQuery;
        
        //Si no hay filtro seleccionado, seleccionamos todas las órdenes
        if(autorizada==null && presentada==null && obraSocial==null && startDate==null && endDate==null)
            pqlQuery = "SELECT o FROM OrdenMedica o ";
        //Si hay algún filtro seleccionado, armamos el comienzo de la query
        else
            pqlQuery = "SELECT o FROM OrdenMedica o WHERE ";
        
        //Si el filtro autorizado fue seteado, agregamos la condición segun el valor del parámetro
        if(autorizada != null){
            if(autorizada)
                pqlQuery += "o.codigoDeAutorizacion IS NOT NULL ";
            else
                pqlQuery += "o.codigoDeAutorizacion IS NULL ";
            autorizadaSet = true;
        }
        //Si el filtro presentada fue seteado, agregamos la condición segun el valor del parámetro
        if(presentada!=null){
            if(autorizadaSet) pqlQuery += "and ";
            pqlQuery += "o.presentadaAlCirculo = :presentada ";
            presentadaSet = true;
        }
        //Si el filtro obraSocial fue seteado, agregamos la condición segun el valor del parámetro
        if(obraSocial != null){
            if(autorizadaSet || presentadaSet) pqlQuery += "and ";
            pqlQuery += "o.obraSocial = :obraSocial ";
            obraSocialSet = true;
        }
        //Si el filtro startDate fue seteado, agregamos la condición segun el valor del parámetro
        if(startDate != null){
            if(autorizadaSet || presentadaSet || obraSocialSet) pqlQuery += "and ";
            pqlQuery += "o.fechaCreacion >= :startDate ";
            startDateSet = true;
        }
        //Si el filtro endDate fue seteado, agregamos la condición segun el valor del parámetro
        if(endDate != null){
            if(autorizadaSet || presentadaSet || obraSocialSet || startDateSet) pqlQuery += "and ";
            pqlQuery += "o.fechaCreacion <= :endDate ";
            endDateSet = true;
        }
        
        Query query = getEntityManager().createQuery(pqlQuery);
        if(presentadaSet) query.setParameter("presentada",presentada);
        if(obraSocialSet) query.setParameter("obraSocial",obraSocial);
        if(startDateSet) query.setParameter("startDate",startDate);
        if(endDateSet) query.setParameter("endDate",endDate);
        
        return query.getResultList();
    }
    
}