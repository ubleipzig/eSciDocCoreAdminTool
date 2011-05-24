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

import com.google.common.base.Preconditions;
import com.vaadin.data.Item;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.ui.Window;

import de.escidoc.admintool.service.internal.ResourceService;
import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.resources.Resource;
import de.escidoc.core.resources.ResourceType;
import de.escidoc.core.resources.cmm.ContentModel;

@SuppressWarnings("serial")
public final class ContentModelSelectListener implements ItemClickListener {

    private final ResourceService contentModelService;

    private ContentModelView contentModelView;

    private final Window mainWindow;

    public ContentModelSelectListener(final ResourceService contentModelService, final Window mainWindow) {
        Preconditions.checkNotNull(contentModelService, "contentModelService is null: %s", contentModelService);
        Preconditions.checkNotNull(mainWindow, "mainWindow is null: %s", mainWindow);
        this.contentModelService = contentModelService;
        this.mainWindow = mainWindow;
    }

    @Override
    public void itemClick(final ItemClickEvent event) {
        final Object itemId = event.getItemId();
        final Item item = event.getItem();

        if (!(itemId instanceof ContentModel)) {
            throw new UnsupportedOperationException("Not yet implemented");
        }

        contentModelView.showEditView(tryFindContentModelById(itemId));
        contentModelView.showEditView(item);
    }

    private Resource tryFindContentModelById(final Object object) {
        try {
            return contentModelService.findById(((ContentModel) object).getObjid());
        }
        catch (final EscidocClientException e) {
            mainWindow.showNotification(e.getMessage());
        }
        return new Resource() {

            @Override
            public ResourceType getResourceType() {
                throw new UnsupportedOperationException("Not yet implemented");
            }

            @Override
            public void generateXLinkHref(final String parentPath) {
                throw new UnsupportedOperationException("Not yet implemented");
            }
        };
    }

    public void setContentModelView(final ContentModelView view) {
        Preconditions.checkNotNull(view, "editView is null: %s", view);
        contentModelView = view;
    }

}