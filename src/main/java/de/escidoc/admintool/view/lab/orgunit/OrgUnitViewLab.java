package de.escidoc.admintool.view.lab.orgunit;

import com.google.common.base.Preconditions;
import com.vaadin.data.Item;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.SplitPanel;

import de.escidoc.admintool.view.ViewConstants;
import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;
import de.escidoc.core.resources.oum.OrganizationalUnit;

public class OrgUnitViewLab extends CustomComponent {
    private static final long serialVersionUID = -4908615257310260128L;

    private final SplitPanel splitPanel = new SplitPanel();

    private final OrgUnitTreeViewLab orgUnitTreeViewLab;

    private final OrgUnitAddViewLab orgUnitAddViewLab;

    private final OrgUnitEditViewLab orgUnitEditViewLab;

    public OrgUnitViewLab(final OrgUnitTreeViewLab orgUnitTreeViewLab,
        final OrgUnitAddViewLab orgUnitAddViewLab,
        final OrgUnitEditViewLab orgUnitEditViewLab) throws EscidocException,
        InternalClientException, TransportException {
        this.orgUnitTreeViewLab = orgUnitTreeViewLab;
        this.orgUnitAddViewLab = orgUnitAddViewLab;
        this.orgUnitEditViewLab = orgUnitEditViewLab;
        checkForNull();
        setCompositionRoot(splitPanel);
        buildUI();
    }

    private void checkForNull() {
        Preconditions.checkNotNull(orgUnitTreeViewLab,
            "orgUnitTreeViewLab can not be null");
        Preconditions.checkNotNull(orgUnitAddViewLab,
            "orgUnitAddViewLab can not be null");
        Preconditions.checkNotNull(orgUnitEditViewLab,
            "orgUnitEditViewLab can not be null");
    }

    private void buildUI() throws EscidocException, InternalClientException,
        TransportException {
        addStyleName("view");
        setSizeFull();
        splitPanel.setSplitPosition(ViewConstants.SPLIT_POSITION_IN_PERCENT);
        splitPanel.setOrientation(SplitPanel.ORIENTATION_HORIZONTAL);
        splitPanel.setFirstComponent(orgUnitTreeViewLab);
        showAddView();
    }

    public void showAddView() {
        splitPanel.setSecondComponent(orgUnitAddViewLab);
    }

    void showEditView(final Item item) {
        orgUnitEditViewLab.setOrgUnit(item);
        splitPanel.setSecondComponent(orgUnitEditViewLab);
    }

    public void select(final Item item) {
        orgUnitTreeViewLab.select(item);
        showEditView(item);
    }

    public void open(final String comment) throws EscidocException,
        InternalClientException, TransportException {
        orgUnitEditViewLab.open(comment);
    }

    public void close(final String comment) throws EscidocException,
        InternalClientException, TransportException {
        orgUnitEditViewLab.close(comment);
    }

    public void deleteOrgUnit() throws EscidocException,
        InternalClientException, TransportException {
        final OrganizationalUnit deletedOrgUnit =
            orgUnitEditViewLab.deleteOrgUnit();
        removeFromTreeView(deletedOrgUnit);
        showAddView();
    }

    private void removeFromTreeView(final OrganizationalUnit orgUnit) {
        orgUnitTreeViewLab.delete(orgUnit);
    }
}