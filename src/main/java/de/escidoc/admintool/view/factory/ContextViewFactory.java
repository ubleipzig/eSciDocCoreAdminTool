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
package de.escidoc.admintool.view.factory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.vaadin.ui.Window;

import de.escidoc.admintool.app.AdminToolApplication;
import de.escidoc.admintool.domain.PdpRequest;
import de.escidoc.admintool.service.ContextService;
import de.escidoc.admintool.service.OrgUnitService;
import de.escidoc.admintool.view.ModalDialog;
import de.escidoc.admintool.view.ViewConstants;
import de.escidoc.admintool.view.context.AddOrgUnitToTheList;
import de.escidoc.admintool.view.context.ContextAddView;
import de.escidoc.admintool.view.context.ContextEditForm;
import de.escidoc.admintool.view.context.ContextListView;
import de.escidoc.admintool.view.context.ContextView;
import de.escidoc.admintool.view.resource.ResourceTreeView;
import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;

public class ContextViewFactory {

    private static final Logger LOG = LoggerFactory.getLogger(ContextViewFactory.class);

    private final Window mainWindow;

    private final OrgUnitService orgUnitService;

    private final AdminToolApplication app;

    private final ContextService contextService;

    private final PdpRequest pdpRequest;

    public ContextViewFactory(final AdminToolApplication app, final Window mainWindow,
        final OrgUnitService orgUnitService, final ContextService contextService, final PdpRequest pdpRequest) {
        Preconditions.checkNotNull(app, "app is null: %s", app);
        Preconditions.checkNotNull(mainWindow, "mainWindow is null: %s", mainWindow);
        Preconditions.checkNotNull(orgUnitService, "orgUnitService is null: %s", orgUnitService);
        Preconditions.checkNotNull(contextService, "contextService is null: %s", contextService);
        this.app = app;
        this.mainWindow = mainWindow;
        this.orgUnitService = orgUnitService;
        this.contextService = contextService;
        this.pdpRequest = pdpRequest;
    }

    private ContextListView contextList;

    private ContextEditForm contextForm;

    private ContextView contextView;

    private ContextAddView contextAddView;

    public ContextView createContextView(final ResourceTreeView resourceTreeView) {
        try {
            createContextListView();
            createContextEditForm(resourceTreeView);
            contextForm.setContextList(contextList);
            createContextAddView(resourceTreeView);
            return new ContextView(app, contextList, contextForm, contextAddView, pdpRequest);
        }
        catch (final EscidocException e) {
            ModalDialog.show(mainWindow, e);
            LOG.error(ViewConstants.SERVER_INTERNAL_ERROR, e);
        }
        catch (final InternalClientException e) {
            ModalDialog.show(mainWindow, e);
            LOG.error(ViewConstants.SERVER_INTERNAL_ERROR, e);
        }
        catch (final TransportException e) {
            ModalDialog.show(mainWindow, e);
            LOG.error(ViewConstants.SERVER_INTERNAL_ERROR, e);
        }
        return contextView;
    }

    public ContextAddView createContextAddView(final ResourceTreeView resourceTreeView) {
        contextAddView =
            new ContextAddView(app, mainWindow, contextList, contextService, new AddOrgUnitToTheList(mainWindow,
                resourceTreeView));
        contextAddView.init();
        return contextAddView;
    }

    private void createContextEditForm(final ResourceTreeView resourceTreeView) {
        final ContextEditForm contextEditForm =
            new ContextEditForm(app, mainWindow, contextService, orgUnitService, new AddOrgUnitToTheList(mainWindow,
                resourceTreeView), pdpRequest);
        contextEditForm.init();
        contextForm = contextEditForm;
    }

    private void createContextListView() throws EscidocException, InternalClientException, TransportException {
        contextList = new ContextListView(app, contextService);
    }
}