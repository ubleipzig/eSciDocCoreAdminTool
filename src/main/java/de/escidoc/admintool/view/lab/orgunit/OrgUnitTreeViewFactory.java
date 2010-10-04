package de.escidoc.admintool.view.lab.orgunit;

import com.google.common.base.Preconditions;
import com.vaadin.data.util.HierarchicalContainer;

import de.escidoc.admintool.app.Command;
import de.escidoc.admintool.service.OrgUnitService;
import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;

public class OrgUnitTreeViewFactory {

    private OrgUnitTreeViewLab orgUnitTreeView;

    private final OrgUnitService orgUnitService;

    private final HierarchicalContainer orgUnitContainer;

    public OrgUnitTreeViewFactory(final OrgUnitService orgUnitService,
        final HierarchicalContainer orgUnitContainer) {
        Preconditions.checkNotNull(orgUnitService,
            "OrgUnitService can not be null.");
        Preconditions.checkNotNull(orgUnitContainer,
            "orgUnitContainer can not be null.");
        this.orgUnitService = orgUnitService;
        this.orgUnitContainer = orgUnitContainer;
    }

    public OrgUnitTreeViewLab getOrgUnitTreeView() throws EscidocException,
        InternalClientException, TransportException {
        if (isNotInitialized()) {
            initOrgUnitView();
        }
        return orgUnitTreeView;
    }

    private boolean isNotInitialized() {
        return orgUnitTreeView == null;
    }

    private void initOrgUnitView() throws EscidocException,
        InternalClientException, TransportException {
        orgUnitTreeView =
            create(orgUnitContainer, new AddChildrenCommand(
                new OrgUnitContainerFactory(orgUnitService)));
    }

    private OrgUnitTreeViewLab create(
        final HierarchicalContainer container, final Command command) {
        return new OrgUnitTreeViewLab(container, command);
    }
}