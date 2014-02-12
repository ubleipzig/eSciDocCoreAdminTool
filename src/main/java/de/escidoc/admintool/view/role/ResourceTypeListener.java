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
package de.escidoc.admintool.view.role;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;

import org.apache.commons.lang.NotImplementedException;

import com.google.common.base.Preconditions;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.POJOContainer;
import com.vaadin.ui.Component;

import de.escidoc.admintool.app.PropertyId;
import de.escidoc.admintool.view.ViewConstants;
import de.escidoc.admintool.view.admintask.ResourceType;
import de.escidoc.admintool.view.util.dialog.ErrorDialog;
import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.resources.Resource;
import de.escidoc.core.resources.aa.useraccount.UserAccount;

class ResourceTypeListener implements ValueChangeListener {

    private static final long serialVersionUID = 2394096937007392588L;

    private final RoleView roleView;

    ResourceTypeListener(final RoleView roleView) {
        Preconditions.checkNotNull(roleView, "roleView is null: %s", roleView);
        this.roleView = roleView;
    }

    @Override
    public void valueChange(final ValueChangeEvent event) {
        try {
            onSelectedResourceType(event);
        }
        catch (final NotImplementedException e) {
            roleView.mainWindow.addWindow(new ErrorDialog(roleView.mainWindow, ViewConstants.ERROR_DIALOG_CAPTION, e
                .getMessage()));
        }
    }

    private void onSelectedResourceType(final ValueChangeEvent event) {
        final Object value = event.getProperty().getValue();
        if (value instanceof ResourceType) {
            final ResourceType type = (ResourceType) value;

            Component newComponent = null;
            switch (type) {
                case CONTEXT:
                    newComponent = assignComponent();
                    loadContextData();
                    break;
                case ORGANIZATIONAL_UNIT:
                    newComponent = assignComponent();
                    loadOrgUnitData();
                    break;
                case CONTAINER:
                    newComponent = assignComponent();
                    loadContainerData();
                    break;
                case ITEM:
                    newComponent = assignComponent();
                    loadItemData();
                    break;
                case USERACCOUNT:
                    newComponent = assignComponent();
                    loadUserData();
                    break;
                default: {
                    clearResourceContainer();
                    throw new NotImplementedException("Scoping for " + type + " is not yet implemented");
                }
            }
            final Iterator<Component> it = roleView.resourceContainer.getComponentIterator();
            if (it.hasNext()) {
                roleView.resourceContainer.replaceComponent(it.next(), newComponent);
            }
            else {
                roleView.resourceContainer.addComponent(newComponent);
            }
        }
    }

    private void loadUserData() {
        final POJOContainer<Resource> dataSource =
            new POJOContainer<Resource>(Resource.class, ViewConstants.X_LINK_TITLE);
        for (final Resource user : findAllUsers()) {
            dataSource.addItem(user);
        }
        roleView.resouceResult.setContainerDataSource(dataSource);
        roleView.resouceResult.setItemCaptionPropertyId(ViewConstants.X_LINK_TITLE);
    }

    private Collection<UserAccount> findAllUsers() {
        try {
            return roleView.userService.findAll();
        }
        catch (final EscidocClientException e) {
            handleError(e);
        }
        return Collections.emptySet();
    }

    private void loadItemData() {
        final POJOContainer<Resource> itemContainer =
            new POJOContainer<Resource>(Resource.class, ViewConstants.X_LINK_TITLE);
        for (final Resource item : findAllItems()) {
            itemContainer.addItem(item);
        }

        roleView.resouceResult.setContainerDataSource(itemContainer);
        roleView.resouceResult.setItemCaptionPropertyId(ViewConstants.X_LINK_TITLE);
    }

    private Set<Resource> findAllItems() {
        try {
            return roleView.serviceContainer.getItemService().findAll();
        }
        catch (final EscidocClientException e) {

            handleError(e);
        }
        return Collections.emptySet();
    }

    private POJOContainer<Resource> newPojoContainer() {
        return new POJOContainer<Resource>(Resource.class, PropertyId.NAME);
    }

    private Component assignComponent() {
        Component newComponent;
        newComponent = roleView.resouceResult;
        return newComponent;
    }

    private void loadContainerData() {
        final POJOContainer<Resource> containerContainer = newPojoContainer();
        for (final Resource orgUnit : findAllContainers()) {
            containerContainer.addItem(orgUnit);
        }
        configureList(containerContainer);
    }

    private Set<Resource> findAllContainers() {
        try {
            return roleView.serviceContainer.getContainerService().findAll();
        }
        catch (final EscidocClientException e) {
            handleError(e);
        }
        return Collections.emptySet();
    }

    private void handleError(final EscidocClientException e) {
        roleView.mainWindow.addWindow(new ErrorDialog(roleView.mainWindow, ViewConstants.ERROR_DIALOG_CAPTION, e
            .getMessage()));
    }

    private void loadOrgUnitData() {
        final POJOContainer<Resource> orgUnitContainer = newPojoContainer();
        for (final Resource orgUnit : findAllOrgUnits()) {
            orgUnitContainer.addItem(orgUnit);
        }
        configureList(orgUnitContainer);
    }

    private Set<Resource> findAllOrgUnits() {
        try {
            return roleView.serviceContainer.getOrgUnitService().findAll();
        }
        catch (final EscidocClientException e) {
            handleError(e);
        }
        return Collections.emptySet();
    }

    private void clearResourceContainer() {
        roleView.resourceContainer.removeAllComponents();
    }

    private void loadContextData() {
        final POJOContainer<Resource> contextContainer = newPojoContainer();
        for (final Resource context : findAllContexts()) {
            contextContainer.addItem(context);
        }
        configureList(contextContainer);
    }

    private void configureList(final POJOContainer<Resource> contextContainer) {
        roleView.resouceResult.setContainerDataSource(contextContainer);
        roleView.resouceResult.setItemCaptionPropertyId(PropertyId.NAME);
    }

    private Set<Resource> findAllContexts() {
        try {
            return roleView.serviceContainer.getContextService().findAll();
        }
        catch (final EscidocClientException e) {
            handleError(e);
        }
        return Collections.emptySet();
    }
}