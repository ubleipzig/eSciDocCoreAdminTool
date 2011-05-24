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

import com.vaadin.data.Item;
import com.vaadin.ui.Window;

import de.escidoc.admintool.app.AdminToolApplication;
import de.escidoc.admintool.domain.PdpRequest;
import de.escidoc.admintool.service.internal.ResourceService;
import de.escidoc.core.resources.Resource;

public class ResourceViewImpl extends AbstractResourceView {

    private static final long serialVersionUID = -5537726031167765555L;

    private final ResourceFolderView resourceListView;

    private final ResourceEditView resourceEditView;

    private final Window mainWindow;

    private final ResourceService resourceService;

    private final ResourceContainer resourceContainer;

    private final AdminToolApplication app;

    private final PdpRequest pdpRequest;

    public ResourceViewImpl(final AdminToolApplication app, final Window mainWindow,
        final ResourceFolderView resourceListView, final ResourceService resourceService,
        final ResourceContainer resourceContainer, final PdpRequest pdpRequest) {

        super(resourceListView);
        this.app = app;
        this.mainWindow = mainWindow;
        this.resourceService = resourceService;
        this.resourceListView = resourceListView;
        this.resourceContainer = resourceContainer;
        this.pdpRequest = pdpRequest;
        resourceEditView =
            new ResourceEditViewImpl(app, mainWindow, this, resourceService, resourceContainer, pdpRequest);

    }

    @Override
    public void showAddView() {
        getSplitPanel().setSecondComponent(
            new ResourceAddViewImpl(app, mainWindow, this, resourceService, resourceContainer, pdpRequest));
    }

    @Override
    public void showEditView(final Item item) {
        resourceEditView.bind(item);
        getSplitPanel().setSecondComponent(resourceEditView);
    }

    public void setFormReadOnly(final boolean isReadOnly) {
        resourceEditView.setFormReadOnly(isReadOnly);
    }

    public void setFooterVisible(final boolean isVisible) {
        resourceEditView.setFooterVisible(isVisible);
    }

    @Override
    public void selectInFolderView(final Resource resource) {
        resourceListView.select(resource);
    }
}