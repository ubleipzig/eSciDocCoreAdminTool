package de.uni_leipzig.ubl.admintool.view.group.selector;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.vaadin.data.Item;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.Notification;

import de.escidoc.admintool.app.AdminToolApplication;
import de.escidoc.admintool.app.PropertyId;
import de.escidoc.admintool.view.ModalDialog;
import de.escidoc.admintool.view.ViewConstants;
import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;
import de.escidoc.core.resources.aa.usergroup.Selector;
import de.escidoc.core.resources.aa.usergroup.SelectorType;
import de.escidoc.core.resources.aa.usergroup.UserGroup;
import de.uni_leipzig.ubl.admintool.service.internal.GroupService;

public class AddInternalSelectorButtonListener implements ClickListener {

	private static final long serialVersionUID = -932605445557648169L;
	
	private static final Logger LOG = LoggerFactory.getLogger(AddInternalSelectorButtonListener.class);
	
	private final AdminToolApplication app;
	
	private final Window mainWindow;
	
	private final Window modalWindow;
	
	private final GroupService groupService;
	
	private final AddInternalSelector addInternalSelector;
	
	private UserGroup userGroup;
	
	private Map<String, List<Item>> selectedItems;
	
	private UserGroup updatedUserGroup;
	
	public AddInternalSelectorButtonListener(final AdminToolApplication app, final Window mainWindow, final Window modalWindow, final GroupService groupService, final AddInternalSelector addInternalSelector) {
		Preconditions.checkNotNull(app, "app is null: %s", app);
		Preconditions.checkNotNull(mainWindow, "mainWindow is null: %s", mainWindow);
		Preconditions.checkNotNull(modalWindow, "modalWindow is null: %s", modalWindow);
		Preconditions.checkNotNull(groupService, "groupService is null: %s", groupService);
		Preconditions.checkNotNull(addInternalSelector, "addInternal Selector is null: %s", addInternalSelector);
		
		this.app = app;
		this.mainWindow = mainWindow;
		this.modalWindow = modalWindow;
		this.groupService = groupService;
		this.addInternalSelector = addInternalSelector;
	}
	
	@Override
	public void buttonClick(ClickEvent event) {
		selectedItems = addInternalSelector.getSelected();
		
		System.out.println("---------------------------------------------");
		System.out.println("internal Selectors (" + selectedItems + ") will be added ...");
		List<Selector> newSelectors = createSelectorsList();
		System.out.println("---------------------------------------------");
		System.out.println("SELECTORLIST: " + newSelectors);
		System.out.println("---------------------------------------------");
		System.out.println("Selectors will be send to groupService ...");
		try {
			this.userGroup = groupService.getGroupById(app.getGroupView().getSelectedItem().getItemProperty(PropertyId.OBJECT_ID).getValue().toString());
			updatedUserGroup = groupService.addSelectors(userGroup, newSelectors);
			System.out.println("---------------------------------------------");
			System.out.println("Selectors updated!");
			
			// close modal Window and update EditFormView
			updateView(updatedUserGroup);
			System.out.println("View updated!");
			closeWindow();
			System.out.println("ModalWindow closed!");
			showMessage();
			System.out.println("Message shown to user!");
		} catch (final EscidocException e) {
			LOG.error(ViewConstants.AN_UNEXPECTED_ERROR_OCCURED_SEE_LOG_FOR_DETAILS, e);
			ModalDialog.show(mainWindow, e);
		} catch (final InternalClientException e) {
			LOG.error(ViewConstants.AN_UNEXPECTED_ERROR_OCCURED_SEE_LOG_FOR_DETAILS, e);
			ModalDialog.show(mainWindow, e);
		} catch (final TransportException e) {
			LOG.error(ViewConstants.AN_UNEXPECTED_ERROR_OCCURED_SEE_LOG_FOR_DETAILS, e);
			ModalDialog.show(mainWindow, e);
		} catch (final EscidocClientException e) {
			LOG.error(ViewConstants.AN_UNEXPECTED_ERROR_OCCURED_SEE_LOG_FOR_DETAILS, e);
			ModalDialog.show(mainWindow, e);
		}
		
	}

	
	private List<Selector> createSelectorsList() {
		final String userAccountConstant = InternalSelectorName.USER_ACCOUNT.getXmlValue();
		final String userGroupConstant = InternalSelectorName.USER_GROUP.getXmlValue();
		
		List<Selector> newSelectors = new ArrayList<Selector>();
		
		// create user account selectors and add to list
		for (Item item : selectedItems.get(userAccountConstant)) {
			newSelectors.add(itemToSelector(item, userAccountConstant));
		}
		// create user group selectors and add to list
		for (Item item : selectedItems.get(userGroupConstant)) {
			newSelectors.add(itemToSelector(item, userGroupConstant));
		}

		return newSelectors;
	}
	
	
	private Selector itemToSelector(final Item item, final String internalSelectorName) {
		final Selector bakedSelector = 
				new Selector(item.getItemProperty(PropertyId.OBJECT_ID).getValue().toString(), internalSelectorName, SelectorType.INTERNAL);
		return bakedSelector;
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
