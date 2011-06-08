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
package de.escidoc.admintool.view.context;

import java.util.HashSet;
import java.util.Set;

import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.Window;

import de.escidoc.admintool.view.ModalDialog;
import de.escidoc.admintool.view.ViewConstants;
import de.escidoc.admintool.view.context.listener.CancelButtonListener;
import de.escidoc.admintool.view.resource.ResourceRefDisplay;
import de.escidoc.admintool.view.resource.OrgUnitTreeView;
import de.escidoc.core.resources.common.properties.PublicStatus;
import de.escidoc.core.resources.oum.OrganizationalUnit;

public final class AddOrgUnitToTheList implements Button.ClickListener {

    private final class OkButtonListener implements ClickListener {
        private static final long serialVersionUID = 7991096609461494934L;

        @Override
        public void buttonClick(final ClickEvent event) {
            addOrgUnitToTheList();
            mainWindow.removeWindow(modalWindow);
        }

        private void addOrgUnitToTheList() {
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
                orgUnitList.addItem(toResourceRefDesplay(selectedOrgUnit));
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

    private static final long serialVersionUID = -4817282732547950142L;

    private final HorizontalLayout buttons = new HorizontalLayout();

    private final Button okButton = new Button(ViewConstants.OK_LABEL);

    private final Button cancelBtn = new Button(ViewConstants.CANCEL_LABEL);

    private final Window mainWindow;

    private Window modalWindow;

    private final OrgUnitTreeView orgUnitTreeView;

    private AbstractSelect orgUnitList;

    private Table orgUnitTable;

    public AddOrgUnitToTheList(final Window mainWindow, final OrgUnitTreeView orgUnitTreeView) {
        this.mainWindow = mainWindow;
        this.orgUnitTreeView = orgUnitTreeView;
    }

    @Override
    public void buttonClick(final ClickEvent event) {
        createModalWindow();
        showModalWindow();
    }

    private void createModalWindow() {
        configure();
        addOrgUnitTreeView();
        addButtons();
    }

    private void addOrgUnitTreeView() {
        orgUnitTreeView.multiSelect();
        modalWindow.addComponent(orgUnitTreeView);
    }

    private void configure() {
        modalWindow = new Window();
        modalWindow.setModal(true);
        modalWindow.setCaption(ViewConstants.SELECT_ORGANIZATIONAL_UNIT);
        modalWindow.setHeight(ViewConstants.MODAL_DIALOG_HEIGHT);
        modalWindow.setWidth(ViewConstants.MODAL_DIALOG_WIDTH);
    }

    private void addButtons() {
        modalWindow.addComponent(buttons);

        addOkButton();
        addCancelButton();
    }

    private void addCancelButton() {
        final CancelButtonListener cancelButtonListener = new CancelButtonListener(mainWindow, modalWindow);
        cancelBtn.addListener(cancelButtonListener);
        buttons.addComponent(cancelBtn);
    }

    private void addOkButton() {
        final OkButtonListener okButtonListener = new OkButtonListener();
        okButton.addListener(okButtonListener);
        buttons.addComponent(okButton);
    }

    private void showModalWindow() {
        mainWindow.addWindow(modalWindow);
    }

    public void using(final AbstractSelect orgUnitList) {
        this.orgUnitList = orgUnitList;
    }
}