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
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import de.escidoc.admintool.service.ServiceContainer;
import de.escidoc.admintool.view.ViewConstants;

public abstract class AbstractAdminTaskView extends CustomComponent {

    private static final long serialVersionUID = 2712816510873244813L;

    protected ServiceContainer services;

    protected final Window mainWindow;

    final VerticalLayout mainLayout = new VerticalLayout();

    protected final CssLayout cssLayout = new CssLayout();

    public AbstractAdminTaskView(final ServiceContainer services, final Window mainWindow) {

        Preconditions.checkNotNull(services, "services is null: %s", services);
        Preconditions.checkNotNull(mainWindow, "mainWindow is null: %s", mainWindow);

        this.services = services;
        this.mainWindow = mainWindow;

        init();
    }

    private void init() {
        setCompositionRoot(mainLayout);
        cssLayout.setHeight(100, UNITS_PERCENTAGE);
        cssLayout.setMargin(true);
        cssLayout.setWidth(ViewConstants._100_PERCENT);
        mainLayout.addComponent(cssLayout);

        final HorizontalLayout texts = new HorizontalLayout();
        texts.setSpacing(true);
        texts.setWidth(ViewConstants._100_PERCENT);
        texts.setMargin(false, false, true, false);
        cssLayout.addComponent(texts);
    }

    public abstract void addView();
}