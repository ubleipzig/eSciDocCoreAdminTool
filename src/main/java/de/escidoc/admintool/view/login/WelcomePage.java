package de.escidoc.admintool.view.login;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import de.escidoc.admintool.app.AdminToolApplication;
import de.escidoc.admintool.view.ViewConstants;

public class WelcomePage extends CustomComponent {
    private static final long serialVersionUID = 5514330045540866939L;

    private final VerticalLayout viewLayout = new VerticalLayout();

    private final Panel panel = new Panel();

    private final FormLayout fLayout = new FormLayout();

    private final TextField escidocServiceUrl = new TextField(
        ViewConstants.ESCIDOC_URL_TEXTFIELD);

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
        escidocServiceUrl.setWidth(265, UNITS_PIXELS);
        escidocServiceUrl.setImmediate(true);
        escidocServiceUrl.focus();
        escidocServiceUrl.setRequired(true);
        escidocServiceUrl
            .setRequiredError("eSciDoc Location can not be empty.");
        escidocServiceUrl.setInputPrompt("http://");
        fLayout.addComponent(escidocServiceUrl);
    }

    private void addLoginButton() {
        loginButton = new Button(ViewConstants.LOGIN_LABEL);
        loginButton.addListener(new LoginButtonListenerImpl(escidocServiceUrl,
            app));

        footer.addComponent(loginButton);
        footer.setComponentAlignment(loginButton, Alignment.MIDDLE_RIGHT);
    }
}