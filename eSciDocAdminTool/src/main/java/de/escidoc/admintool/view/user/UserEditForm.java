package de.escidoc.admintool.view.user;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.vaadin.data.Item;
import com.vaadin.data.util.POJOContainer;
import com.vaadin.terminal.SystemError;
import com.vaadin.ui.Alignment;
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
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Reindeer;

import de.escidoc.admintool.app.AdminToolApplication;
import de.escidoc.admintool.app.PropertyId;
import de.escidoc.admintool.service.OrgUnitServiceLab;
import de.escidoc.admintool.service.UserService;
import de.escidoc.admintool.view.ErrorMessage;
import de.escidoc.admintool.view.ModalDialog;
import de.escidoc.admintool.view.ViewConstants;
import de.escidoc.admintool.view.resource.ResourceRefDisplay;
import de.escidoc.admintool.view.resource.ResourceTreeView;
import de.escidoc.admintool.view.user.password.PasswordView;
import de.escidoc.admintool.view.user.password.PasswordViewImpl;
import de.escidoc.admintool.view.user.password.UpdatePasswordCommandImpl;
import de.escidoc.admintool.view.user.password.UpdatePasswordOkListener;
import de.escidoc.admintool.view.util.Constants;
import de.escidoc.admintool.view.util.Converter;
import de.escidoc.admintool.view.util.LayoutHelper;
import de.escidoc.admintool.view.validator.EmptyFieldValidator;
import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;
import de.escidoc.core.resources.aa.useraccount.Grant;
import de.escidoc.core.resources.aa.useraccount.UserAccount;
import de.escidoc.core.resources.common.reference.Reference;
import de.escidoc.core.resources.common.reference.RoleRef;

public class UserEditForm extends CustomComponent implements ClickListener {
    private static final long serialVersionUID = 3182336883168014436L;

    private static final Logger LOG = LoggerFactory
        .getLogger(UserEditForm.class);

    private static final int ROLE_LIST_HEIGHT = 200;

    private static final int LABEL_WIDTH = 111;

    private static final int LABEL_HEIGHT = 15;

    private final Panel panel = new Panel();

    private final FormLayout form = new FormLayout();

    private final HorizontalLayout footer = new HorizontalLayout();

    private final Button newUserBtn = new Button(ViewConstants.NEW,
        new NewUserListener());

    private final Button deleteUserBtn = new Button(ViewConstants.DELETE,
        new DeleteUserListener());

    private final Button addRoleButton = new Button(ViewConstants.ADD_LABEL,
        new AddRoleButtonListener(this));

    private final Button removeRoleButton = new Button(
        ViewConstants.REMOVE_LABEL, new RemoveRoleButtonListener(this));

    private final Button save = new Button(ViewConstants.SAVE, this);

    private final Button cancel = new Button(ViewConstants.CANCEL, this);

    private final Label objIdField = new Label();

    private final Label modifiedOn = new Label();

    private final Label modifiedBy = new Label();

    private final HorizontalLayout header = new HorizontalLayout();

    final Table roleTable = new Table();

    final AdminToolApplication app;

    final UserService userService;

    private TextField nameField;

    private TextField loginNameField;

    private Label createdOn;

    private Label createdBy;

    private CheckBox state;

    private Item item;

    String userObjectId;

    POJOContainer<Grant> grantContainer;

    private final Window mainWindow;

    private PasswordView passwordView;

    private UpdatePasswordCommandImpl command;

    private final OrgUnitServiceLab orgUnitService;

    public UserEditForm(final AdminToolApplication app,
        final UserService userService, final OrgUnitServiceLab orgUnitService,
        final ResourceTreeView resourceTreeView) {
        Preconditions.checkNotNull(orgUnitService,
            "orgUnitService is null: %s", orgUnitService);
        this.app = app;
        this.userService = userService;
        this.orgUnitService = orgUnitService;
        mainWindow = app.getMainWindow();
        init();
    }

    public final void init() {
        configureLayout();

        addNameField();
        addLoginNameField();
        addPasswordFields();

        addObjectIdLabel();
        addModifiedOnLabel();
        addCreatedOnAndByLabel();
        addActiveStatusCheckBox();

        createAndAddRoleComponent();
        addOrgUnitsWidget();
        addFooter();
    }

    // OrgUnit START

    private final Table orgUnitTable = new Table();

    private final Button addOrgUnitButton = new Button(ViewConstants.ADD_LABEL);

    private final Button removeOrgUnitButton = new Button(
        ViewConstants.REMOVE_LABEL);

    private void addOrgUnitsWidget() {
        createOrgUnitTable();
        addVerticalSpace();
        addToPanel();
    }

    private void createOrgUnitTable() {
        orgUnitTable.setHeight(100, UNITS_PIXELS);
        orgUnitTable.setWidth(300, UNITS_PIXELS);
        orgUnitTable.setSelectable(true);
        orgUnitTable.setNullSelectionAllowed(true);
        orgUnitTable.setMultiSelect(true);
        orgUnitTable.setImmediate(true);
        orgUnitTable.setColumnHeaderMode(Table.COLUMN_HEADER_MODE_HIDDEN);
    }

    private void addVerticalSpace() {
        panel.addComponent(new Label("<br/>", Label.CONTENT_XHTML));
    }

    private VerticalLayout createOrgUnitWidgetWitButtons() {
        setButtonsStyle();
        return createLayout(ViewConstants.ORGANIZATION_UNITS_LABEL,
            orgUnitTable, 120, (int) (0.5 * ROLE_LIST_HEIGHT), false,
            new Button[] {});
    }

    private void setButtonsStyle() {
        addOrgUnitButton.setStyleName(Reindeer.BUTTON_SMALL);
        removeOrgUnitButton.setStyleName(Reindeer.BUTTON_SMALL);
    }

    private void addToPanel() {
        panel.addComponent(createOrgUnitWidgetWitButtons());
    }

    private POJOContainer<ResourceRefDisplay> orgUnitContainer;

    private void bindUserOrgUnitsWithView() {
        final List<String> orgUnitIdsForSelectedUser =
            retrieveOrgUnitObjectIdsForSelectedUser();

        if (orgUnitIdsForSelectedUser.isEmpty() && orgUnitContainer != null) {
            orgUnitContainer.removeAllItems();
        }
        else {
            orgUnitContainer =
                new POJOContainer<ResourceRefDisplay>(ResourceRefDisplay.class,
                    "objectId", "title");
            orgUnitTable.setContainerDataSource(orgUnitContainer);

            orgUnitTable
                .setVisibleColumns(new String[] { "title", "objectId" });
            orgUnitTable
                .setColumnHeaders(new String[] { "Title", "Object ID" });

            for (final String userObjectId : orgUnitIdsForSelectedUser) {
                orgUnitContainer.addPOJO(new ResourceRefDisplay(userObjectId,
                    findTitleFor(userObjectId)));
            }
        }
    }

    private String findTitleFor(final String orgUnitId) {
        try {
            return orgUnitService.findById(orgUnitId).getXLinkTitle();
        }
        catch (final EscidocClientException e) {
            ErrorMessage.show(mainWindow,
                "The organizational unit with the ID: " + orgUnitId
                    + " does exist");
            return ViewConstants.EMPTY_STRING;
        }
    }

    // OrgUnit END

    private void addActiveStatusCheckBox() {
        state = new CheckBox();
        state.setWriteThrough(false);
        panel.addComponent(LayoutHelper.create("Active status", state,
            LABEL_WIDTH, false));
    }

    private void addCreatedOnAndByLabel() {
        createdOn = new Label();
        createdBy = new Label();
        panel.addComponent(LayoutHelper.create("Created", "by", createdOn,
            createdBy, LABEL_WIDTH, LABEL_HEIGHT, false));
    }

    private void addModifiedOnLabel() {
        panel.addComponent(LayoutHelper.create("Modified", "by", modifiedOn,
            modifiedBy, LABEL_WIDTH, LABEL_HEIGHT, false));
    }

    private void addObjectIdLabel() {
        panel.addComponent(LayoutHelper.create(ViewConstants.OBJECT_ID_LABEL,
            objIdField, LABEL_WIDTH, false));
    }

    private void addPasswordFields() {
        createUpdatePasswordView();
        addPasswordView();
    }

    private void addLoginNameField() {
        loginNameField = new TextField();
        loginNameField.setMaxLength(ViewConstants.MAX_TITLE_LENGTH);
        loginNameField.setWidth(ViewConstants.FIELD_WIDTH);
        loginNameField.setReadOnly(true);
        panel.addComponent(LayoutHelper.create(ViewConstants.LOGIN_NAME_LABEL,
            loginNameField, LABEL_WIDTH, false));
    }

    private void addNameField() {
        nameField = new TextField();
        nameField.setMaxLength(ViewConstants.MAX_TITLE_LENGTH);
        nameField.setWidth(ViewConstants.FIELD_WIDTH);
        nameField.setWriteThrough(false);
        panel.addComponent(LayoutHelper.create(ViewConstants.NAME_LABEL,
            nameField, LABEL_WIDTH, true));
    }

    private void createUpdatePasswordView() {
        passwordView = PasswordViewImpl.createView();
        passwordView.addPasswordField();
        passwordView.addRetypePasswordField();
        passwordView.addMinCharValidator();

        final UpdatePasswordOkListener updatePasswordOkListener =
            new UpdatePasswordOkListener(passwordView.getPasswordField(),
                passwordView.getRetypePasswordField());
        updatePasswordOkListener.setMainWindow(mainWindow);

        command = new UpdatePasswordCommandImpl(userService);
        updatePasswordOkListener.setCommand(command);

        passwordView.addOkButton(updatePasswordOkListener);
        final ClickListener cancelListener = new Button.ClickListener() {

            private static final long serialVersionUID = 4304779885338015398L;

            @Override
            public void buttonClick(final ClickEvent event) {
                passwordView.resetFields();
                passwordView.removeErrorMessages();
            }
        };
        passwordView.addCancelButton(cancelListener);
    }

    private void addPasswordView() {
        panel.addComponent(passwordView);
    }

    private void configureLayout() {
        setCompositionRoot(panel);
        panel.setStyleName(Reindeer.PANEL_LIGHT);
        panel.setCaption(ViewConstants.EDIT_USER_VIEW_CAPTION);
        panel.setContent(form);

        form.setSpacing(false);
        form.setWidth(520, UNITS_PIXELS);
        form.addComponent(createHeader());
    }

    private HorizontalLayout createHeader() {
        header.setMargin(true);
        header.setSpacing(true);
        header.addComponent(newUserBtn);
        header.addComponent(deleteUserBtn);
        header.setVisible(true);
        return header;
    }

    private void createAndAddRoleComponent() {
        initRoleTable();
        addRoleButton.setStyleName(Reindeer.BUTTON_SMALL);
        removeRoleButton.setStyleName(Reindeer.BUTTON_SMALL);
        final VerticalLayout rolesComponent =
            createLayout(ViewConstants.ROLES_LABEL, roleTable, 120,
                ROLE_LIST_HEIGHT, false, new Button[] { addRoleButton,
                    removeRoleButton });
        panel.addComponent(rolesComponent);
    }

    private VerticalLayout createLayout(
        final String rolesLabel, final Table table, final int labelWidth,
        final int roleListHeight, final boolean b, final Button[] buttons) {

        final HorizontalLayout hLayout = new HorizontalLayout();
        hLayout.setHeight(roleListHeight + Constants.PX);
        hLayout.addComponent(new Label(" "));

        final Label textLabel =
            new Label(Constants.P_ALIGN_RIGHT + rolesLabel + Constants.P,
                Label.CONTENT_XHTML);
        textLabel.setSizeUndefined();
        textLabel.setWidth(labelWidth + Constants.PX);
        hLayout.addComponent(textLabel);
        hLayout.setComponentAlignment(textLabel, Alignment.MIDDLE_RIGHT);

        hLayout.addComponent(table);
        hLayout.setComponentAlignment(table, Alignment.MIDDLE_RIGHT);
        hLayout.addComponent(new Label(" &nbsp; ", Label.CONTENT_XHTML));

        final VerticalLayout vLayout = new VerticalLayout();
        vLayout.addComponent(hLayout);

        final HorizontalLayout hl = new HorizontalLayout();
        final Label la = new Label("&nbsp;", Label.CONTENT_XHTML);
        la.setSizeUndefined();
        la.setWidth(labelWidth + Constants.PX);
        hl.addComponent(la);

        for (final Button button : buttons) {
            hl.addComponent(button);
        }
        vLayout.addComponent(hl);
        hLayout.setSpacing(false);

        return vLayout;
    }

    private void initRoleTable() {
        roleTable.setHeight(200, UNITS_PIXELS);
        roleTable.setWidth("300px");
        roleTable.setSelectable(true);
        roleTable.setNullSelectionAllowed(true);
        roleTable.setMultiSelect(true);
        roleTable.setImmediate(true);
        roleTable.setColumnHeaderMode(Table.COLUMN_HEADER_MODE_HIDDEN);
    }

    private Collection<Grant> getGrants() {
        return retrieveGrantsFor(userObjectId);
    }

    private Collection<Grant> retrieveGrantsFor(final String userObjectId) {
        try {
            return userService.retrieveCurrentGrants(userObjectId);
        }
        catch (final InternalClientException e) {
            ModalDialog.show(app.getMainWindow(), e);
            LOG.error(
                ViewConstants.AN_UNEXPECTED_ERROR_OCCURED_SEE_LOG_FOR_DETAILS,
                e);
        }
        catch (final TransportException e) {
            ModalDialog.show(app.getMainWindow(), e);
            LOG.error(
                ViewConstants.AN_UNEXPECTED_ERROR_OCCURED_SEE_LOG_FOR_DETAILS,
                e);
        }
        catch (final EscidocClientException e) {
            ModalDialog.show(app.getMainWindow(), e);
            LOG.error(
                ViewConstants.AN_UNEXPECTED_ERROR_OCCURED_SEE_LOG_FOR_DETAILS,
                e);
        }
        return Collections.emptyList();
    }

    private void addFooter() {
        footer.setSpacing(true);
        footer.setMargin(true);
        footer.setVisible(true);

        footer.addComponent(save);
        footer.addComponent(cancel);

        final VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.addComponent(footer);
        verticalLayout.setComponentAlignment(footer, Alignment.MIDDLE_RIGHT);

        panel.addComponent(verticalLayout);
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
            ModalDialog.show(app.getMainWindow(), e);
            LOG.error(
                ViewConstants.AN_UNEXPECTED_ERROR_OCCURED_SEE_LOG_FOR_DETAILS,
                e);
        }
        catch (final InternalClientException e) {
            ModalDialog.show(app.getMainWindow(), e);
            LOG.error(
                ViewConstants.AN_UNEXPECTED_ERROR_OCCURED_SEE_LOG_FOR_DETAILS,
                e);
        }
        catch (final TransportException e) {
            ModalDialog.show(app.getMainWindow(), e);
            LOG.error(
                ViewConstants.AN_UNEXPECTED_ERROR_OCCURED_SEE_LOG_FOR_DETAILS,
                e);
        }
        catch (final EscidocClientException e) {
            ModalDialog.show(app.getMainWindow(), e);
            LOG.error(
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
        bindUpdatePassword();
        bindRolesWithView();
        bindUserOrgUnitsWithView();
    }

    private void bindRolesWithView() {
        final List<Grant> userGrants = (List<Grant>) getGrants();

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
            roleTable
                .setVisibleColumns(new String[] { PropertyId.XLINK_TITLE });
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

    private List<String> retrieveOrgUnitObjectIdsForSelectedUser() {
        try {
            final String selectedItemId = getSelectedItemId();
            return userService.retrieveOrgUnitsFor(selectedItemId);
        }
        catch (final EscidocClientException e) {
            ModalDialog.show(app.getMainWindow(), e);
            LOG.warn(e.getMessage(), e);
        }
        return Collections.emptyList();
    }

    private void bindUpdatePassword() {
        passwordView.resetFields();
        passwordView.removeErrorMessages();
        command.setSelectedUserId(getSelectedItemId());
        command.setLastModificationDate(getLastModificationDate());
    }

    private String getSelectedItemId() {
        if (item == null) {
            return "";
        }
        return (String) item.getItemProperty(PropertyId.OBJECT_ID).getValue();
    }

    private DateTime getLastModificationDate() {
        return (DateTime) item.getItemProperty(
            PropertyId.LAST_MODIFICATION_DATE).getValue();
    }

    public UserAccount deleteUser() throws EscidocClientException {
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
                LOG
                    .error(
                        ViewConstants.AN_UNEXPECTED_ERROR_OCCURED_SEE_LOG_FOR_DETAILS,
                        e);
            }
            catch (final TransportException e) {
                setComponentError(new SystemError(e.getMessage()));
                LOG
                    .error(
                        ViewConstants.AN_UNEXPECTED_ERROR_OCCURED_SEE_LOG_FOR_DETAILS,
                        e);
            }
            catch (final EscidocException e) {
                LOG
                    .error(
                        ViewConstants.AN_UNEXPECTED_ERROR_OCCURED_SEE_LOG_FOR_DETAILS,
                        e);
                setComponentError(new SystemError(e.getMessage()));
            }
            catch (final EscidocClientException e) {
                setComponentError(new SystemError(e.getMessage()));
                LOG.error("An unexpected error occured! See LOG for details.",
                    e);
                ModalDialog.show(mainWindow, e);
            }
        }
    }
}