package ar.edu.utn.frsf.kinesio.test.util;

import java.util.Collections;
import java.util.Enumeration;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.TreeMap;

public class ResourceBundleArquillianHelper {

    static ResourceBundle myBundle = new ResourceBundle() {
        @Override
        protected void setParent(ResourceBundle parent) {
            // overwritten to do nothing, otherwise ResourceBundle.getBundle(String)
            //  gets into an infinite loop!
        }
        TreeMap<String, String> tm = new TreeMap<String, String>() {
            {
                put("SesionUpdated", "");
                put("SesionCreated", "");
                put("SesionDeleted", "");
            }
        };

        @Override
        protected Object handleGetObject(String key) {
            return tm.get(key);
        }

        @Override
        public Enumeration<String> getKeys() {
            return Collections.enumeration(tm.keySet());
        }
    };

    static ResourceBundle.Control myControl = new ResourceBundle.Control() {
        public ResourceBundle newBundle(String baseName, Locale locale, String format,
                ClassLoader loader, boolean reload) {
            return myBundle;
        }
    };

    public static void initBundle() {
        ResourceBundle.getBundle("/Bundle", myControl);
    }
}
