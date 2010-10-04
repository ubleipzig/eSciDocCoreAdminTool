package de.escidoc.admintool.view.user;

import de.escidoc.admintool.app.AdminToolApplication;
import de.escidoc.admintool.view.AbstractResourceSelectListener;
import de.escidoc.admintool.view.ResourceView;

public class UserSelectListener extends AbstractResourceSelectListener {
    private static final long serialVersionUID = 7439976115422091225L;

    private final AdminToolApplication app;

    public UserSelectListener(final AdminToolApplication app) {
        this.app = app;
    }

    @Override
    public ResourceView getView() {
        return app.getUserView();
    }
}