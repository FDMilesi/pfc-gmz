package ar.edu.utn.frsf.kinesio.controllers;

import ar.edu.utn.frsf.kinesio.gestores.OrdenMedicaFacade;
import ar.edu.utn.frsf.kinesio.controllers.util.JsfUtil;
import ar.edu.utn.frsf.kinesio.entities.ObraSocial;
import ar.edu.utn.frsf.kinesio.entities.OrdenMedica;
import ar.edu.utn.frsf.kinesio.gestores.util.FiltroOrdenMedica;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

@Named("listOrdenMedicaController")
@ViewScoped
public class ListOrdenMedicaController implements Serializable {
    
    public ListOrdenMedicaController() {
    }
    
    @EJB
    private OrdenMedicaFacade ejbFacade;
    @Inject
    private ObraSocialController obraSocialController;
    @Inject
    private TipoDeTratamientoController tipoDeTratamientoController;
    
    private List<OrdenMedica> itemsFiltrados = null;
    private OrdenMedica selected;
    private Boolean autorizada;
    private Boolean presentada;
    private ObraSocial obraSocial;
    private Date startDate;
    private Date endDate;
    
    @PostConstruct
    public void init() {
        this.setPresentada(false);
        this.filtrarItems();
    }

    //Metodos de negocio
    public void filtrarItems() {
        FiltroOrdenMedica filtro = new FiltroOrdenMedica();
        if (obraSocial != null) {
            if (obraSocial.equals(obraSocialController.getObraSocialNoIAPOS())) {
                filtro.setObraSocialExcluida(obraSocialController.getObraSocialIAPOS());
                filtro.setTipoDeTratamientoExcluido(tipoDeTratamientoController.getTipoFisikinesioterapia());
                
            } else if (obraSocial.equals(obraSocialController.getObraSocialIAPOSFKT())) {
                filtro.setObraSocial(obraSocialController.getObraSocialIAPOS());
                filtro.setTipoDeTratamiento(tipoDeTratamientoController.getTipoFisikinesioterapia());
                
            } else {
                filtro.setObraSocial(obraSocial);
            }
        }
        
        filtro.setAutorizada(autorizada);
        
        filtro.setStartDate(startDate);
        
        filtro.setEndDate(endDate);
        
        filtro.setPresentada(presentada);
        
        itemsFiltrados = getFacade().getOrdenesByFilters(filtro);
    }
    
    public void update() {
        selected = this.getFacade().autorizarOrden(selected);
        persist(JsfUtil.PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("OrdenMedicaUpdated"));
    }
    
    public void destroy() {
        persist(JsfUtil.PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("OrdenMedicaDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            itemsFiltrados = null;    // Invalidate list of items to trigger re-query.
        }
    }

    //Getters y Setters
    public List<OrdenMedica> getItemsFiltrados() {
        if (itemsFiltrados == null) {
            this.filtrarItems();
        }
        return itemsFiltrados;
    }
    
    private OrdenMedicaFacade getFacade() {
        return ejbFacade;
    }
    
    public OrdenMedica getSelected() {
        return selected;
    }
    
    public void setSelected(OrdenMedica selected) {
        this.selected = selected;
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
    
    public void setItemsFiltrados(List<OrdenMedica> itemsFiltrados) {
        this.itemsFiltrados = itemsFiltrados;
    }
    
    public OrdenMedica getOrdenMedica(java.lang.Integer id) {
        return getFacade().find(id);
    }

    /* Ordenar lista con funcionales
    Collections.sort(itemsFiltrados,
    (o1, o2) -> o1.getTratamiento().getPaciente().getApellido()
    .compareTo(o2.getTratamiento().getPaciente().getApellido()));
     */
    public String redirectToReport() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("ordenesReport", itemsFiltrados);
        return "/protected/ordenMedica/ReportList.xhtml?faces-redirect=true";
    }
    
    public String marcarOrdenesPresentadasYRedirigir() {
        getFacade().marcarOrdenesComoPresentadas(itemsFiltrados);
        return this.redirectToReport();
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
