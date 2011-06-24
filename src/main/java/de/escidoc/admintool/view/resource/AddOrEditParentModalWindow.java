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
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Window;

import de.escidoc.admintool.service.internal.OrgUnitServiceLab;
import de.escidoc.admintool.view.ViewConstants;

@SuppressWarnings("serial")
public class AddOrEditParentModalWindow extends Window {

    private final class CancelButtonListener implements ClickListener {
        private static final long serialVersionUID = -1211409730229979129L;

        public void buttonClick(final ClickEvent event) {
            closeWindow();
        }
    }

    private final HorizontalLayout buttons = new HorizontalLayout();

    private final Button okButton = new Button(ViewConstants.OK_LABEL);

    private final Button cancelBtn = new Button(ViewConstants.CANCEL_LABEL);

    private final OrgUnitServiceLab orgUnitService;

    private final Window mainWindow;

    private final ResourceContainer resourceContainer;

    private final OrgUnitSpecificView orgUnitSpecificView;

    private UpdateParentListener updateParentListener;

    private AddParentOkListener addParentOkListener;

    private String selectedParent;

    public AddOrEditParentModalWindow(final OrgUnitSpecificView orgUnitSpecificView,
        final ResourceContainer resourceContainer, final OrgUnitServiceLab orgUnitService, final Window mainWindow) {

        preconditions(orgUnitSpecificView, resourceContainer, orgUnitService, mainWindow);
        this.orgUnitSpecificView = orgUnitSpecificView;
        this.resourceContainer = resourceContainer;
        this.orgUnitService = orgUnitService;
        this.mainWindow = mainWindow;
        init();
    }

    private void preconditions(
        final OrgUnitSpecificView orgUnitSpecificView, final ResourceContainer resourceContainer,
        final OrgUnitServiceLab orgUnitService, final Window mainWindow) {
        Preconditions.checkNotNull(orgUnitService, "orgUnitService is null: %s", orgUnitService);
        Preconditions.checkNotNull(resourceContainer, "resourceContainer is null: %s", resourceContainer);
        Preconditions.checkNotNull(orgUnitService, "orgUnitService is null: %s", orgUnitService);
        Preconditions.checkNotNull(mainWindow, "mainWindow is null: %s", mainWindow);
    }

    private void init() {
        configureWindow();
        addResourceTreeView();
        addButtons();
        addCancelBtnListener();
    }

    private void addResourceTreeView() {
        addComponent(createResourceTreeView());
    }

    private void addButtons() {
        addOkButton();
        addCancelButton();
        addComponent(buttons);
    }

    private void addOkButton() {
        buttons.addComponent(okButton);
    }

    private void addCancelButton() {
        buttons.addComponent(cancelBtn);
    }

    private OrgUnitTreeView createResourceTreeView() {
        final OrgUnitTreeView orgUnitTreeView =
            new OrgUnitTreeView(getMainWindow(),
                new FolderHeaderImpl(ViewConstants.SELECT_A_PARENT_ORGANIZATIONAL_UNIT), resourceContainer);

        orgUnitTreeView.setCommand(new AddChildrenCommandImpl(orgUnitService, resourceContainer));
        orgUnitTreeView.addResourceNodeExpandListener();

        final ResourceSelectedListener selectedListener = new ResourceSelectedListener(this);

        orgUnitTreeView.addListener(selectedListener);
        return orgUnitTreeView;
    }

    public void addAddParentOkLisner() {
        addParentOkListener = new AddParentOkListener(this, orgUnitService, orgUnitSpecificView);
        addParentOkListener.setMainWindow(mainWindow);
        okButton.removeListener(updateParentListener);
        okButton.addListener(addParentOkListener);
    }

    public void addUpdateParentOkListener() {
        updateParentListener = new UpdateParentListener(this, orgUnitService);
        updateParentListener.setMainWindow(mainWindow);
        okButton.removeListener(addParentOkListener);
        okButton.addListener(updateParentListener);
    }

    private void addCancelBtnListener() {
        cancelBtn.addListener(new CancelButtonListener());
    }

    void closeWindow() {
        getMainWindow().removeWindow(AddOrEditParentModalWindow.this);
    }

    private void configureWindow() {
        setModal(true);
        setCaption(ViewConstants.ADD_PARENT);
        setHeight(ViewConstants.MODAL_DIALOG_HEIGHT);
        setWidth(ViewConstants.MODAL_DIALOG_WIDTH);
    }

    public void setSelected(final String selectedParent) {
        this.selectedParent = selectedParent;
    }

    public void bind(final Item item) {
        updateParentListener.bind(item);
    }

    public void setParentPropertyForAdd(final Property parentProperty) {
        addParentOkListener.setParentProperty(parentProperty);
    }

    public void setParentPropertyForUpdate(final Property parentProperty) {
        updateParentListener.setParentProperty(parentProperty);
    }

    public Window getMainWindow() {
        return mainWindow;
    }

    public String getSelectedParent() {
        return selectedParent;
    }

    public ResourceContainer getResourceContainer() {
        return resourceContainer;
    }
}