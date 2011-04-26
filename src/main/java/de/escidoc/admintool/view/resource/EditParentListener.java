package de.escidoc.admintool.view.resource;

import com.vaadin.data.Item;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Window;

public class EditParentListener implements ClickListener {

    private static final long serialVersionUID = 5088260302217401646L;

    private final Window mainWindow;

    private final AddOrEditParentModalWindow orgUnitSelectDialog;

    public EditParentListener(final Window mainWindow, final AddOrEditParentModalWindow orgUnitSelectDialog) {
        this.mainWindow = mainWindow;
        this.orgUnitSelectDialog = orgUnitSelectDialog;
    }

    @Override
    public void buttonClick(final ClickEvent event) {
        showOrgUnitSelectionDialog();
    }

    private void showOrgUnitSelectionDialog() {
        mainWindow.addWindow(orgUnitSelectDialog);
    }

    public void bind(final Item item) {
        orgUnitSelectDialog.bind(item);
    }
}
