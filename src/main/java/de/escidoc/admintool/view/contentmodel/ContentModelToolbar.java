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

    private final PdpRequest pdpRequest;

    public ContentModelToolbar(final PdpRequest pdpRequest) {
        Preconditions.checkNotNull(pdpRequest, "pdpRequest is null: %s", pdpRequest);
        this.pdpRequest = pdpRequest;
        setCompositionRoot(hLayout);
    }

    public void init() {
        configureLayout();
        addNewButton();
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

    public void bind(final String contentModelId) {
        newBtn.setVisible(isCreateAllowed());
    }

    private boolean isCreateAllowed() {
        return pdpRequest.isPermitted(ActionIdConstants.CREATE_CONTENT_MODEL);
    }
}
