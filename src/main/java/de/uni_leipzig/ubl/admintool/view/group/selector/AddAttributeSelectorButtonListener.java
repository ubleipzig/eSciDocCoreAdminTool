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
import java.util.List;

import org.apache.commons.logging.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Window.Notification;
import com.vaadin.ui.Window;

import de.escidoc.admintool.app.AdminToolApplication;
import de.escidoc.admintool.app.PropertyId;
import de.escidoc.admintool.view.ModalDialog;
import de.escidoc.admintool.view.ViewConstants;
import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.resources.aa.usergroup.Selector;
import de.escidoc.core.resources.aa.usergroup.SelectorType;
import de.escidoc.core.resources.aa.usergroup.UserGroup;
import de.uni_leipzig.ubl.admintool.service.internal.GroupService;

public class AddAttributeSelectorButtonListener implements ClickListener {

	private static final long serialVersionUID = -9107156873777327263L;
	
	private static final Logger LOG = LoggerFactory.getLogger(AddAttributeSelectorButtonListener.class);
	
	private final AddAttributeSelector addAttributeSelector;
	
	private final AdminToolApplication app;
	
	private final GroupService groupService;
	
	private final Window mainWindow;
	
	private final Window modalWindow;
	
	private final ObjectProperty<String> nameProperty;
	
	private final ObjectProperty<String> contentProperty;
	
	private UserGroup userGroup;
	
	
	public AddAttributeSelectorButtonListener(final AddAttributeSelector addAttributSelector) {
		Preconditions.checkNotNull(addAttributSelector, "addAttributSelector is null: %s", addAttributSelector);
		
		this.addAttributeSelector = addAttributSelector;
		this.app = addAttributSelector.app;
		this.groupService = addAttributSelector.groupService;
		this.mainWindow = addAttributSelector.mainWindow;
		this.modalWindow = addAttributSelector.modalWindow;
		this.nameProperty = addAttributSelector.nameProperty;
		this.contentProperty = addAttributSelector.contentProperty;
	}
	
	
	@Override
	public void buttonClick(ClickEvent event) {
		if (addAttributeSelector.isValid()) {
			
			UserGroup updatedUserGroup;
			Selector newSelector = new Selector(contentProperty.getValue(), nameProperty.getValue(), SelectorType.USER_ATTRIBUTE);
			List<Selector> newSelectors = new ArrayList<Selector>();
			newSelectors.add(newSelector);
			
			try {
				this.userGroup = groupService.getGroupById(app.getGroupView().getSelectedItem().getItemProperty(PropertyId.OBJECT_ID).getValue().toString());
				updatedUserGroup = groupService.addSelectors(userGroup, newSelectors);
				
				updateView(updatedUserGroup);
				closeWindow();
				LOG.info("User-Attribute Selector added to User Group »{}«: {}", updatedUserGroup.getProperties().getName(), newSelectors);
				showMessage();
			} catch (EscidocClientException e) {
				LOG.error(ViewConstants.AN_UNEXPECTED_ERROR_OCCURED_SEE_LOG_FOR_DETAILS, e);
				ModalDialog.show(mainWindow, e);
			}
		}
	}


	private void updateView(final UserGroup updatedUserGroup) {
		try {
			// group map must be updated, because app.showGroup() gets cached group from map
			groupService.findAll();
		} catch (EscidocClientException e) {
			ModalDialog.show(mainWindow, e);
			LOG.error(ViewConstants.AN_UNEXPECTED_ERROR_OCCURED_SEE_LOG_FOR_DETAILS, e);
		}
		app.getGroupView().getGroupList().updateGroup(updatedUserGroup);
		app.showGroup(updatedUserGroup);
	}


	private void closeWindow() {
		mainWindow.removeWindow(modalWindow);
	}


	private void showMessage() {
		app.getMainWindow().showNotification(
	            new Notification("Info", "User Group Selectors are updated", Notification.TYPE_TRAY_NOTIFICATION));
	}
}
