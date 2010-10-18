package de.escidoc.admintool.view;

import com.vaadin.ui.Window;

import de.escidoc.admintool.view.util.dialog.ErrorDialog;

public class ErrorMessage {

    public static void show(final Window mainWindow, final Exception e) {
        mainWindow.addWindow(new ErrorDialog(mainWindow,
            ViewConstants.SERVER_INTERNAL_ERROR, e.getMessage()));
    }
}
