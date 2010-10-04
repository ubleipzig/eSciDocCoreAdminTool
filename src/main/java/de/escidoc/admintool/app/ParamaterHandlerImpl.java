package de.escidoc.admintool.app;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import biz.source_code.base64Coder.Base64Coder;

import com.vaadin.terminal.ExternalResource;
import com.vaadin.terminal.ParameterHandler;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.Notification;

import de.escidoc.admintool.messages.Messages;
import de.escidoc.admintool.view.ErrorMessage;
import de.escidoc.admintool.view.ViewConstants;
import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;
import de.escidoc.core.client.exceptions.application.security.AuthenticationException;

@SuppressWarnings("serial")
public class ParamaterHandlerImpl implements ParameterHandler {

    private static final Logger log = LoggerFactory // NOPMD by CHH on 9/17/10
                                                    // 10:19 AM => logger is not
                                                    // not a constants
        .getLogger(ParamaterHandlerImpl.class);

    private final Window mainWindow;

    private final AdminToolApplication app;

    public ParamaterHandlerImpl(final Window mainWindow,
        final AdminToolApplication app) {
        this.mainWindow = mainWindow;
        this.app = app;
    }

    @Override
    public void handleParameters(final Map<String, String[]> parameters) {
        try {
            final String token = parseAndDecodeToken(parameters);
            if (isEmpty(token)) {
                redirectTo(AdminToolContants.ESCIDOC_LOGIN_URL + app.getURL());
            }
            else {
                tryToAutentificate(token);
            }
        }
        catch (final IllegalArgumentException e) {
            mainWindow
                .showNotification(ViewConstants.INVALID_TOKEN_ERROR_MESSAGE);
            redirectTo(AdminToolContants.ESCIDOC_LOGIN_URL + app.getURL());
        }

    }

    private boolean isEmpty(final String token) {
        return token == null || token.isEmpty();
    }

    private void tryToAutentificate(final String token) {
        try {
            // TODO move the responsibility to other class.
            app.authenticate(token);
        }
        catch (final AuthenticationException e) {
            mainWindow.showNotification(new Notification(
                ViewConstants.WRONG_TOKEN_MESSAGE, e.getMessage(),
                Notification.TYPE_ERROR_MESSAGE));
            log.error(Messages.getString("AdminToolApplication.3"), e); //$NON-NLS-1$
        }
        catch (final EscidocException e) {
            ErrorMessage.show(mainWindow, e);
            log.error(ViewConstants.SERVER_INTERNAL_ERROR, e);
        }
        catch (final InternalClientException e) {
            ErrorMessage.show(mainWindow, e);
            log.error(ViewConstants.SERVER_INTERNAL_ERROR, e);
        }
        catch (final TransportException e) {
            ErrorMessage.show(mainWindow, e);
            log.error(ViewConstants.SERVER_INTERNAL_ERROR, e);
        }
    }

    private void redirectTo(final String url) {
        mainWindow.open(new ExternalResource(url));
    }

    private String parseAndDecodeToken(final Map<String, String[]> parameters)
        throws IllegalArgumentException {
        if (parameters.containsKey(AdminToolContants.ESCIDOC_USER_HANDLE)) {
            final String parameter =
                parameters.get(AdminToolContants.ESCIDOC_USER_HANDLE)[0];
            return Base64Coder.decodeString(parameter); // NOPMD by CHH on
                                                        // 9/17/10 10:20 AM
        }
        return "";
    }
}