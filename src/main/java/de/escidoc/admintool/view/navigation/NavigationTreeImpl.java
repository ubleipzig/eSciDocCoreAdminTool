package de.escidoc.admintool.view.navigation;

import com.google.common.base.Preconditions;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Tree;

import de.escidoc.admintool.app.AdminToolApplication;
import de.escidoc.admintool.view.ViewConstants;
import de.escidoc.admintool.view.ViewManager;

public class NavigationTreeImpl extends CustomComponent
    implements NavigationTree {

    private static final long serialVersionUID = -5022424682917592102L;

    private final Tree tree = new Tree();

    final AdminToolApplication app;

    private final ViewManager viewManager;

    public NavigationTreeImpl(final AdminToolApplication app,
        final ViewManager viewManager) {
        Preconditions.checkNotNull(app, "app is null: %s", app);
        Preconditions.checkNotNull(viewManager, "viewManager is null: %s",
            viewManager);
        this.app = app;
        this.viewManager = viewManager;
    }

    public void init() {
        setCompositionRoot(tree);
        configureTree();
        addResourcesNode();
        addAdminTaskNode();
    }

    private void configureTree() {
        tree.setSelectable(true);
        tree.setNullSelectionAllowed(true);
        tree.addListener(new NavigationTreeClickListener(app, viewManager));
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
        addRoot(ViewConstants.ADMIN_TASKS_LABEL);
        addChildren(ViewConstants.ADMIN_TASKS_LABEL,
            ViewConstants.ADMIN_TASKS_NODE);
    }
}