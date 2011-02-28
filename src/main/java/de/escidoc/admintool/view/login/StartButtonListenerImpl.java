package de.escidoc.admintool.view.login;

import com.vaadin.terminal.ExternalResource;
import com.vaadin.ui.AbstractField;

import de.escidoc.admintool.app.AdminToolApplication;

public class StartButtonListenerImpl extends LoginButtonListener {

    private static final long serialVersionUID = 2949659635673188343L;

    public StartButtonListenerImpl(final AbstractField escidocComboBox,
        final AdminToolApplication app) {
        super(escidocComboBox, app);
    }

    @Override
    protected void loginMe() {
        redirectTo(super.app.escidocLoginUrl + super.getApplication().getURL());
    }

    private void redirectTo(final String url) {
        super.getMainWindow().open(new ExternalResource(url));
    }
}