package de.escidoc.admintool.view;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;

import de.escidoc.admintool.app.AdminToolApplication;

public final class LogoutButtonListener implements Button.ClickListener {

    private static final long serialVersionUID = 6434716782391206321L;

    private final AdminToolApplication app;

    public LogoutButtonListener(final AdminToolApplication app) {
        this.app = app;
    }

    @Override
    public void buttonClick(final ClickEvent event) {
        app.close();
    }
}