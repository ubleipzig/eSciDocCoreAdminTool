package de.escidoc.admintool.view.resource;

import com.google.common.base.Preconditions;
import com.vaadin.data.Item;
import com.vaadin.ui.Window;

import de.escidoc.admintool.app.AdminToolApplication;
import de.escidoc.admintool.domain.PdpRequest;
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

    private final AdminToolApplication app;

    private final PdpRequest pdpRequest;

    public ResourceViewComponentImpl(final AdminToolApplication app,
        final Window mainWindow, final ResourceService resourceService,
        final ResourceContainer resourceContainer, final PdpRequest pdpRequest) {
        Preconditions.checkNotNull(mainWindow, "mainWindow is null: %s",
            mainWindow);
        Preconditions.checkNotNull(resourceService,
            "resourceService is null: %s", resourceService);
        Preconditions.checkNotNull(resourceContainer,
            "resourceContainer is null: %s", resourceContainer);
        Preconditions.checkNotNull(pdpRequest, "pdpRequest is null: %s",
            pdpRequest);
        this.app = app;
        this.mainWindow = mainWindow;
        this.resourceService = resourceService;
        this.resourceContainer = resourceContainer;
        this.pdpRequest = pdpRequest;
    }

    public void init() throws EscidocClientException {
        addChildrenCommand = createAddChildrenCommand(resourceContainer);
        resourceView =
            new ResourceViewImpl(app, mainWindow, createFolderView(),
                resourceService, resourceContainer, pdpRequest);
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