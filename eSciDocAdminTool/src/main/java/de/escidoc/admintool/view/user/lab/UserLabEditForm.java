package de.escidoc.admintool.view.user.lab;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.data.Item;
import com.vaadin.data.util.POJOContainer;
import com.vaadin.terminal.SystemError;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

import de.escidoc.admintool.app.AdminToolApplication;
import de.escidoc.admintool.app.PropertyId;
import de.escidoc.admintool.service.RoleService;
import de.escidoc.admintool.service.UserService;
import de.escidoc.admintool.view.ViewConstants;
import de.escidoc.admintool.view.validator.EmptyFieldValidator;
import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;
import de.escidoc.core.resources.aa.role.Role;
import de.escidoc.core.resources.aa.useraccount.Grant;
import de.escidoc.core.resources.aa.useraccount.UserAccount;
import de.escidoc.vaadin.dialog.ErrorDialog;
import de.escidoc.vaadin.utilities.Converter;
import de.escidoc.vaadin.utilities.LayoutHelper;

@SuppressWarnings("serial")
public class UserLabEditForm extends CustomComponent implements ClickListener {

    private static final Logger log =
        LoggerFactory.getLogger(UserLabEditForm.class);

    private static final String EDIT_USER_ACCOUNT = "Edit User Account";

    private static final int ROLE_LIST_HEIGHT = 100;

    private static final String ROLE_LIST_WIDTH = "400px";

    private static final String ROLES_LABEL = "Roles";

    private static final int NUMBER_OF_ROLE_TO_SHOW = 5;

    private static final int LABEL_WIDTH = 100;

    private final Panel panel = new Panel();

    private final FormLayout form = new FormLayout();

    private final ListSelect roleList = new ListSelect();

    private final Button newUserBtn = new Button("New", new NewUserListener());

    private final Button deleteUserBtn =
        new Button("Delete", new DeleteUserListener());

    private final Button save = new Button("Save", this);

    private final Button cancel = new Button("Cancel", this);

    private final Label objIdField = new Label();

    private final AdminToolApplication app;

    private final UserService userService;

    private final RoleService roleService;

    private HorizontalLayout footer;

    private TextField nameField;

    private TextField loginNameField;

    private Label modifiedOn;

    private Label modifiedBy;

    private Label createdOn;

    private Label createdBy;

    private CheckBox state;

    private Item item;

    private String userObjectId;

    private final Button addRoleButton = new Button(ViewConstants.ADD_LABEL);

    private final Button removeRoleButton =
        new Button(ViewConstants.REMOVE_LABEL);

    private POJOContainer<Role> roleContainer;

    public UserLabEditForm(final AdminToolApplication app,
        final UserService userService, final RoleService roleService) {
        this.app = app;
        this.userService = userService;
        this.roleService = roleService;
        init();
    }

    public void init() {
        panel.setContent(form);
        panel.setCaption(EDIT_USER_ACCOUNT);

        form.setSpacing(false);

        panel.addComponent(createHeader());

        nameField = new TextField();
        nameField.setWidth(ROLE_LIST_WIDTH);
        nameField.setWriteThrough(false);

        final int height = 15;
        panel.addComponent(LayoutHelper.create(ViewConstants.NAME_LABEL,
            nameField, LABEL_WIDTH, true));
        loginNameField = new TextField();
        loginNameField.setWidth(ROLE_LIST_WIDTH);
        loginNameField.setWriteThrough(false);
        loginNameField.setReadOnly(true);
        panel.addComponent(LayoutHelper.create(ViewConstants.LOGIN_NAME_LABEL,
            loginNameField, LABEL_WIDTH, false));

        panel.addComponent(LayoutHelper.create(ViewConstants.OBJECT_ID_LABEL,
            objIdField, LABEL_WIDTH, false));

        modifiedOn = new Label();
        modifiedBy = new Label();
        panel.addComponent(LayoutHelper.create("Modified", "by", modifiedOn,
            modifiedBy, LABEL_WIDTH, height, false));

        createdOn = new Label();
        createdBy = new Label();
        panel.addComponent(LayoutHelper.create("Created", "by", createdOn,
            createdBy, LABEL_WIDTH, height, false));

        state = new CheckBox();
        state.setWriteThrough(false);
        panel.addComponent(LayoutHelper.create("Active status", state,
            LABEL_WIDTH, false));

        initRoleComponent();

        panel.addComponent(addFooter());
        setCompositionRoot(panel);
    }

    private HorizontalLayout createHeader() {
        final HorizontalLayout header = new HorizontalLayout();
        header.setMargin(true);
        header.setSpacing(true);
        header.addComponent(newUserBtn);
        header.addComponent(deleteUserBtn);
        header.setVisible(true);
        return header;
    }

    private void initRoleComponent() {
        initRoleList();

        addRoleButton.addListener(new AddRoleButtonListener());
        removeRoleButton.addListener(new RemoveRoleButtonListener());

        panel.addComponent(LayoutHelper.create(ROLES_LABEL, roleList,
            LABEL_WIDTH, ROLE_LIST_HEIGHT, false, new Button[] { addRoleButton,
                removeRoleButton }));
    }

    private void initRoleList() {
        roleList.setRows(NUMBER_OF_ROLE_TO_SHOW);
        roleList.setWidth(ROLE_LIST_WIDTH);
        roleList.setNullSelectionAllowed(true);
        roleList.setMultiSelect(true);
        roleList.setImmediate(true);
    }

    private final class AddRoleButtonListener implements Button.ClickListener {

        @Override
        public void buttonClick(final ClickEvent event) {
            // TODO show role view with selected user and its roles?
            app.showRoleView();
        }
    }

    private final class RemoveRoleButtonListener
        implements Button.ClickListener {

        @Override
        public void buttonClick(final ClickEvent event) {
            final Object selectedRoles = roleList.getValue();

            if (selectedRoles instanceof Set) {
                for (final Object role : ((Set) selectedRoles)) {
                    if (role instanceof Role) {
                        roleContainer.removeItem(role);
                    }
                }
            }
            else {
                log.info("title: "
                    + ((Role) selectedRoles).getProperties().getName());
            }
        }
    }

    private List<Role> getRoles() {
        try {
            return getRolesFor(retrieveGrantsFor(userObjectId));
        }
        catch (final EscidocException e) {
            app.getMainWindow().addWindow(
                new ErrorDialog(app.getMainWindow(), "Error",
                    "An unexpected error occured! See log for details."));
            log.error("An unexpected error occured! See log for details.", e);
            e.printStackTrace();
        }
        catch (final InternalClientException e) {
            app.getMainWindow().addWindow(
                new ErrorDialog(app.getMainWindow(), "Error",
                    "An unexpected error occured! See log for details."));
            log.error("An unexpected error occured! See log for details.", e);
            e.printStackTrace();
        }
        catch (final TransportException e) {
            app.getMainWindow().addWindow(
                new ErrorDialog(app.getMainWindow(), "Error",
                    "An unexpected error occured! See log for details."));
            log.error("An unexpected error occured! See log for details.", e);
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    private List<Role> getRolesFor(final Collection<Grant> userGrants)
        throws EscidocException, InternalClientException, TransportException {
        final List<Role> roles = new ArrayList<Role>();
        for (final Grant grant : userGrants) {
            roles.add(getRoleFor(grant));
        }
        return roles;
    }

    private Role getRoleFor(final Grant grant) throws EscidocException,
        InternalClientException, TransportException {
        return roleService.retrieve(grant
            .getGrantProperties().getRole().getObjid());
    }

    private Collection<Grant> retrieveGrantsFor(final String userObjectId) {
        try {
            return userService.retrieveCurrentGrants(userObjectId);
        }
        catch (final InternalClientException e) {
            app.getMainWindow().addWindow(
                new ErrorDialog(app.getMainWindow(), "Error",
                    "An unexpected error occured! See log for details."));
            log.error("An unexpected error occured! See log for details.", e);
            e.printStackTrace();
        }
        catch (final TransportException e) {
            app.getMainWindow().addWindow(
                new ErrorDialog(app.getMainWindow(), "Error",
                    "An unexpected error occured! See log for details."));
            log.error("An unexpected error occured! See log for details.", e);
            e.printStackTrace();
        }
        catch (final EscidocClientException e) {
            app.getMainWindow().addWindow(
                new ErrorDialog(app.getMainWindow(), "Error",
                    "An unexpected error occured! See log for details."));
            log.error("An unexpected error occured! See log for details.", e);
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    private HorizontalLayout addFooter() {
        footer = new HorizontalLayout();
        footer.setSpacing(true);

        footer.addComponent(save);
        footer.addComponent(cancel);
        return footer;
    }

    public void buttonClick(final ClickEvent event) {
        final Button source = event.getButton();
        if (source == cancel) {
            discardFields();
            removeAllError();
        }
        else if (source == save) {
            if (isValid()) {
                updateUserAccount();
                commitFields();
                removeAllError();
            }
        }
    }

    private void discardFields() {
        nameField.discard();
        roleList.discard();
    }

    private void removeAllError() {
        nameField.setComponentError(null);
        loginNameField.setComponentError(null);
    }

    private void commitFields() {
        nameField.commit();
        loginNameField.commit();
    }

    private void updateUserAccount() {
        try {
            userService.update(getSelectedItemId(), (String) nameField
                .getValue());
            if (state.isModified()) {
                changeState();
            }
        }
        catch (final EscidocException e) {
            log.error("An unexpected error occured! See log for details.", e);
            e.printStackTrace();
        }
        catch (final InternalClientException e) {
            log.error("An unexpected error occured! See log for details.", e);
            e.printStackTrace();
        }
        catch (final TransportException e) {
            log.error("An unexpected error occured! See log for details.", e);
            e.printStackTrace();
        }
        catch (final EscidocClientException e) {
            log.error("An unexpected error occured! See log for details.", e);
            e.printStackTrace();
        }
    }

    private boolean isValid() {
        boolean valid = true;
        valid =
            EmptyFieldValidator.isValid(nameField, "Please enter a "
                + ViewConstants.NAME_ID);
        valid &=
            (EmptyFieldValidator.isValid(loginNameField, "Please enter a "
                + ViewConstants.LOGIN_NAME_ID));
        return valid;
    }

    public void setSelected(final Item item) {
        this.item = item;
        if (item != null) {
            userObjectId = (String) item.getItemProperty("objid").getValue();

            nameField.setPropertyDataSource(item
                .getItemProperty(ViewConstants.NAME_ID));
            loginNameField.setPropertyDataSource(item
                .getItemProperty("properties.loginName"));
            objIdField.setPropertyDataSource(item
                .getItemProperty(PropertyId.OBJECT_ID));
            modifiedOn.setCaption(Converter
                .dateTimeToString((org.joda.time.DateTime) item
                    .getItemProperty("lastModificationDate").getValue()));
            modifiedBy.setPropertyDataSource(item
                .getItemProperty("properties.modifiedBy.objid"));
            state.setPropertyDataSource(item
                .getItemProperty("properties.active"));
            createdOn.setCaption(Converter
                .dateTimeToString((org.joda.time.DateTime) item
                    .getItemProperty("properties.creationDate").getValue()));
            createdBy.setPropertyDataSource(item
                .getItemProperty("properties.createdBy.objid"));

            bindRolesWithView();
        }
    }

    private void bindRolesWithView() {
        final List<Role> userRoles = getRoles();
        if (userRoles.size() > 0) {
            roleContainer =
                new POJOContainer<Role>(userRoles, PropertyId.OBJECT_ID,
                    PropertyId.NAME);
            roleList.setContainerDataSource(roleContainer);
            roleList.setItemCaptionPropertyId(PropertyId.NAME);
        }
    }

    private String getSelectedItemId() {
        if (item == null) {
            return "";
        }
        return (String) item.getItemProperty("objid").getValue();
    }

    public UserAccount deleteUser() throws EscidocException,
        InternalClientException, TransportException {
        return userService.delete(getSelectedItemId());
    }

    public void changeState() throws InternalClientException,
        TransportException, EscidocClientException {
        if (!(Boolean) state.getPropertyDataSource().getValue()) {
            userService.activate(getSelectedItemId());
        }
        else {
            userService.deactivate(getSelectedItemId());
        }
    }

    private class NewUserListener implements Button.ClickListener {
        public void buttonClick(final ClickEvent event) {
            ((UserLabView) getParent().getParent()).showAddView();
        }
    }

    private class DeleteUserListener implements Button.ClickListener {
        public void buttonClick(final ClickEvent event) {
            try {
                final UserAccount deletedUser = deleteUser();
                ((UserLabView) getParent().getParent()).remove(deletedUser);

            }
            catch (final InternalClientException e) {
                setComponentError(new SystemError(e.getMessage()));
                log.error("An unexpected error occured! See log for details.",
                    e);
                e.printStackTrace();
            }
            catch (final TransportException e) {
                setComponentError(new SystemError(e.getMessage()));
                log.error("An unexpected error occured! See log for details.",
                    e);
                e.printStackTrace();
            }
            catch (final EscidocException e) {
                log.error("An unexpected error occured! See log for details.",
                    e);
                e.printStackTrace();
                setComponentError(new SystemError(e.getMessage()));
            }
        }
    }
}
