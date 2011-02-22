package de.escidoc.admintool.view.navigation;

import java.net.URISyntaxException;

import com.google.common.base.Preconditions;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Tree;

import de.escidoc.admintool.service.PdpService;
import de.escidoc.admintool.view.ViewConstants;
import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.resources.aa.useraccount.UserAccount;

public class NavigationTreeImpl extends CustomComponent
    implements NavigationTree {

    private static final long serialVersionUID = -5022424682917592102L;

    private final Tree tree = new Tree();

    private UserAccount currentUser;

    private final NavigationTreeClickListener listener;

    private final PdpService pdpService;

    public NavigationTreeImpl(final NavigationTreeClickListener listener,
        final PdpService pdpService) {
        Preconditions.checkNotNull(listener, "listener is null: %s", listener);
        Preconditions.checkNotNull(pdpService, "pdpService is null: %s",
            pdpService);
        this.listener = listener;
        this.pdpService = pdpService;
    }

    public NavigationTree init(final UserAccount currentUser) {
        this.currentUser = currentUser;
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
            addChild(parent, node);
        }
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
            else {
                addChild(ViewConstants.ADMIN_TASKS_LABEL, node);
            }
        }
    }

    private boolean isAllowedToReindex() {
        try {
            return pdpService
                .isAction(ActionConstants.REINDEX)
                .forUser(currentUser.getObjid()).permitted();
        }
        catch (final EscidocClientException e) {
            return false;
        }
        catch (final URISyntaxException e) {
            return false;
        }
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