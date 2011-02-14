package de.escidoc.admintool.app;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.vaadin.Application;
import com.vaadin.service.ApplicationContext.TransactionListener;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.Notification;

import de.escidoc.admintool.messages.Messages;
import de.escidoc.admintool.view.ModalDialog;
import de.escidoc.admintool.view.ViewConstants;
import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;
import de.escidoc.core.client.exceptions.application.security.AuthenticationException;

final class AdminToolTransactionListener implements TransactionListener {

    private static final Cookie EMPTY_COOKIE = new Cookie("", "");

    private static final Logger LOG = LoggerFactory
        .getLogger(AdminToolTransactionListener.class);

    private static final String ESCIDOC_COOKIE_NAME = "escidocCookie";

    private static final long serialVersionUID = 7130384700120655157L;

    private final AdminToolApplication app;

    private final Window mainWindow;

    public AdminToolTransactionListener(final AdminToolApplication app,
        final Window mainWindow) {
        Preconditions.checkNotNull(app, "app is null: %s", app);
        Preconditions.checkNotNull(mainWindow, "mainWindow is null: %s",
            mainWindow);
        this.app = app;
        this.mainWindow = mainWindow;
    }

    public void transactionStart(
        final Application application, final Object transactionData) {
        final HttpServletRequest request = (HttpServletRequest) transactionData;
        final Cookie escidocCookie = findEscidocCookie(request);

        try {
            setToken(escidocCookie);
        }
        catch (final EscidocClientException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private Cookie findEscidocCookie(final HttpServletRequest request) {
        if (request != null && request.getCookies() != null) {

            for (final Cookie cookie : request.getCookies()) {
                if (isEscidocCookie(cookie)) {
                    return cookie;
                }
            }
            return EMPTY_COOKIE;
        }
        return EMPTY_COOKIE;
    }

    void setToken(final Cookie cookie) throws EscidocClientException {
        if (isEmpty(cookie)) {
            mainWindow.showNotification("the user does not provide any token.");

            LOG.debug("the user does not provide any token.");
            app.showLandingView();

        }
        else {

            LOG.debug("the user has a token.");
            mainWindow.showNotification("the user has a token.");
            tryAuthenticate(cookie);

        }

        // if (isEmpty(cookie)) {
        // System.out.println("the user does not provide any token.");
        // // app.showLandingView();
        // tryAuthenticate(cookie);
        //
        // } else {
        // LOG.debug("the user has a token.");
        // tryAuthenticate(cookie);
        // }
    }

    private void tryAuthenticate(final Cookie token)
        throws EscidocClientException {
        try {
            app.loadProtectedResources(token.getValue());
        }
        catch (final AuthenticationException e) {
            mainWindow.showNotification(new Notification(
                ViewConstants.WRONG_TOKEN_MESSAGE, e.getMessage(),
                Notification.TYPE_ERROR_MESSAGE));
            LOG.error(Messages.getString("AdminToolApplication.3"), e); //$NON-NLS-1$
        }
        catch (final EscidocException e) {
            ModalDialog.show(mainWindow, e);
            LOG.error(ViewConstants.SERVER_INTERNAL_ERROR, e);
        }
        catch (final InternalClientException e) {
            ModalDialog.show(mainWindow, e);
            LOG.error(ViewConstants.SERVER_INTERNAL_ERROR, e);
        }
        catch (final TransportException e) {
            ModalDialog.show(mainWindow, e);
            LOG.error(ViewConstants.SERVER_INTERNAL_ERROR, e);
        }
    }

    private boolean isEmpty(final Cookie cookie) {
        return "".equals(cookie.getValue());
        // return EMPTY_COOKIE.equals(cookie);
    }

    private boolean isEscidocCookie(final Cookie cookie) {
        return ESCIDOC_COOKIE_NAME.equals(cookie.getName());
    }

    public void transactionEnd(
        final Application application, final Object transactionData) {

    }
}