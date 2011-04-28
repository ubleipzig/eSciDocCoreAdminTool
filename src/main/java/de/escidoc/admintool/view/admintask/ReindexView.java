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

import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Window;

import de.escidoc.admintool.service.ServiceContainer;
import de.escidoc.admintool.view.ViewConstants;
import de.escidoc.admintool.view.admintask.Style.H2;
import de.escidoc.admintool.view.admintask.Style.Ruler;

public class ReindexView extends AbstractAdminTaskView {

    private static final long serialVersionUID = -6037070039048596585L;

    public ReindexView(final ServiceContainer services, final Window mainWindow) {
        super(services, mainWindow);
    }

    @Override
    public void addView() {
        Label text = new H2(ViewConstants.REINDEX_RESOURCES_TITLE);
        text.setContentMode(Label.CONTENT_XHTML);

        cssLayout.addComponent(text);
        cssLayout.addComponent(new Ruler());

        text = new Label(ViewConstants.REINDEX_TEXT, Label.CONTENT_XHTML);
        cssLayout.addComponent(text);

        final HorizontalLayout hLayout = new HorizontalLayout();
        hLayout.setWidth("100%");
        hLayout.setHeight("250px");

        final ReindexResourceView reindexResourceView =
            new ReindexResourceViewImpl(services.getAdminService(), mainWindow);
        hLayout.addComponent(reindexResourceView);

        cssLayout.addComponent(hLayout);
    }
}