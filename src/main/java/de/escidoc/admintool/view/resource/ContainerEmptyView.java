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

import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

import de.escidoc.admintool.view.ViewConstants;
import de.escidoc.core.resources.Resource;

public class ContainerEmptyView extends CustomComponent implements ResourceFolderView {

    private static final Label EMPTY = new Label(ViewConstants.EMPTY);

    private static final long serialVersionUID = -7036333192749220272L;

    private final VerticalLayout vLayout = new VerticalLayout();

    public ContainerEmptyView(final FolderHeader toolbar) {
        setCompositionRoot(vLayout);
        vLayout.addComponent(toolbar);
        vLayout.addComponent(EMPTY);
        EMPTY.setHeight(30, UNITS_PIXELS);
    }

    @Override
    public void select(final Resource resource) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}