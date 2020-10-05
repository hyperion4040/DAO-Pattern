package com.akozlowski.intrastructure;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Properties;

class ProfileTest {

    @Test
    void testLoadDbConfig() {
        Properties properties = Profile.getProperties("db");
        Assertions.assertNotNull(properties, "Cannot load a profile");
    }
}