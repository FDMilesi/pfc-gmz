package ar.edu.utn.frsf.kinesio.entities;

import java.io.Serializable;
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
import javax.validation.constraints.Size;

@Entity
@Table(name = "datodecontacto")
@NamedQueries({
    @NamedQuery(name = "DatosDeContacto.findAll", query = "SELECT d FROM DatosDeContacto d"),
    @NamedQuery(name = "DatosDeContacto.findById", query = "SELECT d FROM DatosDeContacto d WHERE d.id = :id"),
    @NamedQuery(name = "DatosDeContacto.findByIdgooglecontacts", query = "SELECT d FROM DatosDeContacto d WHERE d.idgooglecontacts = :idgooglecontacts"),
    @NamedQuery(name = "DatosDeContacto.findByNombregooglecontacts", query = "SELECT d FROM DatosDeContacto d WHERE d.nombregooglecontacts = :nombregooglecontacts"),
    @NamedQuery(name = "DatosDeContacto.findByPaciente", query = "SELECT d FROM DatosDeContacto d WHERE d.paciente = :paciente"),
    @NamedQuery(name = "DatosDeContacto.findByDesearecibirwhatsapp", query = "SELECT d FROM DatosDeContacto d WHERE d.desearecibirwhatsapp = :desearecibirwhatsapp")})
public class DatosDeContacto implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    
    @Size(max = 100)
    @Column(name = "idgooglecontacts")
    private String idgooglecontacts;
    
    @Size(max = 30)
    @Column(name = "nombregooglecontacts")
    private String nombregooglecontacts;
    
    @Column(name = "desearecibirwhatsapp")
    private Boolean desearecibirwhatsapp;
    
    
    @JoinColumn(name = "pacienteid", referencedColumnName = "id")
    @ManyToOne
    private Paciente paciente;

    public DatosDeContacto() {
    }

    public DatosDeContacto(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIdgooglecontacts() {
        return idgooglecontacts;
    }

    public void setIdgooglecontacts(String idgooglecontacts) {
        this.idgooglecontacts = idgooglecontacts;
    }

    public String getNombregooglecontacts() {
        return nombregooglecontacts;
    }

    public void setNombregooglecontacts(String nombregooglecontacts) {
        this.nombregooglecontacts = nombregooglecontacts;
    }

    public Boolean getDesearecibirwhatsapp() {
        return desearecibirwhatsapp;
    }

    public void setDesearecibirwhatsapp(Boolean desearecibirwhatsapp) {
        this.desearecibirwhatsapp = desearecibirwhatsapp;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente pacienteId) {
        this.paciente = pacienteId;
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
        if (!(object instanceof DatosDeContacto)) {
            return false;
        }
        DatosDeContacto other = (DatosDeContacto) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ar.edu.utn.frsf.kinesio.entities.Datodecontacto[ id=" + id + " ]";
    }
    
}
