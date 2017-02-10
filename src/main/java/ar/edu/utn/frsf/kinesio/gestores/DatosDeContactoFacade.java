package ar.edu.utn.frsf.kinesio.gestores;

import ar.edu.utn.frsf.kinesio.entities.DatosDeContacto;
import ar.edu.utn.frsf.kinesio.entities.Paciente;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class DatosDeContactoFacade extends AbstractFacade<DatosDeContacto> {

    @PersistenceContext(unitName = "ar.edu.utn.frsf_kinesio_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public DatosDeContactoFacade() {
        super(DatosDeContacto.class);
    }

    public DatosDeContacto getDatosDeContactoByPaciente(Paciente paciente) {
        List<DatosDeContacto> lista = getEntityManager().createNamedQuery("DatosDeContacto.findByPaciente")
                .setParameter("paciente", paciente).getResultList();
        
        if (!lista.isEmpty())
            return lista.get(0);
        else
            return null;
    }

    public DatosDeContacto initDatosDeContacto(Paciente paciente) {
        DatosDeContacto datosContacto = new DatosDeContacto();
        datosContacto.setPaciente(paciente);
        return datosContacto;
    }
}
