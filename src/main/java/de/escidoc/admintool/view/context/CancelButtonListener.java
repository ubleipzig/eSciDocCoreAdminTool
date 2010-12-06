package de.escidoc.admintool.view.context;

import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Window;

final class CancelButtonListener implements ClickListener {
  private final Window mainWindow;
  private final Window modalWindow;

  CancelButtonListener(Window mainWindow, Window modalWindow) {
    this.mainWindow = mainWindow;
    this.modalWindow = modalWindow;
  }

  private static final long serialVersionUID = -1211409730229979129L;

  public void buttonClick(final ClickEvent event) {
    closeWindow();
  }

  private void closeWindow() {
    mainWindow.removeWindow(modalWindow);
  }
}