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
package de.uni_leipzig.ubl.admintool.view.role;

import com.vaadin.ui.Window.Notification;

import de.escidoc.admintool.app.AdminToolApplication;
import de.escidoc.admintool.app.Command;
import de.escidoc.admintool.service.internal.UserService;
import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.resources.aa.useraccount.Grant;
import de.uni_leipzig.ubl.admintool.service.internal.GroupService;

public class RevokeGrantCommand implements Command {

    private final AdminToolApplication app;
    
    private final GroupService groupService;

    private final String groupId;

    private final Grant grant;

    private String comment;

    public RevokeGrantCommand(final AdminToolApplication app, final GroupService groupService, final String userId, final Grant grant) {
        this.app = app;
    	this.groupService = groupService;
        this.groupId = userId;
        this.grant = grant;
    }

    public void setComment(final String comment) {
        this.comment = comment;
    }

    @Override
    public void execute() throws EscidocClientException {
        groupService.revokeGrant(groupId, grant, comment);
        app.getMainWindow().showNotification("info", "Grant revoked from group.", Notification.TYPE_TRAY_NOTIFICATION);
    }
}
