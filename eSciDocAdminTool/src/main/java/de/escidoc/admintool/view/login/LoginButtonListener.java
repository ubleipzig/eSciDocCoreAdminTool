package de.escidoc.admintool.view.login;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.Application;
import com.vaadin.data.Validator.EmptyValueException;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.Notification;

import de.escidoc.admintool.app.AdminToolApplication;

public abstract class LoginButtonListener implements ClickListener {

    private static final int FIVE_SECONDS = 5000;

    private static final long serialVersionUID = -7482204166398806832L;

    private final static Logger LOG = LoggerFactory.getLogger(LoginButtonListener.class);

    protected final AdminToolApplication app;

    private final AbstractField escidocComboBox;

    private final Window mainWindow;

    public LoginButtonListener(final AbstractField escidocComboBox, final AdminToolApplication app) {
        this.escidocComboBox = escidocComboBox;
        this.app = app;
        mainWindow = app.getMainWindow();
    }

    @Override
    public void buttonClick(final ClickEvent event) {
        validate(escidocComboBox);
    }

    private void validate(final AbstractField escidocUriField) {
        try {
            escidocUriField.validate();
            if (validateConnection(escidocUriField)) {
                login();
            }
            else {
                mainWindow.showNotification("Can not connect to: " + escidocUriField.getValue());
            }

        }
        catch (final EmptyValueException e) {
            mainWindow.showNotification(new Notification(e.getMessage(), Notification.TYPE_ERROR_MESSAGE));
        }
    }

    private void login() {
        setEscidocUri();
        loginMe();
    }

    private void setEscidocUri() {
        final String enteredEscidocUri = getUserInput();
        LOG.debug("login as " + getClass() + " to " + enteredEscidocUri);
        app.setEscidocUri(enteredEscidocUri);
    }

    private String getUserInput() {
        return (String) escidocComboBox.getValue();
    }

    protected abstract void loginMe();

    private boolean validateConnection(final AbstractField escidocUriField) {
        final String strUrl = (String) escidocUriField.getValue();
        URLConnection connection;
        try {
            connection = new URL(strUrl).openConnection();
            connection.setConnectTimeout(FIVE_SECONDS);
            connection.connect();
            final int responseCode = ((HttpURLConnection) connection).getResponseCode();
            return responseCode == 200;
        }
        catch (final IllegalArgumentException e) {
            LOG.warn("Malformed URL: " + e);
            mainWindow.showNotification(new Notification(e.getMessage(), Notification.TYPE_ERROR_MESSAGE));
            return false;
        }
        catch (final MalformedURLException e) {
            LOG.warn("Malformed URL: " + e);
            mainWindow.showNotification(new Notification(e.getMessage(), Notification.TYPE_ERROR_MESSAGE));
            return false;
        }
        catch (final IOException e) {
            LOG.warn("IOException: " + e);
            mainWindow.showNotification(new Notification(e.getMessage(), Notification.TYPE_ERROR_MESSAGE));
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