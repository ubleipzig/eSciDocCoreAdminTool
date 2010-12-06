package de.escidoc.admintool.view.admintask;

import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Window;

import de.escidoc.admintool.service.ServiceContainer;
import de.escidoc.admintool.view.ViewConstants;
import de.escidoc.admintool.view.admintask.Style.H2;
import de.escidoc.admintool.view.admintask.Style.Ruler;

public class ReindexView extends AbstractAdminTaskView {

    private static final long serialVersionUID = -6037070039048596585L;

    public ReindexView(final ServiceContainer services, final Window mainWindow) {
        super(services, mainWindow);
    }

    @Override
    protected void addView() {
        Label text = new H2(ViewConstants.REINDEX_RESOURCES_TITLE);
        text.setContentMode(Label.CONTENT_XHTML);

        cssLayout.addComponent(text);
        cssLayout.addComponent(new Ruler());

        text = new Label(ViewConstants.REINDEX_TEXT, Label.CONTENT_XHTML);
        cssLayout.addComponent(text);

        final HorizontalLayout hLayout = new HorizontalLayout();
        hLayout.setWidth("100%");
        hLayout.setHeight("250px");

        final ReindexResourceView reindexResourceView =
            new ReindexResourceViewImpl(services.getAdminService(), mainWindow);
        hLayout.addComponent(reindexResourceView);

        cssLayout.addComponent(hLayout);
    }
}