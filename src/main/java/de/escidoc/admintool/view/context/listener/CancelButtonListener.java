package de.escidoc.admintool.view.context.listener;

import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Window;

public final class CancelButtonListener implements ClickListener {
    private static final long serialVersionUID = -9187444730054436852L;

    private final Window mainWindow;

    private final Window modalWindow;

    public CancelButtonListener(final Window mainWindow, final Window modalWindow) {
        this.mainWindow = mainWindow;
        this.modalWindow = modalWindow;
    }

    public void buttonClick(final ClickEvent event) {
        closeWindow();
    }

    private void closeWindow() {
        mainWindow.removeWindow(modalWindow);
    }
}