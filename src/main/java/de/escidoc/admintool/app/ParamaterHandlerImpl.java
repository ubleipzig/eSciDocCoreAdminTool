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
import de.escidoc.admintool.view.ErrorMessage;
import de.escidoc.admintool.view.ViewConstants;
import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.client.exceptions.application.security.AuthenticationException;

public class ParamaterHandlerImpl implements ParameterHandler {

    private static final long serialVersionUID = 6392830954652643671L;

    private static final Logger log = LoggerFactory
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
            log.debug("the user has a token.");
            tryToAutentificate(parseAndDecodeToken(parameters));
        }
        else {
            log.debug("the user does not provide any token.");
            app.showLandingView();
        }

        // try {
        // final String token = parseAndDecodeToken(parameters);
        // if (isEmpty(token)) {
        // log.debug("the user does not provide any token.");
        // app.showLandingView();
        // }
        // else {
        // log.debug("the user has a token.");
        // tryToAutentificate(token);
        // }
        // }
        // catch (final IllegalArgumentException e) {
        // mainWindow.showNotification(
        // ViewConstants.INVALID_TOKEN_ERROR_MESSAGE,
        // Notification.TYPE_HUMANIZED_MESSAGE);
        // app.showLandingView();
        // }
    }

    private boolean isTokenExist(final Map<String, String[]> parameters) {
        return parameters.containsKey(AppConstants.ESCIDOC_USER_HANDLE);
    }

    private String parseAndDecodeToken(final Map<String, String[]> parameters) {
        final String parameter =
            parameters.get(AppConstants.ESCIDOC_USER_HANDLE)[0];
        return Base64Coder.decodeString(parameter);
    }

    private boolean isEmpty(final String token) {
        return token == null || token.isEmpty();
    }

    private void tryToAutentificate(final String token) {
        try {
            app.authenticate(token);
        }
        catch (final AuthenticationException e) {
            mainWindow.showNotification(new Notification(
                ViewConstants.WRONG_TOKEN_MESSAGE, e.getMessage(),
                Notification.TYPE_ERROR_MESSAGE));
            log.error(Messages.getString("AdminToolApplication.3"), e); //$NON-NLS-1$
        }
        catch (final EscidocClientException e) {
            ErrorMessage.show(mainWindow, e);
            log.error(ViewConstants.SERVER_INTERNAL_ERROR, e);
        }
    }

}