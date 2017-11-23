package ar.edu.utn.frsf.kinesio.controllers.util;

import ar.edu.utn.frsf.kinesio.entities.Sesion;
import java.util.Date;
import org.primefaces.model.ScheduleEvent;

public class SesionScheduleEventModel implements ScheduleEvent {

    private Sesion sesion;

    private Date startDate;

    private Date endDate;

    private String styleClass;

    public SesionScheduleEventModel(Sesion sesion) {
        this.sesion = sesion;

        this.startDate = (Date) this.sesion.getFechaHoraInicio().clone();

        this.endDate = new Date(startDate.getTime() + (this.sesion.getDuracion() * 60000));

        this.styleClass = obtenerStyleClassParaSesion(this.sesion);

    }

    @Override
    @Deprecated
    public void setId(String string) {
    }

    @Override
    public Object getData() {
        return sesion;
    }

    @Override
    public String getTitle() {
        return sesion.getTratamiento().getPaciente().toString();
    }

    @Override
    public Date getStartDate() {
        return startDate;
    }

    @Override
    public Date getEndDate() {
        return endDate;
    }

    @Override
    public boolean isAllDay() {
        return false;
    }

    @Override
    public String getStyleClass() {
        return this.styleClass;
    }

    @Override
    public boolean isEditable() {
        return !this.sesion.getTranscurrida();
    }

    @Override
    public String getDescription() {
        return this.sesion.toString();
    }

    @Override
    public String getId() {
        return String.valueOf(this.sesion.getIdSesion());
    }

    public Sesion getSesion() {
        return sesion;
    }

    private String obtenerStyleClassParaSesion(Sesion sesion) {
        StringBuilder classStringBuilder = new StringBuilder();

        //Color
        switch (sesion.getTratamiento().getTipoDeTratamiento().getId()) {
            case 1:
                classStringBuilder.append("fisiokinesioterapia");
                break;
            case 2:
                classStringBuilder.append("kinesioterapiaRespiratoria");
                break;
            case 3:
                classStringBuilder.append("drenajeLinfatico");
                break;
            case 4:
                classStringBuilder.append("rehabilitacionNeurologica");
                break;
            case 5:
                classStringBuilder.append("esteticaElectrodos");
                break;
            case 6:
                classStringBuilder.append("esteticaMasajes");
                break;
            case 7:
                classStringBuilder.append("gimnasiaTerapeutica");
                break;
        }

        classStringBuilder.append(" ");

        //Icono
        if (!sesion.getCuenta()) {
            classStringBuilder.append("sesionNoCuenta");
        } else if (sesion.getTranscurrida()) {
            classStringBuilder.append("sesionTranscurrida");
        }

        return classStringBuilder.toString();
    }

}
