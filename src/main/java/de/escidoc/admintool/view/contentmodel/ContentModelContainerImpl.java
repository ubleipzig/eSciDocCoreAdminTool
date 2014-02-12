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

import java.util.Set;

import com.google.common.base.Preconditions;
import com.vaadin.data.Container;
import com.vaadin.data.util.POJOContainer;

import de.escidoc.admintool.app.PropertyId;
import de.escidoc.admintool.service.internal.ResourceService;
import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.resources.Resource;

public class ContentModelContainerImpl {

    private final ResourceService contentModelService;

    private POJOContainer<Resource> itemContainer;

    public ContentModelContainerImpl(final ResourceService contentModelService) {
        Preconditions.checkNotNull(contentModelService, "contentModelService is null: %s", contentModelService);
        this.contentModelService = contentModelService;

    }

    protected void reload() throws EscidocClientException {
        final Set<Resource> allContentModels = contentModelService.findAll();
        itemContainer = new POJOContainer<Resource>(Resource.class, PropertyId.X_LINK_TITLE);
        for (final Resource resource : allContentModels) {
            itemContainer.addItem(resource);
        }
    }

    public Container getDataSource() {
        return itemContainer;
    }

    protected void add(final Resource created) {
        Preconditions.checkNotNull(created, "created is null: %s", created);
        itemContainer.addItem(created);
    }

    protected void remove(final Resource toBeRemoved) {
        Preconditions.checkNotNull(toBeRemoved, "toBeRemoved is null: %s", toBeRemoved);
        itemContainer.removeItem(toBeRemoved);
    }
}
