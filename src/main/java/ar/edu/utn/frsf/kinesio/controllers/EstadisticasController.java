package ar.edu.utn.frsf.kinesio.controllers;

import ar.edu.utn.frsf.kinesio.entities.ObraSocial;
import ar.edu.utn.frsf.kinesio.gestores.EstadisticasFacade;
import ar.edu.utn.frsf.kinesio.gestores.ObraSocialFacade;
import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.Calendar;
import java.util.Locale;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.CategoryAxis;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.LineChartSeries;
import org.primefaces.model.chart.PieChartModel;

@ManagedBean
public class EstadisticasController implements Serializable {

    private LineChartModel animatedModel1;
    private PieChartModel pieModel;

    @EJB
    private EstadisticasFacade estadisticasFacade;
    @EJB
    private ObraSocialFacade obraSocialFacade;

    @PostConstruct
    public void init() {
        createAnimatedModels();
    }

    private EstadisticasFacade getFacade() {
        return estadisticasFacade;
    }

    public ObraSocialFacade getObraSocialFacade() {
        return obraSocialFacade;
    }

    public LineChartModel getAnimatedModel1() {
        return animatedModel1;
    }

    public PieChartModel getPieModel() {
        return pieModel;
    }

    private void createAnimatedModels() {
        animatedModel1 = initLinearModel();
        animatedModel1.setTitle("Cantidad de pacientes en los últimos meses");
        animatedModel1.setAnimate(true);
        animatedModel1.setLegendPosition("ne");
        Axis yAxis = animatedModel1.getAxis(AxisType.Y);
        yAxis.setMin(0);
        yAxis.setMax(5);
        
        pieModel = initPieModel();
        pieModel.setTitle("Obras Sociales mas importantes");
        pieModel.setLegendPosition("e");
//        pieModel.setFill(false);
        pieModel.setShowDataLabels(true);
        pieModel.setDiameter(150);
    }

    private LineChartModel initLinearModel() {

        LineChartModel model = new LineChartModel();
        ObraSocial iapos = this.getObraSocialFacade().getObraSocialIAPOS();

        Calendar c = Calendar.getInstance();
        int mes1 = c.get(Calendar.MONTH);
        int anio1 = c.get(Calendar.YEAR);
        c.add(Calendar.MONTH, -1);
        int mes2 = c.get(Calendar.MONTH);
        int anio2 = c.get(Calendar.YEAR);
        c.add(Calendar.MONTH, -1);
        int mes3 = c.get(Calendar.MONTH);
        int anio3 = c.get(Calendar.YEAR);
        c.add(Calendar.MONTH, -1);
        int mes4 = c.get(Calendar.MONTH);
        int anio4 = c.get(Calendar.YEAR);
        c.add(Calendar.MONTH, -1);
        int mes5 = c.get(Calendar.MONTH);
        int anio5 = c.get(Calendar.YEAR);

        String NombreMes1 = Month.values()[mes1].getDisplayName(TextStyle.FULL, new Locale("ES", "AR"));
        String NombreMes2 = Month.values()[mes2].getDisplayName(TextStyle.FULL, new Locale("ES", "AR"));
        String NombreMes3 = Month.values()[mes3].getDisplayName(TextStyle.FULL, new Locale("ES", "AR"));
        String NombreMes4 = Month.values()[mes4].getDisplayName(TextStyle.FULL, new Locale("ES", "AR"));
        String NombreMes5 = Month.values()[mes5].getDisplayName(TextStyle.FULL, new Locale("ES", "AR"));

        LineChartSeries series1 = new LineChartSeries();
        series1.setLabel("Iapos");
        series1.set(NombreMes5, getFacade().CantidadPacientesPorMesIapos(mes5, anio5, iapos));
        series1.set(NombreMes4, getFacade().CantidadPacientesPorMesIapos(mes4, anio4, iapos));
        series1.set(NombreMes3, getFacade().CantidadPacientesPorMesIapos(mes3, anio3, iapos));
        series1.set(NombreMes2, getFacade().CantidadPacientesPorMesIapos(mes2, anio2, iapos));
        series1.set(NombreMes1, getFacade().CantidadPacientesPorMesIapos(mes1, anio1, iapos));

        LineChartSeries series2 = new LineChartSeries();
        series2.setLabel("Otras Obras Sociales");
        series2.set(NombreMes5, getFacade().CantidadPacientesPorMesTodasMenosIapos(mes5, anio5, iapos));
        series2.set(NombreMes4, getFacade().CantidadPacientesPorMesTodasMenosIapos(mes4, anio4, iapos));
        series2.set(NombreMes3, getFacade().CantidadPacientesPorMesTodasMenosIapos(mes3, anio3, iapos));
        series2.set(NombreMes2, getFacade().CantidadPacientesPorMesTodasMenosIapos(mes2, anio2, iapos));
        series2.set(NombreMes1, getFacade().CantidadPacientesPorMesTodasMenosIapos(mes1, anio1, iapos));

        model.addSeries(series1);
        model.addSeries(series2);

        model.getAxes().put(AxisType.X, new CategoryAxis("Meses"));

        return model;
    }
    
    private PieChartModel initPieModel(){
        PieChartModel model = new PieChartModel();
        ObraSocial iapos = this.getObraSocialFacade().getObraSocialIAPOS();
        ObraSocial jerarquicos = this.getObraSocialFacade().getObraSocialJerarquicos();
        ObraSocial sancor = this.getObraSocialFacade().getObraSocialSancor();
        ObraSocial ospat = this.getObraSocialFacade().getObraSocialOSPAT();
        ObraSocial uta = this.getObraSocialFacade().getObraSocialUTA();
        
        model.set("Iapos", getFacade().CantidadPacientesConObraSocialX(iapos));
        model.set("Jerarquicos Salud", getFacade().CantidadPacientesConObraSocialX(jerarquicos));
        model.set("Sancor Salud", getFacade().CantidadPacientesConObraSocialX(sancor));
        model.set("OSPAT (Pers. del Turf)", getFacade().CantidadPacientesConObraSocialX(ospat));
        model.set("U.T.A. - O.S.C.T.C.P.", getFacade().CantidadPacientesConObraSocialX(uta));
        
        return model;
    }
}
