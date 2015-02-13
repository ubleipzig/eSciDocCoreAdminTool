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
import com.vaadin.data.Item;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

import de.escidoc.admintool.app.AdminToolApplication;
import de.escidoc.admintool.view.ViewConstants;
import de.escidoc.admintool.view.resource.ResourceView;
import de.escidoc.core.resources.Resource;
import de.escidoc.core.resources.aa.usergroup.UserGroup;

@SuppressWarnings("serial")
public class GroupView extends HorizontalSplitPanel implements ResourceView {

	private final AdminToolApplication app;
	
	private final GroupListView groupList;
	
	private final GroupEditView groupEditView;
	
	private final VerticalLayout vLayout = new VerticalLayout();
	
	public GroupView(final AdminToolApplication app, final GroupListView groupListView, final GroupEditView groupEditView) {
		Preconditions.checkNotNull(app, "app is null: %s", app);
		Preconditions.checkNotNull(groupListView, "groupListView is null: %s", groupListView);
		Preconditions.checkNotNull(groupEditView, "groupEditView is null: %s", groupEditView);
		this.app = app;
		this.groupList = groupListView;
		this.groupEditView = groupEditView;
	}
	
	public void init() {
		setSplitPosition(ViewConstants.SPLIT_POSITION_IN_PERCENT);
		
		vLayout.setHeight(100, UNITS_PERCENTAGE);
		addHeader(vLayout);
		addListView(vLayout);
		setFirstComponent(vLayout);
	}
	
	private void addHeader(final VerticalLayout vLayout) {
		vLayout.addComponent(new Label("<b>User Groups</b>", Label.CONTENT_XHTML));
	}

	private void addListView(final VerticalLayout vLayout) {
		groupList.setSizeFull();
		vLayout.addComponent(groupList);
		vLayout.addComponent(groupList.createControls());
		vLayout.setExpandRatio(groupList, 1.0f);
	}

	@Override
	public void showAddView() {
		setSecondComponent(app.newGroupAddView());
	}

	@Override
	public void showEditView(Item item) {
		setSecondComponent(groupEditView);
		groupEditView.setSelected(item);
	}

	@Override
	public void selectInFolderView(Resource resource) {
		// TODO Auto-generated method stub

	}

	public GroupListView getGroupList() {
		return groupList;
	}

	public Item getSelectedItem() {
		return groupList.getItem(groupList.getValue());
	}
	
	public void remove(final UserGroup deletedUserGroup) {
		app.showGroupView();
	}

	public void showAddRoleToGroupView() {
		app.showGroupRoleView();
	}

	public void showSummaryView(UserGroup userGroup) {
		app.showGroupSummaryView(userGroup);
	}
	
}
