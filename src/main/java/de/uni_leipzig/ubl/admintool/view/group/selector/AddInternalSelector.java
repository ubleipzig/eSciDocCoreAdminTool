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
package de.uni_leipzig.ubl.admintool.view.group.selector;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.vaadin.data.Item;
import com.vaadin.data.util.POJOContainer;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import de.escidoc.admintool.app.AdminToolApplication;
import de.escidoc.admintool.app.PropertyId;
import de.escidoc.admintool.service.internal.UserService;
import de.escidoc.admintool.view.ModalDialog;
import de.escidoc.admintool.view.ViewConstants;
import de.escidoc.admintool.view.context.listener.CancelButtonListener;
import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.resources.aa.useraccount.UserAccount;
import de.escidoc.core.resources.aa.usergroup.Selector;
import de.escidoc.core.resources.aa.usergroup.UserGroup;
import de.uni_leipzig.ubl.admintool.service.internal.GroupService;

public final class AddInternalSelector implements ClickListener {

	private static final long serialVersionUID = -6207259029275150329L;

	private static final Logger LOG = LoggerFactory.getLogger(AddInternalSelector.class);
	
	private final HorizontalSplitPanel tables = new HorizontalSplitPanel();
	
	private final HorizontalLayout footer = new HorizontalLayout();
	
	private final AdminToolApplication app;
	
	private final UserService userService;
	
	private final GroupService groupService;
	
	private final Table selectorsInternal;
	
	private List<Object> assignedSelectors;
	
	private UserGroup userGroup;
	
	private Window mainWindow;
	
	private Window modalWindow = new Window();
	
	private POJOContainer<UserAccount> userContainer;
	
	private POJOContainer<UserGroup> groupContainer;
	
	private final Table users = new Table();
	
	private final Table groups = new Table();
	
	private final Button okButton = new Button(ViewConstants.OK_LABEL);
	
	private final Button cancelButton = new Button(ViewConstants.CANCEL_LABEL);
	
	private AddInternalSelectorButtonListener addInternalSelectorButtonListener;
	
	private CancelButtonListener cancelButtonListener;
	
	
	
	public AddInternalSelector(final AdminToolApplication app, final GroupService groupService, final Table selectorsInternal) {
		Preconditions.checkNotNull(app, "app is null: %s", app);
		Preconditions.checkNotNull(groupService, "groupService is null: %s", groupService);
		Preconditions.checkNotNull(selectorsInternal, "selectorsInternal is null: %s", selectorsInternal);
		
		this.app = app;
		this.userService = app.getUserService();
		this.groupService = groupService;
		this.selectorsInternal = selectorsInternal;
		
	}


	@Override
	public void buttonClick(ClickEvent event) {
		init();
		bindData();
		createModalWindow();
		showModalWindow();
	}


	private void init() {
		// TODO may move some things to constructor, which we needn't called every click
		
		final Collection<UserAccount> userList;
		final Collection<UserGroup> groupList;
		
		// set container
		try {
			userList = userService.findAll();
			groupList = groupService.findAll();
			
			if (groupList.isEmpty()) {
				// TODO Do something in case of no groups, is this needed already?
			}
			
			userContainer = new POJOContainer<UserAccount>(userList, new String[] { PropertyId.NAME, PropertyId.OBJECT_ID });
			groupContainer = new POJOContainer<UserGroup>(groupList, new String[] { PropertyId.NAME, PropertyId.OBJECT_ID });
			
		} catch (EscidocClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// set userGroup
		try {
			this.userGroup = groupService.getGroupById(app.getGroupView().getSelectedItem().getItemProperty(PropertyId.OBJECT_ID).toString());
		} catch (EscidocClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		this.mainWindow = app.getMainWindow();
		
		this.assignedSelectors = getAssignedSelectors();
	}
	
	
	private void createModalWindow() {
		// TODO Auto-generated method stub
		configure();
		addUsers();
		addGroups();
		addButtons();
	}


	private void addUsers() {
		configureTable(users);
		users.setCaption(ViewConstants.USERS);
		VerticalLayout vl = new VerticalLayout();
		vl.addComponent(users);
		tables.setFirstComponent(vl);
	}


	private void addGroups() {
		configureTable(groups);
		groups.setCaption(ViewConstants.GROUPS);
		VerticalLayout vl = new VerticalLayout();
		vl.addComponent(groups);
		tables.setSecondComponent(vl);
	}
	
	
	private void addButtons() {
		createButtonListenerIfNotSet();
		okButton.addListener(addInternalSelectorButtonListener);
		cancelButton.addListener(cancelButtonListener);
		footer.addComponent(okButton);
		footer.addComponent(cancelButton);
	}
	
	
	private void createButtonListenerIfNotSet() {
		if (addInternalSelectorButtonListener == null) {
			this.addInternalSelectorButtonListener = new AddInternalSelectorButtonListener(app, mainWindow, modalWindow, groupService, this);
		}
		if (cancelButtonListener == null) {
			this.cancelButtonListener = new CancelButtonListener(mainWindow, modalWindow);
		}
	}


	private void configureTable(final Table table) {
		table.setImmediate(true);
		table.setColumnHeaderMode(Table.COLUMN_HEADER_MODE_HIDDEN);
		table.setSelectable(true);
		table.setMultiSelect(true);
		table.setSortContainerPropertyId(PropertyId.NAME);
		table.setSortAscending(true);
		table.setVisibleColumns(new Object[] { PropertyId.NAME });
		table.setSizeFull();
	}


	private void configure() {
		modalWindow.setModal(true);
		modalWindow.setCaption("Select Internal Selectors");
		modalWindow.setHeight("50%");
		modalWindow.setWidth("50%");
		
		modalWindow.addComponent(tables);
		modalWindow.addComponent(footer);
	}


	private void bindData() {
		prepareUsers();
		prepareGroups();
		bindUsers();
		bindGroups();
	}
	
	
	private void prepareUsers() {
		// remove users from list, which are already assigned
		for (Object itemId : userContainer.getItemIds()) {
			if (assignedSelectors.contains(userContainer.getItem(itemId).getItemProperty(PropertyId.OBJECT_ID).getValue())) {
				userContainer.removeItem(itemId);
			}
		}
	}


	private void prepareGroups() {
		for  (Object itemId : groupContainer.getItemIds()) {
			Object objectId = groupContainer.getItem(itemId).getItemProperty(PropertyId.OBJECT_ID).getValue();
			
			// remove groups from list, which are already assigned or is the actually edited group
			if (assignedSelectors.contains(objectId) || objectId.toString().equals(userGroup.getObjid())) {
				groupContainer.removeItem(itemId);
			}
			// remove groups which are parents of actually edited group
			else if (isSelectorParent(objectId.toString())) {
				groupContainer.removeItem(itemId);
			}
		}
	}
	
	
	private boolean isSelectorParent(final String selectorGroupId) {
		boolean result = false;
		
		// get group and selectors and check recursively if groupId is a child
		try {
			UserGroup selector = groupService.getGroupById(selectorGroupId);
			// if selector == null it is no group selector, so we can skip
			if (selector != null) {
				List<Selector> children = selector.getSelectors();
				if (!children.isEmpty()) {
					// selectors are canditates, check against actual userGroup
					for (Selector child : children) {
						if (userGroup.getObjid().equals(child.getContent())) {
							// child is edited user group, therefor selector already is a parent - MATCH
							return true;
						}
						else if (isSelectorParent(child.getContent())) {
							// any children selector has matched 
							return true;
						}
					}
				}
			}
		} catch (EscidocClientException e) {
			LOG.error(ViewConstants.AN_UNEXPECTED_ERROR_OCCURED_SEE_LOG_FOR_DETAILS, e);
			ModalDialog.show(mainWindow, e);
		}
		
		return result;
	}
	
	
	private List<Object> getAssignedSelectors() {
		List<Object> assignedSelectors = new ArrayList<Object>();
		for (Object itemId : selectorsInternal.getItemIds()) {
			// TODO replace "content" with PropertyId.Variable
			assignedSelectors.add(selectorsInternal.getItem(itemId).getItemProperty("content").getValue());
		}
		return assignedSelectors;
	}
	
	
	private void bindUsers() {
		users.setContainerDataSource(userContainer);
		users.sort();
	}


	private void bindGroups() {
		groups.setContainerDataSource(groupContainer);
		groups.sort();
	}


	private void showModalWindow() {
		mainWindow.addWindow(modalWindow);
	}
	
	
	public Map<String, List<Item>> getSelected() {
		Map<String, List<Item>> selected = new HashMap<String, List<Item>>();
		
		selected.put(InternalSelectorName.USER_ACCOUNT.getXmlValue(), getSelectedUsers());
		selected.put(InternalSelectorName.USER_GROUP.getXmlValue(), getSelectedGroups());
		return selected;
	}
	
	
	@SuppressWarnings("unchecked")
	private List<Item> getSelectedUsers() {
		Collection<Object> userValue = (Collection<Object>) users.getValue();
		List<Item> selectedUsers = new ArrayList<Item>();
		for (Object objectId : userValue) {
			selectedUsers.add(users.getItem(objectId));
		}
		
		return selectedUsers;
	}
	
	
	@SuppressWarnings("unchecked")
	private List<Item> getSelectedGroups() {
		Collection<Object> groupValue = (Collection<Object>) groups.getValue();
		List<Item> selectedGroups = new ArrayList<Item>();
		
		for (Object objectId : groupValue) {
			selectedGroups.add(groups.getItem(objectId));
		}
		
		return selectedGroups;	
	}
	
}
