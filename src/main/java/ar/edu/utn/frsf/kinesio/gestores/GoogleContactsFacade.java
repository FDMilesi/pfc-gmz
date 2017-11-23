package ar.edu.utn.frsf.kinesio.gestores;

import ar.edu.utn.frsf.kinesio.entities.DatosDeContacto;
import ar.edu.utn.frsf.kinesio.entities.Paciente;
import ar.edu.utn.frsf.kinesio.gestores.util.Configuracion;
import ar.edu.utn.frsf.kinesio.gestores.util.OAuthUtil;
import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.Credential;
import com.google.gdata.client.contacts.ContactsService;
import com.google.gdata.data.contacts.ContactEntry;
import com.google.gdata.data.contacts.GroupMembershipInfo;
import com.google.gdata.data.extensions.FullName;
import com.google.gdata.data.extensions.Name;
import com.google.gdata.data.extensions.PhoneNumber;
import com.google.gdata.util.ServiceException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;

@Stateless
public class GoogleContactsFacade {

    static final String CUENTA_GOOGLE = Configuracion.getInstance().getGoogleContactsProperties().getProperty("googlecontacts.cuenta");
    static final String BASE_GOOGLE_CONTACTS_URL = "https://www.google.com/m8/feeds/contacts/" + CUENTA_GOOGLE + "/full";
    static final String CUENTA_GOOGLE_USER_ID = Configuracion.getInstance().getGoogleContactsProperties().getProperty("googlecontacts.credenciales.userId");
    static final String ID_MY_CONTACTS_GROUP = "http://www.google.com/m8/feeds/groups/" + CUENTA_GOOGLE + "/base/6";

    @EJB
    DatosDeContactoFacade datosDeContactoFacade;

    public GoogleContactsFacade() {
    }

    private ContactsService initService() throws IOException, CredencialesNoEncontradasException {

        AuthorizationCodeFlow authFlow = OAuthUtil.initializeFlow();
        Credential credential = authFlow.loadCredential(CUENTA_GOOGLE_USER_ID);

        if (credential == null) {
            throw new CredencialesNoEncontradasException("No se encontraron las credenciales de acceso a Google Contacts para el usuario indicado");
        }

//        System.out.println("refresh token: " + credential.getRefreshToken());
//        System.out.println("expiracion --------->" + credential.getExpiresInSeconds());
        //una vez ya obtenido el refresh token, utilizo esta linea para crear nuevos acces tokens
        credential.refreshToken();

//        System.out.println("expiracion --------->" + credential.getExpiresInSeconds());
        ContactsService myService = new ContactsService("exampleCo-exampleApp-1.0");
        myService.getRequestFactory().setHeader("User-Agent", "exampleCo-exampleApp-1.0");
        myService.setOAuth2Credentials(credential);

        return myService;
    }

    //Retorna el id de google para el contacto (no el nombre)
    private String createContact(Paciente paciente, boolean tieneCelular, String contactName) throws MalformedURLException, IOException, ServiceException, CredencialesNoEncontradasException {
        ContactEntry contact = new ContactEntry();

        URL feedUrl = new URL(BASE_GOOGLE_CONTACTS_URL);

        ContactsService service = this.initService();

        Name name = new Name();

        if (tieneCelular) {
            name.setFullName(new FullName(contactName, null));
            //name.setGivenName(new GivenName(paciente.getNombre(), null));
            //name.setFamilyName(new FamilyName(paciente.getApellido(), null));

            PhoneNumber telefono = new PhoneNumber();
            telefono.setPhoneNumber(paciente.getCelular());
            telefono.setPrimary(true);
            telefono.setRel("http://schemas.google.com/g/2005#mobile");
            contact.addPhoneNumber(telefono);
        } //Si no tiene teléfono aún
        else {
            name.setFullName(new FullName(contactName, null));
        }

        contact.setName(name);
        contact.addGroupMembershipInfo(new GroupMembershipInfo(false, ID_MY_CONTACTS_GROUP));

        ContactEntry createdContact = service.insert(feedUrl, contact);

        //Obtenego el id del contacto
        String[] arrayContactId = createdContact.getId().split("/");

        return arrayContactId[arrayContactId.length - 1];
    }

    //Retorna el nuevo nombre del contacto
    public String editContact(Paciente paciente, String contactId, boolean ahoraTieneCelular) throws MalformedURLException, IOException, ServiceException, CredencialesNoEncontradasException {
        URL feedUrl = new URL(BASE_GOOGLE_CONTACTS_URL + "/" + contactId);
        ContactsService service = this.initService();

        ContactEntry contacto = service.getEntry(feedUrl, ContactEntry.class);

        String nuevoNombreContacto = this.generarNombreContacto(paciente, ahoraTieneCelular);

        //Actualizo el celular
        List<PhoneNumber> listaTelefonos = contacto.getPhoneNumbers();
        //Si le ahora tiene celular, me fijo si tenía
        if (ahoraTieneCelular) {
            boolean tienePrimaryPhone = false;
            for (PhoneNumber telefono : listaTelefonos) //Si tenía celular, se lo actualizo
            {
                if (telefono.getPrimary()) {
                    telefono.setPhoneNumber(paciente.getCelular());
                    tienePrimaryPhone = true;
                }
            }

            //Si no tenía celular y le agrego uno
            if (listaTelefonos.isEmpty() || !tienePrimaryPhone) {
                PhoneNumber telefono = new PhoneNumber();
                telefono.setPhoneNumber(paciente.getCelular());
                telefono.setPrimary(true);
                telefono.setRel("http://schemas.google.com/g/2005#mobile");
                contacto.addPhoneNumber(telefono);
            }
            //Si ahora no tiene celular
        } else {
            listaTelefonos.clear();
        }
        //Fin actualizo el celular

        //Actualizo el nombre
        contacto.getName().getFullName().setValue(nuevoNombreContacto);
        //Fin nombre

        //Actualizo el contacto
        URL editUrl = new URL(contacto.getEditLink().getHref());
        service.update(editUrl, contacto);

        return nuevoNombreContacto;
    }

    public void deleteContact(String contactId) throws MalformedURLException, IOException, ServiceException, CredencialesNoEncontradasException {
        URL feedUrl = new URL(BASE_GOOGLE_CONTACTS_URL + "/" + contactId);
        ContactsService service = this.initService();

        ContactEntry contacto = service.getEntry(feedUrl, ContactEntry.class);

        if (contacto != null) {
            contacto.delete();
        }
    }

    public String sincronizarContactos() throws CredencialesNoEncontradasException {

        //Primero con los pacientes que no tienen datos
        List<Paciente> pacientesSinContacto = datosDeContactoFacade.getPacientesSinDatosDeContacto();

        for (Paciente paciente : pacientesSinContacto) {

            boolean tieneCelular = this.pacienteTieneCelular(paciente);

            String nombreContacto = this.generarNombreContacto(paciente, tieneCelular);
            String idGoogleContact;

            try {

                idGoogleContact = this.createContact(paciente, tieneCelular, nombreContacto);

                DatosDeContacto datosContacto = datosDeContactoFacade.initDatosDeContacto(paciente);
                datosContacto.setNombregooglecontacts(nombreContacto);
                datosContacto.setDesearecibirwhatsapp(tieneCelular);
                datosContacto.setIdgooglecontacts(idGoogleContact);

                datosDeContactoFacade.create(datosContacto);
                
            } catch (IOException | ServiceException ex) {
                StringBuilder msjError = new StringBuilder("Error al sincronizar paciente sin dato de contacto. ");
                msjError.append("Paciente: ");
                msjError.append(paciente.toString());
                msjError.append(". idPaciente: ");
                msjError.append(paciente.getId());
                msjError.append(". nombreContacto: ");
                msjError.append(nombreContacto);
                Logger.getLogger(GoogleContactsFacade.class.getName()).log(Level.SEVERE, msjError.toString(), ex);
            }
        }

        //Ahora con los no sincronizados
        List<DatosDeContacto> datosDeContactoNoSincronizados = datosDeContactoFacade.getDatosDeContactoNoSincronizados();
        boolean ahoraTieneCelular;
        String nuevoNombre;
        for (DatosDeContacto datoDeContacto : datosDeContactoNoSincronizados) {
            try {

                ahoraTieneCelular = this.pacienteTieneCelular(datoDeContacto.getPaciente());
                
                nuevoNombre = this.editContact(datoDeContacto.getPaciente(), datoDeContacto.getIdgooglecontacts(), ahoraTieneCelular);

                datoDeContacto.setSincronizado(true);
                datoDeContacto.setNombregooglecontacts(nuevoNombre);
                datosDeContactoFacade.edit(datoDeContacto);
                
            } catch (IOException | ServiceException ex) {
                StringBuilder msjError = new StringBuilder("Error al sincronizar paciente des-sincronizado. ");
                msjError.append("DatoDeContactoId: ");
                msjError.append(datoDeContacto.getId());
                msjError.append(". NombreContacto: ");
                msjError.append(datoDeContacto.getNombregooglecontacts());
                if (datoDeContacto.getPaciente() != null){
                    msjError.append(". Paciente: ");
                    msjError.append(datoDeContacto.getPaciente().toString());
                    msjError.append(". idPaciente: ");
                    msjError.append(datoDeContacto.getPaciente().getId());
                }

                Logger.getLogger(GoogleContactsFacade.class.getName()).log(Level.SEVERE, msjError.toString(), ex);
            }
        }

        //Ahora los eliminados
        List<DatosDeContacto> eliminados = datosDeContactoFacade.getDatosContactoPacientesEliminados();
        for (DatosDeContacto eliminado : eliminados) {
            try {
                
                this.deleteContact(eliminado.getIdgooglecontacts());
                datosDeContactoFacade.remove(eliminado);
                
            } catch (IOException | ServiceException ex) {
                StringBuilder msjError = new StringBuilder("Error al sincronizar paciente eliminado. ");
                msjError.append("DatoDeContactoId: ");
                msjError.append(eliminado.getId());
                msjError.append(". NombreContacto: ");
                msjError.append(eliminado.getNombregooglecontacts());

                Logger.getLogger(GoogleContactsFacade.class.getName()).log(Level.SEVERE, msjError.toString(), ex);
            }
        }

        String resultado = String.format(
                "Se sincronizaron %1$d pacientes nuevos, %2$d pacientes editados y %3$d pacientes eliminados", 
                pacientesSinContacto.size(),
                datosDeContactoNoSincronizados.size(),
                eliminados.size());
        
        return resultado;
    }

    private boolean pacienteTieneCelular(Paciente paciente) {
        if (paciente.getCelular() != null) {
            if (!paciente.getCelular().isEmpty()) {
                return true;
            }
        }
        return false;
    }

    private String generarNombreContacto(Paciente paciente, boolean tieneCelular) {
        String resultado;
        if (tieneCelular) {
            resultado = paciente.getCelular() + " - " + paciente.getApellido();
        } else {
            resultado = paciente.getNombre() + " " + paciente.getApellido();
        }
        return resultado;
    }

    public class CredencialesNoEncontradasException extends Exception {

        public CredencialesNoEncontradasException(String message) {
            super(message);
        }

    }
}
