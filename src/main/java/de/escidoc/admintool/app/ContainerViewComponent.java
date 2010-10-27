package de.escidoc.admintool.app;

import com.google.common.base.Preconditions;

import de.escidoc.admintool.service.EscidocService;
import de.escidoc.admintool.view.ResourceView;

public class ContainerViewComponent implements ResourceViewComponent {

    private final AdminToolApplication app;

    private final EscidocService service;

    private ResourceListView resourceListView;

    private ResourceView resourceView;

    public ContainerViewComponent(final AdminToolApplication app,
        final EscidocService service) {
        Preconditions
            .checkNotNull(app, "AdminToolApplication can not be null.");
        Preconditions.checkNotNull(service, "service can not be null.");

        this.app = app;
        this.service = service;
    }

    public ResourceListView getResourceListView() {
        return resourceListView;
    }

    public void setResourceListView(final ResourceListView resourceListView) {
        this.resourceListView = resourceListView;
    }

    public ResourceView getContainerView() {
        if (resourceView == null) {
            setResourceListView(new ContainerTreeView(app, service));
            setResourceView(new ContainerResourceView(app,
                getResourceListView()));
        }
        return resourceView;
    }

    private void setResourceView(final ResourceView resourceView) {
        this.resourceView = resourceView;
    }

    public void showAddView() {
        resourceView.showAddView();
    }
}
