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
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;

import de.escidoc.admintool.domain.PdpRequest;
import de.escidoc.admintool.view.ViewConstants;
import de.escidoc.admintool.view.navigation.ActionIdConstants;

@SuppressWarnings("serial")
public class ContentModelToolbar extends CustomComponent {

    private final HorizontalLayout hLayout = new HorizontalLayout();

    private final ShowNewContentModelViewListener listener = new ShowNewContentModelViewListener();

    private final Button newBtn = new Button(ViewConstants.NEW, listener);

    private final DeleteContentModelListener deleteListener;

    private final Button deleteBtn = new Button(ViewConstants.DELETE);

    private final PdpRequest pdpRequest;

    protected ContentModelToolbar(final PdpRequest pdpRequest, final DeleteContentModelListener deleteListener) {
        Preconditions.checkNotNull(pdpRequest, "pdpRequest is null: %s", pdpRequest);
        Preconditions.checkNotNull(deleteListener, "deleteListener is null: %s", deleteListener);
        this.pdpRequest = pdpRequest;
        this.deleteListener = deleteListener;
    }

    protected void init() {
        setCompositionRoot(hLayout);
        configureLayout();
        addNewButton();
        addDeleteButton();
    }

    private void addDeleteButton() {
        deleteBtn.addListener(deleteListener);
        hLayout.addComponent(deleteBtn);
    }

    private void addNewButton() {
        hLayout.addComponent(newBtn);
    }

    private void configureLayout() {
        setSizeFull();
        hLayout.setHeight(100, UNITS_PERCENTAGE);
    }

    public void setContentModelView(final ContentModelView contentModelView) {
        Preconditions.checkNotNull(contentModelView, "contentModelView is null: %s", contentModelView);
        listener.setContentModelView(contentModelView);
    }

    protected void bind(final String contentModelId) {
        newBtn.setVisible(isCreateAllowed());
        deleteBtn.setVisible(isDeleteAllowed());
        deleteListener.setContentModelId(contentModelId);
    }

    private boolean isDeleteAllowed() {
        return pdpRequest.isPermitted(ActionIdConstants.DELETE_CONTENT_MODEL);
    }

    private boolean isCreateAllowed() {
        return pdpRequest.isPermitted(ActionIdConstants.CREATE_CONTENT_MODEL);
    }
}
