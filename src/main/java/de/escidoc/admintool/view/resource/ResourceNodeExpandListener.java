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

public class ResourceNodeExpandListener implements ExpandListener {

    private static final long serialVersionUID = -288898779192780110L;

    private final Tree tree;

    private final AddChildrenCommand addChildrenCommand;

    private final Window mainWindow;

    ResourceNodeExpandListener(final Tree tree, final Window mainWindow, final AddChildrenCommand addChildrenCommand) {
        preconditions(tree, mainWindow, addChildrenCommand);
        this.tree = tree;
        this.mainWindow = mainWindow;
        this.addChildrenCommand = addChildrenCommand;
    }

    private void preconditions(final Tree tree, final Window mainWindow, final AddChildrenCommand addChildrenCommand) {
        Preconditions.checkNotNull(tree, "tree is null: %s", tree);
        Preconditions.checkNotNull(mainWindow, "mainWindow is null: %s", mainWindow);
        Preconditions.checkNotNull(addChildrenCommand, "addChildrenCommand is null: %s", addChildrenCommand);
    }

    @Override
    public void nodeExpand(final ExpandEvent event) {
        // expand selected org unit and show its children.
        final Object selectedOrgUnit = event.getItemId();
        if (selectedOrgUnit == null) {
            return;
        }

        if (isOrgUnit(selectedOrgUnit) && isAddChildrenNeeded((OrganizationalUnit) selectedOrgUnit)) {
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
