package de.escidoc.admintool.view.role;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.POJOContainer;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Reindeer;
import com.vaadin.ui.themes.Runo;

import de.escidoc.admintool.app.AdminToolApplication;
import de.escidoc.admintool.app.PropertyId;
import de.escidoc.admintool.service.ContextService;
import de.escidoc.admintool.service.RoleService;
import de.escidoc.admintool.service.ServiceContainer;
import de.escidoc.admintool.service.UserService;
import de.escidoc.admintool.view.ViewConstants;
import de.escidoc.admintool.view.util.dialog.ErrorDialog;
import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;
import de.escidoc.core.client.exceptions.application.notfound.RoleNotFoundException;
import de.escidoc.core.resources.Resource;
import de.escidoc.core.resources.aa.role.Role;
import de.escidoc.core.resources.aa.useraccount.UserAccount;
import de.escidoc.core.resources.common.reference.ContextRef;
import de.escidoc.core.resources.om.context.Context;

public class RoleView extends CustomComponent {

    private static final long serialVersionUID = -1590899235898433438L;

    private static final Logger LOG = LoggerFactory.getLogger(RoleView.class);

    private static final String SEARCH_LABEL = "Search";

    private static final int RESOURCE_SELECTION_HEIGHT_IN_INTEGER = 400;

    private static final String RESOURCE_SELECTION_HEIGHT =
        RESOURCE_SELECTION_HEIGHT_IN_INTEGER + "px";

    private static final int COMPONENT_WIDTH_IN_INTEGER = 300;

    private static final String COMPONENT_WIDTH = COMPONENT_WIDTH_IN_INTEGER
        + "px";

    private static final String CAPTION = "Role Management";

    private final Panel panel = new Panel();

    private final VerticalLayout verticalLayout = new VerticalLayout();

    private final ComboBox userComboBox = new ComboBox("User Name:");

    private final ComboBox roleComboBox = new ComboBox("Role:");

    private final ComboBox resourceTypeComboBox = new ComboBox("Resouce Type:");

    private final ListSelect resouceResult = new ListSelect();

    // TODO refactor all footer to a class.
    private final HorizontalLayout footer = new HorizontalLayout();

    private final Button saveBtn = new Button(ViewConstants.SAVE,
        new SaveBtnListener());

    private final Button cancelBtn = new Button(ViewConstants.CANCEL,
        new CancelBtnListener());

    private final Window mainWindow;

    private final ComponentContainer mainLayout = new FormLayout();

    private final VerticalLayout resourceContainer = new VerticalLayout();

    private final TextField searchBox = new TextField("Resource Title: ");

    private final Button searchButton = new Button(SEARCH_LABEL);

    private final ContextService contextService;

    private final AdminToolApplication app;

    private final RoleService roleService;

    private final UserService userService;

    private UserAccount selectedUser;

    private POJOContainer<UserAccount> userContainer;

    private final ServiceContainer serviceContainer;

    // TODO: add logged in user;
    public RoleView(final AdminToolApplication app,
        final RoleService roleService, final UserService userService,
        final ContextService contextService,
        final ServiceContainer serviceContainer) {

        if (app == null || roleService == null || userService == null
            || contextService == null) {
            throw new IllegalArgumentException(
                "Constructor arguments can not be null.");
        }
        this.app = app;
        this.roleService = roleService;
        this.userService = userService;
        this.contextService = contextService;

        this.serviceContainer = serviceContainer;

        mainWindow = app.getMainWindow();
        init();
        bindData();
    }

    private void init() {
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
        setSizeFull();
        mainLayout.setWidth(400, UNITS_PIXELS);

        panel.setContent(verticalLayout);
        panel.setCaption(CAPTION);

        // TODO how to make panel take the whole vertical screen, if it does not
        // contain any child component;
        verticalLayout.setSpacing(true);
        verticalLayout.setMargin(true, false, false, true);
        verticalLayout.addComponent(mainLayout);
    }

    private void addUserField() {
        userComboBox.setWidth(COMPONENT_WIDTH);
        userComboBox.setNullSelectionAllowed(false);
        userComboBox.setMultiSelect(false);
        userComboBox.setRequired(true);
        mainLayout.addComponent(userComboBox);
    }

    private void addRoleField() {
        roleComboBox.setWidth(COMPONENT_WIDTH);
        roleComboBox.setNullSelectionAllowed(false);
        roleComboBox.setImmediate(true);
        roleComboBox.setRequired(true);
        roleComboBox.addListener(new RoleSelectListener());
        mainLayout.addComponent(roleComboBox);
    }

    private void addResourceType() {
        resourceTypeComboBox.setEnabled(false);
        resourceTypeComboBox.setWidth(COMPONENT_WIDTH);
        resourceTypeComboBox.setImmediate(true);
        mainLayout.addComponent(resourceTypeComboBox);
    }

    private void addResourceSearchBox() {
        searchBox.setWidth(Integer.toString(3 / 2 * COMPONENT_WIDTH_IN_INTEGER)
            + "px");
        searchBox.setEnabled(false);
        searchButton.setEnabled(false);
        mainLayout.addComponent(searchBox);
        searchButton.addListener(new SearchBtnListener());
        mainLayout.addComponent(searchButton);
    }

    private void addResourceSelection() {
        resouceResult.setSizeFull();
        resouceResult.setHeight(RESOURCE_SELECTION_HEIGHT);
        resourceContainer.setStyleName(Reindeer.PANEL_LIGHT);
        resourceContainer.setWidth(Integer
            .toString(3 / 2 * COMPONENT_WIDTH_IN_INTEGER) + "px");
        resourceContainer.setHeight(RESOURCE_SELECTION_HEIGHT);
        mainLayout.addComponent(resourceContainer);
    }

    private void addFooter() {
        footer.addComponent(saveBtn);
        // footer.addComponent(cancelBtn);

        final VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.addComponent(footer);
        verticalLayout.setComponentAlignment(footer, Alignment.MIDDLE_RIGHT);

        mainLayout.addComponent(verticalLayout);
    }

    private void bindData() {
        bindUserAccountData();
        bindRoleData();
        bindResourceTypeData();
    }

    private void bindUserAccountData() {
        userContainer =
            new POJOContainer<UserAccount>(UserAccount.class, PropertyId.NAME);
        for (final UserAccount user : getAllUserAccounts()) {
            userContainer.addPOJO(user);
        }
        userComboBox.setContainerDataSource(userContainer);
        userComboBox.setItemCaptionPropertyId(PropertyId.NAME);
    }

    private void bindRoleData() {
        final POJOContainer<Role> roleContainer =
            new POJOContainer<Role>(Role.class, PropertyId.OBJECT_ID,
                PropertyId.NAME);
        for (final Role role : getAllRoles()) {
            roleContainer.addPOJO(role);
        }
        roleComboBox.setContainerDataSource(roleContainer);
        roleComboBox.setItemCaptionPropertyId(PropertyId.NAME);
    }

    private void bindResourceTypeData() {
        final BeanItemContainer<ResourceType> resourceTypeContainer =
            new BeanItemContainer<ResourceType>(Arrays.asList(ResourceType
                .values()));
        resourceTypeComboBox.setContainerDataSource(resourceTypeContainer);
        resourceTypeComboBox.addListener(new ResourceTypeListener());
    }

    private Collection<Context> getAllContexts() {
        try {
            return contextService.getCache();
        }
        catch (final EscidocException e) {
            mainWindow.addWindow(new ErrorDialog(mainWindow,
                ViewConstants.ERROR_DIALOG_CAPTION, e.getMessage()));
        }
        catch (final InternalClientException e) {
            mainWindow.addWindow(new ErrorDialog(mainWindow,
                ViewConstants.ERROR_DIALOG_CAPTION, e.getMessage()));
        }
        catch (final TransportException e) {
            mainWindow.addWindow(new ErrorDialog(mainWindow,
                ViewConstants.ERROR_DIALOG_CAPTION, e.getMessage()));
        }
        return Collections.emptyList();
    }

    private List<Role> getAllRoles() {
        try {
            return (List<Role>) roleService.findAll();
        }
        catch (final EscidocClientException e) {
            mainWindow.addWindow(new ErrorDialog(mainWindow,
                ViewConstants.ERROR_DIALOG_CAPTION, e.getMessage()));
        }
        return Collections.emptyList();
    }

    private Collection<UserAccount> getAllUserAccounts() {
        try {
            return userService.findAll();
        }
        catch (final EscidocClientException e) {
            mainWindow.addWindow(new ErrorDialog(mainWindow,
                ViewConstants.ERROR_DIALOG_CAPTION, e.getMessage()));
        }
        return Collections.emptyList();
    }

    public void selectUser(final UserAccount userAccount) {
        selectedUser = userAccount;
        userComboBox.select(userAccount);
    }

    private class SaveBtnListener implements Button.ClickListener {

        private static final long serialVersionUID = -7128599340989436927L;

        @Override
        public void buttonClick(final ClickEvent event) {
            onSaveClick();
        }

        private void onSaveClick() {
            // TODO add validation
            assignRole();
        }

        private void assignRole() {
            try {
                userService
                    .assign(getSelectedUser()).withRole(getSelectedRole())
                    .onResources(getSelectedResources()).execute();
                app.showUserInEditView(getSelectedUser());
            }
            catch (final RoleNotFoundException e) {
                app.getMainWindow().addWindow(
                    new ErrorDialog(app.getMainWindow(),
                        ViewConstants.ERROR_DIALOG_CAPTION,
                        ViewConstants.REQUESTED_ROLE_HAS_NO_SCOPE_DEFINITIONS));
                LOG.error("An unexpected error occured! See LOG for details.",
                    e);
            }
            catch (final EscidocClientException e) {
                app.getMainWindow().addWindow(
                    new ErrorDialog(app.getMainWindow(),
                        ViewConstants.ERROR_DIALOG_CAPTION, e.getMessage()));
                LOG.error("An unexpected error occured! See LOG for details.",
                    e);
            }
        }

        private UserAccount getSelectedUser() {
            if (selectedUser == null) {
                return (UserAccount) userComboBox.getValue();
            }
            return selectedUser;
        }

        private Role getSelectedRole() {
            final Object value = roleComboBox.getValue();
            if (value instanceof Role) {
                return (Role) value;
            }
            return new Role();
        }

        private Set<ContextRef> getSelectedResources() {
            final Object value = resouceResult.getValue();
            if (value instanceof Context) {
                return Collections.singleton(new ContextRef(((Context) value)
                    .getObjid()));
            }
            return Collections.emptySet();
        }
    }

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

    private class RoleSelectListener implements ValueChangeListener {

        private static final long serialVersionUID = -4595870805889611817L;

        @Override
        public void valueChange(final ValueChangeEvent event) {
            onSelectedRole(event);
        }

        private void onSelectedRole(final ValueChangeEvent event) {
            final Object value = event.getProperty().getValue();
            if (value instanceof Role) {
                final Role r = (Role) value;

                enableScoping(isScopingEnable(r));
            }
        }

        private boolean isScopingEnable(final Role role) {
            if (role.getObjid().equals(
                RoleType.SYSTEM_ADMINISTRATOR.getObjectId())
                || role.getObjid().equals(
                    RoleType.SYSTEM_INSPECTOR.getObjectId())) {
                return false;
            }
            else {
                return true;
            }
        }

        private void enableScoping(final boolean isScopingEnabled) {
            resourceTypeComboBox.setEnabled(isScopingEnabled);
            searchBox.setEnabled(isScopingEnabled);
            searchButton.setEnabled(isScopingEnabled);
        }
    }

    private class SearchBtnListener implements Button.ClickListener {

        private static final long serialVersionUID = -2520068834542312077L;

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
                // final String message = "Not found";
                if (isContextFound()) {
                    // message = foundContexts.iterator().next().getObjid();
                    mainWindow.showNotification(foundContexts
                        .iterator().next().getObjid());
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
            catch (final EscidocException e) {
                app.getMainWindow().addWindow(
                    new ErrorDialog(app.getMainWindow(),
                        ViewConstants.ERROR_DIALOG_CAPTION,
                        "An unexpected error occured! See LOG for details."));
                LOG.error("An unexpected error occured! See LOG for details.",
                    e);
            }
            catch (final InternalClientException e) {
                app.getMainWindow().addWindow(
                    new ErrorDialog(app.getMainWindow(),
                        ViewConstants.ERROR_DIALOG_CAPTION,
                        "An unexpected error occured! See LOG for details."));
                LOG.error("An unexpected error occured! See LOG for details.",
                    e);
            }
            catch (final TransportException e) {
                app.getMainWindow().addWindow(
                    new ErrorDialog(app.getMainWindow(),
                        ViewConstants.ERROR_DIALOG_CAPTION,
                        "An unexpected error occured! See LOG for details."));
                LOG.error("An unexpected error occured! See LOG for details.",
                    e);
            }
            return Collections.emptyList();
        }
    }

    private class ResourceTypeListener implements ValueChangeListener {

        private static final long serialVersionUID = 2394096937007392588L;

        @Override
        public void valueChange(final ValueChangeEvent event) {
            try {
                onSelectedResourceType(event);
            }
            catch (final NotImplementedException e) {
                mainWindow.addWindow(new ErrorDialog(mainWindow,
                    ViewConstants.ERROR_DIALOG_CAPTION, e.getMessage()));
            }
        }

        private void onSelectedResourceType(final ValueChangeEvent event) {
            final Object value = event.getProperty().getValue();
            if (value instanceof ResourceType) {
                final ResourceType type = (ResourceType) value;
                mainWindow.showNotification(type.toString());

                Component newComponent = null;
                switch (type) {
                    case CONTEXT:
                        newComponent = resouceResult;
                        loadContextData();
                        break;
                    case ORG_UNIT:
                        newComponent = resouceResult;
                        loadOrgUnitData();
                        break;
                    default: {
                        clearResourceContainer();
                        throw new NotImplementedException("Scoping for " + type
                            + " is not yet implemented");
                    }
                }
                final Iterator<Component> it =
                    resourceContainer.getComponentIterator();
                if (it.hasNext()) {
                    resourceContainer.replaceComponent(it.next(), newComponent);
                }
                else {
                    resourceContainer.addComponent(newComponent);
                }
            }
        }

        private void loadOrgUnitData() {
            final Set<Resource> organizationalUnits =
                getAllOrganizationalUnits();
            if (isNotEmpty(organizationalUnits)) {
                final POJOContainer<Resource> container =
                    new POJOContainer<Resource>(
                        (Collection<Resource>) organizationalUnits,
                        PropertyId.NAME);
                resouceResult.setContainerDataSource(container);
                resouceResult.setItemCaptionPropertyId(PropertyId.NAME);
            }

        }

        private boolean isNotEmpty(final Set<Resource> organizationalUnits) {
            return organizationalUnits != null
                && organizationalUnits.size() > 0;
        }

        private Set<Resource> getAllOrganizationalUnits() {

            try {
                final Set<Resource> all =
                    serviceContainer.getOrgUnitService().findAll();
                return all;

            }
            catch (final EscidocClientException e) {
                mainWindow.addWindow(new ErrorDialog(mainWindow,
                    ViewConstants.ERROR_DIALOG_CAPTION, e.getMessage()));
            }
            return Collections.emptySet();
        }

        private void clearResourceContainer() {
            resourceContainer.removeAllComponents();
        }

        private void loadContextData() {
            final POJOContainer<Context> contextContainer =
                new POJOContainer<Context>(getAllContexts(), PropertyId.NAME);
            resouceResult.setContainerDataSource(contextContainer);
            resouceResult.setItemCaptionPropertyId(PropertyId.NAME);
        }

    }

    private enum ResourceType {
        ORG_UNIT(ViewConstants.ORGANIZATION_UNITS_LABEL), CONTEXT(
            ViewConstants.CONTEXTS_LABEL), CONTAINER("Container"), ITEM("Item"), COMPONENT(
            "Component");

        private String name;

        ResourceType(final String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }

    }
}