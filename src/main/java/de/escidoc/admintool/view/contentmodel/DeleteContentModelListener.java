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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.Notification;

import de.escidoc.admintool.app.AdminToolApplication;
import de.escidoc.admintool.service.internal.ContentModelService;
import de.escidoc.admintool.view.ViewConstants;
import de.escidoc.admintool.view.util.dialog.ErrorDialog;
import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.resources.Resource;

/**
 * @author chh
 * 
 */
@SuppressWarnings("serial")
public class DeleteContentModelListener implements ClickListener {

    private static final Logger LOG = LoggerFactory.getLogger(DeleteContentModelListener.class);

    private final ContentModelService service;

    private final ContentModelContainerImpl container;

    private final AdminToolApplication app;

    private final Window mainWindow;

    private String contentModelId;

    public DeleteContentModelListener(final ContentModelService service, final ContentModelContainerImpl container,
        final AdminToolApplication app) {
        Preconditions.checkNotNull(service, "service is null: %s", service);
        Preconditions.checkNotNull(container, "container is null: %s", container);
        Preconditions.checkNotNull(app, "app is null: %s", app);
        this.service = service;
        this.container = container;
        this.app = app;
        mainWindow = app.getMainWindow();
        Preconditions.checkNotNull(mainWindow, "mainWindow is null: %s", mainWindow);
    }

    @Override
    public void buttonClick(final ClickEvent event) {
        Preconditions.checkNotNull(contentModelId, "contentModelId is null: %s", contentModelId);
        try {
            final Resource resource = service.findById(contentModelId);
            service.delete(contentModelId);
            container.remove(resource);
            showSuccessMessage();
            app.showContentModelView();
        }
        catch (final EscidocClientException e) {
            LOG.error("Exception while trying to delete content model with an id: " + contentModelId, e);
            mainWindow.addWindow(new ErrorDialog(mainWindow, ViewConstants.ERROR, e.getMessage()));
        }

    }

    private void showSuccessMessage() {
        Preconditions.checkNotNull(mainWindow, "mainWindow is null: %s", mainWindow);
        mainWindow.showNotification(new Window.Notification("Info", "Succesfully delete content model.",
            Notification.TYPE_TRAY_NOTIFICATION));
    }

    public void setContentModelId(final String contentModelId) {
        Preconditions.checkNotNull(contentModelId, "contentModelId is null: %s", contentModelId);
        this.contentModelId = contentModelId;
    }
}