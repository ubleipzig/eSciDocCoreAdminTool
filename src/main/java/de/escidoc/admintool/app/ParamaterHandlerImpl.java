package de.escidoc.admintool.app;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import biz.source_code.base64Coder.Base64Coder;

import com.google.common.base.Preconditions;
import com.vaadin.terminal.ParameterHandler;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.Notification;

import de.escidoc.admintool.messages.Messages;
import de.escidoc.admintool.view.ModalDialog;
import de.escidoc.admintool.view.ViewConstants;
import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.client.exceptions.application.security.AuthenticationException;

public class ParamaterHandlerImpl implements ParameterHandler {

    private static final long serialVersionUID = 6392830954652643671L;

    private static final Logger LOG = LoggerFactory
        .getLogger(ParamaterHandlerImpl.class);

    private final Window mainWindow;

    private final AdminToolApplication app;

    public ParamaterHandlerImpl(final Window mainWindow,
        final AdminToolApplication app) {

        Preconditions.checkNotNull(mainWindow,
            "MainWindow can not be null: %s", mainWindow);
        Preconditions.checkNotNull(app,
            "AdminToolApplication can not be null: %s", app);

        this.mainWindow = mainWindow;
        this.app = app;
    }

    @Override
    public void handleParameters(final Map<String, String[]> parameters) {
        if (isTokenExist(parameters)) {
            LOG.debug("the user has a token.");
            tryToLoadProtectedSource(parseAndDecodeToken(parameters));
        }
        else {
            LOG.debug("the user does not provide any token.");
            app.showLandingView();
        }
    }

    private boolean isTokenExist(final Map<String, String[]> parameters) {
        return parameters.containsKey(AppConstants.ESCIDOC_USER_HANDLE);
    }

    private void tryToLoadProtectedSource(final String token) {
        try {
            app.loadProtectedResources(token);
        }
        catch (final IllegalArgumentException e) {
            LOG.error(Messages.getString("AdminToolApplication.3"), e);
            mainWindow.showNotification(new Notification(
                ViewConstants.WRONG_TOKEN_MESSAGE, "Wrong token",
                Notification.TYPE_ERROR_MESSAGE));
            app.showLandingView();
        }
        catch (final AuthenticationException e) {
            LOG.error(Messages.getString("AdminToolApplication.3"), e);
            mainWindow.showNotification(new Notification(
                ViewConstants.WRONG_TOKEN_MESSAGE, e.getMessage(),
                Notification.TYPE_ERROR_MESSAGE));
            app.showLandingView();

        }
        catch (final EscidocClientException e) {
            LOG.error(ViewConstants.SERVER_INTERNAL_ERROR, e);
            ModalDialog.show(mainWindow, e);
            app.showLandingView();
        }
    }

    private String parseAndDecodeToken(final Map<String, String[]> parameters) {
        final String parameter =
            parameters.get(AppConstants.ESCIDOC_USER_HANDLE)[0];
        return tryToDecode(parameter);
    }

    private String tryToDecode(final String parameter) {
        try {
            return Base64Coder.decodeString(parameter);
        }
        catch (final IllegalArgumentException e) {
            LOG.error(Messages.getString("AdminToolApplication.3"), e);
            app.showLandingView();

            mainWindow.showNotification(new Notification(
                ViewConstants.WRONG_TOKEN_MESSAGE, "Wrong token",
                Notification.TYPE_ERROR_MESSAGE));
        }
        return "";
    }

}