package fai.utb.gui;

import java.util.ResourceBundle;

/**
 * @author Šimon Zouvala
 */
public class I18n {
    private final ResourceBundle bundle;
    private final Class<?> clazz;
    private final boolean debug;

    public I18n(Class<?> clazz) {
        String bundleName ="Bundle";
        this.bundle = ResourceBundle.getBundle(bundleName);
        this.clazz = clazz;
        this.debug = false;
    }

    public String getString(String key) {
        String fullKey = clazz.getSimpleName() + "." + key;
        String result = bundle.getString(fullKey);
        if (debug) {
            result = fullKey + result;
        }
        return result;
    }

    public String getString(Enum<?> key) {
        return getString(key.getDeclaringClass().getSimpleName()
                + "." + key.name());
    }

}
