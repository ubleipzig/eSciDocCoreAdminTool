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

import com.google.common.base.Preconditions;
import com.vaadin.data.util.POJOItem;
import com.vaadin.event.ItemClickEvent;

import de.escidoc.admintool.app.AdminToolApplication;
import de.escidoc.admintool.app.PropertyId;
import de.escidoc.admintool.view.resource.AbstractResourceSelectListener;
import de.escidoc.admintool.view.resource.ResourceView;
import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.resources.aa.usergroup.UserGroup;
import de.uni_leipzig.ubl.admintool.service.internal.GroupService;

public class GroupSelectListener extends AbstractResourceSelectListener {

	private static final long serialVersionUID = -1451681532928396045L;

	private final AdminToolApplication app;
	
	private final GroupService groupService;
	
	public GroupSelectListener(final AdminToolApplication app, final GroupService groupService) {
		Preconditions.checkNotNull(app, "app is null: %s", app);
		Preconditions.checkNotNull(groupService, "groupService is null: %s", groupService);
		this.app = app;
		this.groupService = groupService;
	}
	
	@Override
	public void itemClick(final ItemClickEvent event) {
		try {
			getView().showEditView(userGroupToItem(groupService.retrieve(getSelectedObjectId(event))));
		} catch (EscidocClientException e) {
			app.getMainWindow().showNotification(e.getMessage());
		}
	}
	
	private String getSelectedObjectId(final ItemClickEvent event) {
		return (String) event.getItem().getItemProperty(PropertyId.OBJECT_ID).getValue();
	}

	private POJOItem<UserGroup> userGroupToItem(final UserGroup userGroup) {
        return new POJOItem<UserGroup>(userGroup, new String[] { PropertyId.OBJECT_ID, PropertyId.NAME,
                PropertyId.CREATED_ON, PropertyId.CREATED_BY, PropertyId.LAST_MODIFICATION_DATE, PropertyId.MODIFIED_BY,
                PropertyId.ACTIVE, PropertyId.DESCRIPTION, PropertyId.PROP_LABEL, PropertyId.EMAIL });
	}

	@Override
	public ResourceView getView() {
		return app.getGroupView();
	}

}
