package de.escidoc.admintool.view.lab.orgunit;

import com.vaadin.ui.Tree;
import com.vaadin.ui.Tree.ExpandEvent;

import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.resources.oum.OrganizationalUnit;

class NodeExpandListener implements Tree.ExpandListener {

    private static final long serialVersionUID = -288898779192780110L;

    private final OrgUnitTreeView treeView;

    /**
     * @param orgUnitTreeView
     */
    NodeExpandListener(final OrgUnitTreeView orgUnitTreeView) {
        treeView = orgUnitTreeView;
    }

    @Override
    public void nodeExpand(final ExpandEvent event) {
        final Object selectedOrgUnit = event.getItemId();
        if (selectedOrgUnit == null) {
            return;
        }

        if (selectedOrgUnit instanceof OrganizationalUnit
            && isAddChildrenNeeded(selectedOrgUnit)) {
            addChildren((OrganizationalUnit) selectedOrgUnit);
        }
    }

    private boolean isAddChildrenNeeded(final Object selectedOrgUnit) {
        return !treeView.tree.hasChildren(selectedOrgUnit);
    }

    private void addChildren(final OrganizationalUnit selectedOrgUnit) {
        ((AddChildrenCommand) treeView.addChildrenCommand)
            .setParent(selectedOrgUnit);
        try {
            treeView.addChildrenCommand.execute();
        }
        catch (final EscidocClientException e) {
            // ErrorMessage.show(e);
            e.printStackTrace();
        }
    }
}