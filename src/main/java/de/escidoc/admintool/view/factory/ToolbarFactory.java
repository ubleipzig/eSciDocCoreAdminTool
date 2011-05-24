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
/**
 * 
 */
package de.escidoc.admintool.view.factory;

import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;

import de.escidoc.admintool.app.AppConstants;
import de.escidoc.admintool.view.ViewConstants;

/**
 * @author ASP
 * 
 */
public class ToolbarFactory {

    private static final int ROWS = 1;

    private static final int COLUMNS = 3;

    private final HorizontalLayout hLayout = new HorizontalLayout();

    private final Embedded embedded = new Embedded();

    private final ThemeResource imageResource = new ThemeResource(AppConstants.ESCIDOC_LOGO);

    private GridLayout gLayout;

    public ToolbarFactory() {
        // do not init
    }

    public GridLayout createToolbar(final HorizontalLayout layout) {
        if (gLayout == null) {
            gLayout = new GridLayout(COLUMNS, ROWS);
            gLayout.setMargin(false);
            gLayout.setSpacing(false);
            gLayout.setWidth("100%");
            gLayout.setStyleName(ViewConstants.TOOLBAR_STYLE_NAME);
            gLayout.addComponent(hLayout, 0, 0);
            addBackgroundImage();
            add(layout);
        }

        return gLayout;
    }

    private void add(final HorizontalLayout layout) {
        gLayout.addComponent(layout, 2, 0);
        gLayout.setComponentAlignment(layout, Alignment.MIDDLE_RIGHT);
    }

    private void addBackgroundImage() {
        embedded.setType(Embedded.TYPE_IMAGE);
        embedded.setSource(imageResource);
        gLayout.setComponentAlignment(embedded, Alignment.TOP_LEFT);
        hLayout.addComponent(embedded);
        hLayout.setExpandRatio(embedded, 1);
    }

}