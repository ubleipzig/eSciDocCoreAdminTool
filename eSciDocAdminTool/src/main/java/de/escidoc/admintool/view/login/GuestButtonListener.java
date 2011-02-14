package de.escidoc.admintool.view.login;

import com.vaadin.ui.ComboBox;

import de.escidoc.admintool.app.AdminToolApplication;

public class GuestButtonListener extends LoginButtonListener {
    private static final long serialVersionUID = -4664303312775183482L;

    public GuestButtonListener(ComboBox escidocComboBox,
        AdminToolApplication app) {
        super(escidocComboBox, app);
    }

    @Override
    protected void loginMe() {
        getMainWindow()
            .showNotification("Currently you can not login as guest");
    }
}