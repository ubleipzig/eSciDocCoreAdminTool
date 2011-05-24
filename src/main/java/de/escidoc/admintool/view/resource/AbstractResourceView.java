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
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.SplitPanel;

import de.escidoc.admintool.view.ViewConstants;

public abstract class AbstractResourceView extends CustomComponent implements ResourceView {
    private static final long serialVersionUID = 2631752920109093495L;

    private final SplitPanel splitPanel = new SplitPanel();

    private final ResourceFolderView resourceListView;

    public AbstractResourceView(final ResourceFolderView resourceListView) {
        Preconditions.checkNotNull(resourceListView, "resourceListView is null: %s", resourceListView);
        this.resourceListView = resourceListView;
        buildUI();
    }

    private void buildUI() {
        setSizeFull();
        getSplitPanel().setSizeFull();
        setCompositionRoot(getSplitPanel());

        getSplitPanel().setSplitPosition(ViewConstants.SPLIT_POSITION_IN_PERCENT);
        getSplitPanel().setOrientation(SplitPanel.ORIENTATION_HORIZONTAL);
        getSplitPanel().setFirstComponent(resourceListView);
    }

    public SplitPanel getSplitPanel() {
        return splitPanel;
    }
}
