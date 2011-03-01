package de.escidoc.admintool.view;

import com.google.common.base.Preconditions;
import com.vaadin.terminal.ExternalResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Window;

public final class LoginButtonListener implements Button.ClickListener {

    private static final long serialVersionUID = -7439896206254776195L;

    private final Window mainWindow;

    private final String loginUrl;

    public LoginButtonListener(final Window mainWindow, final String loginUrl) {
        Preconditions.checkNotNull(mainWindow, "mainWindow is null: %s",
            mainWindow);
        Preconditions.checkNotNull(loginUrl, "loginUrl is null: %s", loginUrl);
        this.mainWindow = mainWindow;
        this.loginUrl = loginUrl;
    }

    @Override
    public void buttonClick(final ClickEvent event) {
        redirectTo(loginUrl);
    }

    private void redirectTo(final String url) {
        mainWindow.open(new ExternalResource(url));
    }
}