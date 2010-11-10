package de.escidoc.admintool.view.resource;

import com.google.common.base.Preconditions;
import com.vaadin.data.Container;
import com.vaadin.data.Item;


public class ResouceViewComponentImpl implements ResourceViewComponent {

    private static final String RESOURCES = "Resources";

    private final FolderHeader header = new FolderHeaderImpl(RESOURCES);

    private final ResourceViewImpl resourceView;

    private final Container resourceContainer;

    private ShowEditResourceView showEditResourceView;

    public ResouceViewComponentImpl(final Container resourceContainer) {

        Preconditions.checkNotNull(resourceContainer,
            "resourceContainer is null: %s", resourceContainer);

        this.resourceContainer = resourceContainer;
        resourceView = new ResourceViewImpl(createFolderView());
    }

    private ResourceFolderView createFolderView() {
        if (resourceContainer.size() == 0) {
            return new ContainerEmptyView(header);
        }
        else {
            showEditResourceView = new ShowEditResourceView() {
                @Override
                void execute(final Item item) {
                    resourceView.showEditView(item);
                }
            };
            return new ResourceTreeView(header, resourceContainer,
                showEditResourceView);
        }
    }

    public ResourceView getContainerView() {
        return resourceView;
    }
}