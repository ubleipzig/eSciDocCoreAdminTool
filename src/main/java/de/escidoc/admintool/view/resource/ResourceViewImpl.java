package de.escidoc.admintool.view.resource;

import com.vaadin.data.Item;
import com.vaadin.ui.Window;

import de.escidoc.admintool.service.ResourceService;

public class ResourceViewImpl extends AbstractResourceView {

    private static final long serialVersionUID = -5537726031167765555L;

    private final ResourceEditView resourceEditView;

    private final ResourceAddView resourceAddView;

    public ResourceViewImpl(final Window mainWindow,
        final ResourceFolderView resourceListView,
        final ResourceService resourceService,
        final ResourceContainer resourceContainer) {

        super(resourceListView);

        resourceEditView =
            new ResourceEditViewImpl(mainWindow, this, resourceService,
                resourceContainer);
        resourceAddView =
            new ResourceAddViewImpl(mainWindow, this, resourceService,
                resourceContainer);
    }

    @Override
    public void showAddView() {
        getSplitPanel().setSecondComponent(resourceAddView);
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
}