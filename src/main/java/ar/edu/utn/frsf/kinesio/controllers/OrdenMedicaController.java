/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.edu.utn.frsf.kinesio.controllers;

import ar.edu.utn.frsf.kinesio.gestores.OrdenMedicaFacade;
import ar.edu.utn.frsf.kinesio.controllers.util.JsfUtil;
import ar.edu.utn.frsf.kinesio.entities.OrdenMedica;
import ar.edu.utn.frsf.kinesio.entities.Tratamiento;
import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 *
 * @author Nacho Gómez
 */
@Named("ordenMedicaController")
@ViewScoped
public class OrdenMedicaController implements Serializable {

    public OrdenMedicaController() {

    }
    
    @EJB
    private OrdenMedicaFacade ejbFacade;
    private List<OrdenMedica> items = null;
    private List<OrdenMedica> itemsFiltrados = null;
    private List<OrdenMedica> itemsTratamiento = null;
    private OrdenMedica selected;

    public List<OrdenMedica> getItemsTratamiento() {
        return itemsTratamiento;
    }

    public void setItemsTratamiento(List<OrdenMedica> itemsTratamiento) {
        this.itemsTratamiento = itemsTratamiento;
    }
    private Tratamiento tratamiento;
    
//    @Resource
//    private UserTransaction utx = null;
//    @PersistenceUnit(unitName = "ar.edu.utn.frsf_kinesio_war_1.0-SNAPSHOTPU")
//    private EntityManagerFactory emf = null;

    @PostConstruct
    public void init(){
        items = getFacade().findAll();
        tratamiento = (Tratamiento) FacesContext.getCurrentInstance()
                .getExternalContext()
                .getSessionMap()
                .get("tratamientoEdicion");
        itemsTratamiento = getFacade().getOrdenesByTratamiento(tratamiento);
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
    
    public void filtrarItemsByAutorizacion(){
        
    }
    
    public List<OrdenMedica> getItemsFiltrados() {
        return itemsFiltrados;
    }
    
    public void setItemsFiltrados(List<OrdenMedica> ordenesFiltradas) {
        this.itemsFiltrados = ordenesFiltradas;
    }
    
    public OrdenMedica prepareCreate() {
        selected = getFacade().initOrden();
        if(tratamiento!=null){
            selected.setTratamiento(tratamiento);
            selected.setNumeroAfiliadoPaciente(tratamiento.getPaciente().getNroAfiliadoOS());
            selected.setObraSocial(tratamiento.getPaciente().getObraSocial());
        }
        return selected;
    }

    public void create() {
        persist(JsfUtil.PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("OrdenMedicaCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
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
