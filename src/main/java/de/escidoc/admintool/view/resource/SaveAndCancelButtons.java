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

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;

import de.escidoc.admintool.view.ViewConstants;

public class SaveAndCancelButtons extends CustomComponent {
    private static final long serialVersionUID = 5031656063216982470L;

    final HorizontalLayout hl = new HorizontalLayout();

    private final HorizontalLayout footers = new HorizontalLayout();

    private final Button cancelBtn = new Button(ViewConstants.CANCEL);

    private final Button saveBtn = new Button(ViewConstants.SAVE_LABEL);

    public SaveAndCancelButtons() {
        setCompositionRoot(footers);
        footers.setWidth(100, UNITS_PERCENTAGE);

        hl.addComponent(saveBtn);
        hl.addComponent(cancelBtn);
        footers.addComponent(hl);
        footers.setComponentAlignment(hl, Alignment.MIDDLE_RIGHT);
    }

    public void setOkButtonListener(final ResourceBtnListener resourceBtnListener) {
        saveBtn.addListener(resourceBtnListener);
    }

    public void setCancelButtonListener(final CancelButtonListener cancelButtonListener) {
        cancelBtn.addListener(cancelButtonListener);
    }

    public Button getCancelBtn() {
        return cancelBtn;
    }
}