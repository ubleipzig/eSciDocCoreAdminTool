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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

import de.escidoc.admintool.app.AdminToolApplication;
import de.escidoc.admintool.view.ViewConstants;
import de.escidoc.admintool.view.context.listener.CancelButtonListener;
import de.escidoc.admintool.view.util.LayoutHelper;
import de.escidoc.admintool.view.validator.EmptyFieldValidator;
import de.uni_leipzig.ubl.admintool.service.internal.GroupService;

public class AddAttributeSelector implements ClickListener {

	private static final long serialVersionUID = -5934130000820378294L;

	private static final Logger LOG = LoggerFactory.getLogger(AddInternalSelector.class);

	private static final int LEFT_MARGIN = 70;
	
	private final FormLayout form = new FormLayout();
	
	private final HorizontalLayout footer = new HorizontalLayout();
	
	protected final AdminToolApplication app;
	
	protected final GroupService groupService;
	
	protected final Table selectorsAttribute;
	
	protected Window mainWindow;
	
	protected final Window modalWindow = new Window();
	
	private final TextField name = new TextField();
	
	private final TextField content = new TextField();
	
	protected ObjectProperty<String> nameProperty;
	
	protected ObjectProperty<String> contentProperty;
	
	private final Button okButton = new Button(ViewConstants.OK_LABEL);
	
	private final Button cancelButton = new Button(ViewConstants.CANCEL_LABEL);
	
	private AddAttributeSelectorButtonListener addAttributeSelectorButtonListener;
	
	private CancelButtonListener cancelButtonListener;


	public AddAttributeSelector(final AdminToolApplication app, final GroupService groupService, final Table selectorsAttribute) {
		Preconditions.checkNotNull(app, "app is null: %s", app);
		Preconditions.checkNotNull(groupService, "groupService is null: %s", groupService);
		Preconditions.checkNotNull(selectorsAttribute, "selectorsAttribute is null: %s", selectorsAttribute);
		
		this.app = app;
		this.groupService = groupService;
		this.selectorsAttribute = selectorsAttribute;
		
		createModalWindow();
	}
	
	
	@Override
	public void buttonClick(ClickEvent event) {
		resetFields();
		showModalWindow();
	}


	protected void resetFields() {
		clearFields();
		removeAllError();
	}
	
	protected boolean isValid() {
		boolean isValid = true;
		isValid = EmptyFieldValidator.isValid(name, "Please enter an attribute name.");
		isValid &= EmptyFieldValidator.isValid(content, "Please enter a content value of the user-attribute.");
		return isValid;
	}


	private void createModalWindow() {
		configure();
		addName();
		addContent();
		addSpace();
		addButtons();
	}


	private void configure() {
		this.mainWindow = app.getMainWindow();
		
		modalWindow.setModal(true);
		modalWindow.setCaption("Create Attribute Selectors");
		modalWindow.setHeight("250px");
		modalWindow.setWidth("550px");
		
		modalWindow.addComponent(form);
		modalWindow.addComponent(footer);
	}
	

	private void addSpace() {
		form.addComponent(new Label("<br /><br />", Label.CONTENT_XHTML));
	}
	
	
	private void addName() {
		nameProperty = new ObjectProperty<String>(ViewConstants.EMPTY_STRING, String.class);
		name.setPropertyDataSource(nameProperty);
    	name.setMaxLength(ViewConstants.MAX_TITLE_LENGTH);
    	name.setWidth(ViewConstants.FIELD_WIDTH);
    	name.setImmediate(true);
    	form.addComponent(LayoutHelper.create(ViewConstants.NAME_LABEL, name, LEFT_MARGIN, true));
	}


	private void addContent() {
		contentProperty = new ObjectProperty<String>(ViewConstants.EMPTY_STRING, String.class);
		content.setPropertyDataSource(contentProperty);
    	content.setMaxLength(ViewConstants.MAX_TITLE_LENGTH);
    	content.setWidth(ViewConstants.FIELD_WIDTH);
    	content.setImmediate(true);
    	form.addComponent(LayoutHelper.create("content", content, LEFT_MARGIN, true));
	}


	private void addButtons() {
		createButtonListenerIfNotSet();
		okButton.addListener(addAttributeSelectorButtonListener);
		cancelButton.addListener(cancelButtonListener);
		footer.addComponent(okButton);
		footer.addComponent(cancelButton);
		footer.setComponentAlignment(okButton, Alignment.MIDDLE_CENTER);
		footer.setComponentAlignment(cancelButton, Alignment.MIDDLE_CENTER);
	}


	private void createButtonListenerIfNotSet() {
		if (addAttributeSelectorButtonListener == null) {
			this.addAttributeSelectorButtonListener = new AddAttributeSelectorButtonListener(this);
		}
		if (cancelButtonListener == null) {
			this.cancelButtonListener = new CancelButtonListener(mainWindow, modalWindow);
		}
	}


	private void showModalWindow() {
		mainWindow.addWindow(modalWindow);
	}
	
	
	private void clearFields() {
		name.setValue(ViewConstants.EMPTY_STRING);
		content.setValue(ViewConstants.EMPTY_STRING);
	}
	
	
	private void removeAllError() {
		name.setComponentError(null);
		content.setComponentError(null);
	}

}