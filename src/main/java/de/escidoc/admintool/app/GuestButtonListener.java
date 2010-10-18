package de.escidoc.admintool.app;

import com.vaadin.ui.ComboBox;

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