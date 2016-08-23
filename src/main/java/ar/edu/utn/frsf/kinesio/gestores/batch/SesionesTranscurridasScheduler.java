package ar.edu.utn.frsf.kinesio.gestores.batch;

import java.util.Properties;
import javax.batch.runtime.BatchRuntime;
import javax.ejb.Schedule;
import javax.ejb.Singleton;

@Singleton
public class SesionesTranscurridasScheduler {
    
    @Schedule(hour = "2", minute = "59", second = "59")
    public void sesionesTranscurridasJob(){
        BatchRuntime.getJobOperator().start("sesionesTranscurridasJob", new Properties());
    }
}
