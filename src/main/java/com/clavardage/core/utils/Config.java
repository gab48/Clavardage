package com.clavardage.core.utils;

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

    public static String get(String key) {
        return INSTANCE.getConfig().getProperty(key);
    }

    public static Config getInstance() {
        return INSTANCE;
    }
}
