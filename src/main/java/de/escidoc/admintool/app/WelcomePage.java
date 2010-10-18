package de.escidoc.admintool.app;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.ui.AbstractSelect.Filtering;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

import de.escidoc.admintool.view.ViewConstants;

public class WelcomePage extends CustomComponent {

    private static final Logger log = LoggerFactory
        .getLogger(WelcomePage.class);

    private static final String LOGIN = "Login";

    private static final String GUEST_LOGIN = "Guest";

    private final VerticalLayout viewLayout = new VerticalLayout();

    private final Panel panel = new Panel();

    private final FormLayout fLayout = new FormLayout();

    private final ComboBox escidocComboBox = new ComboBox(
        ViewConstants.ESCIDOC_URL_TEXTFIELD, Arrays.asList(new String[] {
            "http://localhost:8080", "http://escidev4.fiz-karlsruhe.de:8080",
            "http://escidev6.fiz-karlsruhe.de:8080" }));

    private final HorizontalLayout footer = new HorizontalLayout();

    private Button okButton;

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

        panel.setWidth("400px");
        panel.setCaption(ViewConstants.WELCOMING_MESSAGE);

        addComboBox();
        addFooters();

        panel.addComponent(fLayout);
    }

    private void addFooters() {
        footer.setWidth(100, UNITS_PERCENTAGE);
        footer.setMargin(true);
        fLayout.addComponent(footer);
        addLoginAsGuestButton();
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

    private void addLoginAsGuestButton() {
        okButton = new Button(GUEST_LOGIN);
        okButton.addListener(new GuestButtonListener(escidocComboBox, app));

        footer.addComponent(okButton);
        footer.setComponentAlignment(okButton, Alignment.BOTTOM_RIGHT);
    }

    private void addLoginButton() {
        loginButton = new Button(LOGIN);
        loginButton.addListener(new LoginButtonListenerImpl(escidocComboBox,
            app));

        footer.addComponent(loginButton);
        // footer.setComponentAlignment(loginButton, Alignment.BOTTOM_RIGHT);
    }
}