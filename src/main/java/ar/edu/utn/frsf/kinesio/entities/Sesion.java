package ar.edu.utn.frsf.kinesio.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
//@EntityListeners(SesionFacade.class)
@Table(name = "sesion")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Sesion.findAll", query = "SELECT s FROM Sesion s"),
    @NamedQuery(name = "Sesion.findById", query = "SELECT s FROM Sesion s WHERE s.idSesion = :id"),
    @NamedQuery(name = "Sesion.findByNumeroDeSesion", query = "SELECT s FROM Sesion s WHERE s.numeroDeSesion = :numeroDeSesion"),
    @NamedQuery(name = "Sesion.findByFechaHoraInicio", query = "SELECT s FROM Sesion s WHERE s.fechaHoraInicio = :fechaHoraInicio"),
    @NamedQuery(name = "Sesion.countByRangoFechaHoraInicio", query = "SELECT COUNT(s) FROM Sesion s WHERE s.fechaHoraInicio BETWEEN :fechaDesde AND :fechaHasta"),
    @NamedQuery(name = "Sesion.findSesionesByAgendaYRangoFechas", query = "SELECT s FROM Sesion s WHERE s.agenda = :agenda AND s.fechaHoraInicio BETWEEN :fechaDesde AND :fechaHasta"),
    @NamedQuery(name = "Sesion.findByTratamiento", query = "SELECT s FROM Sesion s WHERE s.tratamiento = :tratamiento ORDER BY s.fechaHoraInicio"),
    @NamedQuery(name = "Sesion.findByTratamientoQueCuentan",
            query = "SELECT s FROM Sesion s WHERE s.tratamiento = :tratamiento and s.cuenta = TRUE"),
    @NamedQuery(name = "Sesion.countByTratamientoQueCuentan",
            query = "SELECT COUNT(s) FROM Sesion s WHERE s.tratamiento = :tratamiento and s.cuenta = TRUE"),
    @NamedQuery(name = "Sesion.countByTratamientoNoTranscurridas",
            query = "SELECT COUNT(s) FROM Sesion s WHERE s.tratamiento = :tratamiento and s.transcurrida = FALSE"),
    @NamedQuery(name = "Sesion.countByTratamientoQueCuentanTranscurridas",
            query = "SELECT COUNT(s) FROM Sesion s WHERE s.tratamiento = :tratamiento and s.cuenta = TRUE and s.transcurrida = TRUE"),
    @NamedQuery(name = "Sesion.findByAgenda", query = "SELECT s FROM Sesion s WHERE s.agenda = :agenda"),
    @NamedQuery(name = "Sesion.findByTranscurrida", query = "SELECT s FROM Sesion s WHERE s.transcurrida = :transcurrida"),
    @NamedQuery(name = "Sesion.findByCuenta", query = "SELECT s FROM Sesion s WHERE s.cuenta = :cuenta"),
    @NamedQuery(name = "Sesion.countByPacientePorAnio",
            query = "SELECT COUNT(s) FROM Sesion s WHERE s.tratamiento.paciente = :paciente and s.cuenta = TRUE and s.tratamiento.particular = false and s.fechaHoraInicio BETWEEN :fechaDesde AND :fechaHasta"),
    @NamedQuery(name = "Sesion.findByRangoFechas", query = "SELECT s, d.nombregooglecontacts FROM Sesion s, DatosDeContacto d WHERE d.paciente = s.tratamiento.paciente AND d.desearecibirwhatsapp = TRUE AND s.fechaHoraInicio BETWEEN :fechaDesde AND :fechaHasta AND s.transcurrida = FALSE AND s.cuenta = TRUE")})

public class Sesion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer idSesion;

    @Column(name = "numerodesesion")
    private Short numeroDeSesion;

    @NotNull(message = "Ingrese la fecha y hora de la sesión")
    @Column(name = "fechahorainicio")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHoraInicio;

    @Column(name = "duracion")
    private Short duracion;

    @Column(name = "transcurrida")
    private Boolean transcurrida;

    @Column(name = "cuenta")
    private Boolean cuenta;

    @NotNull(message = "Seleccione una agenda")
    @JoinColumn(name = "agendaid", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Agenda agenda;

    @NotNull(message = "Seleccione un tratamiento")
    @JoinColumn(name = "tratamientoid", referencedColumnName = "id")
    @ManyToOne
    private Tratamiento tratamiento;

    public Sesion() {
    }

    public Sesion(Short numeroDeSesion) {
        this.numeroDeSesion = numeroDeSesion;
    }

    public Integer getIdSesion() {
        return idSesion;
    }

    public void setIdSesion(Integer idSesion) {
        this.idSesion = idSesion;
    }

    public Short getDuracion() {
        return duracion;
    }

    public void setDuracion(Short duracion) {
        this.duracion = duracion;
    }

    public Boolean getTranscurrida() {
        return transcurrida;
    }

    public void setTranscurrida(Boolean transcurrida) {
        this.transcurrida = transcurrida;
    }

    public Boolean getCuenta() {
        return cuenta;
    }

    public void setCuenta(Boolean cuenta) {
        this.cuenta = cuenta;
    }

    public Short getNumeroDeSesion() {
        return numeroDeSesion;
    }

    public void setNumeroDeSesion(Short numeroDeSesion) {
        this.numeroDeSesion = numeroDeSesion;
    }

    public Date getFechaHoraInicio() {
        return fechaHoraInicio;
    }

    public void setFechaHoraInicio(Date fechaHoraInicio) {
        this.fechaHoraInicio = fechaHoraInicio;
    }

    public Agenda getAgenda() {
        return agenda;
    }

    public void setAgenda(Agenda agenda) {
        this.agenda = agenda;
    }

    public Tratamiento getTratamiento() {
        return tratamiento;
    }

    public void setTratamiento(Tratamiento tratamiento) {
        this.tratamiento = tratamiento;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idSesion != null ? idSesion.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Sesion)) {
            return false;
        }
        Sesion other = (Sesion) object;
        if ((this.idSesion == null && other.idSesion != null) || (this.idSesion != null && !this.idSesion.equals(other.idSesion))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Sesion[ id=" + idSesion + " ]";
    }
}
