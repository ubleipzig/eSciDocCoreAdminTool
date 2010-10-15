package de.escidoc.admintool.view.user;

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
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.Reindeer;

import de.escidoc.admintool.app.AdminToolApplication;
import de.escidoc.admintool.app.Command;
import de.escidoc.admintool.app.PropertyId;
import de.escidoc.admintool.service.UserService;
import de.escidoc.admintool.view.ErrorMessage;
import de.escidoc.admintool.view.ViewConstants;
import de.escidoc.admintool.view.role.RevokeGrantCommand;
import de.escidoc.admintool.view.role.RevokeGrantWindow;
import de.escidoc.admintool.view.validator.EmptyFieldValidator;
import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;
import de.escidoc.core.resources.aa.useraccount.Grant;
import de.escidoc.core.resources.aa.useraccount.UserAccount;
import de.escidoc.core.resources.common.reference.Reference;
import de.escidoc.core.resources.common.reference.RoleRef;
import de.escidoc.vaadin.utilities.Converter;
import de.escidoc.vaadin.utilities.LayoutHelper;

public class UserEditForm extends CustomComponent implements ClickListener {
    private static final long serialVersionUID = 3182336883168014436L;

    private static final Logger log = LoggerFactory
        .getLogger(UserEditForm.class);

    private static final int ROLE_LIST_HEIGHT = 100;

    private static final String ROLE_LIST_WIDTH = "400px";

    private static final int LABEL_WIDTH = 100;

    private static final int LABEL_HEIGHT = 15;

    private final Panel panel = new Panel();

    private final FormLayout form = new FormLayout();

    private final Table roleTable = new Table();

    private final HorizontalLayout footer = new HorizontalLayout();

    private final Button newUserBtn = new Button(ViewConstants.NEW,
        new NewUserListener());

    private final Button deleteUserBtn = new Button(ViewConstants.DELETE,
        new DeleteUserListener());

    private final Button addRoleButton = new Button(ViewConstants.ADD_LABEL,
        new AddRoleButtonListener());

    private final Button removeRoleButton = new Button(
        ViewConstants.REMOVE_LABEL, new RemoveRoleButtonListener());

    private final Button save = new Button(ViewConstants.SAVE, this);

    private final Button cancel = new Button(ViewConstants.CANCEL, this);

    private final Label objIdField = new Label();

    private final Label modifiedOn = new Label();

    private final Label modifiedBy = new Label();

    private final HorizontalLayout header = new HorizontalLayout();

    private final AdminToolApplication app;

    private final UserService userService;

    private TextField nameField;

    private TextField loginNameField;

    private Label createdOn;

    private Label createdBy;

    private CheckBox state;

    private Item item;

    private String userObjectId;

    private POJOContainer<Grant> grantContainer;

    public UserEditForm(final AdminToolApplication app,
        final UserService userService) {
        this.app = app;
        this.userService = userService;
        init();
    }

    public final void init() {
        setCompositionRoot(panel);
        panel.setStyleName(Reindeer.PANEL_LIGHT);
        panel.setContent(form);
        form.setSpacing(false);
        panel.setCaption(ViewConstants.EDIT_USER_VIEW_CAPTION);
        panel.addComponent(createHeader());

        nameField = new TextField();
        nameField.setWidth(ROLE_LIST_WIDTH);
        nameField.setWriteThrough(false);
        panel.addComponent(LayoutHelper.create(ViewConstants.NAME_LABEL,
            nameField, LABEL_WIDTH, true));

        loginNameField = new TextField();
        loginNameField.setWidth(ROLE_LIST_WIDTH);
        loginNameField.setReadOnly(true);
        panel.addComponent(LayoutHelper.create(ViewConstants.LOGIN_NAME_LABEL,
            loginNameField, LABEL_WIDTH, false));

        panel.addComponent(LayoutHelper.create(ViewConstants.OBJECT_ID_LABEL,
            objIdField, LABEL_WIDTH, false));

        panel.addComponent(LayoutHelper.create("Modified", "by", modifiedOn,
            modifiedBy, LABEL_WIDTH, LABEL_HEIGHT, false));

        createdOn = new Label();
        createdBy = new Label();
        panel.addComponent(LayoutHelper.create("Created", "by", createdOn,
            createdBy, LABEL_WIDTH, LABEL_HEIGHT, false));

        state = new CheckBox();
        state.setWriteThrough(false);
        panel.addComponent(LayoutHelper.create("Active status", state,
            LABEL_WIDTH, false));

        initRoleComponent();
        panel.addComponent(addFooter());
    }

    private HorizontalLayout createHeader() {
        header.setMargin(true);
        header.setSpacing(true);
        header.addComponent(newUserBtn);
        header.addComponent(deleteUserBtn);
        header.setVisible(true);
        return header;
    }

    private void initRoleComponent() {
        initRoleTable();
        panel.addComponent(LayoutHelper.create(ViewConstants.ROLES_LABEL,
            roleTable, LABEL_WIDTH, ROLE_LIST_HEIGHT, false, new Button[] {
                addRoleButton, removeRoleButton }));
    }

    private void initRoleTable() {
        roleTable.setWidth(ROLE_LIST_WIDTH);
        roleTable.setSelectable(true);
        roleTable.setNullSelectionAllowed(true);
        roleTable.setMultiSelect(true);
        roleTable.setImmediate(true);
    }

    private final class AddRoleButtonListener implements Button.ClickListener {
        private static final long serialVersionUID = 2520625502594778921L;

        @Override
        public void buttonClick(final ClickEvent event) {
            app.showRoleView();
            app.showRoleView(userService.getUserById(userObjectId));
        }
    }

    private final class RemoveRoleButtonListener
        implements Button.ClickListener {

        private static final long serialVersionUID = -605606788213049694L;

        @Override
        public void buttonClick(final ClickEvent event) {
            final Object selectedGrants = roleTable.getValue();

            if (selectedGrants instanceof Set<?>) {
                for (final Object grant : ((Set<?>) selectedGrants)) {
                    if (grant instanceof Grant) {
                        app.getMainWindow().addWindow(
                            createModalWindow((Grant) grant).getModalWindow());
                    }
                }
            }
        }

        private RevokeGrantWindow createModalWindow(final Grant grant) {
            return new RevokeGrantWindow(createRevokeGrantCommand(grant),
                grant, grantContainer);
        }

        private Command createRevokeGrantCommand(final Grant grant) {
            return new RevokeGrantCommand(userService, userObjectId, grant);
        }
    }

    private Collection<Grant> getGrants() {
        return retrieveGrantsFor(userObjectId);
    }

    private Collection<Grant> retrieveGrantsFor(final String userObjectId) {
        try {
            return userService.retrieveCurrentGrants(userObjectId);
        }
        catch (final InternalClientException e) {
            ErrorMessage.show(app.getMainWindow(), e);
            log.error(
                ViewConstants.AN_UNEXPECTED_ERROR_OCCURED_SEE_LOG_FOR_DETAILS,
                e);
        }
        catch (final TransportException e) {
            ErrorMessage.show(app.getMainWindow(), e);
            log.error(
                ViewConstants.AN_UNEXPECTED_ERROR_OCCURED_SEE_LOG_FOR_DETAILS,
                e);
        }
        catch (final EscidocClientException e) {
            ErrorMessage.show(app.getMainWindow(), e);
            log.error(
                ViewConstants.AN_UNEXPECTED_ERROR_OCCURED_SEE_LOG_FOR_DETAILS,
                e);
        }
        return Collections.emptyList();
    }

    private HorizontalLayout addFooter() {
        footer.setSpacing(true);
        footer.addComponent(save);
        footer.addComponent(cancel);
        return footer;
    }

    public void buttonClick(final ClickEvent event) {
        final Button source = event.getButton();
        if (source.equals(cancel)) {
            discardFields();
            removeAllError();
        }
        else if (source.equals(save) && isValid()) {
            updateUserAccount();
            commitFields();
            removeAllError();
        }
    }

    private void discardFields() {
        nameField.discard();
        roleTable.discard();
    }

    private void removeAllError() {
        nameField.setComponentError(null);
        loginNameField.setComponentError(null);
    }

    private void commitFields() {
        nameField.commit();
        loginNameField.commit();
        state.commit();
    }

    private void updateUserAccount() {
        try {
            userService.update(getSelectedItemId(),
                (String) nameField.getValue());
            if (state.isModified()) {
                changeState();
            }
        }
        catch (final EscidocException e) {
            ErrorMessage.show(app.getMainWindow(), e);
            log.error(
                ViewConstants.AN_UNEXPECTED_ERROR_OCCURED_SEE_LOG_FOR_DETAILS,
                e);
        }
        catch (final InternalClientException e) {
            ErrorMessage.show(app.getMainWindow(), e);
            log.error(
                ViewConstants.AN_UNEXPECTED_ERROR_OCCURED_SEE_LOG_FOR_DETAILS,
                e);
        }
        catch (final TransportException e) {
            ErrorMessage.show(app.getMainWindow(), e);
            log.error(
                ViewConstants.AN_UNEXPECTED_ERROR_OCCURED_SEE_LOG_FOR_DETAILS,
                e);
        }
        catch (final EscidocClientException e) {
            ErrorMessage.show(app.getMainWindow(), e);
            log.error(
                ViewConstants.AN_UNEXPECTED_ERROR_OCCURED_SEE_LOG_FOR_DETAILS,
                e);
        }
    }

    private boolean isValid() {
        boolean isValid = true;
        isValid =
            EmptyFieldValidator.isValid(nameField, "Please enter a "
                + ViewConstants.NAME_ID);
        isValid &=
            EmptyFieldValidator.isValid(loginNameField, "Please enter a "
                + ViewConstants.LOGIN_NAME_ID);
        return isValid;
    }

    public void setSelected(final Item item) {
        if (item == null) {
            return;
        }
        this.item = item;
        bindData();
    }

    private void bindData() {
        userObjectId =
            (String) item.getItemProperty(PropertyId.OBJECT_ID).getValue();
        nameField.setPropertyDataSource(item
            .getItemProperty(ViewConstants.NAME_ID));
        loginNameField.setPropertyDataSource(item
            .getItemProperty(PropertyId.LOGIN_NAME));
        objIdField.setPropertyDataSource(item
            .getItemProperty(PropertyId.OBJECT_ID));
        modifiedOn.setCaption(Converter
            .dateTimeToString((org.joda.time.DateTime) item.getItemProperty(
                PropertyId.LAST_MODIFICATION_DATE).getValue()));
        modifiedBy.setPropertyDataSource(item
            .getItemProperty(PropertyId.MODIFIED_BY));
        state.setPropertyDataSource(item.getItemProperty(PropertyId.ACTIVE));
        createdOn.setCaption(Converter
            .dateTimeToString((org.joda.time.DateTime) item.getItemProperty(
                PropertyId.CREATED_ON).getValue()));
        createdBy.setPropertyDataSource(item
            .getItemProperty(PropertyId.CREATED_BY));
        bindRolesWithView();
    }

    private void bindRolesWithView() {

        final List<Grant> userGrants = (List<Grant>) getGrants();

        for (final Grant grant : userGrants) {
            log.info("Grant title: " + grant.getXLinkTitle());
        }

        if (userGrants.isEmpty()) {
            if (grantContainer != null) {
                grantContainer.removeAllItems();
            }
        }
        else {

            grantContainer =
                new POJOContainer<Grant>(Grant.class, PropertyId.XLINK_TITLE,
                    PropertyId.OBJECT_ID, PropertyId.GRANT_ROLE_OBJECT_ID,
                    PropertyId.ASSIGN_ON);
            roleTable.setContainerDataSource(grantContainer);
            roleTable.setVisibleColumns(new String[] { PropertyId.XLINK_TITLE,
                PropertyId.GRANT_ROLE_OBJECT_ID, PropertyId.ASSIGN_ON });
            roleTable.setColumnHeaders(ViewConstants.ROLE_COLUMN_HEADERS);

            for (final Grant grant : userGrants) {
                final Reference assignedOn =
                    grant.getGrantProperties().getAssignedOn();
                if (assignedOn == null) {
                    grant.getGrantProperties().setAssignedOn(
                        new RoleRef("", ""));
                }
                grantContainer.addPOJO(grant);
            }

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
        private static final long serialVersionUID = -9112247524189044505L;

        public void buttonClick(final ClickEvent event) {
            ((UserView) getParent().getParent()).showAddView();
        }
    }

    private class DeleteUserListener implements Button.ClickListener {

        private static final long serialVersionUID = 2287544338040780227L;

        public void buttonClick(final ClickEvent event) {
            try {
                final UserAccount deletedUser = deleteUser();
                ((UserView) getParent().getParent()).remove(deletedUser);

            }
            catch (final InternalClientException e) {
                setComponentError(new SystemError(e.getMessage()));
                log
                    .error(
                        ViewConstants.AN_UNEXPECTED_ERROR_OCCURED_SEE_LOG_FOR_DETAILS,
                        e);
            }
            catch (final TransportException e) {
                setComponentError(new SystemError(e.getMessage()));
                log
                    .error(
                        ViewConstants.AN_UNEXPECTED_ERROR_OCCURED_SEE_LOG_FOR_DETAILS,
                        e);
            }
            catch (final EscidocException e) {
                log
                    .error(
                        ViewConstants.AN_UNEXPECTED_ERROR_OCCURED_SEE_LOG_FOR_DETAILS,
                        e);
                setComponentError(new SystemError(e.getMessage()));
            }
        }
    }
}