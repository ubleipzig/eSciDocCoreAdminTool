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

import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.data.util.POJOItem;
import com.vaadin.data.validator.EmailValidator;
import com.vaadin.terminal.UserError;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Window.Notification;
import com.vaadin.ui.themes.Reindeer;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;

import de.escidoc.admintool.app.AdminToolApplication;
import de.escidoc.admintool.domain.PdpRequest;
import de.escidoc.admintool.service.internal.UserService;
import de.escidoc.admintool.view.ErrorMessage;
import de.escidoc.admintool.view.ViewConstants;
import de.escidoc.admintool.view.navigation.ActionIdConstants;
import de.escidoc.admintool.view.user.UserAddView;
import de.escidoc.admintool.view.user.UserListView;
import de.escidoc.admintool.view.util.LayoutHelper;
import de.escidoc.admintool.view.validator.EmptyFieldValidator;
import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;
import de.escidoc.core.client.exceptions.application.security.AuthorizationException;
import de.escidoc.core.client.exceptions.application.violated.UniqueConstraintViolationException;
import de.escidoc.core.resources.aa.usergroup.UserGroup;
import de.uni_leipzig.ubl.admintool.service.internal.GroupService;

@SuppressWarnings("serial")
public class GroupAddView extends CustomComponent implements ClickListener {

	private static final Logger LOG = LoggerFactory.getLogger(UserAddView.class);
	
	private static final int LEFT_MARGIN = 111;
	
    private final Panel panel = new Panel();

    private final FormLayout form = new FormLayout();

    private final HorizontalLayout footer = new HorizontalLayout();

    private final Button saveButton = new Button(ViewConstants.SAVE_LABEL, this);

    private final Button cancelButton = new Button(ViewConstants.CANCEL, this);

    private final GroupListView groupListView;

    private final GroupService groupService;
    
    private final PdpRequest pdpRequest;

    final AdminToolApplication app;
    
    // fields
    
    private TextField name;
    
    private TextField label;
    
    private TextArea description;
    
    private TextField email;
    
    // properties
    
    private ObjectProperty<String> nameProperty;
    
    private ObjectProperty<String> labelProperty;
    
    private ObjectProperty<String> descriptionProperty;
    
    private ObjectProperty<String> emailProperty;
    
    public GroupAddView(final AdminToolApplication app, final GroupService groupService, final PdpRequest pdpRequest, final GroupListView groupListView) {
    	Preconditions.checkNotNull(app, "app is null: %s", app);
    	Preconditions.checkNotNull(groupService, "groupService is null: %s", groupService);
    	Preconditions.checkNotNull(pdpRequest, "pdpRequest is null: %s", pdpRequest);
    	Preconditions.checkNotNull(groupListView, "groupListView is null: %s", groupListView);
    	
    	this.app = app;
    	this.groupService = groupService;
    	this.pdpRequest = pdpRequest;
    	this.groupListView = groupListView;
    }
    
    public void init() {
    	configureLayout();
    	addName();
    	addLabel();
    	addDescription();
    	addEmail();
    	addSpace();
    	addFooter();
    }

    private void configureLayout() {
        setCompositionRoot(panel);

        panel.setStyleName(Reindeer.PANEL_LIGHT);
        panel.setCaption(ViewConstants.GROUP_ADD_VIEW_CAPTION);
        panel.setContent(form);
        panel.setVisible(isCreateNewGroupAllowed());

        form.setSpacing(false);
        form.setWidth(520, UNITS_PIXELS);
    }

    
    private void addSpace() {
        form.addComponent(new Label("<br/>", Label.CONTENT_XHTML));
        form.addComponent(new Label("<br/>", Label.CONTENT_XHTML));
    }

    
    private void addName() {
    	nameProperty = new ObjectProperty<String>(ViewConstants.EMPTY_STRING, String.class);
    	name = new TextField(nameProperty);
    	name.setMaxLength(ViewConstants.MAX_TITLE_LENGTH);
    	name.setWidth(ViewConstants.FIELD_WIDTH);
    	form.addComponent(LayoutHelper.create(ViewConstants.NAME_LABEL, name, LEFT_MARGIN, true));
    }

    private void addLabel() {
    	labelProperty = new ObjectProperty<String>(ViewConstants.EMPTY_STRING, String.class);
    	label = new TextField(labelProperty);
    	label.setMaxLength(ViewConstants.MAX_TITLE_LENGTH);
    	label.setWidth(ViewConstants.FIELD_WIDTH);
    	form.addComponent(LayoutHelper.create(ViewConstants.LABEL, label, LEFT_MARGIN, true));
    }
    
    private void addDescription() {
    	descriptionProperty = new ObjectProperty<String>(ViewConstants.EMPTY_STRING, String.class);
    	description = new TextArea(descriptionProperty);
    	description.setMaxLength(ViewConstants.MAX_DESC_LENGTH);
    	description.setRows(ViewConstants.DESCRIPTION_ROWS);
    	description.setWidth(ViewConstants.FIELD_WIDTH);
    	description.setRequired(false);
    	form.addComponent(LayoutHelper.create(ViewConstants.DESCRIPTION_LABEL, description, LEFT_MARGIN, 60, false));
    }
    
    private void addEmail() {
    	emailProperty = new ObjectProperty<String>(ViewConstants.EMPTY_STRING, String.class);
    	email = new TextField(emailProperty);
    	email.setWidth(ViewConstants.FIELD_WIDTH);
    	email.setRequired(false);
    	email.addValidator(new EmailValidator("Please enter a valid e-mail address."));
    	form.addComponent(LayoutHelper.create(ViewConstants.EMAIL_LABEL, email, LEFT_MARGIN, false));
    }
    
    private void addFooter() {
        footer.setWidth(100, UNITS_PERCENTAGE);

        final HorizontalLayout hLayout = new HorizontalLayout();
        hLayout.addComponent(saveButton);
        hLayout.addComponent(cancelButton);

        footer.addComponent(hLayout);
        footer.setComponentAlignment(hLayout, Alignment.MIDDLE_RIGHT);

        form.addComponent(footer);
    }
    

    private boolean isCreateNewGroupAllowed() {
    	return pdpRequest.isPermitted(ActionIdConstants.CREATE_USER_GROUP);
    }

    
	@Override
	public void buttonClick(ClickEvent event) {
		final Button source = event.getButton();
		if (source.equals(cancelButton)) {
			resetFields();
		}
		else if (source.equals(saveButton)) {
			validateAndSave();
		}
	}
	
	private void validateAndSave() {
		boolean isValid = true;
		isValid = isNameValid();
		isValid &= isLabelValid();
		isValid &= isEmailValid();
		
		if (isValid) {
			trySaveAndUpdateView();
		}
	}

	private boolean isNameValid() {
		return EmptyFieldValidator.isValid(name, "Please enter a " + ViewConstants.NAME_LABEL);
	}

	private boolean isLabelValid() {
		if (EmptyFieldValidator.isValid(label, "Please enter a " + ViewConstants.LABEL)) {
			return isLabelUnique();
		}
		return false;
	}

	private boolean isLabelUnique() {
		final Collection<UserGroup> allUserGroups;
		final String labelValue = label.getValue().toString();
		
		try {
			allUserGroups = groupService.findAll();
			for (UserGroup userGroup : allUserGroups) {
				String existingLabel = userGroup.getProperties().getLabel();
				if (labelValue.trim().equals(existingLabel.trim())) {
					label.setComponentError(null);
					label.setComponentError(new UserError("The Label already exists."));
					return false;
				}
			}
		} catch (EscidocClientException e) {
			ErrorMessage.show(app.getMainWindow(), e);
            LOG.error("An unexpected error occured! See LOG for details.", e);
			return false;
		}
		
		label.setComponentError(null);
		return true;
	}

	private boolean isEmailValid() {
		return email.isValid();
	}

	private void resetFields() {
		name.setComponentError(null);
		label.setComponentError(null);
		description.setComponentError(null);
		email.setComponentError(null);
		name.setValue(ViewConstants.EMPTY_STRING);
		label.setValue(ViewConstants.EMPTY_STRING);
		description.setValue(ViewConstants.EMPTY_STRING);
		email.setValue(ViewConstants.EMPTY_STRING);
	}

	private void trySaveAndUpdateView() {
		try {
			final UserGroup createdUserGroup= createUserGroup();
			final POJOItem<UserGroup> item = groupListView.addGroup(createdUserGroup);
			Preconditions.checkNotNull(item, "Add new user-group to the list failed: %s", item);
			resetFields();
			app.showGroup(createdUserGroup);
			showMessage();
		}
        catch (final EscidocException e) {
            if (e instanceof AuthorizationException) {
                ErrorMessage.show(app.getMainWindow(), "Not Authorized", e);
            }
            else if (e instanceof UniqueConstraintViolationException) {
                label.setComponentError(new UserError(e.getMessage()));
            }
        }
        catch (final InternalClientException e) {
            ErrorMessage.show(app.getMainWindow(), "Not Authorized", e);
            LOG.error("An unexpected error occured! See LOG for details.", e);
        }
        catch (final TransportException e) {
            ErrorMessage.show(app.getMainWindow(), "Not Authorized", e);
            LOG.error("An unexpected error occured! See LOG for details.", e);
        }
	}

	private UserGroup createUserGroup() throws EscidocException, InternalClientException, TransportException {
		final UserGroup createdUserGroup =
				groupService.create(nameProperty.getValue(), labelProperty.getValue(), descriptionProperty.getValue(), emailProperty.getValue());
		if (createdUserGroup != null) {
			LOG.info("User Group »{}« created.", createdUserGroup.getProperties().getName());
		}
		return createdUserGroup;
	}

	private void showMessage() {
		app.getMainWindow().showNotification(
	            new Notification("Info", "User Group is created", Notification.TYPE_TRAY_NOTIFICATION));
	}
	
}
