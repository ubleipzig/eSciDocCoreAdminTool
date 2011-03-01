package de.escidoc.admintool.view.navigation;

import com.google.common.base.Preconditions;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Tree;

import de.escidoc.admintool.domain.PdpRequest;
import de.escidoc.admintool.view.ViewConstants;

public class NavigationTreeImpl extends CustomComponent
    implements NavigationTree {

    private static final long serialVersionUID = -5022424682917592102L;

    private final Tree tree = new Tree();

    private final ItemClickListener listener;

    private final PdpRequest pdpRequest;

    public NavigationTreeImpl(final ItemClickListener listener,
        final PdpRequest pdpRequest) {
        Preconditions.checkNotNull(listener, "listener is null: %s", listener);
        Preconditions.checkNotNull(pdpRequest, "pdpRequest is null: %s",
            pdpRequest);
        this.listener = listener;
        this.pdpRequest = pdpRequest;
    }

    @Override
    public NavigationTree init() {
        setCompositionRoot(tree);
        configureTree();
        addNodes();
        return this;
    }

    private void configureTree() {
        tree.setSelectable(true);
        tree.setNullSelectionAllowed(true);
        tree.addListener(listener);
    }

    private void addNodes() {
        addResourcesNode();
        addAdminTaskNode();
    }

    private void addResourcesNode() {
        addRoot(ViewConstants.RESOURCES);
        addChildren(ViewConstants.RESOURCES, ViewConstants.RESOURCES_NODE);
    }

    private void addRoot(final String node) {
        tree.addItem(node);
        tree.setChildrenAllowed(node, true);
    }

    private void addChildren(final String parent, final String[] nodes) {
        for (final String node : nodes) {
            if (isEquals(node, ViewConstants.ROLE)) {
                if (isAllowedToGrantRole()) {
                    addChild(parent, node);
                }
            }
            else if (isEquals(node, ViewConstants.CONTENT_MODELS)) {
                if (isAllowedToCreateContentModel()) {
                    addChild(parent, node);
                }
            }

            else {
                addChild(parent, node);
            }
        }
    }

    private boolean isAllowedToCreateContentModel() {
        return pdpRequest.isPermitted(ActionIdConstants.CREATE_CONTENT_MODEL);
    }

    private boolean isAllowedToGrantRole() {
        return pdpRequest.isPermitted(ActionIdConstants.CREATE_GRANT);
    }

    private boolean isEquals(final String node, final String nodeLabel) {
        return node.equalsIgnoreCase(nodeLabel);
    }

    private void setAsLeaf(final String node) {
        tree.setChildrenAllowed(node, false);
    }

    private void addAdminTaskNode() {
        addRoot(ViewConstants.ADMIN_TASKS_LABEL);
        for (final String node : ViewConstants.ADMIN_TASKS_NODE) {
            if (isReindex(node)) {
                if (isAllowedToReindex()) {
                    addChild(ViewConstants.ADMIN_TASKS_LABEL, node);
                }
            }
            else if (isEquals(node, ViewConstants.LOAD_EXAMPLES)) {
                if (isAllowedToCreateItem()) {
                    addChild(ViewConstants.ADMIN_TASKS_LABEL, node);
                }
            }
            else {
                addChild(ViewConstants.ADMIN_TASKS_LABEL, node);
            }
        }
    }

    private boolean isAllowedToCreateItem() {
        return pdpRequest.isPermitted(ActionIdConstants.CREATE_ITEM);
    }

    private boolean isAllowedToReindex() {
        return pdpRequest.isPermitted(ActionIdConstants.REINDEX_ACTION_ID);
    }

    private boolean isReindex(final String node) {
        return node.equalsIgnoreCase(ViewConstants.REINDEX);
    }

    private void addChild(final String parent, final String node) {
        Preconditions.checkNotNull(node, "action is null: %s", node);
        tree.addItem(node);
        tree.setParent(node, parent);
        setAsLeaf(node);
        tree.expandItemsRecursively(parent);
    }
}