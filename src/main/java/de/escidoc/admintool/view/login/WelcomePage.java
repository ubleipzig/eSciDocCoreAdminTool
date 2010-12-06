package de.escidoc.admintool.view.login;

import java.util.Arrays;

import com.vaadin.ui.AbstractSelect.Filtering;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

import de.escidoc.admintool.app.AdminToolApplication;
import de.escidoc.admintool.view.ViewConstants;

public class WelcomePage extends CustomComponent {
    private static final long serialVersionUID = 5514330045540866939L;

    private final VerticalLayout viewLayout = new VerticalLayout();

    private final Panel panel = new Panel();

    private final FormLayout fLayout = new FormLayout();

    private final ComboBox escidocComboBox = new ComboBox(
        ViewConstants.ESCIDOC_URL_TEXTFIELD, Arrays.asList(new String[] {
            "http://localhost:8080", "http://escidev4.fiz-karlsruhe.de:8080",
            "http://escidev6.fiz-karlsruhe.de:8080" }));

    private final HorizontalLayout footer = new HorizontalLayout();

    private final AdminToolApplication app;

    private Button loginButton;

    public WelcomePage(final AdminToolApplication app) {
        this.app = app;
        init();
    }

    private void init() {
        setStyleName("landing-view");
        setCompositionRoot(viewLayout);
        setSizeFull();
        viewLayout.setSizeFull();
        viewLayout.setStyleName("landing-view");

        viewLayout.addComponent(panel);
        viewLayout.setComponentAlignment(panel, Alignment.MIDDLE_CENTER);

        panel.setWidth(ViewConstants.LOGIN_WINDOW_WIDTH);
        panel.setCaption(ViewConstants.WELCOMING_MESSAGE);

        addComboBox();
        addFooters();

        panel.addComponent(fLayout);
    }

    private void addFooters() {
        footer.setWidth(100, UNITS_PERCENTAGE);
        footer.setMargin(true);
        fLayout.addComponent(footer);
        addLoginButton();
    }

    private void addComboBox() {
        escidocComboBox.setWidth(265, UNITS_PIXELS);
        escidocComboBox.setFilteringMode(Filtering.FILTERINGMODE_CONTAINS);
        escidocComboBox.setImmediate(true);
        escidocComboBox.setNullSelectionAllowed(false);
        escidocComboBox.focus();
        escidocComboBox.setRequired(true);
        escidocComboBox.setRequiredError("eSciDoc Location can not be empty.");
        escidocComboBox.setInputPrompt("http://localhost:8080");
        escidocComboBox.setValue("http://localhost:8080");
        escidocComboBox.setNewItemsAllowed(true);
        fLayout.addComponent(escidocComboBox);
    }

    private void addLoginButton() {
        loginButton = new Button(ViewConstants.LOGIN_LABEL);
        loginButton.addListener(new LoginButtonListenerImpl(escidocComboBox,
            app));

        footer.addComponent(loginButton);
        footer.setComponentAlignment(loginButton, Alignment.MIDDLE_RIGHT);
    }
}