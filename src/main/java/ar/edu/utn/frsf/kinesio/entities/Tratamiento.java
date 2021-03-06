package ar.edu.utn.frsf.kinesio.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "tratamiento")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Tratamiento.findAll", query = "SELECT t FROM Tratamiento t"),
    @NamedQuery(name = "Tratamiento.findById", query = "SELECT t FROM Tratamiento t WHERE t.id = :id"),
    @NamedQuery(name = "Tratamiento.findByParticular", query = "SELECT t FROM Tratamiento t WHERE t.particular = :particular"),
    @NamedQuery(name = "Tratamiento.findByFinalizado", query = "SELECT t FROM Tratamiento t WHERE t.finalizado = :finalizado"),
    @NamedQuery(name = "Tratamiento.countByPaciente", query = "SELECT count(t) FROM Tratamiento t WHERE t.paciente = :paciente"),
    @NamedQuery(name = "Tratamiento.findByPacienteEnCurso",
            query = "SELECT t FROM Tratamiento t WHERE t.paciente = :paciente and t.finalizado = FALSE"),
    @NamedQuery(name = "Tratamiento.tratmientosConSesionesAFavor",
            query = "SELECT t FROM Tratamiento t WHERE t.paciente = :paciente and t.finalizado = TRUE and t.sesionesAFavor > 0 and t.tipoDeTratamiento = :tipoDeTratamiento and t.sesionesAFavorUsadas = FALSE"),
    @NamedQuery(name = "Tratamiento.findByPaciente", query = "SELECT t FROM Tratamiento t WHERE t.paciente = :paciente"),
    @NamedQuery(name = "Tratamiento.findByEnCursoAndNoParticular", query = "SELECT t FROM Tratamiento t WHERE t.finalizado = FALSE and t.particular = FALSE")})
public class Tratamiento implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Basic(optional = false)
    @NotNull(message = "Ingrese la cantidad de sesiones")
    @Min(value = 1, message = "La cantidad de sesiones debe ser 1 o más")
    @Max(value = 99, message = "La cantidad de sesiones debe ser menor que 99")
    @Column(name = "cantidaddesesiones")
    private short cantidadDeSesiones;

    @Size(max = 100, message = "Ingrese menos de 100 caracteres para el diagnóstico")
    @Column(name = "diagnostico")
    private String diagnostico;

    @Basic(optional = false)
    @NotNull
    @Column(name = "particular")
    private boolean particular;

    @Column(name = "finalizado")
    private Boolean finalizado;

    @Size(max = 2147483647)
    @Column(name = "observaciones")
    private String observaciones;

    @Column(name = "fechacreacion")
    @Temporal(TemporalType.DATE)
    private Date fechaCreacion;

    @Column(name = "sesionesafavor")
    private Short sesionesAFavor;

    @Column(name = "sesionesafavorusadas")
    private Boolean sesionesAFavorUsadas;

    @Column(name = "fechaultimaautorizacion")
    @Temporal(TemporalType.DATE)
    private Date fechaUltimaAutorizacion;

    @Size(max = 100)
    @Column(name = "medicoderivante")
    private String medicoDerivante;

    @Basic(optional = false)
    @NotNull
    @Column(name = "accidentetrabajo")
    private boolean accidenteTrabajo;

    @NotNull
    @JoinColumn(name = "pacienteid", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Paciente paciente;

    @JoinColumn(name = "tipodetratamientoid", referencedColumnName = "id")
    @ManyToOne(optional = false)
    @NotNull(message = "Seleccione un tipo de tratamiento")
    private TipoDeTratamiento tipoDeTratamiento;

    @OneToOne
    @JoinColumn(name = "tratamientoasociadoid")
    private Tratamiento tratamientoAsociado;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tratamiento")
    private List<Estudio> estudioList;

    /**
     * Representa el número de sesiones que fueron marcadas como
     * Transcurridas=true, es decir, la cantidad de sesiones a las que el
     * paciente asistió.
     */
    @Transient
    private int sesionesRealizadas;

    public Tratamiento() {
    }

    public Tratamiento(Integer id, short cantidaddesesiones, boolean particular) {
        this.id = id;
        this.cantidadDeSesiones = cantidaddesesiones;
        this.particular = particular;
    }

    public Tratamiento(Paciente paciente) {
        this.paciente = paciente;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDiagnostico() {
        return diagnostico;
    }

    public void setDiagnostico(String diagnostico) {
        this.diagnostico = diagnostico;
    }

    public boolean getParticular() {
        return particular;
    }

    public void setParticular(boolean particular) {
        this.particular = particular;
    }

    public Boolean getFinalizado() {
        return finalizado;
    }

    public void setFinalizado(Boolean finalizado) {
        this.finalizado = finalizado;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public short getCantidadDeSesiones() {
        return cantidadDeSesiones;
    }

    public void setCantidadDeSesiones(short cantidadDeSesiones) {
        this.cantidadDeSesiones = cantidadDeSesiones;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Short getSesionesAFavor() {
        return sesionesAFavor;
    }

    public void setSesionesAFavor(Short sesionesAFavor) {
        this.sesionesAFavor = sesionesAFavor;
    }

    public Boolean getSesionesAFavorUsadas() {
        return sesionesAFavorUsadas;
    }

    public void setSesionesAFavorUsadas(Boolean sesionesAFavorUsadas) {
        this.sesionesAFavorUsadas = sesionesAFavorUsadas;
    }

    public Date getFechaUltimaAutorizacion() {
        return fechaUltimaAutorizacion;
    }

    public void setFechaUltimaAutorizacion(Date fechaUltimaAutorizacion) {
        this.fechaUltimaAutorizacion = fechaUltimaAutorizacion;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public TipoDeTratamiento getTipoDeTratamiento() {
        return tipoDeTratamiento;
    }

    public void setTipoDeTratamiento(TipoDeTratamiento tipoDeTratamiento) {
        this.tipoDeTratamiento = tipoDeTratamiento;
    }

    public Tratamiento getTratamientoAsociado() {
        return tratamientoAsociado;
    }

    public void setTratamientoAsociado(Tratamiento tratamientoAsociado) {
        this.tratamientoAsociado = tratamientoAsociado;
    }

    public int getSesionesRealizadas() {
        return sesionesRealizadas;
    }

    public void setSesionesRealizadas(int sesionesRealizadas) {
        this.sesionesRealizadas = sesionesRealizadas;
    }

    public String getMedicoDerivante() {
        return medicoDerivante;
    }

    public void setMedicoDerivante(String medicoDerivante) {
        this.medicoDerivante = medicoDerivante;
    }

    public Boolean getAccidentetrabajo() {
        return accidenteTrabajo;
    }

    public void setAccidentetrabajo(Boolean accidenteTrabajo) {
        this.accidenteTrabajo = accidenteTrabajo;
    }

    public List<Estudio> getEstudioList() {
        return estudioList;
    }

    public void setEstudioList(List<Estudio> estudioList) {
        this.estudioList = estudioList;
    }

    public void addEstudio(Estudio e) {
        estudioList.add(e);
    }

    public String toStringSesionesAFavor(){
        return "(" + this.sesionesAFavor.toString() + ") " + this.toString();
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Tratamiento)) {
            return false;
        }
        Tratamiento other = (Tratamiento) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        String result = "" + tipoDeTratamiento;
        result += (diagnostico == null) ? "" : "-" + diagnostico;
        if (result.length() > 30) {
            result = result.substring(0, 27) + "...";
        }
        return result;
    }

}
