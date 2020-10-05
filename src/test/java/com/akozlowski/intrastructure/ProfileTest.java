package com.akozlowski.intrastructure;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.SetSystemProperty;

import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ProfileTest {

    @Test
    @SetSystemProperty(key = "env",value = "test")
    void testLoadDbConfig() {
        Properties properties = Profile.getProperties("db");
        assertNotNull(properties, "Cannot load a profile");

        final String databaseName = properties.getProperty("database");
        assertEquals("peopletest", databaseName,"Wrong database name is used");
    }
}