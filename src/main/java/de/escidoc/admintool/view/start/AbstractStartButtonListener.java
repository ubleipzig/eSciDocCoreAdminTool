package de.escidoc.admintool.view.start;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.vaadin.Application;
import com.vaadin.data.Validator.EmptyValueException;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.Notification;

import de.escidoc.admintool.app.AdminToolApplication;

public abstract class AbstractStartButtonListener implements ClickListener {

    private static final long serialVersionUID = -7482204166398806832L;

    private final static Logger LOG = LoggerFactory
        .getLogger(AbstractStartButtonListener.class);

    private static final int FIVE_SECONDS = 5000;

    protected final AdminToolApplication app;

    private final AbstractField escidocUriField;

    private final Window mainWindow;

    public AbstractStartButtonListener(final AbstractField escidocUriField,
        final AdminToolApplication app) {
        Preconditions.checkNotNull(escidocUriField,
            "escidocUriField is null: %s", escidocUriField);
        Preconditions.checkNotNull(app, "app is null: %s", app);
        this.escidocUriField = escidocUriField;
        this.app = app;
        mainWindow = app.getMainWindow();
    }

    @Override
    public void buttonClick(final ClickEvent event) {
        validate(escidocUriField);
    }

    private void validate(final AbstractField escidocUriField) {
        try {
            validateInputField(escidocUriField);
            testConnection(escidocUriField);

        }
        catch (final EmptyValueException e) {
            mainWindow.showNotification(new Notification(e.getMessage(),
                Notification.TYPE_ERROR_MESSAGE));
        }
    }

    private void testConnection(final AbstractField escidocUriField) {
        if (validateConnection(escidocUriField)) {
            initApplication();
        }
        else {
            mainWindow.showNotification(new Window.Notification(
                "Can not connect to: " + escidocUriField.getValue(),
                Notification.TYPE_ERROR_MESSAGE));
        }
    }

    private void validateInputField(final AbstractField escidocUriField) {
        escidocUriField.validate();
    }

    private void initApplication() {
        setEscidocUri();
        redirectToMainView();
    }

    private void setEscidocUri() {
        final String enteredEscidocUri = getUserInput();
        LOG.debug("using eSciDoc instance in: " + enteredEscidocUri);
        app.setEscidocUri(enteredEscidocUri);
    }

    protected String getEscidocUri() {
        return getUserInput();
    }

    private String getUserInput() {
        return (String) escidocUriField.getValue();
    }

    protected abstract void redirectToMainView();

    private boolean validateConnection(final AbstractField escidocUriField) {
        final String strUrl = (String) escidocUriField.getValue();
        URLConnection connection;
        try {
            connection = new URL(strUrl).openConnection();
            connection.setConnectTimeout(FIVE_SECONDS);
            connection.connect();
            final int responseCode =
                ((HttpURLConnection) connection).getResponseCode();
            return responseCode == 200;
        }
        catch (final IllegalArgumentException e) {
            LOG.warn("Malformed URL: " + e);
            mainWindow.showNotification(new Notification(e.getMessage(),
                Notification.TYPE_ERROR_MESSAGE));
            return false;
        }
        catch (final MalformedURLException e) {
            LOG.warn("Malformed URL: " + e);
            mainWindow.showNotification(new Notification(e.getMessage(),
                Notification.TYPE_ERROR_MESSAGE));
            return false;
        }
        catch (final IOException e) {
            LOG.warn("IOException: " + e);
            mainWindow.showNotification(new Notification(e.getMessage(),
                Notification.TYPE_ERROR_MESSAGE));
            return false;
        }
    }

    protected Application getApplication() {
        return app;
    }

    public Window getMainWindow() {
        return mainWindow;
    }
}