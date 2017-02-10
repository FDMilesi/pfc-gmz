package ar.edu.utn.frsf.kinesio.gestores.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Configuracion {

    private Properties estudiosProperties;
    private Properties googleContactsProperties;

    private Configuracion() {

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream inputEstudios = classLoader.getResourceAsStream("estudiosConfig.properties");
        InputStream inputGoogleContacts = classLoader.getResourceAsStream("googleContactsConfig.properties");

        estudiosProperties = new Properties();
        googleContactsProperties = new Properties();

        try {

            estudiosProperties.load(inputEstudios);

            googleContactsProperties.load(inputGoogleContacts);

        } catch (IOException ex) {
            Logger.getLogger(Configuracion.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static Configuracion getInstance() {
        return ConfiguracionHolder.INSTANCE;
    }

    private static class ConfiguracionHolder {

        private static final Configuracion INSTANCE = new Configuracion();
    }

    public Properties getEstudiosProperties() {
        return estudiosProperties;
    }

    public Properties getGoogleContactsProperties() {
        return googleContactsProperties;
    }

}
