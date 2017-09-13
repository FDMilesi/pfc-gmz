package ar.edu.utn.frsf.kinesio.gestores;

import ar.edu.utn.frsf.kinesio.entities.TipoTratamientoObraSocial;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 */
@Stateless
public class TipoTratamientoObraSocialFacade extends AbstractFacade<TipoTratamientoObraSocial> {

    public static final String DEFAULT_TOPE_SESIONES_ANIO_PREFERENCE_KEY = "defaultTopeSesionesAnio";

    @PersistenceContext(unitName = "ar.edu.utn.frsf_kinesio_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    public TipoTratamientoObraSocialFacade() {
        super(TipoTratamientoObraSocial.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

}
