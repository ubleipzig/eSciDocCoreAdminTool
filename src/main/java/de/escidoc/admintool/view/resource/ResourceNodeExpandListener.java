package de.escidoc.admintool.view.resource;

import com.google.common.base.Preconditions;
import com.vaadin.ui.Tree;
import com.vaadin.ui.Tree.ExpandEvent;
import com.vaadin.ui.Tree.ExpandListener;
import com.vaadin.ui.Window;
import de.escidoc.admintool.view.ModalDialog;
import de.escidoc.admintool.view.resource.ResourceTreeView.AddChildrenCommand;
import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.resources.oum.OrganizationalUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResourceNodeExpandListener implements ExpandListener {

    private static final long serialVersionUID = -288898779192780110L;

    private static final Logger LOG = LoggerFactory
        .getLogger(ResourceNodeExpandListener.class);

    private final Tree tree;

    private final AddChildrenCommand addChildrenCommand;

    private final Window mainWindow;

    ResourceNodeExpandListener(final Tree tree, final Window mainWindow,
        final AddChildrenCommand addChildrenCommand) {
        preconditions(tree, mainWindow, addChildrenCommand);
        this.tree = tree;
        this.mainWindow = mainWindow;
        this.addChildrenCommand = addChildrenCommand;
    }

    private void preconditions(
        final Tree tree, final Window mainWindow,
        final AddChildrenCommand addChildrenCommand) {
        Preconditions.checkNotNull(tree, "tree is null: %s", tree);
        Preconditions.checkNotNull(mainWindow, "mainWindow is null: %s",
            mainWindow);
        Preconditions.checkNotNull(addChildrenCommand,
            "addChildrenCommand is null: %s", addChildrenCommand);
    }

    @Override
    public void nodeExpand(final ExpandEvent event) {
        // expand selected org unit and show its children.
        final Object selectedOrgUnit = event.getItemId();
        if (selectedOrgUnit == null) {
            return;
        }

        if (isOrgUnit(selectedOrgUnit)
            && isAddChildrenNeeded((OrganizationalUnit) selectedOrgUnit)) {
            addChildrenFor((OrganizationalUnit) selectedOrgUnit);
        }
    }

    private boolean isOrgUnit(final Object selectedOrgUnit) {
        return selectedOrgUnit instanceof OrganizationalUnit;
    }

    private boolean isAddChildrenNeeded(final OrganizationalUnit selectedOrgUnit) {
        return !tree.hasChildren(selectedOrgUnit);
    }

    private void addChildrenFor(final OrganizationalUnit parent) {
        Preconditions.checkNotNull(parent, "parent is null: %s", parent);
        try {
            addChildrenCommand.addChildrenFor(parent);
        }
        catch (final EscidocClientException e) {
            ModalDialog.show(mainWindow, e);
        }
    }
}
