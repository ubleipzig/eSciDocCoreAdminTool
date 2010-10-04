package de.escidoc.admintool.view.lab.orgunit;

import com.google.common.base.Preconditions;
import com.vaadin.data.util.HierarchicalContainer;
import com.vaadin.ui.Window;

import de.escidoc.admintool.service.OrgUnitService;

public class OrgUnitAddViewFactory {
    private final Window mainWindow;

    private final OrgUnitService orgUnitService;

    private final HierarchicalContainer orgUnitContainer;

    public OrgUnitAddViewFactory(final Window mainWindow,
        final OrgUnitService orgUnitService,
        final HierarchicalContainer orgUnitContainer) {
        Preconditions.checkNotNull(mainWindow, "mainWindow can not be null.");
        Preconditions.checkNotNull(orgUnitService,
            "orgUnitService can not be null.");
        Preconditions.checkNotNull(orgUnitContainer,
            "orgUnitContainer can not be null.");
        this.mainWindow = mainWindow;
        this.orgUnitService = orgUnitService;
        this.orgUnitContainer = orgUnitContainer;
    }

    public OrgUnitAddViewLab getOrgUnitAddView() {
        assert (mainWindow != null) : "MainWindow can not be null";
        final OrgUnitAddViewLab orgUnitAddView =
            new OrgUnitAddViewLab(orgUnitService, orgUnitContainer, mainWindow);
        return orgUnitAddView;
    }
}