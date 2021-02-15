package com.clavardage.core.utils;

import com.clavardage.client.views.AlertWindow;
import com.clavardage.client.views.MainWindow;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Config {

    private static final String CONFIG_FILE_NAME = "client.config";
    private static final Config INSTANCE = new Config();
    private Properties conf;

    private Config() {
        conf = new Properties();
        try {
            FileInputStream confFile = new FileInputStream(CONFIG_FILE_NAME);
            conf.load(confFile);
        } catch (IOException e) {
            System.err.println("Unable to load config file");
        }
    }

    private Properties getConfig() {
        return this.conf;
    }

    public void setProperties(Properties properties) {
        this.conf = properties;
    }

    private static String get(String key) {
        String result = INSTANCE.getConfig().getProperty(key);
        if (result == null) {
            Config.error(key);
        }
        return result;
    }

    public static String getString(String key) {
        return Config.get(key);
    }

    public static Config getInstance() {
        return INSTANCE;
    }

    public static Boolean getBoolean(String key) {
        return Boolean.parseBoolean(Config.get(key));
    }

    public static Short getShort(String key) {
        try {
            return Short.parseShort(Config.get(key));
        } catch (NumberFormatException e) {
            Config.error(key);
            return 0; // Unreachable code
        }
    }

    public static Integer getInteger(String key) {
        try {
            return Integer.parseInt(Config.get(key));
        } catch (NumberFormatException e) {
            Config.error(key);
            return 0; // Unreachable code
        }
    }

    public static void error(String key) {
        AlertWindow.displayError("The client.config file is not correct (especially the "
                + key + " parameter).\n" +
                "(An email was sent to M. Yangui with a valid config file attached," +
                "allowing to use some always-running database and servlet)");
       System.exit(1);
    }
}
