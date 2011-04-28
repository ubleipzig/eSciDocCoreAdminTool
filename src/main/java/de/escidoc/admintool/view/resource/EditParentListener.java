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

import com.vaadin.data.Item;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Window;

public class EditParentListener implements ClickListener {

    private static final long serialVersionUID = 5088260302217401646L;

    private final Window mainWindow;

    private final AddOrEditParentModalWindow orgUnitSelectDialog;

    public EditParentListener(final Window mainWindow, final AddOrEditParentModalWindow orgUnitSelectDialog) {
        this.mainWindow = mainWindow;
        this.orgUnitSelectDialog = orgUnitSelectDialog;
    }

    @Override
    public void buttonClick(final ClickEvent event) {
        showOrgUnitSelectionDialog();
    }

    private void showOrgUnitSelectionDialog() {
        mainWindow.addWindow(orgUnitSelectDialog);
    }

    public void bind(final Item item) {
        orgUnitSelectDialog.bind(item);
    }
}
