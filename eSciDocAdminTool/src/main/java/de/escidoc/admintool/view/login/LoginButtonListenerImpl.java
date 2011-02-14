package de.escidoc.admintool.view.login;

import com.vaadin.terminal.ExternalResource;
import com.vaadin.ui.ComboBox;

import de.escidoc.admintool.app.AdminToolApplication;

public class LoginButtonListenerImpl extends LoginButtonListener {

    private static final long serialVersionUID = 2949659635673188343L;

    public LoginButtonListenerImpl(ComboBox escidocComboBox,
        AdminToolApplication app) {
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