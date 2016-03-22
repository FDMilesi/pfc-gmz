/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.edu.utn.frsf.kinesio.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
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
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;

/**
 *
 * @author Fran
 */
@Entity
@Table(name = "agenda")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Agenda.findAll", query = "SELECT a FROM Agenda a"),
    @NamedQuery(name = "Agenda.findById", query = "SELECT a FROM Agenda a WHERE a.id = :id"),
    @NamedQuery(name = "Agenda.findByProfesional", query = "SELECT a FROM Agenda a WHERE a.profesional = :profesional")})

public class Agenda implements Serializable, ScheduleModel {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Short id;
    
    @Size(max = 50)
    @Column(name = "profesional")
    private String profesional;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "agenda")
    private List<Sesion> sesiones;
    
    @JoinColumn(name = "especialidadid", referencedColumnName = "id")
    @ManyToOne
    private Especialidad especialidad;

    public Agenda() {
    }

    public Agenda(Short id) {
        this.id = id;
    }

    public Short getId() {
        return id;
    }

    public void setId(Short id) {
        this.id = id;
    }

    public String getProfesional() {
        return profesional;
    }

    public void setProfesional(String profesional) {
        this.profesional = profesional;
    }

    public List<Sesion> getSesiones() {
        return sesiones;
    }

    public void setSesiones(List<Sesion> sesiones) {
        this.sesiones = sesiones;
    }

    public Especialidad getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(Especialidad especialidad) {
        this.especialidad = especialidad;
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
        if (!(object instanceof Agenda)) {
            return false;
        }
        Agenda other = (Agenda) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Agenda[ id=" + id + " ]";
    }

    @Override
    public void addEvent(ScheduleEvent se) {
        sesiones.add((Sesion) se);
    }

    @Override
    public boolean deleteEvent(ScheduleEvent se) {
        return sesiones.remove((Sesion) se);
    }

    @Override
    public List<ScheduleEvent> getEvents() {
        return new ArrayList<ScheduleEvent>(sesiones);
    }

    @Override
    public ScheduleEvent getEvent(String id) {
        for(ScheduleEvent event : sesiones) {
			if(event.getId().equals(id))
				return event;
		}
		
		return null;
    }

    @Override
    public void updateEvent(ScheduleEvent se) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getEventCount() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}