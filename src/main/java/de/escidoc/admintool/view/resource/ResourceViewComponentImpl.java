package de.escidoc.admintool.view.resource;

import com.google.common.base.Preconditions;
import com.vaadin.data.Item;
import com.vaadin.ui.Window;

import de.escidoc.admintool.service.OrgUnitServiceLab;
import de.escidoc.admintool.service.ResourceService;
import de.escidoc.admintool.view.resource.ResourceTreeView.AddChildrenCommand;
import de.escidoc.core.client.exceptions.EscidocClientException;

public class ResourceViewComponentImpl implements ResourceViewComponent {

    private final FolderHeader header = new FolderHeaderImpl(
        "Organizational Units");

    private ResourceViewImpl resourceView;

    private final ResourceContainer resourceContainer;

    private AddChildrenCommand addChildrenCommand;

    private final ResourceService resourceService;

    private ShowEditResourceView showEditResourceView;

    private final Window mainWindow;

    private ResourceTreeView resourceTreeView;

    public ResourceViewComponentImpl(final Window mainWindow,
        final ResourceService resourceService,
        final ResourceContainer resourceContainer) {
        Preconditions.checkNotNull(mainWindow, "mainWindow is null: %s",
            mainWindow);
        Preconditions.checkNotNull(resourceService,
            "resourceService is null: %s", resourceService);
        Preconditions.checkNotNull(resourceContainer,
            "resourceContainer is null: %s", resourceContainer);
        this.mainWindow = mainWindow;
        this.resourceService = resourceService;
        this.resourceContainer = resourceContainer;
    }

    public void init() throws EscidocClientException {
        // resourceContainer = getResourceContainer();
        addChildrenCommand = createAddChildrenCommand(resourceContainer);
        resourceView =
            new ResourceViewImpl(mainWindow, createFolderView(),
                resourceService, resourceContainer);
    }

    private ResourceFolderView createFolderView() {
        if (resourceContainer.size() == 0) {
            return new ContainerEmptyView(header);
        }
        else {
            showEditResourceView = new ShowEditResourceView() {
                @Override
                public void execute(final Item item) {
                    resourceView.showEditView(item);
                }
            };
            resourceTreeView =
                new ResourceTreeView(mainWindow, header, resourceContainer);
            resourceTreeView.setEditView(showEditResourceView);
            resourceTreeView.setCommand(addChildrenCommand);
            resourceTreeView.addResourceNodeExpandListener();
            resourceTreeView.addResourceNodeClickedListener();
            return resourceTreeView;
        }
    }

    public ResourceView getResourceView() {
        return resourceView;
    }

    private AddChildrenCommandImpl createAddChildrenCommand(
        final ResourceContainer resourceContainer) {
        return new AddChildrenCommandImpl((OrgUnitServiceLab) resourceService,
            resourceContainer);
    }

    // public ResourceContainer getResourceContainer() throws EscidocException,
    // InternalClientException, TransportException {
    // if (resourceContainer == null) {
    // resourceContainer = createResourceContainer();
    // }
    // return resourceContainer;
    // }

    // public ResourceContainer createResourceContainer() throws
    // EscidocException,
    // InternalClientException, TransportException {
    // return new ResourceContainerImpl(getTopLevelOrgUnits());
    // }

    // private Collection<OrganizationalUnit> getTopLevelOrgUnits()
    // throws EscidocException, InternalClientException, TransportException {
    // return ((OrgUnitServiceLab) resourceService).getTopLevelOrgUnits();
    // }

    @Override
    public void showFirstItemInEditView() {
        if (resourceContainer.isEmpty()) {
            resourceView.showAddView();
        }
        else {
            resourceTreeView.tree.select(resourceContainer.firstResource());
            final Item item = resourceContainer.firstResourceAsItem();
            resourceView.showEditView(item);
        }
    }
}