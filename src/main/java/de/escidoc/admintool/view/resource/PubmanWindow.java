package de.escidoc.admintool.view.resource;

import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;

import de.escidoc.admintool.view.ViewConstants;

public class PubmanWindow extends Window {

    private final class CancelButtonListener implements ClickListener {
        private static final long serialVersionUID = -1211409730229979129L;

        @Override
        public void buttonClick(final ClickEvent event) {
            closeWindow();
        }
    }

    private static final long serialVersionUID = -4691275024835350852L;

    private static final String MODAL_DIALOG_WIDTH = "460px";

    private static final String MODAL_DIALOG_HEIGHT = "300px";

    private final Button okButton = new Button(ViewConstants.OK_LABEL);

    private final Button cancelBtn = new Button(ViewConstants.CANCEL_LABEL);

    String selectedParent;

    Window mainWindow;

    CssLayout pubman = new CssLayout();

    protected final TextField alternativeField = new TextField(
        ViewConstants.ALTERNATIVE_LABEL);

    protected final TextField identifierField = new TextField(
        ViewConstants.IDENTIFIER_LABEL);

    protected final TextField orgTypeField = new TextField(
        ViewConstants.ORGANIZATION_TYPE);

    private final TextField cityField = new TextField(ViewConstants.CITY_LABEL);

    private final TextField countryField = new TextField(
        ViewConstants.COUNTRY_LABEL);

    protected final TextField coordinatesField = new TextField(
        ViewConstants.COORDINATES_LABEL);

    FormLayout fl = new FormLayout();

    public PubmanWindow(final Window mainWindow) {
        this.mainWindow = mainWindow;
        init();
    }

    private void init() {
        setContent(fl);
        fl.setMargin(true);
        configureWindow();
        configureCountryField();
        addButtons();
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
        setCaption("PubMan Metadata");
        setHeight(MODAL_DIALOG_HEIGHT);
        setWidth(MODAL_DIALOG_WIDTH);
    }

    private void configureCountryField() {
        alternativeField.setWidth(300, Sizeable.UNITS_PIXELS);
        identifierField.setWidth(300, Sizeable.UNITS_PIXELS);
        orgTypeField.setWidth(300, Sizeable.UNITS_PIXELS);
        coordinatesField.setWidth(300, Sizeable.UNITS_PIXELS);
        cityField.setWidth(300, Sizeable.UNITS_PIXELS);
        countryField.setWidth(300, Sizeable.UNITS_PIXELS);

        fl.addComponent(alternativeField);
        fl.addComponent(identifierField);
        fl.addComponent(orgTypeField);
        fl.addComponent(coordinatesField);
        fl.addComponent(cityField);
        fl.addComponent(countryField);
    }

    public void setSelected(final String selectedParent) {
        this.selectedParent = selectedParent;
    }
}