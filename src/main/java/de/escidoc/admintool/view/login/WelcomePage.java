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
    private static final String LANDING_VIEW_STYLE_NAME = "landing-view";

    private static final long serialVersionUID = 5514330045540866939L;

    private final VerticalLayout viewLayout = new VerticalLayout();

    private final Panel panel = new Panel();

    private final FormLayout fLayout = new FormLayout();

    private final TextField escidocServiceUrl = new TextField(
        ViewConstants.ESCIDOC_URL_TEXTFIELD);

    private final HorizontalLayout footer = new HorizontalLayout();

    private final AdminToolApplication app;

    private Button startButton;

    public WelcomePage(final AdminToolApplication app) {
        this.app = app;
    }

    public void init() {
        setStyleName(LANDING_VIEW_STYLE_NAME);
        setCompositionRoot(viewLayout);
        setSizeFull();
        viewLayout.setSizeFull();
        viewLayout.setStyleName(LANDING_VIEW_STYLE_NAME);

        viewLayout.addComponent(panel);
        viewLayout.setComponentAlignment(panel, Alignment.MIDDLE_CENTER);

        panel.setWidth(ViewConstants.LOGIN_WINDOW_WIDTH);
        panel.setCaption(ViewConstants.WELCOMING_MESSAGE);

        addEscidocUrlField();
        addFooters();

        panel.addComponent(fLayout);
    }

    private void addFooters() {
        footer.setWidth(100, UNITS_PERCENTAGE);
        footer.setMargin(true);
        fLayout.addComponent(footer);
        addStartButton();
    }

    private void addEscidocUrlField() {
        escidocServiceUrl.setWidth(265, UNITS_PIXELS);
        escidocServiceUrl.setImmediate(true);
        escidocServiceUrl.focus();
        escidocServiceUrl.setRequired(true);
        escidocServiceUrl
            .setRequiredError("eSciDoc Location can not be empty.");
        escidocServiceUrl.setInputPrompt("http://");
        fLayout.addComponent(escidocServiceUrl);
    }

    private void addStartButton() {
        startButton = new Button(ViewConstants.OK_LABEL);
        final StartButtonListenerImpl listener =
            new StartButtonListenerImpl(escidocServiceUrl, app);
        startButton.addListener(listener);
        footer.addComponent(startButton);
        footer.setComponentAlignment(startButton, Alignment.MIDDLE_RIGHT);
    }
}