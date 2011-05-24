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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;

import de.escidoc.admintool.view.ModalDialog;
import de.escidoc.admintool.view.ViewConstants;
import de.escidoc.core.client.exceptions.EscidocClientException;

final class AddRoleButtonListener implements Button.ClickListener {

    private static final Logger LOG = LoggerFactory.getLogger(AddRoleButtonListener.class);

    private final UserEditForm userEditForm;

    AddRoleButtonListener(final UserEditForm userEditForm) {
        this.userEditForm = userEditForm;
    }

    private static final long serialVersionUID = 2520625502594778921L;

    @Override
    public void buttonClick(final ClickEvent event) {
        try {
            userEditForm.app.showRoleView();
            userEditForm.app.showRoleView(userEditForm.userService.getUserById(userEditForm.userObjectId));
        }
        catch (final EscidocClientException e) {
            ModalDialog.show(userEditForm.app.getMainWindow(), e);
            LOG.error(ViewConstants.AN_UNEXPECTED_ERROR_OCCURED_SEE_LOG_FOR_DETAILS, e);
        }
    }
}