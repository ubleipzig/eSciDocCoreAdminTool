package de.escidoc.admintool.view.resource;

import java.util.Collection;

import com.vaadin.data.Item;
import com.vaadin.ui.Window;

import de.escidoc.admintool.service.OrgUnitServiceLab;
import de.escidoc.admintool.service.ResourceService;
import de.escidoc.admintool.view.resource.ResourceTreeView.AddChildrenCommand;
import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;
import de.escidoc.core.resources.oum.OrganizationalUnit;

public class ResourceViewComponentImpl implements ResourceViewComponent {

    private final FolderHeader header = new FolderHeaderImpl(
        "Organizational Units");

    private final ResourceViewImpl resourceView;

    private final ResourceContainer resourceContainer;

    private final AddChildrenCommand addChildrenCommand;

    private final ResourceService resourceService;

    private ShowEditResourceView showEditResourceView;

    private final Window mainWindow;

    public ResourceViewComponentImpl(final Window mainWindow,
        final ResourceService resourceService) throws EscidocClientException {

        this.mainWindow = mainWindow;
        this.resourceService = resourceService;

        resourceContainer = createResourceContainer();
        addChildrenCommand = createAddChildrenCommand(resourceContainer);

        resourceView =
            new ResourceViewImpl(mainWindow, createFolderView(),
                resourceService, resourceContainer);
        resourceView.showAddView();
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
            final ResourceTreeView resourceTreeView =
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

    public ResourceContainer createResourceContainer() throws EscidocException,
        InternalClientException, TransportException {
        return new ResourceContainerImpl(getTopLevelOrgUnits());
    }

    private Collection<OrganizationalUnit> getTopLevelOrgUnits()
        throws EscidocException, InternalClientException, TransportException {
        return ((OrgUnitServiceLab) resourceService).getTopLevelOrgUnits();
    }
}