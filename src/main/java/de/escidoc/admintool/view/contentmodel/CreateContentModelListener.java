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
package de.escidoc.admintool.view.contentmodel;

import java.util.Collection;
import java.util.Map;

import com.google.common.base.Preconditions;
import com.vaadin.data.Item;
import com.vaadin.terminal.UserError;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Field;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.Notification;

import de.escidoc.admintool.service.internal.ResourceService;
import de.escidoc.admintool.view.ModalDialog;
import de.escidoc.admintool.view.resource.ResourceBtnListener;
import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.resources.Resource;
import de.escidoc.core.resources.cmm.ContentModel;

@SuppressWarnings("serial")
class CreateContentModelListener implements ResourceBtnListener {

    private final Collection<Field> allFields;

    private final ResourceService contentModelService;

    private final Map<String, Field> fieldByName;

    private final Window mainWindow;

    private final ContentModelContainerImpl container;

    private ContentModelView contentModelView;

    private ContentModel build;

    private Resource created;

    protected CreateContentModelListener(final Collection<Field> allFields, final ResourceService contentModelService,
        final Map<String, Field> fieldByName, final Window mainWindow, final ContentModelContainerImpl container) {
        Preconditions.checkNotNull(mainWindow, "mainWindow is null: %s", mainWindow);
        Preconditions.checkNotNull(fieldByName, "fieldByName is null: %s", fieldByName);
        Preconditions.checkNotNull(contentModelService, "contentModelService is null: %s", contentModelService);
        Preconditions.checkNotNull(allFields, "allFields is null: %s", allFields);
        Preconditions.checkNotNull(container, "container is null: %s", container);

        this.mainWindow = mainWindow;
        this.contentModelService = contentModelService;

        this.allFields = allFields;
        this.fieldByName = fieldByName;
        this.container = container;
    }

    public void setContentModelView(final ContentModelView contentModelView) {
        Preconditions.checkNotNull(contentModelView, "contentModelView is null: %s", contentModelView);
        this.contentModelView = contentModelView;
    }

    @Override
    public void buttonClick(final ClickEvent event) {
        if (validateAllFields() && saveToReposity()) {
            removeValidationErrors();
            commitAllFields();
            updateResourceContainer();
            showInListView();
            showInEditView();
            showSuccessMessage();
        }
    }

    private void showInEditView() {
        showInEditView(created);
    }

    private void showInListView() {
        contentModelView.select(created);
    }

    private void showSuccessMessage() {
        mainWindow.showNotification(new Window.Notification("Info", "A new Content Model with the ID "
            + created.getObjid() + " is created.", Notification.TYPE_TRAY_NOTIFICATION));
    }

    private void updateResourceContainer() {
        container.add(created);

    }

    private boolean saveToReposity() {
        createModel();
        return updatePersistence();
    }

    private boolean updatePersistence() {
        try {
            created = contentModelService.create(build);
            if (created != null && created.getObjid() != null) {
                return true;
            }
            return false;
        }
        catch (final EscidocClientException e) {
            ModalDialog.show(mainWindow, e);
            return false;
        }
    }

    private void showInEditView(final Resource created) {
        Preconditions.checkNotNull(created, "created is null: %s", created);
        Preconditions.checkNotNull(contentModelView, "contentModelView is null: %s", contentModelView);

        contentModelView.showEditView(created);
    }

    private void createModel() {
        build = new ContentModelBuilder(getTitle()).description(getDescription()).build();
    }

    private String getDescription() {
        return (String) fieldByName.get("description").getValue();
    }

    private String getTitle() {
        return (String) fieldByName.get("title").getValue();
    }

    private boolean validateAllFields() {
        for (final Field field : allFields) {
            try {
                field.validate();
            }
            catch (final Exception e) {
                ((AbstractComponent) field).setComponentError(new UserError(field.getCaption() + " is required"));
                return false;
            }
        }
        return true;
    }

    private void removeValidationErrors() {
        for (final Field field : allFields) {
            ((AbstractComponent) field).setComponentError(null);
        }
    }

    private void commitAllFields() {
        // do nothing
    }

    @Override
    public void bind(final Item item) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
