package de.escidoc.admintool.view.admintask;

import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Window;

import de.escidoc.admintool.service.ServiceContainer;
import de.escidoc.admintool.view.ViewConstants;
import de.escidoc.admintool.view.admintask.Style.H2;
import de.escidoc.admintool.view.admintask.Style.Ruler;

public class RepositoryInfoFooView extends AbstractAdminTaskView {

    private static final long serialVersionUID = 3758827172683416515L;

    public RepositoryInfoFooView(final ServiceContainer services,
        final Window mainWindow) {
        super(services, mainWindow);
    }

    @Override
    public void addView() {

        Label text = new H2(ViewConstants.REPOSITORY_INFORMATION_TITLE);
        text.setContentMode(Label.CONTENT_XHTML);
        cssLayout.addComponent(text);

        cssLayout.addComponent(new Ruler());

        text = new Label(ViewConstants.REPO_INFO_TEXT, Label.CONTENT_XHTML);
        cssLayout.addComponent(text);

        final HorizontalLayout hLayout = new HorizontalLayout();
        hLayout.setWidth(100, UNITS_PERCENTAGE);
        hLayout.setHeight(100, UNITS_PERCENTAGE);

        final RepoInfoClickListener listener =
            new RepoInfoClickListener(mainWindow, services.getAdminService());
        hLayout.addComponent(new RepositoryInfoView(listener));
        cssLayout.addComponent(hLayout);
    }

}
