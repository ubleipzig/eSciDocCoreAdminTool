package de.escidoc.admintool.app;

import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.PropertyConfigurator;

public class ConfigureLogger {

    public static void execute() throws IOException {
        final Properties log4JProp = new Properties();
        log4JProp.load(PropertyUtilities
            .getResourceAsStream("log4j.properties"));
        PropertyConfigurator.configure(log4JProp);
    }
}
