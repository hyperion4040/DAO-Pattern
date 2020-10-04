package com.akozlowski.intrastructure;

import com.akozlowski.App;

import java.io.IOException;
import java.util.Optional;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Profile {

    public static Properties getProperties(final String propertiesFileName) {

        final Properties properties = new Properties();
        final String env = Optional.ofNullable(System.getProperty("env")).orElse("dev");
        final String propertiesFile = String.format("/config/%s.%s.properties",propertiesFileName,env);

        try {
            properties.load(App.class.getResourceAsStream(propertiesFile));
        } catch (Exception e) {
            throw new RuntimeException("Cannot load properties file: " + propertiesFile);
        }

        return properties;
    }
}
