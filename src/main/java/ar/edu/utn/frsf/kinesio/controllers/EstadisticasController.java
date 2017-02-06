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
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.CategoryAxis;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.LineChartSeries;
import org.primefaces.model.chart.PieChartModel;

@ManagedBean
public class EstadisticasController implements Serializable {

    private LineChartModel animatedModel1;
    private PieChartModel pieModel;
    private BarChartModel barModel;

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

    public BarChartModel getBarModel() {
        return barModel;
    }

    private void createAnimatedModels() {
        //grafico de lineas
        animatedModel1 = initLinearModel();
        animatedModel1.setTitle("IAPOS vs Otras Obras Sociales");
        animatedModel1.setAnimate(true);
        animatedModel1.setLegendPosition("ne");
        Axis yAxis = animatedModel1.getAxis(AxisType.Y);
        yAxis.setMin(0);
        yAxis.setMax(5);

        //grafico de torta
        pieModel = initPieModel();
        pieModel.setTitle("Obras Sociales mas importantes");
        pieModel.setLegendPosition("e");
//        pieModel.setFill(false);
        pieModel.setShowDataLabels(true);
        pieModel.setDiameter(150);

        //grafico de barras
        barModel = initBarModel();
        barModel.setTitle("Cantidad pacientes por mes");
        barModel.setAnimate(true);
        Axis yAxisBarModel = barModel.getAxis(AxisType.Y);
        yAxisBarModel.setMin(0);
        yAxisBarModel.setMax(5);
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

    private PieChartModel initPieModel() {
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

    private BarChartModel initBarModel() {
        BarChartModel model = new BarChartModel();

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
        c.add(Calendar.MONTH, -1);
        int mes6 = c.get(Calendar.MONTH);
        int anio6 = c.get(Calendar.YEAR);
        c.add(Calendar.MONTH, -1);
        int mes7 = c.get(Calendar.MONTH);
        int anio7 = c.get(Calendar.YEAR);
        c.add(Calendar.MONTH, -1);
        int mes8 = c.get(Calendar.MONTH);
        int anio8 = c.get(Calendar.YEAR);
        c.add(Calendar.MONTH, -1);
        int mes9 = c.get(Calendar.MONTH);
        int anio9 = c.get(Calendar.YEAR);
        c.add(Calendar.MONTH, -1);
        int mes10 = c.get(Calendar.MONTH);
        int anio10 = c.get(Calendar.YEAR);
        c.add(Calendar.MONTH, -1);
        int mes11 = c.get(Calendar.MONTH);
        int anio11 = c.get(Calendar.YEAR);
        c.add(Calendar.MONTH, -1);
        int mes12 = c.get(Calendar.MONTH);
        int anio12 = c.get(Calendar.YEAR);

        String NombreMes1 = Month.values()[mes1].getDisplayName(TextStyle.FULL, new Locale("ES", "AR"));
        String NombreMes2 = Month.values()[mes2].getDisplayName(TextStyle.FULL, new Locale("ES", "AR"));
        String NombreMes3 = Month.values()[mes3].getDisplayName(TextStyle.FULL, new Locale("ES", "AR"));
        String NombreMes4 = Month.values()[mes4].getDisplayName(TextStyle.FULL, new Locale("ES", "AR"));
        String NombreMes5 = Month.values()[mes5].getDisplayName(TextStyle.FULL, new Locale("ES", "AR"));
        String NombreMes6 = Month.values()[mes6].getDisplayName(TextStyle.FULL, new Locale("ES", "AR"));
        String NombreMes7 = Month.values()[mes7].getDisplayName(TextStyle.FULL, new Locale("ES", "AR"));
        String NombreMes8 = Month.values()[mes8].getDisplayName(TextStyle.FULL, new Locale("ES", "AR"));
        String NombreMes9 = Month.values()[mes9].getDisplayName(TextStyle.FULL, new Locale("ES", "AR"));
        String NombreMes10 = Month.values()[mes10].getDisplayName(TextStyle.FULL, new Locale("ES", "AR"));
        String NombreMes11 = Month.values()[mes11].getDisplayName(TextStyle.FULL, new Locale("ES", "AR"));
        String NombreMes12 = Month.values()[mes12].getDisplayName(TextStyle.FULL, new Locale("ES", "AR"));

        ChartSeries pacientesPorMes = new ChartSeries();
        pacientesPorMes.setLabel("pacientesPorMes");
        pacientesPorMes.set(NombreMes12, this.getFacade().CantidadPacientesPorMesTotal(mes12, anio12));
        pacientesPorMes.set(NombreMes11, this.getFacade().CantidadPacientesPorMesTotal(mes11, anio11));
        pacientesPorMes.set(NombreMes10, this.getFacade().CantidadPacientesPorMesTotal(mes10, anio10));
        pacientesPorMes.set(NombreMes9, this.getFacade().CantidadPacientesPorMesTotal(mes9, anio9));
        pacientesPorMes.set(NombreMes8, this.getFacade().CantidadPacientesPorMesTotal(mes8, anio8));
        pacientesPorMes.set(NombreMes7, this.getFacade().CantidadPacientesPorMesTotal(mes7, anio7));
        pacientesPorMes.set(NombreMes6, this.getFacade().CantidadPacientesPorMesTotal(mes6, anio6));
        pacientesPorMes.set(NombreMes5, this.getFacade().CantidadPacientesPorMesTotal(mes5, anio5));
        pacientesPorMes.set(NombreMes4, this.getFacade().CantidadPacientesPorMesTotal(mes4, anio4));
        pacientesPorMes.set(NombreMes3, this.getFacade().CantidadPacientesPorMesTotal(mes3, anio3));
        pacientesPorMes.set(NombreMes2, this.getFacade().CantidadPacientesPorMesTotal(mes2, anio2));
        pacientesPorMes.set(NombreMes1, this.getFacade().CantidadPacientesPorMesTotal(mes1, anio1));

        model.addSeries(pacientesPorMes);
        model.getAxes().put(AxisType.X, new CategoryAxis("Meses"));

        return model;
    }
}
