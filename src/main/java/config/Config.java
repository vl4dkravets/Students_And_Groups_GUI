package config;

import java.io.IOException;
import java.util.Properties;

public class Config
{
    private static final String fileName = "db.properties";
    private static final Properties config = new Properties();
    static ClassLoader loader = Thread.currentThread().getContextClassLoader();


    // Loads properties file
    public static void initGlobalConfig() throws IOException {
        initGlobalConfig(null);
    }

    // loads config file to Properties instance
    // if name=null, use default name
    public static void initGlobalConfig(String name) throws IOException {
        if (name != null && !name.trim().isEmpty()) {
            config.load(loader.getResourceAsStream(name));
        } else {
            config.load(loader.getResourceAsStream(fileName));
        }
    }

    // Gets an attribute based on a name
    public static String getProperty(String property) {
        return config.getProperty(property);
    }
}
