package de.escidoc.admintool.view.context;

import com.google.common.base.Preconditions;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;

import de.escidoc.admintool.app.AdminToolApplication;

@SuppressWarnings("serial")
public final class LinkClickListener implements Button.ClickListener {

    private String userId;

    private final AdminToolApplication app;

    public LinkClickListener(final AdminToolApplication app) {
        Preconditions.checkNotNull(app, "app is null: %s", app);
        this.app = app;
    }

    @Override
    public void buttonClick(final ClickEvent event) {
        final Object source = event.getSource();
        Preconditions.checkNotNull(userId, "userId is null: %s", userId);
        app.showUser(userId);
    }

    public void setUser(final String userId) {
        Preconditions.checkNotNull(userId, "userId is null: %s", userId);
        this.userId = userId;
    }
}