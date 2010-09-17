package de.escidoc.admintool.messages;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Messages {

    private static final Logger log = LoggerFactory.getLogger(Messages.class); // NOPMD by CHH on 9/17/10 10:19 AM

    private static final String BUNDLE_NAME = "messages"; //$NON-NLS-1$

    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle
        .getBundle(BUNDLE_NAME);

    private Messages() {
    }

    public static String getString(final String key) {
        try {
            return RESOURCE_BUNDLE.getString(key); // NOPMD by CHH on 9/17/10 10:19 AM
        }
        catch (final MissingResourceException e) {
            log.error("Label for " + key + " could not be found", e);
            return '!' + key + '!';
        }
    }
}
