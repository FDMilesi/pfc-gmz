package ar.edu.utn.frsf.kinesio.controllers;

import ar.edu.utn.frsf.kinesio.controllers.util.JsfUtil;
import ar.edu.utn.frsf.kinesio.controllers.util.JsfUtil.PersistAction;
import ar.edu.utn.frsf.kinesio.entities.Caja;
import ar.edu.utn.frsf.kinesio.entities.Concepto;
import ar.edu.utn.frsf.kinesio.entities.MovimientoCaja;
import ar.edu.utn.frsf.kinesio.gestores.MovimientoCajaFacade;

import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

@Named("cajaController")
@ViewScoped
public class CajaController implements Serializable {

    @EJB
    private MovimientoCajaFacade ejbFacade;
    private List<MovimientoCaja> items = null;
    private List<Concepto> conceptos = null;
    private MovimientoCaja selected;
    private Caja caja;

    @PostConstruct
    protected void init() {
        caja = this.getFacade().getCaja();
        conceptos = this.getFacade().findAllConceptos();
    }

    public CajaController() {
    }

    //Getters and Setters
    public Caja getCaja() {
        return caja;
    }

    public MovimientoCaja getSelected() {
        return selected;
    }

    public void setSelected(MovimientoCaja selected) {
        this.selected = selected;
    }

    private MovimientoCajaFacade getFacade() {
        return ejbFacade;
    }

    public String getTipoIngresoAsString() {
        return MovimientoCaja.TipoMovimiento.INGRESO.name();
    }

    public String getTipoEgresoAsString() {
        return MovimientoCaja.TipoMovimiento.EGRESO.name();
    }

    //Métodos de negocio    
    public List<MovimientoCaja> getItems() {
        if (items == null) {
            items = getFacade().getMovimientosByCaja(caja);
        }
        return items;
    }

    public List<Concepto> getConceptos() {
        return this.conceptos;
    }

    public Concepto getConcepto(Short id) {
        if (this.conceptos != null) {
            List<Concepto> resultado = this.conceptos.stream().filter(c -> c.getId().equals(id)).collect(Collectors.toList());
            if (!resultado.isEmpty()) {
                return resultado.get(0);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public MovimientoCaja prepareCreateIngreso() {
        selected = getFacade().initMovimientoCaja(MovimientoCaja.TipoMovimiento.INGRESO, caja);
        return selected;
    }

    public MovimientoCaja prepareCreateEgreso() {
        selected = getFacade().initMovimientoCaja(MovimientoCaja.TipoMovimiento.EGRESO, caja);
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("MovimientoCajaCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    private void persist(PersistAction persistAction, String successMessage) {
        if (selected != null) {
            try {
                if (persistAction == PersistAction.CREATE) {
                    getFacade().create(selected);
                } else if (persistAction == PersistAction.DELETE) {
                    getFacade().remove(selected);
                }
                JsfUtil.addSuccessMessage(successMessage);

            } catch (EJBException ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));

            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            }
        }
    }

}
