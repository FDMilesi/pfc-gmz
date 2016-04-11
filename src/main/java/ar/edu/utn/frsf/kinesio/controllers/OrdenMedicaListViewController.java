/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.edu.utn.frsf.kinesio.controllers;

import ar.edu.utn.frsf.kinesio.gestores.OrdenMedicaFacade;
import ar.edu.utn.frsf.kinesio.controllers.util.JsfUtil;
import ar.edu.utn.frsf.kinesio.entities.ObraSocial;
import ar.edu.utn.frsf.kinesio.entities.OrdenMedica;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 *
 * @author Nacho Gómez
 */
@Named("ordenMedicaListViewController")
@ViewScoped
public class OrdenMedicaListViewController implements Serializable {

    public OrdenMedicaListViewController() {

    }
    
    @EJB
    private OrdenMedicaFacade ejbFacade;
    private List<OrdenMedica> items = null;
    private List<OrdenMedica> itemsFiltrados = null;
    private OrdenMedica selected;
    private Boolean autorizada;
    private Boolean presentada;
    private ObraSocial obraSocial;
    private Date startDate;
    private Date endDate;

    @PostConstruct
    public void init(){
        items = getFacade().findAll();
        itemsFiltrados = getFacade().findAll();//creo q no hace falta
    }
    
    private OrdenMedicaFacade getFacade() {
        return ejbFacade;
    }
    
    public void cancel() {
        selected = null;
    }

    public void destroy() {
        persist(JsfUtil.PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("OrdenMedicaDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }
    
    public OrdenMedica getSelected() {
        return selected;
    }

    public void setSelected(OrdenMedica selected) {
        this.selected = selected;
    }

    public List<OrdenMedica> getOrdenes() {
        return items;
    }
    
    public void setOrdenes(List<OrdenMedica> ordenes) {
        this.items = ordenes;
    }

    public Boolean getAutorizada() {
        return autorizada;
    }

    public void setAutorizada(Boolean autorizada) {
        this.autorizada = autorizada;
    }

    public Boolean getPresentada() {
        return presentada;
    }

    public void setPresentada(Boolean presentada) {
        this.presentada = presentada;
    }

    public ObraSocial getObraSocial() {
        return obraSocial;
    }

    public void setObraSocial(ObraSocial obraSocial) {
        this.obraSocial = obraSocial;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }
    
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    
    public void filtrarItems(){
        itemsFiltrados = getFacade().getOrdenesByFilters(autorizada, presentada,obraSocial,startDate,endDate);
    }
    
    public void limpiarFechas(){
        startDate=null;
        endDate=null;
    }
    
    public List<OrdenMedica> getItemsFiltrados() {
        return itemsFiltrados;
    }
    
    public void setItemsFiltrados(List<OrdenMedica> itemsFiltrados) {
        this.itemsFiltrados = itemsFiltrados;
    }
    
    public void update() {
        persist(JsfUtil.PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("OrdenMedicaUpdated"));
    }
    
    public OrdenMedica getOrdenMedica(java.lang.Integer id) {
        return getFacade().find(id);
    }
    
    private void persist(JsfUtil.PersistAction persistAction, String successMessage) {
        if (selected != null) {
            try {
                switch (persistAction) {
                    case CREATE:
                        getFacade().create(selected);
                        break;
                    case UPDATE:
                        getFacade().edit(selected);
                        break;
                    case DELETE:
                        getFacade().remove(selected);
                        break;
                }
                JsfUtil.addSuccessMessage(successMessage);
            } catch (EJBException ex) {
                String msg = "";
                Throwable cause = ex.getCause();
                if (cause != null) {
                    msg = cause.getLocalizedMessage();
                }
                if (msg.length() > 0) {
                    JsfUtil.addErrorMessage(msg);
                } else {
                    JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
                }
            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            }
        }
    }
    
}
