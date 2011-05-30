package de.escidoc.admintool.view.resource;

import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;

import de.escidoc.admintool.view.ViewConstants;

public class RawXmlWindow extends Window {

    private final class CancelButtonListener implements ClickListener {
        private static final long serialVersionUID = -1211409730229979129L;

        @Override
        public void buttonClick(final ClickEvent event) {
            closeWindow();
        }
    }

    private static final long serialVersionUID = -4691275024835350852L;

    private static final String MODAL_DIALOG_WIDTH = "460px";

    private static final String MODAL_DIALOG_HEIGHT = "450px";

    private final Button okButton = new Button(ViewConstants.OK_LABEL);

    private final Button cancelBtn = new Button(ViewConstants.CANCEL_LABEL);

    String selectedParent;

    Window mainWindow;

    FormLayout fl = new FormLayout();

    public RawXmlWindow(final Window mainWindow) {
        this.mainWindow = mainWindow;
        init();
    }

    private void init() {
        setContent(fl);
        fl.setMargin(true);
        configureWindow();

        configureTextArea();
        addButtons();
    }

    private final TextField textArea = new TextField(ViewConstants.RAW_METADATA);

    private void configureTextArea() {
        textArea.setRows(20);
        textArea.setWidth(300, Sizeable.UNITS_PIXELS);
        fl.addComponent(textArea);
    }

    private final HorizontalLayout buttons = new HorizontalLayout();

    private void addButtons() {
        fl.addComponent(buttons);
        fl.setComponentAlignment(buttons, Alignment.MIDDLE_RIGHT);
        addOkButton();
        addCancelButton();
    }

    private void addCancelButton() {
        cancelBtn.addListener(new CancelButtonListener());
        buttons.addComponent(cancelBtn);
    }

    void closeWindow() {
        mainWindow.removeWindow(this);
    }

    private void addOkButton() {
        buttons.addComponent(okButton);
    }

    private void configureWindow() {
        setModal(true);
        setCaption("Enter Metadata as Raw XML");
        setHeight(MODAL_DIALOG_HEIGHT);
        setWidth(MODAL_DIALOG_WIDTH);
    }

    public void setSelected(final String selectedParent) {
        this.selectedParent = selectedParent;
    }
}
