package de.escidoc.admintool.view.navigation;

import com.google.common.base.Preconditions;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Tree;

import de.escidoc.admintool.app.AdminToolApplication;
import de.escidoc.admintool.view.ViewConstants;

public class NavigationTreeImpl extends CustomComponent
    implements NavigationTree {

    private static final long serialVersionUID = -5022424682917592102L;

    private final Tree tree = new Tree();

    final AdminToolApplication app;

    public NavigationTreeImpl(final AdminToolApplication app) {
        Preconditions.checkNotNull(app, "app is null: %s", app);
        this.app = app;

        init();
    }

    private void init() {
        setCompositionRoot(tree);

        tree.setSelectable(true);
        tree.setNullSelectionAllowed(true);
        tree.addListener(new NavigationClickListener(app));

        addResourcesNode();
        addAdminTaskNode();
    }

    private void addResourcesNode() {
        addRoot(ViewConstants.RESOURCES);
        addChildren(ViewConstants.RESOURCES, ViewConstants.RESOURCES_NODE);
    }

    private void addChildren(final String parent, final String[] nodes) {
        for (final String node : nodes) {
            Preconditions.checkNotNull(node, "action is null: %s", node);

            tree.addItem(node);
            tree.setParent(node, parent);
            tree.setChildrenAllowed(node, false);

            tree.expandItemsRecursively(parent);
        }
    }

    private void addRoot(final String node) {
        tree.addItem(node);
        tree.setChildrenAllowed(node, true);
    }

    private void addAdminTaskNode() {
        addRoot(ViewConstants.ADMIN_TASK);
        addChildren(ViewConstants.ADMIN_TASK, ViewConstants.ADMIN_TASKS_NODE);
    }
}