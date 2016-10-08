/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.edu.utn.frsf.kinesio.entities;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;
import javax.persistence.Basic;
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
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Entidad del modelo que representa un paciente del consultorio.
 */
@Entity
@Table(name = "paciente")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Paciente.findAll", query = "SELECT p FROM Paciente p"),
    @NamedQuery(name = "Paciente.findById", query = "SELECT p FROM Paciente p WHERE p.id = :id"),
    @NamedQuery(name = "Paciente.findByApellido", query = "SELECT p FROM Paciente p WHERE p.apellido = :apellido"),
    @NamedQuery(name = "Paciente.findByNombre", query = "SELECT p FROM Paciente p WHERE p.nombre = :nombre"),
    @NamedQuery(name = "Paciente.findByDni", query = "SELECT p FROM Paciente p WHERE p.dni = :dni"),
    @NamedQuery(name = "Paciente.autocompletar", 
            query = "SELECT p FROM Paciente p WHERE lower(p.apellido) LIKE :query OR lower(p.nombre) LIKE :query")})
public class Paciente implements Serializable {

    private static final long serialVersionUID = 1L;

    public Paciente() {
    }

    public Paciente(String apellido, String nombre, String dni) {
        this.apellido = apellido;
        this.nombre = nombre;
        this.dni = dni;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Basic(optional = false)
    @NotNull(message = "Ingrese el apellido del paciente")
    @Size(max = 50, message = "Apellido demasiado extenso, máximo de 50 caracteres")
    @Pattern(regexp = "[a-zA-ZñÑ'áéíóúÁÉÍÓÚ]+[a-zA-ZñÑ'áéíóúÁÉÍÓÚ\\s]*", message = "Error en el campo apellido: ingrese sólo caracteres alfabéticos o espacios")
    @Column(name = "apellido")
    private String apellido;

    @Basic(optional = false)
    @NotNull(message = "Ingrese el nombre del paciente")
    @Size(max = 50, message = "Nombre demasiado extenso, máximo de 50 caracteres")
    @Pattern(regexp = "[a-zA-ZñÑ'áéíóúÁÉÍÓÚ]+[a-zA-ZñÑ'áéíóúÁÉÍÓÚ\\s]*", message = "Error en el campo nombre: ingrese sólo caracteres alfabéticos o espacios")
    @Column(name = "nombre")
    private String nombre;

    @Pattern(regexp = "\\d{7,8}+", message = "Error en el campo DNI: solo se admite un número de 7 u 8 dígitos")
    @Column(name = "dni")
    private String dni;

    @Size(max = 50, message = "Máximo de 50 caracteres para el domicilio")
    @Column(name = "domicilio")
    private String domicilio;

    @Pattern(regexp = "\\d*", message = "Ingrese un teléfono válido")
    @Column(name = "telefono")
    private String telefono;

    @Pattern(regexp = "\\d*", message = "Ingrese un celular válido")
    @Column(name = "celular")
    private String celular;

    @Column(name = "fechadenacimiento")
    @Temporal(TemporalType.DATE)
    @Past(message = "Seleccione una fecha de nacimiento anterior al día actual")
    private Date fechaDeNacimiento;

    @Size(max = 20, message = "El N° de afiliado debe tener menos de 20 caracteres")
    @Column(name = "nroafiliadoos")
    private String nroAfiliadoOS;

    @Basic(optional = false)
    @NotNull
    @Column(name = "fechaalta")
    @Temporal(TemporalType.DATE)
    private Date fechaAlta;

    @JoinColumn(name = "obrasocialid", referencedColumnName = "id")
    @ManyToOne
    private ObraSocial obraSocial;

    @Transient
    private String edad;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getDomicilio() {
        return domicilio;
    }

    public void setDomicilio(String domicilio) {
        this.domicilio = domicilio;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public Date getFechaDeNacimiento() {
        return fechaDeNacimiento;
    }

    public void setFechaDeNacimiento(Date fechaDeNacimiento) {
        this.fechaDeNacimiento = fechaDeNacimiento;
        this.setEdad(null);
    }

    public String getNroAfiliadoOS() {
        return nroAfiliadoOS;
    }

    public void setNroAfiliadoOS(String nroAfiliadoOS) {
        this.nroAfiliadoOS = nroAfiliadoOS;
    }

    public Date getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(Date fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    public ObraSocial getObraSocial() {
        return obraSocial;
    }

    public void setObraSocial(ObraSocial obraSocial) {
        this.obraSocial = obraSocial;
    }

    public String getEdad() {
        if (edad == null) {
            this.calcularEdadPaciente();
        }
        return edad;
    }

    public void setEdad(String edad) {
        this.edad = edad;
    }

    private void calcularEdadPaciente() {
        if (fechaDeNacimiento != null) {
            LocalDate today = LocalDate.now();
            Period p = Period.between(fechaDeNacimiento.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), today);
            this.setEdad(String.valueOf(p.getYears()));
        } else {
            this.setEdad("-");
        }
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
        if (!(object instanceof Paciente)) {
            return false;
        }
        Paciente other = (Paciente) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return apellido + ", " + nombre;
    }
}
