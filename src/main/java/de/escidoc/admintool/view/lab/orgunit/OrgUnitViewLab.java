package de.escidoc.admintool.view.lab.orgunit;

import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.SplitPanel;

import de.escidoc.admintool.view.ViewConstants;
import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;
import de.escidoc.core.resources.oum.OrganizationalUnit;

@SuppressWarnings("serial")
public class OrgUnitViewLab extends CustomComponent {

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
        setCompositionRoot(splitPanel);
        buildUI();
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

    private void showAddView() {
        splitPanel.setSecondComponent(orgUnitAddViewLab);
    }

    // FIXME: What is the diff between select(X x) and showEditView(X x)
    public void select(final OrganizationalUnit orgUnit) {
        (orgUnitTreeViewLab).select(orgUnit);
        showEditView(orgUnit);
    }

    private void showEditView(final OrganizationalUnit orgUnit) {
        orgUnitEditViewLab.setOrgUnit(orgUnit);
        splitPanel.setSecondComponent(orgUnitEditViewLab);
    }
}