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
import com.vaadin.ui.Window;

import de.escidoc.admintool.app.PropertyId;
import de.escidoc.admintool.service.internal.OrgUnitServiceLab;
import de.escidoc.admintool.service.internal.ResourceService;
import de.escidoc.admintool.view.ModalDialog;
import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.client.exceptions.application.invalid.InvalidStatusException;
import de.escidoc.core.resources.Resource;

public abstract class AbstractUpdateable implements Updateable {

    private final Window mainWindow;

    private Item item;

    private ResourceService orgUnitService;

    private ResourceContainer resourceContainer;

    public AbstractUpdateable(final Window mainWindow, final ResourceService orgUnitService,
        final ResourceContainer resourceContainer) {
        Preconditions.checkNotNull(mainWindow, "mainWindow is null: %s", mainWindow);
        Preconditions.checkNotNull(orgUnitService, "orgUnitService is null: %s", orgUnitService);
        Preconditions.checkNotNull(resourceContainer, "resourceContainer is null: %s", resourceContainer);
        this.mainWindow = mainWindow;
        this.orgUnitService = orgUnitService;
        this.resourceContainer = resourceContainer;
    }

    protected void onUpdate() {
        try {
            updatePersistence();
            updateResourceContainer();
            updateItem();
            checkPostConditions();
        }
        catch (final EscidocClientException e) {
            if (e instanceof InvalidStatusException) {
                ModalDialog.show(mainWindow, e.getMessage());
            }
            else {
                ModalDialog.show(mainWindow, e);
            }
        }
    }

    public abstract void updatePersistence() throws EscidocClientException;

    public abstract void updateResourceContainer() throws EscidocClientException;

    @Override
    public void updateItem() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void checkPostConditions() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    protected String getChildId() {
        return (String) getItem().getItemProperty(PropertyId.OBJECT_ID).getValue();
    }

    public void setOrgUnitService(final OrgUnitServiceLab orgUnitService) {
        this.orgUnitService = orgUnitService;
    }

    public OrgUnitServiceLab getOrgUnitService() {
        return (OrgUnitServiceLab) orgUnitService;
    }

    public void setResourceContainer(final ResourceContainer resourceContainer) {
        this.resourceContainer = resourceContainer;
    }

    public ResourceContainer getResourceContainer() {
        return resourceContainer;
    }

    protected Resource getOrgUnit() throws EscidocClientException {
        return getOrgUnitService().findById(getChildId());
    }

    @Override
    public void bind(final Item item) {
        setItem(item);
    }

    public void setItem(final Item item) {
        this.item = item;
    }

    public Item getItem() {
        return item;
    }
}