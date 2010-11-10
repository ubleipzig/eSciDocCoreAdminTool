package de.escidoc.admintool.view.lab.orgunit;

import com.google.common.base.Preconditions;
import com.vaadin.data.util.HierarchicalContainer;
import com.vaadin.ui.Window;

import de.escidoc.admintool.service.OrgUnitService;
import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;

public class OrgUnitViewLabFactory {

    private OrgUnitTreeViewFactory orgUnitTreeViewFactory;

    final OrgUnitService orgUnitService;

    final Window mainWindow;

    private OrgUnitAddViewFactory OrgUnitAddViewFactory;

    private OrgUnitViewLab orgUnitViewLab;

    private OrgUnitTreeView orgUnitTreeViewLab;

    private OrgUnitAddViewLab orgUnitAddViewLab;

    private OrgUnitEditViewLab orgUnitEditViewLab;

    private HierarchicalContainer container;

    private OrgUnitContainerFactory orgUnitContainerFactory;

    public OrgUnitViewLabFactory(final OrgUnitService orgUnitService,
        final Window mainWindow) throws EscidocException,
        InternalClientException, TransportException {

        checkForNull(orgUnitService, mainWindow);

        this.orgUnitService = orgUnitService;
        this.mainWindow = mainWindow;

        createContainerFactory();
        createOrgUnitContainer();
        createOrgUnitTreeViewFactory();
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

    private void createOrgUnitTreeViewFactory() {
        orgUnitTreeViewFactory =
            new OrgUnitTreeViewFactory(orgUnitService, container);
    }

    private void createOrgUnitAddViewFactory() throws EscidocException,
        InternalClientException, TransportException {
        OrgUnitAddViewFactory =
            new OrgUnitAddViewFactory(mainWindow, orgUnitService, container,
                orgUnitContainerFactory);
    }

    private void createOrgUnitEditViewFactory() {
        orgUnitEditViewLab =
            new OrgUnitEditViewLab(orgUnitService, mainWindow,
                orgUnitContainerFactory);
    }

    public OrgUnitViewLab getOrgUnitViewLab() throws EscidocException,
        InternalClientException, TransportException {
        if (orgUnitViewLab == null) {
            createOrgUnitViewLab();
        }
        orgUnitAddViewLab.setOrgUnitView(orgUnitViewLab);
        orgUnitTreeViewLab.setOrgUnitView(orgUnitViewLab);
        orgUnitEditViewLab.setOrgUnitView(orgUnitViewLab);
        return orgUnitViewLab;
    }

    private void createOrgUnitViewLab() throws EscidocException,
        InternalClientException, TransportException {
        orgUnitTreeViewLab = orgUnitTreeViewFactory.getOrgUnitTreeView();
        orgUnitAddViewLab = OrgUnitAddViewFactory.getOrgUnitAddView();
        orgUnitViewLab =
            new OrgUnitViewLab(orgUnitTreeViewLab, orgUnitAddViewLab,
                orgUnitEditViewLab);
    }
}