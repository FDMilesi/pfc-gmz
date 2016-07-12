package ar.edu.utn.frsf.kinesio.gestores.batch;

import ar.edu.utn.frsf.kinesio.gestores.SesionFacade;
import javax.batch.api.AbstractBatchlet;
import javax.batch.runtime.BatchStatus;
import javax.ejb.EJB;
import javax.inject.Named;

@Named("sesionesTranscurridasBatchlet")
public class SesionesTranscurridasBatchlet extends AbstractBatchlet {

    @EJB
    SesionFacade sesionFacade;

    public SesionesTranscurridasBatchlet() {
    }

    @Override
    public String process() {
        sesionFacade.marcarSesionesTranscurridas();
        return BatchStatus.COMPLETED.toString();
    }

}
