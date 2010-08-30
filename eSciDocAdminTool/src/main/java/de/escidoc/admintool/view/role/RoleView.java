package de.escidoc.admintool.view.role;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
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
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Tree;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.themes.Reindeer;

import de.escidoc.admintool.app.AdminToolApplication;
import de.escidoc.admintool.app.PropertyId;
import de.escidoc.admintool.service.ContextService;
import de.escidoc.admintool.service.RoleService;
import de.escidoc.admintool.service.UserService;
import de.escidoc.admintool.view.ViewConstants;
import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;
import de.escidoc.core.resources.aa.useraccount.UserAccount;
import de.escidoc.core.resources.om.context.Context;
import de.escidoc.vaadin.dialog.ErrorDialog;

public class RoleView extends CustomComponent {
    private static final Logger log = LoggerFactory.getLogger(RoleView.class);

    private static final String SEARCH_LABEL = "Search";

    private static final int RESOURCE_SELECTION_HEIGHT_IN_INTEGER = 400;

    private static final String RESOURCE_SELECTION_HEIGHT =
        RESOURCE_SELECTION_HEIGHT_IN_INTEGER + "px";

    private static final String SYSADMIN_LOGIN_NAME = "sysadmin";

    private static final int COMPONENT_WIDTH_IN_INTEGER = 300;

    private static final String COMPONENT_WIDTH =
        COMPONENT_WIDTH_IN_INTEGER + "px";

    private static final int ONE_ROW = 1;

    private static final String CAPTION = "Role Management";

    private final Panel panel = new Panel();

    private final VerticalLayout verticalLayout = new VerticalLayout();

    private final AdminToolApplication app;

    private final RoleService roleService;

    private final UserService userService;

    private final ComboBox userComboBox = new ComboBox("User Name:");

    private final ComboBox roleComboBox = new ComboBox("Role:");

    private final ComboBox resourceTypeComboBox = new ComboBox("Resouce Type:");

    // TODO move to another class.
    // private final OrgUnitTree orgUnitTree;
    private final Tree orgUnitTree = new Tree();

    private final ListSelect resouceResult = new ListSelect();

    // TODO refactor all footer to a class.
    private final HorizontalLayout footer = new HorizontalLayout();

    private final Button saveBtn = new Button("Save", new SaveBtnListener());

    private final Button cancelBtn =
        new Button("Cancel", new CancelBtnListener());

    private final Window mainWindow;

    private final ComponentContainer mainLayout = new FormLayout();

    private final VerticalLayout resourceContainer = new VerticalLayout();

    private final TextField searchBox = new TextField("Resource Title: ");

    private final Button searchButton = new Button(SEARCH_LABEL);

    private final Set<ResourceType> resourceTypes =
        EnumSet.copyOf(Arrays.asList(ResourceType.values()));

    private BeanItemContainer<ResourceType> resourceTypeContainer;

    private final ContextService contextService;

    // TODO: add logged in user;
    public RoleView(final AdminToolApplication app,
        final RoleService roleService, final UserService userService,
        final ContextService contextService) {
        this.app = app;
        this.roleService = roleService;
        this.userService = userService;
        this.contextService = contextService;
        // orgUnitTree= new OrgUnitTree(service);
        mainWindow = app.getMainWindow();
        init();
        bindData();
    }

    // final ComponentContainer cssLayout = new CssLayout();

    private void init() {
        setCompositionRoot(panel);
        panel.setContent(verticalLayout);
        panel.setCaption(CAPTION);

        // TODO how to make panel take the whole vertical screen, if it does not
        // contain any child component;
        verticalLayout.setSpacing(true);
        verticalLayout.setMargin(true, false, false, true);
        verticalLayout.addComponent(mainLayout);
        // TODO experimenting with CSS Layout
        // cssLayout.addStyleName("myLayout");
        // panel.setContent(cssLayout);
        // panel.setContent(mainLayout);

        addUserField();
        addRoleField();
        addResourceType();
        addResourceSearchBox();
        addResourceSelection();
        // TODO depends on which resources are selected
        // panel.addComponent(orgUnitTree);
        addFooter();
    }

    private void addUserField() {
        userComboBox.setWidth(COMPONENT_WIDTH);
        userComboBox.setNullSelectionAllowed(false);

        // TODO try css layout
        // cssLayout.addComponent(userComboBox);
        mainLayout.addComponent(userComboBox);
    }

    private void addRoleField() {
        roleComboBox.setWidth(COMPONENT_WIDTH);
        roleComboBox.setNullSelectionAllowed(false);
        roleComboBox.setImmediate(true);

        mainLayout.addComponent(roleComboBox);
    }

    private void addResourceType() {
        resourceTypeComboBox.setEnabled(false);
        resourceTypeComboBox.setWidth(COMPONENT_WIDTH);
        resourceTypeComboBox.setImmediate(true);
        // resourceTypeComboBox.setNullSelectionAllowed(false);
        mainLayout.addComponent(resourceTypeComboBox);
    }

    private void addResourceSearchBox() {
        searchBox.setWidth(Integer.toString(3 / 2 * COMPONENT_WIDTH_IN_INTEGER)
            + "px");
        searchBox.setEnabled(false);
        searchButton.setEnabled(false);
        // final HorizontalLayout hl = new HorizontalLayout();
        // hl.addComponent(searchBox);
        // hl.addComponent(searchButton);
        // mainLayout.addComponent(hl);
        mainLayout.addComponent(searchBox);
        mainLayout.addComponent(searchButton);
    }

    private void addResourceSelection() {
        resouceResult.setSizeFull();
        resouceResult.setHeight(RESOURCE_SELECTION_HEIGHT);

        resourceContainer.setStyleName(Reindeer.PANEL_LIGHT);
        resourceContainer.setWidth(Integer
            .toString(3 / 2 * COMPONENT_WIDTH_IN_INTEGER)
            + "px");
        resourceContainer.setHeight(RESOURCE_SELECTION_HEIGHT);
        mainLayout.addComponent(resourceContainer);
    }

    private void addFooter() {
        footer.addComponent(saveBtn);
        footer.addComponent(cancelBtn);
        mainLayout.addComponent(footer);
    }

    private void bindData() {
        // User
        final POJOContainer<UserAccount> userContainer =
            new POJOContainer<UserAccount>(getAllUserAccounts(),
                PropertyId.NAME);
        userComboBox.setContainerDataSource(userContainer);
        userComboBox.setItemCaptionPropertyId(PropertyId.NAME);

        // role
        roleComboBox.setContainerDataSource(new BeanItemContainer<RoleType>(
            getRolesAvailableFor(SYSADMIN_LOGIN_NAME)));
        roleComboBox.addListener(new RoleSelectListener());

        searchButton.addListener(new SearchBtnListener());

        // resouce type
        resourceTypeContainer =
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
            mainWindow.addWindow(new ErrorDialog(mainWindow, "Error", e
                .getMessage()));
        }
        catch (final InternalClientException e) {
            mainWindow.addWindow(new ErrorDialog(mainWindow, "Error", e
                .getMessage()));
        }
        catch (final TransportException e) {
            mainWindow.addWindow(new ErrorDialog(mainWindow, "Error", e
                .getMessage()));
        }
        return Collections.emptyList();
    }

    private List<RoleType> getRolesAvailableFor(final String sysadminLoginName) {
        return Arrays.asList(RoleType.values());
    }

    private Collection<UserAccount> getAllUserAccounts() {
        try {
            return userService.findAll();
        }
        catch (final EscidocClientException e) {
            mainWindow.addWindow(new ErrorDialog(mainWindow, "Error", e
                .getMessage()));
        }
        return Collections.emptyList();
    }

    private static class SaveBtnListener implements Button.ClickListener {

        @Override
        public void buttonClick(final ClickEvent event) {
            onSaveClick();
        }

        private void onSaveClick() {
            // TODO Auto-generated method stub
        }
    }

    private static class CancelBtnListener implements Button.ClickListener {

        @Override
        public void buttonClick(final ClickEvent event) {
            onCancelClick();
        }

        private void onCancelClick() {
            // TODO Auto-generated method stub

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
            if (value instanceof RoleType) {
                final RoleType type = (RoleType) value;
                // mainWindow.showNotification(type.toString());
                switch (type) {
                    case COLLABOLATOR: {
                        enableScoping(true);
                        showResourceTypeFor(type);
                    }
                        break;
                    default: {
                        enableScoping(false);
                    }
                        break;
                }
            }
        }

        private void enableScoping(final boolean isScopingEnabled) {
            resourceTypeComboBox.setEnabled(isScopingEnabled);
            searchBox.setEnabled(isScopingEnabled);
            searchButton.setEnabled(isScopingEnabled);
        }

        private void showResourceTypeFor(final RoleType type) {
            switch (type) {
                case COLLABOLATOR: {
                    // final EnumSet<ResourceType> rts =
                    // EnumSet.copyOf(Arrays.asList(ResourceType.values()));
                    // // rts.complementOf(ResourceType.ORG_UNIT);
                    // final EnumSet<ResourceType> range =
                    // rts.range(ResourceType.CONTEXT, ResourceType.COMPONENT);
                    //
                    // for (final ResourceType rt : range) {
                    // resourceTypeContainer.removeItem(rt);
                    // }
                    resourceTypeContainer.removeItem(ResourceType.ORG_UNIT);

                }
                    break;
                default: {
                    enableScoping(false);
                }
                    break;
            }
        }
    }

    private class SearchBtnListener implements Button.ClickListener {
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
                final Collection<Context> foundContexts =
                    seachContextByName(userInput);
                String message = "Not found";
                if (foundContexts.size() > 0) {
                    message = foundContexts.iterator().next().getObjid();
                }
                mainWindow.showNotification(message);

            }
        }

        private Collection<Context> seachContextByName(final String userInput) {
            try {
                return contextService.findByTitle(userInput);
            }
            catch (final EscidocException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            catch (final InternalClientException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            catch (final TransportException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
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
                mainWindow.addWindow(new ErrorDialog(mainWindow, "Error", e
                    .getMessage()));
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

    // TODO retrieve predefined roles from repository.
    private enum RoleType {
        SYSTEM_ADMINISTRATOR("System Administrator"), SYSTEM_INSPECTOR(
            "System Inspector"), AUTHOR("Author"), ADMINISTRATOR(
            "Administrator"), MD_EDITOR("MD-Editor"), Moderator("Moderator"), DEPOSITOR(
            "Depositor"), INSPECTOR("Inspector"), COLLABOLATOR("Collaborator");

        private String name;

        RoleType(final String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}