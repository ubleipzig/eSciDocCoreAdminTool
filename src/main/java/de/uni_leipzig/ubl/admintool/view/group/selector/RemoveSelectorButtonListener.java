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
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Window.Notification;
import com.vaadin.ui.Table;

import de.escidoc.admintool.app.AdminToolApplication;
import de.escidoc.admintool.view.ModalDialog;
import de.escidoc.admintool.view.ViewConstants;
import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;
import de.escidoc.core.resources.aa.usergroup.Selector;
import de.escidoc.core.resources.aa.usergroup.UserGroup;
import de.uni_leipzig.ubl.admintool.service.internal.GroupService;
import de.uni_leipzig.ubl.admintool.view.group.GroupEditForm;

public class RemoveSelectorButtonListener implements ClickListener {

	private static final long serialVersionUID = -6241426568514588169L;
	
	private static final Logger LOG = LoggerFactory.getLogger(RemoveSelectorButtonListener.class);
	
	private final GroupEditForm groupEditForm;
	
	private final Table selectorsTable;
	
	private AdminToolApplication app;
	
	private GroupService groupService;
	
	
	public RemoveSelectorButtonListener(final GroupEditForm groupEditForm, final Table selectorsTable) {
		this.groupEditForm = groupEditForm;
		this.selectorsTable = selectorsTable;
		
	}

	
	@Override
	public void buttonClick(final ClickEvent event) {
		this.app = groupEditForm.getApp();
		this.groupService = groupEditForm.getGroupService();
		final List<Selector> removableSelectors = prepareRemoveSelectors();
		if (!removableSelectors.isEmpty()) {
			UserGroup updatedUserGroup = removeSelectors(removableSelectors);
			if (updatedUserGroup != null) {
				updateView(updatedUserGroup);
				LOG.info("Selectors removed from User Group »{}«: {}", updatedUserGroup.getProperties().getName(), removableSelectors);
				showMessage();
			}
		}
	}


	@SuppressWarnings("unchecked")
	private List<Selector> prepareRemoveSelectors() {
		List<Selector> removableSelectors = new ArrayList<Selector>();
		final Object selectedSelectors = selectorsTable.getValue();
		
		if (selectedSelectors != null) {
			if (selectedSelectors instanceof Set<?> && !((Set<?>)selectedSelectors).isEmpty()) {
				for (Object selector : (Set<Object>) selectedSelectors) {
					removableSelectors.add((Selector)selector);
				}
			}
			else if (selectedSelectors instanceof Selector) {
				removableSelectors.add((Selector) selectedSelectors);
			}
		}
		return removableSelectors;
	}


	private UserGroup removeSelectors(final List<Selector> removableSelectors) {
		try {
			UserGroup updatedUserGroup = groupService.removeSelectors(groupService.getGroupById(groupEditForm.getGroupId()), removableSelectors);
			return updatedUserGroup;
		} 
		catch (EscidocException e) {
			LOG.error(ViewConstants.AN_UNEXPECTED_ERROR_OCCURED_SEE_LOG_FOR_DETAILS, e);
			ModalDialog.show(app.getMainWindow(), e);
		} 
		catch (InternalClientException e) {
			LOG.error(ViewConstants.AN_UNEXPECTED_ERROR_OCCURED_SEE_LOG_FOR_DETAILS, e);
			ModalDialog.show(app.getMainWindow(), e);
		} 
		catch (TransportException e) {
			LOG.error(ViewConstants.AN_UNEXPECTED_ERROR_OCCURED_SEE_LOG_FOR_DETAILS, e);
			ModalDialog.show(app.getMainWindow(), e);
		} 
		catch (EscidocClientException e) {
			LOG.error(ViewConstants.AN_UNEXPECTED_ERROR_OCCURED_SEE_LOG_FOR_DETAILS, e);
			ModalDialog.show(app.getMainWindow(), e);
		}
		return null;
	}


	private void updateView(final UserGroup updatedUserGroup) {
		try {
			// group map must be updated, because app.showGroup() gets cached group from map
			groupService.findAll();
		} catch (EscidocClientException e) {
			ModalDialog.show(app.getMainWindow(), e);
			LOG.error(ViewConstants.AN_UNEXPECTED_ERROR_OCCURED_SEE_LOG_FOR_DETAILS, e);
		}
		app.getGroupView().getGroupList().updateGroup(updatedUserGroup);
		app.showGroup(updatedUserGroup);
	}


	private void showMessage() {
		app.getMainWindow().showNotification(
	            new Notification("Info", "User Group Selectors are updated", Notification.TYPE_TRAY_NOTIFICATION));
	}
}
