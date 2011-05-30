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
package de.escidoc.admintool.view.context;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.vaadin.data.Item;
import com.vaadin.ui.Label;
import com.vaadin.ui.SplitPanel;
import com.vaadin.ui.VerticalLayout;

import de.escidoc.admintool.app.AdminToolApplication;
import de.escidoc.admintool.domain.PdpRequest;
import de.escidoc.admintool.exception.ResourceNotFoundException;
import de.escidoc.admintool.view.ModalDialog;
import de.escidoc.admintool.view.ViewConstants;
import de.escidoc.admintool.view.navigation.ActionIdConstants;
import de.escidoc.admintool.view.resource.ResourceView;
import de.escidoc.core.resources.Resource;

@SuppressWarnings("serial")
public class ContextView extends SplitPanel implements ResourceView {

    private final Logger LOG = LoggerFactory.getLogger(ContextView.class);

    private final ContextListView contextList;

    private final ContextEditForm contextEditForm;

    private final AdminToolApplication app;

    private final PdpRequest pdpRequest;

    public ContextView(final AdminToolApplication app, final ContextListView contextList,
        final ContextEditForm contextEditForm, final ContextAddView contextAddView, final PdpRequest pdpRequest) {

        Preconditions.checkNotNull(app, "App can not be null");
        Preconditions.checkNotNull(contextList, "contextList can not be null");
        Preconditions.checkNotNull(contextEditForm, "contextEditForm can not be null");
        Preconditions.checkNotNull(contextAddView, "contextAddView can not be null");
        Preconditions.checkNotNull(pdpRequest, "pdpRequest is null: %s", pdpRequest);

        this.app = app;
        this.contextList = contextList;
        this.contextEditForm = contextEditForm;
        this.pdpRequest = pdpRequest;

        final VerticalLayout vLayout = new VerticalLayout();
        vLayout.setHeight(100, UNITS_PERCENTAGE);
        final Label text = new Label("<b>" + "Contexts" + "</b>", Label.CONTENT_XHTML);
        vLayout.addComponent(text);
        contextList.setSizeFull();

        vLayout.addComponent(contextList);
        vLayout.addComponent(contextList.createControls());

        vLayout.setExpandRatio(contextList, 1.0f);
        setFirstComponent(vLayout);

        setSplitPosition(ViewConstants.SPLIT_POSITION_IN_PERCENT);
        setOrientation(ORIENTATION_HORIZONTAL);
        setSecondComponent(new Label(""));
    }

    public ContextListView getContextList() {
        return contextList;
    }

    public void showAddView() {
        setSecondComponent(app.newContextAddView());
    }

    public void showEditView(final Item item) {
        setSecondComponent(contextEditForm);
        try {
            contextEditForm.setSelected(item);
        }
        catch (final ResourceNotFoundException e) {
            LOG.error("root cause: " + ExceptionUtils.getRootCauseMessage(e), e);
            ModalDialog.show(app.getMainWindow(), e);
        }
    }

    public Item getSelectedItem() {
        return contextList.getItem(contextList.getValue());
    }

    @Override
    public void selectInFolderView(final Resource resource) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void showFirstItemInEditView() {
        Preconditions.checkNotNull(contextList, "contextList is null: %s", contextList);

        final Object firstItemId = contextList.firstItemId();
        if (firstItemId == null && isPermitToCreate()) {
            showAddView();
        }
        else if (firstItemId == null && !isPermitToCreate()) {
            return;
        }
        else {
            contextList.select(firstItemId);
            showEditView(contextList.getContainerDataSource().getItem(firstItemId));
        }
    }

    private boolean isPermitToCreate() {
        return pdpRequest.isPermitted(ActionIdConstants.CREATE_CONTEXT);
    }
}