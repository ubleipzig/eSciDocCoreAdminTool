/**
 * CDDL HEADER START
 *
 * The contents of this file are subject to the terms of the
 * Common Development and Distribution License, Version 1.0 only
 * (the "License").  You may not use this file except in compliance
 * with the License.
 *
 * You can obtain a copy of the license at license/ESCIDOC.LICENSE
 * or https://www.escidoc.org/license/ESCIDOC.LICENSE .
 * See the License for the specific language governing permissions
 * and limitations under the License.
 *
 * When distributing Covered Code, include this CDDL HEADER in each
 * file and include the License file at license/ESCIDOC.LICENSE.
 * If applicable, add the following below this CDDL HEADER, with the
 * fields enclosed by brackets "[]" replaced with your own identifying
 * information: Portions Copyright [yyyy] [name of copyright owner]
 *
 * CDDL HEADER END
 *
 *
 *
 * Copyright 2011 Fachinformationszentrum Karlsruhe Gesellschaft
 * fuer wissenschaftlich-technische Information mbH and Max-Planck-
 * Gesellschaft zur Foerderung der Wissenschaft e.V.
 * All rights reserved.  Use is subject to license terms.
 */
package de.escidoc.admintool.view.navigation;

import com.google.common.base.Preconditions;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Tree;

import de.escidoc.admintool.domain.PdpRequest;
import de.escidoc.admintool.view.ViewConstants;

public class NavigationTreeImpl extends CustomComponent implements NavigationTree {

    private static final long serialVersionUID = -5022424682917592102L;

    private final Tree tree = new Tree();

    private final ItemClickListener listener;

    private final PdpRequest pdpRequest;

    public NavigationTreeImpl(final ItemClickListener listener, final PdpRequest pdpRequest) {
        Preconditions.checkNotNull(listener, "listener is null: %s", listener);
        Preconditions.checkNotNull(pdpRequest, "pdpRequest is null: %s", pdpRequest);
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
            else {
                addChild(parent, node);
            }
        }
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

    @Override
    public void selectUserView() {
        tree.select(ViewConstants.USERS);
    }

    @Override
    public boolean isExpanded(final Object itemId) {
        return tree.isExpanded(itemId);
    }

    @Override
    public void expandItem(final Object itemId) {
        tree.expandItem(itemId);
    }

    @Override
    public void collapseItem(final Object itemId) {
        tree.collapseItem(itemId);
    }
}