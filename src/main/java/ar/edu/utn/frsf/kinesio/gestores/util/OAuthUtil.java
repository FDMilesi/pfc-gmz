package ar.edu.utn.frsf.kinesio.gestores.util;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;

/**
 * user4 -> fernando.milesi.8 user5 -> pfc.gmz
 */
public class OAuthUtil {

    static final String CALLBACK_SERVLET_PATH = "/TestContactsGoogle/CallbackServlet";
    static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    static final JacksonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final List<String> SCOPES = Arrays.asList("https://www.google.com/m8/feeds/");
    private static final java.io.File DATA_STORE_DIR = new File(Configuracion
            .getInstance().getGoogleContactsProperties().getProperty("googlecontacts.credenciales.path"));
//            new java.io.File(
//            System.getProperty("user.home"), ".credentials/otraPrueba");
    private static final String FILE_NAME_CLIENT_SECRETS = Configuracion.getInstance().getGoogleContactsProperties().getProperty("google.api.clienteSecrets.fileName");

    private static FileDataStoreFactory DATA_STORE_FACTORY;
    static String MAIN_SERVLET_PATH = "/deltagestion/faces";

    static {
        try {
            DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);
        } catch (IOException t) {
            Logger.getLogger(OAuthUtil.class.getName()).log(Level.SEVERE, null, t);
        }
    }

    static public GoogleAuthorizationCodeFlow initializeFlow() throws IOException {
        GoogleAuthorizationCodeFlow flow
                = new GoogleAuthorizationCodeFlow.Builder(
                        HTTP_TRANSPORT,
                        JSON_FACTORY,
                        getClientSecrets(),
                        SCOPES)
                .setDataStoreFactory(DATA_STORE_FACTORY)
                .setAccessType("offline")
                .build();
        return flow;
    }

    private static GoogleClientSecrets getClientSecrets() throws IOException {
        InputStream inputStream = OAuthUtil.class.getResourceAsStream(FILE_NAME_CLIENT_SECRETS);
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(inputStream));
        return clientSecrets;
    }

    public static String getRedirectURI(HttpServletRequest req) {
        GenericUrl requestUrl = new GenericUrl(req.getRequestURL().toString());
        requestUrl.setRawPath(CALLBACK_SERVLET_PATH);
        return requestUrl.build();
    }

}
