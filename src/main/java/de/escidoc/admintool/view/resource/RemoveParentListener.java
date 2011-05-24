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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

import de.escidoc.admintool.app.PropertyId;
import de.escidoc.admintool.view.ModalDialog;
import de.escidoc.admintool.view.ViewConstants;
import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.client.exceptions.application.invalid.InvalidStatusException;
import de.escidoc.core.resources.oum.OrganizationalUnit;
import de.escidoc.core.resources.oum.Parents;

final class RemoveParentListener implements ClickListener {

    private static final long serialVersionUID = 1887055528394173137L;

    private static final Logger LOG = LoggerFactory.getLogger(RemoveParentListener.class);

    private final OrgUnitSpecificView orgUnitSpecificView;

    private Property parentProperty;

    private OrganizationalUnit selectedOrgUnit;

    RemoveParentListener(final OrgUnitSpecificView orgUnitSpecificView) {
        this.orgUnitSpecificView = orgUnitSpecificView;
    }

    @Override
    public void buttonClick(final ClickEvent event) {
        if (getSelectedOrgUnitId().equals(ViewConstants.EMPTY_STRING)) {
            return;
        }
        removeParent();
    }

    private void removeParent() {
        try {
            retrieveOrgUnit(getSelectedOrgUnitId());
            updatePersistence();
            updateResourceContainer();
            updateItem();
            updateView();
        }
        catch (final EscidocClientException e) {
            if (e instanceof InvalidStatusException) {
                ModalDialog.show(orgUnitSpecificView.mainWindow, e.getMessage());
            }
            else {
                LOG.warn("Internal server error.", e);
                ModalDialog.show(orgUnitSpecificView.mainWindow, e);
            }
        }
    }

    private void updateItem() {
        final Item updateItem = orgUnitSpecificView.item;

        final Property itemProperty = updateItem.getItemProperty(PropertyId.PARENTS);
        itemProperty.setValue(new Parents());
    }

    protected void updatePersistence() throws EscidocClientException {
        orgUnitSpecificView.orgUnitService.removeParent(selectedOrgUnit);
    }

    private void updateResourceContainer() {
        orgUnitSpecificView.resourceContainer.removeParent(selectedOrgUnit);
    }

    private void updateView() {
        parentProperty.setValue(new ResourceRefDisplay());
    }

    private OrganizationalUnit retrieveOrgUnit(final String objectId) throws EscidocClientException {
        return selectedOrgUnit = orgUnitSpecificView.orgUnitService.findById(objectId);
    }

    private String getSelectedOrgUnitId() {
        if (orgUnitSpecificView.item == null) {
            return ViewConstants.EMPTY_STRING;
        }
        return (String) orgUnitSpecificView.item.getItemProperty(PropertyId.OBJECT_ID).getValue();
    }

    public void setParentProperty(final Property parentProperty) {
        this.parentProperty = parentProperty;
    }
}