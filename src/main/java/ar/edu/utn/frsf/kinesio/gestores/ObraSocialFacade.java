package ar.edu.utn.frsf.kinesio.gestores;

import ar.edu.utn.frsf.kinesio.entities.ObraSocial;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 */
@Stateless
public class ObraSocialFacade extends AbstractFacade<ObraSocial> {

    @PersistenceContext(unitName = "ar.edu.utn.frsf_kinesio_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ObraSocialFacade() {
        super(ObraSocial.class);
    }

    public ObraSocial getObraSocialIAPOS() {
        return (ObraSocial) this.getEntityManager()
                .createNamedQuery("ObraSocial.findByNombre")
                .setParameter("nombre", "IAPOS")
                .getResultList().get(0);
    }
    
    public ObraSocial getObraSocialIAPOSAccidenteTrabajo() {
        return (ObraSocial) this.getEntityManager()
                .createNamedQuery("ObraSocial.findByNombre")
                .setParameter("nombre", "IAPOS - ACCIDENTE DE TRABAJO")
                .getResultList().get(0);
    }
}
