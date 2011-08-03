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
package de.escidoc.admintool.view.role;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.vaadin.data.util.POJOContainer;
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
import com.vaadin.ui.themes.Reindeer;
import com.vaadin.ui.themes.Runo;

import de.escidoc.admintool.app.AdminToolApplication;
import de.escidoc.admintool.app.PropertyId;
import de.escidoc.admintool.service.ServiceContainer;
import de.escidoc.admintool.service.internal.ContextService;
import de.escidoc.admintool.service.internal.RoleService;
import de.escidoc.admintool.service.internal.UserService;
import de.escidoc.admintool.view.ViewConstants;
import de.escidoc.admintool.view.util.dialog.ErrorDialog;
import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.client.exceptions.application.notfound.RoleNotFoundException;
import de.escidoc.core.resources.aa.role.Role;
import de.escidoc.core.resources.aa.useraccount.UserAccount;
import de.escidoc.core.resources.common.reference.ContextRef;
import de.escidoc.core.resources.om.context.Context;

@SuppressWarnings("serial")
public class RoleView extends CustomComponent {

    private static final Logger LOG = LoggerFactory.getLogger(RoleView.class);

    private static final int RESOURCE_SELECTION_HEIGHT_IN_INTEGER = 400;

    private static final String RESOURCE_SELECTION_HEIGHT = RESOURCE_SELECTION_HEIGHT_IN_INTEGER + "px";

    private static final int COMPONENT_WIDTH_IN_INTEGER = 300;

    private static final String COMPONENT_WIDTH = COMPONENT_WIDTH_IN_INTEGER + "px";

    private final Panel panel = new Panel();

    private final VerticalLayout verticalLayout = new VerticalLayout();

    private final NativeSelect userSelection = new NativeSelect(ViewConstants.USER_NAME);

    private final NativeSelect roleSelection = new NativeSelect(ViewConstants.SELECT_ROLE_LABEL);

    private final NativeSelect resourcetypeSelection = new NativeSelect(ViewConstants.RESOURCE_TYPE);

    final ListSelect resouceResult = new ListSelect();

    private final HorizontalLayout footer = new HorizontalLayout();

    private final Button saveBtn = new Button(ViewConstants.SAVE_LABEL, new SaveBtnListener());

    private final VerticalLayout footerLayout = new VerticalLayout();

    final Window mainWindow;

    private final ComponentContainer mainLayout = new FormLayout();

    final VerticalLayout resourceContainer = new VerticalLayout();

    final TextField searchBox = new TextField("Resource Title: ");

    private final Button searchButton = new Button(ViewConstants.SEARCH_LABEL);

    private final ContextService contextService;

    private final AdminToolApplication app;

    private final RoleService roleService;

    private final UserService userService;

    private UserAccount selectedUser;

    private POJOContainer<UserAccount> userContainer;

    final ServiceContainer serviceContainer;

    // TODO: add logged in user;
    public RoleView(final AdminToolApplication app, final RoleService roleService, final UserService userService,
        final ContextService contextService, final ServiceContainer serviceContainer) {
        Preconditions.checkNotNull(app, "app is null: %s", app);
        Preconditions.checkNotNull(roleService, "roleService is null: %s", roleService);
        Preconditions.checkNotNull(userService, "userService is null: %s", userService);
        Preconditions.checkNotNull(contextService, "contextService is null: %s", contextService);
        Preconditions.checkNotNull(serviceContainer, "serviceContainer is null: %s", serviceContainer);
        this.app = app;
        this.roleService = roleService;
        this.userService = userService;
        this.contextService = contextService;
        this.serviceContainer = serviceContainer;
        mainWindow = app.getMainWindow();
        bindData();
    }

    public void init() {
        initLayout();
        addUserField();
        addRoleField();
        addResourceType();
        addResourceSearchBox();
        addResourceSelection();
        addFooter();
    }

    private void initLayout() {
        setCompositionRoot(panel);
        panel.setStyleName(Runo.PANEL_LIGHT);
        panel.setSizeFull();
        setSizeFull();
        verticalLayout.setWidth("100%");
        mainLayout.setWidth(400, UNITS_PIXELS);

        panel.setContent(verticalLayout);
        panel.setCaption(ViewConstants.CAPTION);

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
        mainLayout.addComponent(userSelection);
    }

    private void addRoleField() {
        roleSelection.setWidth(COMPONENT_WIDTH);
        roleSelection.setNullSelectionAllowed(false);
        roleSelection.setImmediate(true);
        roleSelection.setRequired(true);
        roleSelection.addListener(new RoleSelectListener(resourcetypeSelection, searchBox, searchButton));
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
        mainLayout.addComponent(searchBox);
        searchButton.addListener(new SearchBtnListener());
    }

    private void addResourceSelection() {
        resouceResult.setSizeFull();
        resouceResult.setHeight(RESOURCE_SELECTION_HEIGHT);
        resouceResult.setImmediate(true);

        resouceResult.addListener(new ResourceSelectionListener(this));
        resourceContainer.setStyleName(Reindeer.PANEL_LIGHT);
        resourceContainer.setWidth(Integer.toString(3 / 2 * COMPONENT_WIDTH_IN_INTEGER) + "px");
        resourceContainer.setHeight(RESOURCE_SELECTION_HEIGHT);
        mainLayout.addComponent(resourceContainer);
    }

    private void addFooter() {
        footer.addComponent(saveBtn);
        footerLayout.addComponent(footer);
        footerLayout.setComponentAlignment(footer, Alignment.MIDDLE_RIGHT);
        mainLayout.addComponent(footerLayout);
    }

    private void bindData() {
        bindUserAccountData();
        bindRoleData();
        bindResourceTypeData();
    }

    private void bindUserAccountData() {
        userContainer = new POJOContainer<UserAccount>(UserAccount.class, PropertyId.NAME);
        for (final UserAccount user : getAllUserAccounts()) {
            userContainer.addPOJO(user);
        }
        userSelection.setContainerDataSource(userContainer);
        userSelection.setItemCaptionPropertyId(PropertyId.NAME);
    }

    private void bindRoleData() {
        final POJOContainer<Role> roleContainer =
            new POJOContainer<Role>(Role.class, PropertyId.OBJECT_ID, PropertyId.NAME);
        for (final Role role : getAllRoles()) {
            roleContainer.addPOJO(role);
        }
        roleSelection.setContainerDataSource(roleContainer);
        roleSelection.setItemCaptionPropertyId(PropertyId.NAME);
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

    public void selectUser(final UserAccount userAccount) {
        selectedUser = userAccount;
        userSelection.select(userAccount);
    }

    private class SaveBtnListener implements Button.ClickListener {

        private static final long serialVersionUID = -7128599340989436927L;

        @Override
        public void buttonClick(final ClickEvent event) {
            onSaveClick();
        }

        private void onSaveClick() {
            assignRole();
        }

        private void assignRole() {
            try {
                userService
                    .assign(getSelectedUser()).withRole(getSelectedRole()).onResources(getSelectedResources())
                    .execute();
                app.showUser(getSelectedUser());
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

        private Role getSelectedRole() {
            final Object value = roleSelection.getValue();
            if (value instanceof Role) {
                return (Role) value;
            }
            return new Role();
        }

        private Set<ContextRef> getSelectedResources() {
            final Object value = resouceResult.getValue();
            if (value instanceof Context) {
                return Collections.singleton(new ContextRef(((Context) value).getObjid()));
            }
            return Collections.emptySet();
        }
    }

    @SuppressWarnings("unused")
    private static class CancelBtnListener implements Button.ClickListener {

        private static final long serialVersionUID = -5938771331937438272L;

        @Override
        public void buttonClick(final ClickEvent event) {
            onCancelClick();
        }

        private void onCancelClick() {
            // TODO implement cancel behaviour
        }
    }

    private class SearchBtnListener implements Button.ClickListener {

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
                foundContexts = seachContextByName(userInput);
                if (isContextFound()) {
                    mainWindow.showNotification(foundContexts.iterator().next().getObjid());
                }
                mainWindow.showNotification("Not found");
            }
        }

        private boolean isContextFound() {
            return !foundContexts.isEmpty();
        }

        private Collection<Context> seachContextByName(final String userInput) {
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