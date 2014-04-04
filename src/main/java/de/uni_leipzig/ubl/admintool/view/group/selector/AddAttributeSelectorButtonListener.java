package de.uni_leipzig.ubl.admintool.view.group.selector;

import java.util.ArrayList;
import java.util.List;

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
