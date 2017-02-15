package ar.edu.utn.frsf.kinesio.gestores;

import ar.edu.utn.frsf.kinesio.entities.DatosDeContacto;
import ar.edu.utn.frsf.kinesio.entities.Paciente;
import javax.ejb.EJB;
import javax.ejb.Stateless;

@Stateless
public class ABMPacienteFacade {

    @EJB
    PacienteFacade pacienteFacade;
    @EJB
    DatosDeContactoFacade datosDeContactoFacade;

    public ABMPacienteFacade() {
    }

    //No usado por ahora
    private void create(Paciente paciente, String nombreContacto, String idGoogleContact) {

        pacienteFacade.create(paciente);

        DatosDeContacto datosContacto = datosDeContactoFacade.initDatosDeContacto(paciente);
        datosContacto.setNombregooglecontacts(nombreContacto);
        datosContacto.setIdgooglecontacts(idGoogleContact);

        datosDeContactoFacade.create(datosContacto);
    }

    public void edit(Paciente paciente) {
        pacienteFacade.edit(paciente);

        DatosDeContacto datosDeContacto = datosDeContactoFacade.getDatosDeContactoByPaciente(paciente);

        if (datosDeContacto != null) {
            datosDeContacto.setSincronizado(Boolean.FALSE);
            datosDeContactoFacade.edit(datosDeContacto);
        }
    }

}
