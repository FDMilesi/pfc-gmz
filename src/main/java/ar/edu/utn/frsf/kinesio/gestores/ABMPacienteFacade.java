package ar.edu.utn.frsf.kinesio.gestores;

import ar.edu.utn.frsf.kinesio.entities.DatosDeContacto;
import ar.edu.utn.frsf.kinesio.entities.Paciente;
import com.google.gdata.util.ServiceException;
import java.io.IOException;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;

@Stateless
public class ABMPacienteFacade {

    @EJB
    PacienteFacade pacienteFacade;
    @EJB
    DatosDeContactoFacade datosDeContactoFacade;
    @EJB
    GoogleContactsFacade googleContactsFacade;

    public ABMPacienteFacade() {
    }

    public void create(Paciente paciente, String nombreContacto, String idGoogleContact) {

        pacienteFacade.create(paciente);

        DatosDeContacto datosContacto = datosDeContactoFacade.initDatosDeContacto(paciente);
        datosContacto.setNombregooglecontacts(nombreContacto);
        datosContacto.setDesearecibirwhatsapp(this.pacienteTieneCelular(paciente));//TODO: que sea un checkbox que cargue el usr
        datosContacto.setIdgooglecontacts(idGoogleContact);

        datosDeContactoFacade.create(datosContacto);
    }

    //Posición 0 nombre contacto, posición 1 id de google
    public String[] createGoogleContact(Paciente paciente) throws GoogleContactsFacade.CredencialesNoEncontradasException {

        String[] retorno = new String[2];

        boolean tieneCelular = this.pacienteTieneCelular(paciente);

        retorno[0] = googleContactsFacade.generarNombreContacto(paciente, tieneCelular);
        String idGoogleContact = "";

        try {
            idGoogleContact = googleContactsFacade.createContact(paciente, tieneCelular, retorno[0]);
        } catch (IOException | ServiceException ex) {
            throw new EJBException(ex);
        }

        retorno[1] = idGoogleContact;

        return retorno;
    }

    public void edit(Paciente paciente, String nuevoNombreContacto) {
        pacienteFacade.edit(paciente);

        DatosDeContacto datosDeContacto = datosDeContactoFacade.getDatosDeContactoByPaciente(paciente);
        datosDeContacto.setNombregooglecontacts(nuevoNombreContacto);
        datosDeContacto.setDesearecibirwhatsapp(this.pacienteTieneCelular(paciente));

        datosDeContactoFacade.edit(datosDeContacto);
    }

    public void edit(Paciente paciente, String nombreContacto, String idGoogleContact) {
        pacienteFacade.edit(paciente);

        DatosDeContacto datosContacto = datosDeContactoFacade.initDatosDeContacto(paciente);
        datosContacto.setNombregooglecontacts(nombreContacto);
        datosContacto.setDesearecibirwhatsapp(this.pacienteTieneCelular(paciente));//TODO: que sea un checkbox que cargue el usr
        datosContacto.setIdgooglecontacts(idGoogleContact);

        datosDeContactoFacade.create(datosContacto);
    }

    public String editGoogleContact(Paciente paciente, String idGoogleContact) throws GoogleContactsFacade.CredencialesNoEncontradasException {
        boolean ahoraTieneCelular = pacienteTieneCelular(paciente);

        String nuevoNombreContacto = "";

        try {
            nuevoNombreContacto = googleContactsFacade.editContact(paciente, idGoogleContact, ahoraTieneCelular);
        } catch (IOException | ServiceException ex) {
            throw new EJBException(ex);
        }

        return nuevoNombreContacto;
    }

    /**
     * *
     * Elimina el paciente de la BD, junto con sus datos de contacto si es que
     * tiene. Si el paciente posee datos de contacto, retorna su id de Google
     * Contacts
     *
     * @param paciente
     * @return
     */
    public String remove(Paciente paciente) {
        String idGoogleContacts = new String();

        //Antes de eliminar consigo el id de google contactos del paciente (si es que existe en google)
        DatosDeContacto datosDeContacto = datosDeContactoFacade.getDatosDeContactoByPaciente(paciente);
        if (datosDeContacto != null) {
            idGoogleContacts = datosDeContacto.getIdgooglecontacts();
        }

        //Se elimina en cascada los datos de contacto
        pacienteFacade.remove(paciente);

        return idGoogleContacts;
    }

    public void removeGoogleContact(String idGoogleContacts) throws GoogleContactsFacade.CredencialesNoEncontradasException {
        if (!idGoogleContacts.isEmpty()) {
            try {
                googleContactsFacade.deleteContact(idGoogleContacts);
            } catch (IOException | ServiceException ex) {
                throw new EJBException(ex);
            }
        }
    }

    private boolean pacienteTieneCelular(Paciente paciente) {
        if (paciente.getCelular() != null) {
            if (!paciente.getCelular().isEmpty()) {
                return true;
            }
        }
        return false;
    }

    public DatosDeContacto getDatosDeContacto(Paciente paciente) {
        return datosDeContactoFacade.getDatosDeContactoByPaciente(paciente);
    }

}
