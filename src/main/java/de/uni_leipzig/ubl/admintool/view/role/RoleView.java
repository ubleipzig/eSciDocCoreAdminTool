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
 * Copyright 2011 Fachinformationszentrum Karlsruhe Gesellschaft
 * fuer wissenschaftlich-technische Information mbH and Max-Planck-
 * Gesellschaft zur Foerderung der Wissenschaft e.V.
 * All rights reserved.  Use is subject to license terms.
 */
package de.uni_leipzig.ubl.admintool.view.role;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.vaadin.data.util.POJOContainer;
import com.vaadin.terminal.UserError;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.Notification;
import com.vaadin.ui.themes.Reindeer;
import com.vaadin.ui.themes.Runo;

import de.escidoc.admintool.app.AdminToolApplication;
import de.escidoc.admintool.app.PropertyId;
import de.escidoc.admintool.service.ServiceContainer;
import de.escidoc.admintool.service.internal.ContextService;
import de.escidoc.admintool.service.internal.RoleService;
import de.escidoc.admintool.service.internal.UserService;
import de.escidoc.admintool.view.ViewConstants;
import de.escidoc.admintool.view.admintask.ResourceType;
import de.escidoc.admintool.view.util.dialog.ErrorDialog;
import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.client.exceptions.application.notfound.RoleNotFoundException;
import de.escidoc.core.resources.aa.role.Role;
import de.escidoc.core.resources.aa.useraccount.UserAccount;
import de.escidoc.core.resources.aa.usergroup.UserGroup;
import de.escidoc.core.resources.common.reference.ContextRef;
import de.escidoc.core.resources.om.context.Context;
import de.uni_leipzig.ubl.admintool.service.internal.GroupService;

public class RoleView extends CustomComponent {

	private static final long serialVersionUID = 3817713626985667626L;

	private static final Logger LOG = LoggerFactory.getLogger(RoleView.class);

    private static final int RESOURCE_SELECTION_HEIGHT_IN_INTEGER = 400;

    private static final String RESOURCE_SELECTION_HEIGHT = RESOURCE_SELECTION_HEIGHT_IN_INTEGER + "px";

    private static final int COMPONENT_WIDTH_IN_INTEGER = 300;

    private static final String COMPONENT_WIDTH = COMPONENT_WIDTH_IN_INTEGER + "px";
    
    private static final int WINDOW_WIDTH_IN_INTEGER = 480;
    
    private static final String WINDOW_WIDTH = WINDOW_WIDTH_IN_INTEGER + "px";
    
    private static final int WINDOW_HEIGHT_IN_INTEGER = 650;
    
    private static final String WINDOW_HEIGHT = WINDOW_HEIGHT_IN_INTEGER + "px";
    
    private final NativeSelect userSelection = new NativeSelect(ViewConstants.USER_NAME);

    private final NativeSelect groupSelection = new NativeSelect(ViewConstants.NAME_LABEL);

    private final NativeSelect roleSelection = new NativeSelect(ViewConstants.SELECT_ROLE_LABEL);

    private final NativeSelect resourcetypeSelection = new NativeSelect(ViewConstants.RESOURCE_TYPE);

    final ListSelect resourceResult = new ListSelect();

    private final Panel panel = new Panel();
    
    private final VerticalLayout verticalLayout = new VerticalLayout();
    
    private final HorizontalLayout footer = new HorizontalLayout();

    private final Button saveBtn = new Button(ViewConstants.SAVE_LABEL, new SaveBtnListener());
    
    private final Button cancelBtn = new Button(ViewConstants.CANCEL, new CancelBtnListener());

    private final VerticalLayout footerLayout = new VerticalLayout();

    final Window mainWindow;
    
    final Window modalWindow = new Window();

    private final ComponentContainer mainLayout = new FormLayout();

    final VerticalLayout resourceContainer = new VerticalLayout();

    final TextField searchBox = new TextField("Resource Title: ");

    private final Button searchButton = new Button(ViewConstants.SEARCH_LABEL);

    private final ContextService contextService;

    private final AdminToolApplication app;

    private final RoleService roleService;

    final UserService userService;
    
    final GroupService groupService;
    
    private UserAccount selectedUser;
    
    private UserGroup selectedGroup;
    
    private ResourceType resourceType;

    private POJOContainer<UserAccount> userContainer;
    
    private POJOContainer<UserGroup> groupContainer;

    final ServiceContainer serviceContainer;

    // TODO: add logged in user;
    public RoleView(final AdminToolApplication app, final RoleService roleService, final UserService userService,
        final GroupService groupService, final ContextService contextService, final ServiceContainer serviceContainer, 
        final ResourceType resourceType) {
        Preconditions.checkNotNull(app, "app is null: %s", app);
        Preconditions.checkNotNull(roleService, "roleService is null: %s", roleService);
        Preconditions.checkNotNull(userService, "userService is null: %s", userService);
        Preconditions.checkNotNull(groupService, "groupService is null: %s", groupService);
        Preconditions.checkNotNull(contextService, "contextService is null: %s", contextService);
        Preconditions.checkNotNull(serviceContainer, "serviceContainer is null: %s", serviceContainer);
        Preconditions.checkNotNull(resourceType, "resourceType is null: %s", resourceType);
        this.app = app;
        this.roleService = roleService;
        this.userService = userService;
        this.groupService = groupService;
        this.contextService = contextService;
        this.serviceContainer = serviceContainer;
        this.resourceType = resourceType;
        mainWindow = app.getMainWindow();
        bindData();
    }

    public void init() {
        configure();
    	initLayout();
        if (resourceType.equals(ResourceType.USERGROUP)) {
        	addGroupField();
        }
        else {
        	addUserField();
        }
        addRoleField();
        addResourceType();
        addResourceSearchBox();
        addResourceSelection();
        addFooter();
    }
    
    public void show() {
    	mainWindow.addWindow(modalWindow);
    }
    
    private void configure() {
    	if (resourceType.equals(ResourceType.USERGROUP)) {
    		modalWindow.setCaption("Add Role to User Group");
    	}
    	else {
    		modalWindow.setCaption("Add Role to User Account");
    	}
    	
    	modalWindow.setModal(true);
    	modalWindow.setWidth(WINDOW_WIDTH);
    	modalWindow.setHeight(WINDOW_HEIGHT);
    	modalWindow.setContent(this);    	
    }

    private void initLayout() {
        setCompositionRoot(panel);
        panel.setStyleName(Runo.PANEL_LIGHT);
        panel.setSizeFull();
        setSizeFull();
        verticalLayout.setWidth("100%");
        mainLayout.setWidth(400, UNITS_PIXELS);

        panel.setContent(verticalLayout);
//        panel.setCaption(ViewConstants.CAPTION);

        // TODO how to make panel take the whole vertical screen, if it does not
        // contain any child component;
        verticalLayout.setSpacing(true);
        verticalLayout.setMargin(true, false, false, true);
        verticalLayout.addComponent(mainLayout);
    }

    private void addUserField() {
        userSelection.setWidth(COMPONENT_WIDTH);
        userSelection.setNullSelectionAllowed(false);
        userSelection.setMultiSelect(false);
        userSelection.setRequired(true);
        userSelection.setNewItemsAllowed(false);
        userSelection.setImmediate(true);
        userSelection.setRequiredError("User is required");
    	userSelection.setReadOnly(true);
        mainLayout.addComponent(userSelection);
    }

    private void addGroupField() {
    	groupSelection.setWidth(COMPONENT_WIDTH);
    	groupSelection.setNullSelectionAllowed(false);
    	groupSelection.setMultiSelect(false);
    	groupSelection.setRequired(true);
    	groupSelection.setNewItemsAllowed(false);
    	groupSelection.setImmediate(true);
    	groupSelection.setReadOnly(true);
    	groupSelection.setRequiredError("User is required");
    	mainLayout.addComponent(groupSelection);
    }
    
    private void addRoleField() {
        roleSelection.setWidth(COMPONENT_WIDTH);
        roleSelection.setNullSelectionAllowed(false);
        roleSelection.setImmediate(true);
        roleSelection.setRequired(true);
        roleSelection.addListener(new RoleSelectListener(resourcetypeSelection, searchBox, searchButton, saveBtn,
            resourceResult));
        mainLayout.addComponent(roleSelection);
    }

    private void addResourceType() {
        resourcetypeSelection.setEnabled(false);
        resourcetypeSelection.setWidth(COMPONENT_WIDTH);
        resourcetypeSelection.setImmediate(true);
        resourcetypeSelection.setNullSelectionAllowed(false);
        mainLayout.addComponent(resourcetypeSelection);
    }

    private void addResourceSearchBox() {
        searchBox.setWidth(Integer.toString(3 / 2 * COMPONENT_WIDTH_IN_INTEGER) + "px");
        searchBox.setEnabled(false);
        searchButton.setEnabled(false);
        searchBox.setReadOnly(true);
        mainLayout.addComponent(searchBox);
        searchButton.addListener(new SearchBtnListener());
    }

    private void addResourceSelection() {
        resourceResult.setSizeFull();
        resourceResult.setHeight(RESOURCE_SELECTION_HEIGHT);
        resourceResult.setImmediate(true);

        resourceResult.addListener(new ResourceSelectionListener(this));
        resourceContainer.setStyleName(Reindeer.PANEL_LIGHT);
        resourceContainer.setWidth(Integer.toString(3 / 2 * COMPONENT_WIDTH_IN_INTEGER) + "px");
        resourceContainer.setHeight(RESOURCE_SELECTION_HEIGHT);
        mainLayout.addComponent(resourceContainer);
    }

    private void addFooter() {
        footer.addComponent(saveBtn);
        footer.addComponent(cancelBtn);
        footerLayout.addComponent(footer);
        footerLayout.setComponentAlignment(footer, Alignment.MIDDLE_RIGHT);
        saveBtn.setVisible(false);
        mainLayout.addComponent(footerLayout);
    }

    private void bindData() {
    	if (resourceType.equals(ResourceType.USERGROUP)) {
        	bindUserGroupData();
        }
        else {
        	bindUserAccountData();
        }
        bindRoleData();
        bindResourceTypeData();
    }

    private void bindUserGroupData() {
    	groupContainer = new POJOContainer<UserGroup>(UserGroup.class, PropertyId.NAME);
    	for (final UserGroup group : getAllUserGroups()) {
    		groupContainer.addPOJO(group);
    	}
    	groupContainer.sort(new String[] {PropertyId.NAME}, new boolean[] {true});
    	groupSelection.setContainerDataSource(groupContainer);
    	groupSelection.setItemCaptionPropertyId(PropertyId.NAME);
	}

	private void bindUserAccountData() {
        userContainer = new POJOContainer<UserAccount>(UserAccount.class, PropertyId.NAME);
        for (final UserAccount user : getAllUserAccounts()) {
            userContainer.addPOJO(user);
        }
        userContainer.sort(new String[] {PropertyId.NAME}, new boolean[] {true});
        userSelection.setContainerDataSource(userContainer);
        userSelection.setItemCaptionPropertyId(PropertyId.NAME);
    }

    private void bindRoleData() {
        final POJOContainer<Role> roleContainer =
            new POJOContainer<Role>(Role.class, PropertyId.OBJECT_ID, PropertyId.NAME);
        for (final Role role : getAllRoles()) {
            final String roleName = role.getProperties().getName();
        	if (notStatistic(roleName) && notContentRelation(roleName)
        			&& notAudience(roleName) && notOrgUnitAdmint(roleName)) {
                roleContainer.addPOJO(role);
            }
        }
        roleContainer.sort(new Object[] {PropertyId.NAME}, new boolean[] {true});
        roleSelection.setContainerDataSource(roleContainer);
        roleSelection.setItemCaptionPropertyId(PropertyId.NAME);
    }

    private boolean notContentRelation(final String roleName) {
        return !roleName.startsWith("ContentRelation");
    }

    private boolean notOrgUnitAdmint(final String roleName) {
        return !roleName.startsWith("OU-Admin");
    }

    private boolean notAudience(final String roleName) {
        return !roleName.startsWith("Audience");
    }

    private boolean notStatistic(final String roleName) {
        return !roleName.startsWith("Statistics");
    }

    private void bindResourceTypeData() {
        resourcetypeSelection.addListener(new ResourceTypeListener(this));
    }

    Collection<Context> getAllContexts() {
        try {
            return contextService.getCache();
        }
        catch (final EscidocClientException e) {
            mainWindow.addWindow(new ErrorDialog(mainWindow, ViewConstants.ERROR_DIALOG_CAPTION, e.getMessage()));
        }
        return Collections.emptyList();
    }

    private List<Role> getAllRoles() {
        try {
            return (List<Role>) roleService.findAll();
        }
        catch (final EscidocClientException e) {
            mainWindow.addWindow(new ErrorDialog(mainWindow, ViewConstants.ERROR_DIALOG_CAPTION, e.getMessage()));
        }
        return Collections.emptyList();
    }

    private Collection<UserAccount> getAllUserAccounts() {
        try {
            return userService.findAll();
        }
        catch (final EscidocClientException e) {
            mainWindow.addWindow(new ErrorDialog(mainWindow, ViewConstants.ERROR_DIALOG_CAPTION, e.getMessage()));
        }
        return Collections.emptyList();
    }

    private Collection<UserGroup> getAllUserGroups() {
        try {
            return groupService.findAll();
        }
        catch (final EscidocClientException e) {
            mainWindow.addWindow(new ErrorDialog(mainWindow, ViewConstants.ERROR_DIALOG_CAPTION, e.getMessage()));
        }
        return Collections.emptyList();
    }
    
    public void selectUser(final UserAccount userAccount) {
        if (resourceType.equals(ResourceType.USERACCOUNT)) {
	    	selectedUser = userAccount;
	        userSelection.select(userAccount);
        }
    }
    
    public void selectGroup(final UserGroup userGroup) {
    	if (resourceType.equals(ResourceType.USERGROUP)) {
	    	selectedGroup = userGroup;
	    	groupSelection.select(userGroup);
    	}
    }

    private void closeWindow() {
		mainWindow.removeWindow(modalWindow);
	}

	private void showMessage() {
		String message = "Role is updated on " + resourceType.getLabel();
		mainWindow.showNotification(new Notification("Info", message, Notification.TYPE_TRAY_NOTIFICATION));
	}

	private class SaveBtnListener implements Button.ClickListener {

		private static final long serialVersionUID = 1385240583604960734L;

		@Override
        public void buttonClick(final ClickEvent event) {
            onSaveClick(); 
        }

        // FIXME !!! split action by mode 
        private void onSaveClick() {
            if (getSelectedUser() != null || getSelectedGroup() != null) {
                assignRole();
            }
            else if (resourceType.equals(ResourceType.USERACCOUNT)) {
                userSelection.setComponentError(new UserError("User is required"));
            }
            else {
            	groupSelection.setComponentError(new UserError("Group is required"));
            }
        }

        private void assignRole() {
            try {
                if (resourceType.equals(ResourceType.USERACCOUNT)) {
	            	userService
	                    .assign(getSelectedUser()).withRole(getSelectedRole()).onResources(getSelectedResources())
	                    .execute();
	                app.showUser(getSelectedUser());
                } 
                else if (resourceType.equals(ResourceType.USERGROUP)) {
                	groupService
                		.assign(getSelectedGroup()).withRole(getSelectedRole()).onResources(getSelectedResources())
                		.execute();
                	app.showGroup(getSelectedGroup());
                }
                closeWindow();
                showMessage();
            }
            catch (final RoleNotFoundException e) {
                app.getMainWindow().addWindow(
                    new ErrorDialog(app.getMainWindow(), ViewConstants.ERROR_DIALOG_CAPTION,
                        ViewConstants.REQUESTED_ROLE_HAS_NO_SCOPE_DEFINITIONS));
                LOG.error("An unexpected error occured! See LOG for details.", e);
            }
            catch (final EscidocClientException e) {
                app.getMainWindow().addWindow(
                    new ErrorDialog(app.getMainWindow(), ViewConstants.ERROR_DIALOG_CAPTION, e.getMessage()));
                LOG.error("An unexpected error occured! See LOG for details.", e);
            }
        }

        private UserAccount getSelectedUser() {
            if (selectedUser == null) {
                return (UserAccount) userSelection.getValue();
            }
            return selectedUser;
        }
        
        private UserGroup getSelectedGroup() {
        	if (selectedGroup == null) {
        		return (UserGroup) groupSelection.getValue();
        	}
        	return selectedGroup;
        }

        private Role getSelectedRole() {
            final Object value = roleSelection.getValue();
            if (value instanceof Role) {
                return (Role) value;
            }
            return new Role();
        }

        private Set<ContextRef> getSelectedResources() {
            final Object value = resourceResult.getValue();
            if (value instanceof Context) {
                return Collections.singleton(new ContextRef(((Context) value).getObjid()));
            }
            return Collections.emptySet();
        }
        
    }

    private class CancelBtnListener implements Button.ClickListener {

        private static final long serialVersionUID = -5938771331937438272L;

        @Override
        public void buttonClick(final ClickEvent event) {
            onCancelClick();
        }

        private void onCancelClick() {
            closeWindow();
        }
    }

    private class SearchBtnListener implements Button.ClickListener {

		private static final long serialVersionUID = -5195195105140856995L;
		private Collection<Context> foundContexts;

        @Override
        public void buttonClick(final ClickEvent event) {
            onSearchClick(event);
        }

        private void onSearchClick(final ClickEvent event) {
            final Object value = searchBox.getValue();

            if (value instanceof String) {
                // TODO search resource with type:[resourceType] and
                // title:[userInput] OR objectID:[userInput]
                final String userInput = (String) value;
                foundContexts = searchContextByName(userInput);
                if (isContextFound()) {
                    mainWindow.showNotification(foundContexts.iterator().next().getObjid());
                }
                mainWindow.showNotification("Not found");
            }
        }

        private boolean isContextFound() {
            return !foundContexts.isEmpty();
        }

        private Collection<Context> searchContextByName(final String userInput) {
            try {
                return contextService.findByTitle(userInput);
            }
            catch (final EscidocClientException e) {
                app.getMainWindow().addWindow(
                    new ErrorDialog(app.getMainWindow(), ViewConstants.ERROR_DIALOG_CAPTION,
                        "An unexpected error occured! See LOG for details."));
                LOG.error("An unexpected error occured! See LOG for details.", e);
            }
            return Collections.emptyList();
        }
    }
}