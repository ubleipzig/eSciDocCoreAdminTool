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
package de.escidoc.admintool.view.admintask.filter;

import java.util.Set;

import com.google.common.base.Preconditions;
import com.vaadin.data.util.POJOContainer;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;

import de.escidoc.admintool.app.PropertyId;
import de.escidoc.admintool.domain.PdpRequest;
import de.escidoc.admintool.view.ViewConstants;
import de.escidoc.admintool.view.navigation.ActionIdConstants;
import de.escidoc.core.resources.Resource;

final class ShowFilterResultCommandImpl implements ShowFilterResultCommand {

    private final FormLayout formLayout = new FormLayout();

    private final PdpRequest pdpRequest;

    final FilterResourceView filterResourceView;

    Table filterResultTable;

    POJOContainer<Resource> filteredResourcesContainer;

    public ShowFilterResultCommandImpl(final FilterResourceView filterResourceView, final PdpRequest pdpRequest) {
        Preconditions.checkNotNull(filterResourceView, "filterResourceView is null: %s", filterResourceView);
        Preconditions.checkNotNull(pdpRequest, "pdpRequest is null: %s", pdpRequest);
        this.filterResourceView = filterResourceView;
        this.pdpRequest = pdpRequest;
    }

    @Override
    public void execute(final Set<Resource> filteredResources) {
        showFilteredResources(filteredResources);
    }

    private void showFilteredResources(final Set<Resource> filteredResources) {
        resetFilterResultView();
        if (isEmpty(filteredResources)) {
            showNoResult();
        }
        else {
            createFilterResultView(filteredResources);
            showFilterResultView();
        }
    }

    private boolean isEmpty(final Set<Resource> filteredResources) {
        return filteredResources == null || filteredResources.isEmpty();
    }

    private void showNoResult() {
        formLayout.addComponent(new Label(ViewConstants.NO_RESULT));
    }

    private void resetFilterResultView() {
        filterResourceView.getViewLayout().addComponent(formLayout);
        formLayout.removeAllComponents();
    }

    private void createFilterResultView(final Set<Resource> filteredResources) {
        filteredResourcesContainer =
            new POJOContainer<Resource>(filteredResources, PropertyId.OBJECT_ID, PropertyId.XLINK_TITLE);

        filterResultTable = new Table(ViewConstants.FILTERED_RESOURCES, filteredResourcesContainer);
        filterResultTable.setWidth("70%");
        filterResultTable.setColumnWidth(PropertyId.OBJECT_ID, 70);

        filterResultTable.setVisibleColumns(new Object[] { PropertyId.OBJECT_ID, PropertyId.XLINK_TITLE });
        filterResultTable.setColumnHeader(PropertyId.OBJECT_ID, ViewConstants.OBJECT_ID_LABEL);
        filterResultTable.setColumnHeader(PropertyId.XLINK_TITLE, ViewConstants.TITLE_LABEL);

        filterResultTable.setSelectable(true);
        filterResultTable.setMultiSelect(true);
    }

    private void showFilterResultView() {
        final VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.addComponent(filterResultTable);
        formLayout.addComponent(verticalLayout);
        if (isPurgePermitted()) {
            addHintForSelection();
            addPurgeButton();
        }
    }

    private void addHintForSelection() {
        final Label hintText =
            new Label("<div><em>Hint: </em>"
                + "To select multiple resources, hold down the CONTROL key while you click on the resource.</br></div>"
                + "<strong>Warning:</strong> Purging resources can cause inconsitencies in the repository.</div>",
                Label.CONTENT_XHTML);
        formLayout.addComponent(hintText);
    }

    private boolean isPurgePermitted() {
        return pdpRequest.isPermitted(ActionIdConstants.PURGE_RESOURCES);
    }

    private void addPurgeButton() {
        final Button purgeBtn = new Button(ViewConstants.PURGE);
        purgeBtn.setWidth("150px");
        formLayout.addComponent(purgeBtn);
        purgeBtn.addListener(new PurgeResourcesListener(this, formLayout, filterResourceView.adminService,
            filterResourceView.mainWindow));
    }
}
