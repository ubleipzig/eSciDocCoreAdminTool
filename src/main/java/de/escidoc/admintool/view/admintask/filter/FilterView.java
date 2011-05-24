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
package de.escidoc.admintool.view.admintask.filter;

import com.google.common.base.Preconditions;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Window;

import de.escidoc.admintool.domain.PdpRequest;
import de.escidoc.admintool.service.ServiceContainer;
import de.escidoc.admintool.view.ViewConstants;
import de.escidoc.admintool.view.admintask.AbstractAdminTaskView;
import de.escidoc.admintool.view.admintask.Style;
import de.escidoc.admintool.view.admintask.Style.H2;

public class FilterView extends AbstractAdminTaskView {

    private static final long serialVersionUID = -1412202753685048760L;

    private final PdpRequest pdpRequest;

    public FilterView(final ServiceContainer services, final Window mainWindow, final PdpRequest pdpRequest) {
        super(services, mainWindow);
        Preconditions.checkNotNull(pdpRequest, "pdpRequest is null: %s", pdpRequest);
        this.pdpRequest = pdpRequest;
    }

    @Override
    public void addView() {
        addHeader();
        addRuler();
        addDescription();
        addContent();
    }

    private void addContent() {
        final HorizontalLayout hLayout = new HorizontalLayout();
        hLayout.setWidth(100, UNITS_PERCENTAGE);
        hLayout.setHeight(100, UNITS_PERCENTAGE);

        final FilterResourceView filterView = new FilterResourceView(services, mainWindow, pdpRequest);
        filterView.init();
        hLayout.addComponent(filterView);
        cssLayout.addComponent(hLayout);
    }

    private void addDescription() {
        cssLayout.addComponent(new Label(ViewConstants.FILTER_DESCRIPTION_TEXT, Label.CONTENT_XHTML));
    }

    private void addRuler() {
        cssLayout.addComponent(new Style.Ruler());
    }

    private void addHeader() {
        final Label text = new H2(ViewConstants.FILTERING_RESOURCES_TITLE);
        text.setContentMode(Label.CONTENT_XHTML);
        cssLayout.addComponent(text);
    }
}