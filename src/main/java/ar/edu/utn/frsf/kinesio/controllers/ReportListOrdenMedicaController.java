package ar.edu.utn.frsf.kinesio.controllers;

import ar.edu.utn.frsf.kinesio.entities.OrdenMedica;
import java.io.Serializable;
import java.util.Calendar;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

@Named("reportListOrdenMedicaController")
@ViewScoped
public class ReportListOrdenMedicaController implements Serializable {

    private List<OrdenMedica> items;

    public ReportListOrdenMedicaController() {
    }

    @PostConstruct
    public void init() {
        items = (List<OrdenMedica>) FacesContext.getCurrentInstance()
                .getExternalContext()
                .getSessionMap()
                .get("ordenesReport");
    }

    public List<OrdenMedica> getItems() {
        return items;
    }

    public String getMesActual() {
        switch (Calendar.getInstance().get(Calendar.MONTH) + 1) {
            case 1:
                return "Enero";
            case 2:
                return "Febrero";
            case 3:
                return "Marzo";
            case 4:
                return "Abril";
            case 5:
                return "Mayo";
            case 6:
                return "Junio";
            case 7:
                return "Julio";
            case 8:
                return "Agosto";
            case 9:
                return "Septiembre";
            case 10:
                return "Octubre";
            case 11:
                return "Noviembre";
            case 12:
                return "Diciembre";
        }
        return "";
    }

}
