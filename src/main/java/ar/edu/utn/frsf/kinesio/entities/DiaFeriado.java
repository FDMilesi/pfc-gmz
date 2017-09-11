package ar.edu.utn.frsf.kinesio.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "diaferiado")
@NamedQueries({
    @NamedQuery(name = "Diaferiado.findAll", query = "SELECT d FROM DiaFeriado d"),
    @NamedQuery(name = "Diaferiado.findById", query = "SELECT d FROM DiaFeriado d WHERE d.id = :id"),
    @NamedQuery(name = "Diaferiado.findAllDates", query = "SELECT d.dia FROM DiaFeriado d"),
    @NamedQuery(name = "Diaferiado.findByDia", query = "SELECT d FROM DiaFeriado d WHERE d.dia = :dia")})
public class DiaFeriado implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Short id;
    
    @Column(name = "dia")
    @NotNull(message = "Ingrese una fecha")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dia;

    @Size(max = 30)
    @Column(name = "descripcion")
    private String descripcion;
    
    public DiaFeriado() {
    }

    public DiaFeriado(Short id) {
        this.id = id;
    }

    public Short getId() {
        return id;
    }

    public void setId(Short id) {
        this.id = id;
    }

    public Date getDia() {
        return dia;
    }

    public void setDia(Date dia) {
        this.dia = dia;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
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
        if (!(object instanceof DiaFeriado)) {
            return false;
        }
        DiaFeriado other = (DiaFeriado) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ar.edu.utn.frsf.kinesio.entities.Diaferiado[ id=" + id + " ]";
    }
    
}
