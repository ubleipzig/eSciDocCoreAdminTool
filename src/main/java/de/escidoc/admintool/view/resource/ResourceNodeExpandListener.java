package de.escidoc.admintool.view.resource;

import com.vaadin.ui.Tree.ExpandEvent;
import com.vaadin.ui.Tree.ExpandListener;

import de.escidoc.admintool.view.lab.orgunit.AddChildrenCommand;
import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.resources.oum.OrganizationalUnit;

public class ResourceNodeExpandListener implements ExpandListener {

    private static final long serialVersionUID = -288898779192780110L;

    private final ResourceTreeView treeView;

    ResourceNodeExpandListener(final ResourceTreeView treeView) {
        this.treeView = treeView;
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
