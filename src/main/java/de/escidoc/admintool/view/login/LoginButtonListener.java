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
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.Notification;

import de.escidoc.admintool.app.AdminToolApplication;

public abstract class LoginButtonListener implements ClickListener {

    private static final long serialVersionUID = -7482204166398806832L;

    private final static Logger log = LoggerFactory
        .getLogger(LoginButtonListener.class);

    private final AdminToolApplication app;

    private final ComboBox escidocComboBox;

    private final Window mainWindow;

    public LoginButtonListener(final ComboBox escidocComboBox,
        final AdminToolApplication app) {
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
                mainWindow.showNotification("eSciDoc instance on: "
                    + escidocUriField.getValue() + " is up and running");
                login();
            }
            else {
                mainWindow.showNotification("Can not connect to: "
                    + escidocUriField.getValue());
            }

        }
        catch (final EmptyValueException e) {
            mainWindow.showNotification(new Notification(e.getMessage(),
                Notification.TYPE_ERROR_MESSAGE));
        }
    }

    private void login() {
        final String enteredEscidocUri = getUserInput();
        log.debug("login as " + getClass() + " to " + enteredEscidocUri);
        app.setEscidocUri(enteredEscidocUri);
        loginMe();
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
            connection.connect();
            final int responseCode =
                ((HttpURLConnection) connection).getResponseCode();
            return responseCode == 200;
        }
        catch (final MalformedURLException e) {
            log.warn("Malformed URL: " + e);
            mainWindow.showNotification(new Notification(e.getMessage(),
                Notification.TYPE_ERROR_MESSAGE));
            return false;
        }
        catch (final IOException e) {
            log.warn("IOException: " + e);
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