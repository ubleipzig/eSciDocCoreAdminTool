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
package de.escidoc.admintool.view.user;

import com.google.common.base.Preconditions;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

final class CloseOrgUnitSelectionWidget implements ClickListener {
    /**
     * 
     */
    private final UserAddView userAddView;

    /**
     * @param userAddView
     */
    CloseOrgUnitSelectionWidget(UserAddView userAddView) {
        this.userAddView = userAddView;
    }

    private static final long serialVersionUID = -1514595787473181424L;

    @Override
    public void buttonClick(final ClickEvent event) {
        Preconditions.checkNotNull(this.userAddView.app.getMainWindow(), "app.getMainWindow() is null: %s",
            this.userAddView.app.getMainWindow());
        Preconditions.checkNotNull(this.userAddView.modalWindow, "modalWindow is null: %s",
            this.userAddView.modalWindow);
        this.userAddView.app.getMainWindow().removeWindow(this.userAddView.modalWindow);
    }
}