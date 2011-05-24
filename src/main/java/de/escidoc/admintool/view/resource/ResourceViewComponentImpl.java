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

import de.escidoc.admintool.app.AdminToolApplication;
import de.escidoc.admintool.domain.PdpRequest;
import de.escidoc.admintool.service.OrgUnitServiceLab;
import de.escidoc.admintool.service.ResourceService;
import de.escidoc.admintool.view.navigation.ActionIdConstants;
import de.escidoc.admintool.view.resource.ResourceTreeView.AddChildrenCommand;
import de.escidoc.core.client.exceptions.EscidocClientException;

public class ResourceViewComponentImpl implements ResourceViewComponent {

    private final FolderHeader header = new FolderHeaderImpl("Organizational Units");

    private ResourceViewImpl resourceView;

    private final ResourceContainer resourceContainer;

    private AddChildrenCommand addChildrenCommand;

    private final ResourceService resourceService;

    private ShowEditResourceView showEditResourceView;

    private final Window mainWindow;

    private ResourceTreeView resourceTreeView;

    private final AdminToolApplication app;

    private final PdpRequest pdpRequest;

    public ResourceViewComponentImpl(final AdminToolApplication app, final Window mainWindow,
        final ResourceService resourceService, final ResourceContainer resourceContainer, final PdpRequest pdpRequest) {
        Preconditions.checkNotNull(mainWindow, "mainWindow is null: %s", mainWindow);
        Preconditions.checkNotNull(resourceService, "resourceService is null: %s", resourceService);
        Preconditions.checkNotNull(resourceContainer, "resourceContainer is null: %s", resourceContainer);
        Preconditions.checkNotNull(pdpRequest, "pdpRequest is null: %s", pdpRequest);
        this.app = app;
        this.mainWindow = mainWindow;
        this.resourceService = resourceService;
        this.resourceContainer = resourceContainer;
        this.pdpRequest = pdpRequest;
    }

    public void init() throws EscidocClientException {
        addChildrenCommand = createAddChildrenCommand(resourceContainer);
        resourceView =
            new ResourceViewImpl(app, mainWindow, createFolderView(), resourceService, resourceContainer, pdpRequest);
    }

    private ResourceFolderView createFolderView() {
        showEditResourceView = new ShowEditResourceView() {
            @Override
            public void execute(final Item item) {
                resourceView.showEditView(item);
            }
        };
        resourceTreeView = new ResourceTreeView(mainWindow, header, resourceContainer);
        resourceTreeView.setEditView(showEditResourceView);
        resourceTreeView.setCommand(addChildrenCommand);
        resourceTreeView.addResourceNodeExpandListener();
        resourceTreeView.addResourceNodeClickedListener();
        return resourceTreeView;
    }

    public ResourceView getResourceView() {
        return resourceView;
    }

    private AddChildrenCommandImpl createAddChildrenCommand(final ResourceContainer resourceContainer) {
        return new AddChildrenCommandImpl((OrgUnitServiceLab) resourceService, resourceContainer);
    }

    @Override
    public void showFirstItemInEditView() {
        if (resourceContainer.isEmpty() && isPermitToCreate()) {
            resourceView.showAddView();
        }
        else if (resourceContainer.isEmpty() && !isPermitToCreate()) {
            return;
        }
        else {
            resourceTreeView.tree.select(resourceContainer.firstResource());
            final Item item = resourceContainer.firstResourceAsItem();
            resourceView.showEditView(item);
        }
    }

    private boolean isPermitToCreate() {
        return pdpRequest.isPermitted(ActionIdConstants.CREATE_USER_ACCOUNT);
    }
}