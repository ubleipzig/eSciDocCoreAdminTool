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
package de.escidoc.admintool.view.user.password;

import org.joda.time.DateTime;

import de.escidoc.admintool.service.UserService;
import de.escidoc.core.client.exceptions.EscidocClientException;

public class UpdatePasswordCommandImpl implements UpdatePasswordCommand {

    private final UserService userService;

    private String selectedUserId;

    private DateTime lastModificationDate;

    public UpdatePasswordCommandImpl(final UserService userService) {
        this.userService = userService;
    }

    public void setSelectedUserId(final String selectedUserId) {
        this.selectedUserId = selectedUserId;
    }

    public void setLastModificationDate(final DateTime lastModificationDate) {
        this.lastModificationDate = lastModificationDate;
    }

    @Override
    public void execute(final String newPassword) throws EscidocClientException {
        userService.updatePassword(selectedUserId, newPassword, lastModificationDate);
    }

}
