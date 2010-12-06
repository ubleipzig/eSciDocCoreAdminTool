package de.escidoc.admintool.view.lab.orgunit;

import java.util.Collection;

import com.google.common.base.Preconditions;
import com.vaadin.data.Item;
import com.vaadin.data.util.HierarchicalContainer;
import com.vaadin.ui.Window;

import de.escidoc.admintool.service.OrgUnitService;
import de.escidoc.admintool.service.OrgUnitServiceLab;
import de.escidoc.admintool.view.ViewConstants;
import de.escidoc.admintool.view.resource.AddChildrenCommandImpl;
import de.escidoc.admintool.view.resource.FolderHeader;
import de.escidoc.admintool.view.resource.FolderHeaderImpl;
import de.escidoc.admintool.view.resource.ResourceContainer;
import de.escidoc.admintool.view.resource.ResourceContainerImpl;
import de.escidoc.admintool.view.resource.ResourceTreeView;
import de.escidoc.admintool.view.resource.ShowEditResourceView;
import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;
import de.escidoc.core.resources.oum.OrganizationalUnit;

public class OrgUnitViewLabFactory {

    final OrgUnitService orgUnitService;

    final Window mainWindow;

    private OrgUnitAddViewFactory orgUnitAddViewFactory;

    private OrgUnitViewLab orgUnitViewLab;

    private ResourceTreeView resourceTreeView;

    private OrgUnitAddViewLab orgUnitAddViewLab;

    private OrgUnitEditViewLab orgUnitEditViewLab;

    private HierarchicalContainer container;

    private OrgUnitContainerFactory orgUnitContainerFactory;

    private final OrgUnitServiceLab resourceService;

    public OrgUnitViewLabFactory(final OrgUnitService orgUnitService,
        final OrgUnitServiceLab resourceService, final Window mainWindow)
        throws EscidocException, InternalClientException, TransportException {

        checkForNull(orgUnitService, mainWindow);

        this.orgUnitService = orgUnitService;
        this.resourceService = resourceService;
        this.mainWindow = mainWindow;
        resourceContainer = createResourceContainer();

        createContainerFactory();
        createOrgUnitContainer();
        createOrgUnitAddViewFactory();
        createOrgUnitEditViewFactory();
    }

    private void checkForNull(
        final OrgUnitService orgUnitService, final Window mainWindow) {
        Preconditions.checkNotNull(orgUnitService,
            "orgUnitService can not be null. %s", orgUnitService);
        Preconditions.checkNotNull(mainWindow,
            "mainWindow can not be null. %s", mainWindow);
    }

    private void createContainerFactory() {
        orgUnitContainerFactory = new OrgUnitContainerFactory(orgUnitService);
    }

    private void createOrgUnitContainer() throws EscidocException,
        InternalClientException, TransportException {
        container = orgUnitContainerFactory.create();
    }

    private void createOrgUnitAddViewFactory() throws EscidocException,
        InternalClientException, TransportException {
        orgUnitAddViewFactory =
            new OrgUnitAddViewFactory(mainWindow, orgUnitService, container,
                orgUnitContainerFactory, resourceService, resourceContainer);
    }

    private void createOrgUnitEditViewFactory() {
        orgUnitEditViewLab =
            new OrgUnitEditViewLab(orgUnitService, mainWindow,
                orgUnitContainerFactory, resourceService, resourceContainer);
    }

    public OrgUnitViewLab getOrgUnitViewLab() throws EscidocException,
        InternalClientException, TransportException {
        if (orgUnitViewLab == null) {
            createOrgUnitViewLab();
        }
        orgUnitAddViewLab.setOrgUnitView(orgUnitViewLab);
        orgUnitEditViewLab.setOrgUnitView(orgUnitViewLab);
        return orgUnitViewLab;
    }

    private final FolderHeader header = new FolderHeaderImpl(
        ViewConstants.ORGANIZATIONAL_UNIT_LABEL);

    private final ResourceContainer resourceContainer;

    private ShowEditResourceView showEditResourceView;

    private void createOrgUnitViewLab() throws EscidocException,
        InternalClientException, TransportException {
        resourceTreeView =
            new ResourceTreeView(mainWindow, header, resourceContainer);
        showEditResourceView = new ShowEditResourceView() {

            @Override
            public void execute(final Item item) {
                orgUnitViewLab.select(item);
            }
        };
        resourceTreeView.setEditView(showEditResourceView);
        resourceTreeView.setCommand(new AddChildrenCommandImpl(resourceService,
            resourceContainer));
        resourceTreeView.addResourceNodeExpandListener();
        resourceTreeView.addResourceNodeClickedListener();

        orgUnitAddViewLab = orgUnitAddViewFactory.getOrgUnitAddView();
        orgUnitViewLab =
            new OrgUnitViewLab(resourceTreeView, orgUnitAddViewLab,
                orgUnitEditViewLab);
    }

    private ResourceContainer createResourceContainer()
        throws EscidocException, InternalClientException, TransportException {
        return new ResourceContainerImpl(getTopLevelOrgUnits());
    }

    private Collection<OrganizationalUnit> getTopLevelOrgUnits()
        throws EscidocException, InternalClientException, TransportException {
        return resourceService.getTopLevelOrgUnits();
    }
}