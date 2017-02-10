package ar.edu.utn.frsf.kinesio.gestores.batch;

import ar.edu.utn.frsf.kinesio.gestores.NotificacionFacade;
import javax.batch.api.AbstractBatchlet;
import javax.batch.runtime.BatchStatus;
import javax.ejb.EJB;
import javax.inject.Named;

@Named("notificacionesBatchlet")
public class NotificacionesBatchlet extends AbstractBatchlet {

    @EJB
    NotificacionFacade notificacionFacade;

    public NotificacionesBatchlet() {
    }

    @Override
    public String process() {
        notificacionFacade.NotificarEntregaPlanillaAlCirculo();
        notificacionFacade.NotificarEntregaPlanillaIaposAlCirculo();
        notificacionFacade.NotificarSiPacienteDebeOrden();
        return BatchStatus.COMPLETED.toString();
    }

}
