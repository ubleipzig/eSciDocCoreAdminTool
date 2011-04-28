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

import java.util.Set;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;

import de.escidoc.admintool.app.Command;
import de.escidoc.admintool.view.role.RevokeGrantCommand;
import de.escidoc.admintool.view.role.RevokeGrantWindow;
import de.escidoc.core.resources.aa.useraccount.Grant;

final class RemoveRoleButtonListener implements Button.ClickListener {

    /**
     * 
     */
    private final UserEditForm userEditForm;

    /**
     * @param userEditForm
     */
    RemoveRoleButtonListener(UserEditForm userEditForm) {
        this.userEditForm = userEditForm;
    }

    private static final long serialVersionUID = -605606788213049694L;

    @Override
    public void buttonClick(final ClickEvent event) {
        final Object selectedGrants = this.userEditForm.roleTable.getValue();

        if (selectedGrants instanceof Set<?>) {
            for (final Object grant : ((Set<?>) selectedGrants)) {
                if (grant instanceof Grant) {
                    this.userEditForm.app.getMainWindow().addWindow(createModalWindow((Grant) grant).getModalWindow());
                }
            }
        }
    }

    private RevokeGrantWindow createModalWindow(final Grant grant) {
        return new RevokeGrantWindow(createRevokeGrantCommand(grant), grant, this.userEditForm.grantContainer);
    }

    private Command createRevokeGrantCommand(final Grant grant) {
        return new RevokeGrantCommand(this.userEditForm.userService, this.userEditForm.userObjectId, grant);
    }
}