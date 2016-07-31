package ar.edu.utn.frsf.kinesio.controllers;

import ar.edu.utn.frsf.kinesio.gestores.OrdenMedicaFacade;
import ar.edu.utn.frsf.kinesio.controllers.util.JsfUtil;
import ar.edu.utn.frsf.kinesio.entities.ObraSocial;
import ar.edu.utn.frsf.kinesio.entities.OrdenMedica;
import ar.edu.utn.frsf.kinesio.gestores.TipoTratamientoObraSocialFacade;
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
import javax.inject.Named;

/**
 *
 */
@Named("reportListOrdenMedicaController")
@ViewScoped
public class ReportListOrdenMedicaController implements Serializable {

    public ReportListOrdenMedicaController() {

    }

    @EJB
    private OrdenMedicaFacade ejbFacade;
    private List<OrdenMedica> items = null;

    @PostConstruct
    public void init() {
        this.getListaOrdenesReporte();
    }

    //Metodos de negocio
    public void getListaOrdenesReporte() {
        items = (List<OrdenMedica>) FacesContext.getCurrentInstance()
                .getExternalContext()
                .getSessionMap()
                .get("ordenesReport");
                
    }

    //Getters y Setters
    public List<OrdenMedica> getItemsFiltrados() {
        if (items == null){
            this.getListaOrdenesReporte();
        }
        return items;
    }

    private OrdenMedicaFacade getFacade() {
        return ejbFacade;
    }

    public void setItems(List<OrdenMedica> itemsFiltrados) {
        this.items = itemsFiltrados;
    }

    public List<OrdenMedica> getItems() {
        return items;
    }

    public OrdenMedica getOrdenMedica(java.lang.Integer id) {
        return getFacade().find(id);
    }

}
