package com.autovideo.config;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * Manages the autovideo.conf file so users don't have to edit it manually.
 * Lets the GUI read and write config without being a pain.
 */
public class ConfigManager {
    private static final String CONFIG_FILE = "autovideo.conf";
    
    /**
     * Check if config file exists and has the bare minimum settings.
     */
    public static boolean configExists() {
        File file = new File(CONFIG_FILE);
        if (!file.exists()) {
            return false;
        }
        
        // Check if it has the required keys
        try {
            Properties props = loadProperties();
            return props.getProperty("aws_access_key") != null &&
                   props.getProperty("aws_secret_key") != null &&
                   props.getProperty("pixabay_api_key") != null;
        } catch (IOException e) {
            return false;
        }
    }
    
    /**
     * Load config from file.
     */
    public static Properties loadProperties() throws IOException {
        Properties props = new Properties();
        try (InputStream input = new FileInputStream(CONFIG_FILE)) {
            props.load(input);
        }
        return props;
    }
    
    /**
     * Save config to file.
     * Creates the file if it doesn't exist.
     */
    public static void saveProperties(Properties props) throws IOException {
        try (OutputStream output = new FileOutputStream(CONFIG_FILE)) {
            props.store(output, "Wikipedia Autovideo Configuration");
        }
    }
    
    /**
     * Create a new config with the given credentials.
     */
    public static void createConfig(String awsAccessKey, String awsSecretKey, 
                                   String awsRegion, String pixabayApiKey) throws IOException {
        Properties props = new Properties();
        props.setProperty("aws_access_key", awsAccessKey);
        props.setProperty("aws_secret_key", awsSecretKey);
        props.setProperty("aws_region", awsRegion != null ? awsRegion : "us-east-1");
        props.setProperty("pixabay_api_key", pixabayApiKey);
        saveProperties(props);
    }
    
    /**
     * Get a specific config value.
     */
    public static String getProperty(String key) throws IOException {
        Properties props = loadProperties();
        return props.getProperty(key);
    }
    
    /**
     * Update a single config value.
     */
    public static void setProperty(String key, String value) throws IOException {
        Properties props = loadProperties();
        props.setProperty(key, value);
        saveProperties(props);
    }
}

