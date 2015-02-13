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
 * Copyright 2015 Leipzig University Library
 * 
 * This code is the result of the project
 * "Die Bibliothek der Milliarden Woerter". This project is funded by
 * the European Social Fund. "Die Bibliothek der Milliarden Woerter" is
 * a cooperation project between the Leipzig University Library, the
 * Natural Language Processing Group at the Institute of Computer
 * Science at Leipzig University, and the Image and Signal Processing
 * Group at the Institute of Computer Science at Leipzig University.
 * 
 * All rights reserved.  Use is subject to license terms.
 * 
 * @author Uwe Kretschmer <u.kretschmer@denk-selbst.de>
 * 
 */
package de.uni_leipzig.ubl.admintool.view.group;

import java.util.Set;

import com.google.common.base.Preconditions;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Window.Notification;

import de.escidoc.admintool.app.AdminToolApplication;
import de.escidoc.admintool.app.Command;
import de.escidoc.admintool.view.ViewConstants;
import de.escidoc.admintool.view.role.RevokeGrantWindow;
import de.escidoc.core.resources.aa.useraccount.Grant;
import de.uni_leipzig.ubl.admintool.view.group.GroupEditForm;
import de.uni_leipzig.ubl.admintool.view.role.RevokeGrantCommand;

public class RemoveRoleButtonListener implements ClickListener {

	private static final long serialVersionUID = -2125733049252623555L;
	
	private final GroupEditForm groupEditForm;
	
	/**
	 * @param groupEditForm
	 */
	public RemoveRoleButtonListener(final GroupEditForm groupEditForm) {
		Preconditions.checkNotNull(groupEditForm, "groupEditForm is null: %s", groupEditForm);
		this.groupEditForm = groupEditForm;
	}

	@Override
	public void buttonClick(ClickEvent event) {
		final Object selectedGrants = this.groupEditForm.roles.getValue();
		
		if (selectedGrants instanceof Set<?>) {
			for (final Object grant : ((Set<?>) selectedGrants)) {
				if ( grant instanceof Grant) {
					final Window commentWindow = createModalWindow((Grant) grant).getModalWindow();
					commentWindow.setCaption("Revoke Grant " + ((Grant) grant).getXLinkTitle());
					this.groupEditForm.app.getMainWindow().addWindow(commentWindow);
				}
			}
		}
	}
	
	private RevokeGrantWindow createModalWindow(final Grant grant) {
		return new RevokeGrantWindow(createRevokeGrantCommand(grant), grant, this.groupEditForm.grantContainer);
	}

	private Command createRevokeGrantCommand(final Grant grant) {
		return new RevokeGrantCommand(this.groupEditForm.app, this.groupEditForm.groupService, this.groupEditForm.groupObjectId, grant);
	}

}
