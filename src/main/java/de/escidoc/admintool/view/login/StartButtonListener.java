package de.escidoc.admintool.view.login;

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

public abstract class StartButtonListener implements ClickListener {

    private static final String CAN_NOT_CONNECT_TO = "Can not connect to: ";

    private static final int FIVE_SECONDS = 5000;

    private final static Logger LOG = LoggerFactory
        .getLogger(LoginButtonListener.class);

    private final AbstractField escidocUrlField;

    private final Window mainWindow;

    private final AdminToolApplication app;

    public StartButtonListener(final AbstractField escidocUrlField,
        final Window mainWindow, final AdminToolApplication app) {

        Preconditions.checkNotNull(escidocUrlField,
            "escidocUrlField is null: %s", escidocUrlField);
        Preconditions.checkNotNull(mainWindow, "mainWindow is null: %s",
            mainWindow);
        Preconditions.checkNotNull(app, "app is null: %s", app);
        this.escidocUrlField = escidocUrlField;
        this.mainWindow = mainWindow;
        this.app = app;
    }

    @Override
    public void buttonClick(final ClickEvent event) {
        validate(escidocUrlField);
    }

    private void validate(final AbstractField escidocUriField) {
        try {
            escidocUriField.validate();
            if (tryToConnect(escidocUriField)) {
                login();
            }
            else {
                mainWindow.showNotification(new Notification(CAN_NOT_CONNECT_TO
                    + escidocUriField.getValue(),
                    Notification.TYPE_WARNING_MESSAGE));
            }

        }
        catch (final EmptyValueException e) {
            mainWindow.showNotification(new Notification(e.getMessage(),
                Notification.TYPE_ERROR_MESSAGE));
        }
    }

    private void login() {
        setEscidocUri();
        showMainPage();
    }

    private void setEscidocUri() {
        final String enteredEscidocUri = getUserInput();
        LOG.debug("login as " + getClass() + " to " + enteredEscidocUri);
        app.setEscidocUri(enteredEscidocUri);
    }

    private String getUserInput() {
        return (String) escidocUrlField.getValue();
    }

    protected abstract void showMainPage();

    private boolean tryToConnect(final AbstractField escidocUriField) {
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