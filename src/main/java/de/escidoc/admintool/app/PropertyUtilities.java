package de.escidoc.admintool.app;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Helper class to read and write property files.
 * 
 * @author ASP
 */
public class PropertyUtilities {
    private static final Logger LOG = LoggerFactory
        .getLogger(PropertyUtilities.class);

    /**
     * Writes a property file to hard drive.
     * 
     * @param path
     *            the path where the file should be created.
     * @param prop
     *            the property file to store.
     * @param caption
     *            the caption to insert into the file.
     */
    public static synchronized void writeProperties(
        final String path, final Properties prop, final String caption) {
        try {
            final FileOutputStream out = new FileOutputStream(path);
            prop.store(out, caption);
            out.close();
        }
        catch (final FileNotFoundException e) {
            LOG.error("The file " + path + " could not be read!", e);
            e.printStackTrace();
        }
        catch (final IOException e) {
            LOG
                .error("IOException occured wile reading from :" + path + "!",
                    e);
            e.printStackTrace();
        }
    }

    public static InputStream getResourceAsStream(final String path) {
        return Thread
            .currentThread().getContextClassLoader().getResourceAsStream(path);
    }

    /**
     * Reads a property file from the hard drive.
     * 
     * @param path
     *            the location.
     * @return the property file.
     */
    public static synchronized Properties readProperties(final String path) {
        try {
            final Properties properties = new Properties();
            properties.load(getResourceAsStream(path));
            return properties;
        }
        catch (final FileNotFoundException e) {
            LOG.error("The file " + path + " could not be read!", e);
            e.printStackTrace();
        }
        catch (final IOException e) {
            LOG
                .error("IOException occured wile reading from :" + path + "!",
                    e);
            e.printStackTrace();
        }
        return new Properties();
    }

}
