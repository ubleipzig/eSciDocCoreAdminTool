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
package de.escidoc.admintool.view.admintask;

import com.google.common.base.Preconditions;
import com.vaadin.ui.Window;

import de.escidoc.admintool.service.ServiceContainer;
import de.escidoc.admintool.view.ErrorMessage;
import de.escidoc.admintool.view.resource.ResourceContainer;
import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.resources.Resource;
import de.escidoc.core.resources.adm.LoadExamplesResult.Entry;

public class AddToResourceContainer implements AddToContainer {

    private final Window mainWindow;

    private final ServiceContainer services;

    private final ResourceContainer resourceContainer;

    public AddToResourceContainer(final Window mainWindow, final ServiceContainer services,
        final ResourceContainer resourceContainer) {

        Preconditions.checkNotNull(mainWindow, "mainWindow is null: %s", mainWindow);
        Preconditions.checkNotNull(services, "services is null: %s", services);
        Preconditions.checkNotNull(resourceContainer, "resourceContainer is null: %s", resourceContainer);

        this.mainWindow = mainWindow;
        this.services = services;
        this.resourceContainer = resourceContainer;
    }

    public void execute(final Entry entry) {
        Preconditions.checkNotNull(entry, "entry is null: %s", entry);

        final Resource orgUnit = findOrgUnitById(entry);

        if (orgUnit == null) {
            return;
        }

        resourceContainer.add(orgUnit);
    }

    private Resource findOrgUnitById(final Entry entry) {
        Preconditions.checkNotNull(services, "services is null: %s", services);
        try {
            return services.getOrgUnitService().findById(entry.getObjid());
        }
        catch (final EscidocClientException e) {
            Preconditions.checkNotNull(mainWindow, "mainWindow is null: %s", mainWindow);
            ErrorMessage.show(mainWindow, e);
        }
        return null;
    }
}
