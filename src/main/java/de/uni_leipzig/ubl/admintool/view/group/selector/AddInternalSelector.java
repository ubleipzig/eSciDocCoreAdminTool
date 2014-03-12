package de.uni_leipzig.ubl.admintool.view.group.selector;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

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
import de.escidoc.admintool.view.ViewConstants;
import de.escidoc.admintool.view.context.listener.CancelButtonListener;
import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.resources.aa.useraccount.UserAccount;
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
	
	private UserGroup userGroup;
	
	private Window mainWindow;
	
	private Window modalWindow;
	
	private POJOContainer<UserAccount> userContainer;
	
	private POJOContainer<UserGroup> groupContainer;
	
	private final Table users = new Table();
	
	private final Table groups = new Table();
	
	private final Button okButton = new Button(ViewConstants.OK_LABEL);
	
	private final Button cancelButton = new Button(ViewConstants.CANCEL_LABEL);
	
	
	
	public AddInternalSelector(final AdminToolApplication app, final GroupService groupService) {
		Preconditions.checkNotNull(app, "app is null: %s", app);
		Preconditions.checkNotNull(groupService, "groupService is null: %s", groupService);
		
		this.app = app;
		this.userService = app.getUserService();
		this.groupService = groupService;
	}


	@Override
	public void buttonClick(ClickEvent event) {
		init();
		bindData();
		createModalWindow();
		showModalWindow();
	}


	private void init() {
		final Collection<UserAccount> userList;
		final Collection<UserGroup> groupList;
		
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
		okButton.addListener(new AddInternalSelectorButtonListener(app, this));
		cancelButton.addListener(new CancelButtonListener(mainWindow, modalWindow));
		footer.addComponent(okButton);
		footer.addComponent(cancelButton);
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
		// TODO Auto-generated method stub
		modalWindow = new Window();
		modalWindow.setModal(true);
		modalWindow.setCaption("Select Internal Selectors");
		modalWindow.setHeight("50%");
		modalWindow.setWidth("50%");
		
		try {
			this.userGroup = groupService.getGroupById(app.getGroupView().getSelectedItem().getItemProperty(PropertyId.OBJECT_ID).toString());
		} catch (EscidocClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.mainWindow = app.getMainWindow();
		
		modalWindow.addComponent(tables);
		modalWindow.addComponent(footer);
	}


	private void bindData() {
		bindUsers();
		bindGroups();
	}
	
	
	private void bindUsers() {
		users.setContainerDataSource(userContainer);
		// TODO init some sorting of user-accounts
	}


	private void bindGroups() {
		groups.setContainerDataSource(groupContainer);
		groups.sort();
	}


	private void showModalWindow() {
		mainWindow.addWindow(modalWindow);
	}
	
	
	public List<Item> getSelected() {
		List<Item> selected = new ArrayList<Item>();
		
		selected.addAll(getSelectedUsers());
		selected.addAll(getSelectedGroups());
		
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
