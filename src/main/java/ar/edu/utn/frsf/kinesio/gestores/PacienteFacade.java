package ar.edu.utn.frsf.kinesio.gestores;

import ar.edu.utn.frsf.kinesio.entities.Paciente;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class PacienteFacade extends AbstractFacade<Paciente> {

    @PersistenceContext(unitName = "ar.edu.utn.frsf_kinesio_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public void setEm(EntityManager em) {
        this.em = em;
    }

    public PacienteFacade() {
        super(Paciente.class);
    }

    public Paciente initPaciente() {
        Paciente paciente = new Paciente();
        paciente.setFechaAlta(new Date());
        return paciente;
    }

    public List<Paciente> getPacientesAutocompletar(String query) {
        return getEntityManager().createNamedQuery("Paciente.autocompletar")
                .setParameter("query", query+"%").getResultList();
    }

    public boolean puedoEliminarPaciente(Paciente selected) {
        return ((Number)getEntityManager().createNamedQuery("Tratamiento.countByPaciente")
                .setParameter("paciente", selected).getSingleResult()).intValue() == 0;
    }
}
