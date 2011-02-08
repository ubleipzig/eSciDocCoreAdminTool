package de.escidoc.admintool.view.resource;

import com.vaadin.data.Item;
import com.vaadin.ui.Window;

import de.escidoc.admintool.app.AdminToolApplication;
import de.escidoc.admintool.service.ResourceService;
import de.escidoc.core.resources.Resource;

public class ResourceViewImpl extends AbstractResourceView {

    private static final long serialVersionUID = -5537726031167765555L;

    private final ResourceFolderView resourceListView;

    private final ResourceEditView resourceEditView;

    private final Window mainWindow;

    private final ResourceService resourceService;

    private final ResourceContainer resourceContainer;

    private final AdminToolApplication app;

    public ResourceViewImpl(final AdminToolApplication app,
        final Window mainWindow, final ResourceFolderView resourceListView,
        final ResourceService resourceService,
        final ResourceContainer resourceContainer) {

        super(resourceListView);
        this.app = app;
        this.mainWindow = mainWindow;
        this.resourceService = resourceService;
        this.resourceListView = resourceListView;
        this.resourceContainer = resourceContainer;
        resourceEditView =
            new ResourceEditViewImpl(app, mainWindow, this, resourceService,
                resourceContainer);

    }

    @Override
    public void showAddView() {
        getSplitPanel().setSecondComponent(
            new ResourceAddViewImpl(mainWindow, this, resourceService,
                resourceContainer));
    }

    @Override
    public void showEditView(final Item item) {
        resourceEditView.bind(item);
        getSplitPanel().setSecondComponent(resourceEditView);
    }

    public void setFormReadOnly(final boolean isReadOnly) {
        resourceEditView.setFormReadOnly(isReadOnly);
    }

    public void setFooterVisible(final boolean isVisible) {
        resourceEditView.setFooterVisible(isVisible);
    }

    @Override
    public void selectInFolderView(final Resource resource) {
        resourceListView.select(resource);
    }
}