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
package de.escidoc.admintool.view.user;

import java.util.HashSet;
import java.util.Set;

import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Table;
import com.vaadin.ui.Window;

import de.escidoc.admintool.view.ModalDialog;
import de.escidoc.admintool.view.resource.ResourceRefDisplay;
import de.escidoc.admintool.view.resource.OrgUnitTreeView;
import de.escidoc.core.resources.common.properties.PublicStatus;
import de.escidoc.core.resources.oum.OrganizationalUnit;

final class AddOrgUnitsToTable implements ClickListener {

    private static final long serialVersionUID = 7991096609461494934L;

    private final Window mainWindow;

    private final Window modalWindow;

    private final OrgUnitTreeView orgUnitTreeView;

    private final Table orgUnitTable;

    public AddOrgUnitsToTable(final Window mainWindow, final Window modalWindow, final OrgUnitTreeView orgUnitTreeView,
        final Table orgUnitTable) {
        this.mainWindow = mainWindow;
        this.modalWindow = modalWindow;
        this.orgUnitTreeView = orgUnitTreeView;
        this.orgUnitTable = orgUnitTable;
    }

    @Override
    public void buttonClick(final ClickEvent event) {
        addOrgUnitToTable();
        mainWindow.removeWindow(modalWindow);
    }

    private void addOrgUnitToTable() {
        getSelectedOrgUnitFromTree();
    }

    private Set<OrganizationalUnit> notOpened;

    @SuppressWarnings("unchecked")
    private void getSelectedOrgUnitFromTree() {
        notOpened = new HashSet<OrganizationalUnit>();
        final Object selected = orgUnitTreeView.getSelected();

        if (moreThanOne(selected)) {
            for (final OrganizationalUnit selectedOrgUnit : (Set<OrganizationalUnit>) selected) {
                tryAddToList(selectedOrgUnit);
            }
        }
        else if (selected instanceof OrganizationalUnit) {
            final OrganizationalUnit selectedOrgUnit = (OrganizationalUnit) selected;
            tryAddToList(selectedOrgUnit);

        }
        showMessage();

    }

    private void tryAddToList(final OrganizationalUnit selectedOrgUnit) {
        if (isOpen(selectedOrgUnit)) {
            orgUnitTable.addItem(toResourceRefDesplay(selectedOrgUnit));
        }
        else {
            notOpened.add(selectedOrgUnit);
        }

    }

    private void showMessage() {
        if (!notOpened.isEmpty()) {
            final StringBuilder stringBuilder = new StringBuilder();
            for (final OrganizationalUnit notOpenedOrgUnit : notOpened) {
                final String xLinkTitle = notOpenedOrgUnit.getXLinkTitle();
                stringBuilder.append(xLinkTitle).append(", ");
            }
            stringBuilder.append(" is not in status opened.");
            ModalDialog.showMessage(mainWindow, "Can only add organizational unit in status open. ",
                stringBuilder.toString());

        }
    }

    private boolean moreThanOne(final Object selected) {
        return selected instanceof Set;
    }

    private boolean isOpen(final OrganizationalUnit selectedOrgUnit) {
        return PublicStatus.OPENED.equals(selectedOrgUnit.getProperties().getPublicStatus());
    }

    private ResourceRefDisplay toResourceRefDesplay(final OrganizationalUnit selectedOrgUnit) {
        return new ResourceRefDisplay(selectedOrgUnit.getObjid(), selectedOrgUnit.getProperties().getName());
    }
}