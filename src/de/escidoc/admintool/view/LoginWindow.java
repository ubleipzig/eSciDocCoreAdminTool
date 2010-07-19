package de.escidoc.admintool.view;

import java.io.Serializable;

import com.vaadin.terminal.ExternalResource;
import com.vaadin.ui.LoginForm;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.LoginForm.LoginEvent;

import de.escidoc.admintool.app.AdminToolApplication;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;
import de.escidoc.core.client.exceptions.application.security.AuthenticationException;

@SuppressWarnings("serial")
public class LoginWindow extends Window implements Serializable {

    public LoginWindow() {
        final VerticalLayout layout = new VerticalLayout();
        setContent(layout);

        final LoginForm login = new LoginForm();
        login.setWidth("100%");
        login.setHeight("300px");

        login.addListener(new LoginForm.LoginListener() {
            public void onLogin(final LoginEvent event) {
                try {
                    AdminToolApplication.getInstance().authenticate(
                        event.getLoginParameter("username"),
                        event.getLoginParameter("password"));

                    LoginWindow.this.open(new ExternalResource(
                        AdminToolApplication.getInstance().getURL()));
                }
                catch (final AuthenticationException e) {
                    LoginWindow.this.showNotification(new Window.Notification(
                        "Wrong credentials",
                        "The username or password you entered is incorrect",
                        Window.Notification.TYPE_ERROR_MESSAGE));
                    e.printStackTrace();
                }
                catch (final InternalClientException e) {
                    LoginWindow.this.showNotification(new Window.Notification(
                        "Internal Server Error", e.getMessage(),
                        Window.Notification.TYPE_ERROR_MESSAGE));
                    e.printStackTrace();
                }
                catch (final TransportException e) {
                    LoginWindow.this.showNotification(new Window.Notification(
                        "Internal Server Error", e.getMessage(),
                        Window.Notification.TYPE_ERROR_MESSAGE));
                    e.printStackTrace();
                }
            }
        });
        layout.addComponent(login);
    }
}