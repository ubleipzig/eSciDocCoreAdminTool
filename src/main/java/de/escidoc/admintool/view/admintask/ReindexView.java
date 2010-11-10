package de.escidoc.admintool.view.admintask;

import com.google.common.base.Preconditions;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import de.escidoc.admintool.service.ServiceContainer;
import de.escidoc.admintool.view.ViewConstants;
import de.escidoc.admintool.view.admintask.Style.H2;
import de.escidoc.admintool.view.admintask.Style.Ruler;

public class ReindexView extends CustomComponent {

    private static final long serialVersionUID = 8826408006508418648L;

    private final VerticalLayout mainLayout = new VerticalLayout();

    private final CssLayout margin = new CssLayout();

    private final ServiceContainer services;

    private final Window mainWindow;

    public ReindexView(final ServiceContainer services, final Window mainWindow) {
        Preconditions.checkNotNull(services, "services is null: %s", services);
        Preconditions.checkNotNull(mainWindow, "mainWindow is null: %s",
            mainWindow);

        this.services = services;
        this.mainWindow = mainWindow;

        init();
    }

    protected void init() {
        setCompositionRoot(mainLayout);

        margin.setMargin(true);
        margin.setWidth("100%");
        mainLayout.addComponent(margin);

        final HorizontalLayout texts = new HorizontalLayout();
        texts.setSpacing(true);
        texts.setWidth("100%");
        texts.setMargin(false, false, true, false);
        margin.addComponent(texts);

        addReindexResourceView();
    }

    protected void addReindexResourceView() {
        Label text = new H2(ViewConstants.REINDEX_RESOURCES_TITLE);
        text.setContentMode(Label.CONTENT_XHTML);

        margin.addComponent(text);
        margin.addComponent(new Ruler());

        text = new Label(ViewConstants.REINDEX_TEXT, Label.CONTENT_XHTML);
        margin.addComponent(text);

        final HorizontalLayout hLayout = new HorizontalLayout();
        hLayout.setWidth("100%");
        hLayout.setHeight("250px");

        final ReindexResourceView reindexResourceView =
            new ReindexResourceViewImpl(services.getAdminService(), mainWindow);
        hLayout.addComponent(reindexResourceView);

        margin.addComponent(hLayout);
    }
}
