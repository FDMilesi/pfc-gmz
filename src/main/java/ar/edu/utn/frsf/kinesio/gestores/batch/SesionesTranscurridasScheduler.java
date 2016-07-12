package ar.edu.utn.frsf.kinesio.gestores.batch;

import java.util.Properties;
import javax.batch.runtime.BatchRuntime;
import javax.ejb.Schedule;
import javax.ejb.Singleton;

@Singleton
public class SesionesTranscurridasScheduler {
    
//    @Schedule(hour = "21", minute = "35", second = "20")
    public void sesionesTranscurridasJob(){
        BatchRuntime.getJobOperator().start("sesionesTranscurridasJob", new Properties());
    }
}
