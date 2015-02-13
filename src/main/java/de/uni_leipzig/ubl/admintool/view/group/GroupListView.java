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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.vaadin.data.util.POJOContainer;
import com.vaadin.data.util.POJOItem;
import com.vaadin.ui.Table;

import de.escidoc.admintool.app.AdminToolApplication;
import de.escidoc.admintool.app.PropertyId;
import de.escidoc.admintool.view.EscidocPagedTable;
import de.escidoc.admintool.view.ViewConstants;
import de.escidoc.admintool.view.util.dialog.ErrorDialog;
import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.resources.aa.usergroup.UserGroup;
import de.uni_leipzig.ubl.admintool.service.internal.GroupService;

@SuppressWarnings("serial")
public class GroupListView extends EscidocPagedTable {

	private static final Logger LOG = LoggerFactory.getLogger(GroupListView.class);
	
	private final AdminToolApplication app;
	
	private final GroupService groupService;
	
	private final Collection<UserGroup> allUserGroups = new ArrayList<UserGroup>();
	
	private POJOContainer<UserGroup> groupContainer;
	
	public GroupListView(final AdminToolApplication app, final GroupService groupService) {
		Preconditions.checkNotNull(app, "app is null: %s", app);
		Preconditions.checkNotNull(groupService, "groupService is null: %s", groupService);
		this.app = app;
		this.groupService = groupService;
		buildView();
		findAllGroups();
		bindDataSource();
		setPageLength(50);
	}

	private void buildView() {
		setSizeFull();
		setSelectable(true);
		setImmediate(true);
		addListener(new GroupSelectListener(app, groupService));
        setNullSelectionAllowed(false);
        setColumnHeaderMode(1);
        setColumnCollapsingAllowed(true);
	}

	private void findAllGroups() {
		try {
			allUserGroups.addAll(groupService.findAll());
		} catch (final EscidocClientException e) {
			app.getMainWindow().addWindow(
                new ErrorDialog(app.getMainWindow(), "Error", "An unexpected error occured! See LOG for details."));
            LOG.error("An unexpected error occured! See LOG for details.", e);
		}
	}

	private void bindDataSource() {
		if(isGroupExist()) {
			initGroupContainer(allUserGroups);
		}
	}

	private boolean isGroupExist() {
		return allUserGroups != null && !allUserGroups.isEmpty();
	}
	
	private void initGroupContainer(final Collection<UserGroup> initialUserGroups) {
		groupContainer =
				new POJOContainer<UserGroup>(initialUserGroups, PropertyId.OBJECT_ID, PropertyId.NAME,
                PropertyId.CREATED_ON, PropertyId.CREATED_BY, PropertyId.LAST_MODIFICATION_DATE, PropertyId.MODIFIED_BY,
                PropertyId.ACTIVE);
		setContainerDataSource(groupContainer);
		
		setSortContainerPropertyId(PropertyId.NAME);
		setSortAscending(true);

		setColumnHeader(PropertyId.NAME, ViewConstants.NAME_LABEL);
		setColumnExpandRatio(PropertyId.NAME, 0.75f);
		setColumnHeader(PropertyId.ACTIVE, ViewConstants.ACTIVE_STATUS);
		setColumnExpandRatio(PropertyId.ACTIVE, 0.25f);
		setColumnHeader(PropertyId.LAST_MODIFICATION_DATE, ViewConstants.MODIFIED_ON_LABEL);
		setColumnCollapsed(PropertyId.LAST_MODIFICATION_DATE, true);
		
		setVisibleColumns(new Object[] { PropertyId.NAME, PropertyId.LAST_MODIFICATION_DATE, PropertyId.ACTIVE });
	}
	
	public void remove(final UserGroup deletedUserGroup) {
		final boolean removeItem = groupContainer.removeItem(deletedUserGroup);
		assert removeItem == true : "Failed to remove user group from the container.";
	}
	
	public POJOItem<UserGroup> addGroup(final UserGroup createdUserGroup) {
		final POJOItem<UserGroup> item;
		if (groupContainer == null) {
			List<UserGroup> initGroup = new ArrayList<UserGroup>();
			initGroup.add(createdUserGroup);
			initGroupContainer(initGroup);
			item = groupContainer.getItem(groupContainer.firstItemId());
		}
		else {
			item = groupContainer.addItem(createdUserGroup);
		}
		sort(new Object[] { getSortContainerPropertyId() }, new boolean[] { true });
        return item;
	}

	public void updateGroup(UserGroup updatedUserGroup) {
		// container has to be updated by removing old item and adding updated item
		remove(updatedUserGroup);
		addGroup(updatedUserGroup);		
	}

}
