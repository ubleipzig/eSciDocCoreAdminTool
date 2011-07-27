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
package de.escidoc.admintool.view.user;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.vaadin.data.Item;
import com.vaadin.data.util.POJOContainer;
import com.vaadin.data.util.POJOItem;
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
import com.vaadin.ui.Window.Notification;
import com.vaadin.ui.themes.BaseTheme;
import com.vaadin.ui.themes.Reindeer;

import de.escidoc.admintool.app.AdminToolApplication;
import de.escidoc.admintool.app.PropertyId;
import de.escidoc.admintool.domain.PdpRequest;
import de.escidoc.admintool.service.internal.OrgUnitServiceLab;
import de.escidoc.admintool.service.internal.UserService;
import de.escidoc.admintool.view.ErrorMessage;
import de.escidoc.admintool.view.ModalDialog;
import de.escidoc.admintool.view.ViewConstants;
import de.escidoc.admintool.view.context.AddOrgUnitToTheList;
import de.escidoc.admintool.view.context.LinkClickListener;
import de.escidoc.admintool.view.navigation.ActionIdConstants;
import de.escidoc.admintool.view.resource.OrgUnitTreeView;
import de.escidoc.admintool.view.resource.ResourceRefDisplay;
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
import de.escidoc.core.resources.common.reference.UserAccountRef;

@SuppressWarnings("serial")
public class UserEditForm extends CustomComponent implements ClickListener {

    private static final Logger LOG = LoggerFactory.getLogger(UserEditForm.class);

    private static final int ROLE_LIST_HEIGHT = 200;

    private static final int LABEL_WIDTH = 111;

    private static final int LABEL_HEIGHT = 15;

    private final Panel panel = new Panel();

    private final FormLayout form = new FormLayout();

    private final HorizontalLayout footer = new HorizontalLayout();

    private final Button newUserBtn = new Button(ViewConstants.NEW, new NewUserListener());

    private final Button deleteUserBtn = new Button(ViewConstants.DELETE, new DeleteUserListener());

    private final Button addRoleButton = new Button(ViewConstants.ADD_LABEL, new AddRoleButtonListener(this));

    private final Button removeRoleButton = new Button(ViewConstants.REMOVE_LABEL, new RemoveRoleButtonListener(this));

    private final Button saveButton = new Button(ViewConstants.SAVE_LABEL, this);

    private final Button cancelButton = new Button(ViewConstants.CANCEL, this);

    private final Label objIdField = new Label();

    private final Label modifiedOn = new Label();

    private final HorizontalLayout header = new HorizontalLayout();

    final Table roleTable = new Table();

    final AdminToolApplication app;

    final UserService userService;

    private TextField nameField;

    private TextField loginNameField;

    private final Label createdOn = new Label();

    private CheckBox activeStatus;

    private Item item;

    String userObjectId;

    POJOContainer<Grant> grantContainer;

    private final Window mainWindow;

    private PasswordView passwordView;

    private UpdatePasswordCommandImpl command;

    private final OrgUnitServiceLab orgUnitService;

    private final PdpRequest pdpRequest;

    private final OrgUnitTreeView orgUnitTreeView;

    private final SetOrgUnitsCommandImpl setOrgUnitsCommand;

    private Button createdByLink;

    private LinkClickListener modifiedByLinkListener;

    private Button modifiedByLink;

    private LinkClickListener createdByLinkListener;

    public UserEditForm(final AdminToolApplication app, final UserService userService,
        final OrgUnitServiceLab orgUnitService, final OrgUnitTreeView orgUnitTreeView, final PdpRequest pdpRequest) {
        Preconditions.checkNotNull(app, "app is null: %s", app);
        Preconditions.checkNotNull(userService, "userService is null: %s", userService);
        Preconditions.checkNotNull(orgUnitService, "orgUnitService is null: %s", orgUnitService);
        Preconditions.checkNotNull(orgUnitTreeView, "resourceTreeView is null: %s", orgUnitTreeView);
        Preconditions.checkNotNull(pdpRequest, "pdpRequest is null: %s", pdpRequest);
        Preconditions.checkNotNull(pdpRequest, "pdpRequest is null: %s", pdpRequest);

        this.app = app;
        this.userService = userService;
        this.orgUnitService = orgUnitService;
        this.orgUnitTreeView = orgUnitTreeView;
        this.pdpRequest = pdpRequest;
        mainWindow = app.getMainWindow();

        setOrgUnitsCommand = new SetOrgUnitsCommandImpl(userService);
    }

    public final void init() {
        configureLayout();

        addNameField();
        addLoginNameField();
        addPasswordFields();

        addObjectIdLabel();

        addCreated();

        addModified();
        addActiveStatusCheckBox();

        createAndAddRoleComponent();
        addOrgUnitsWidget();
        addFooter();
    }

    // OrgUnit START

    private final Table orgUnitTable = new Table();

    private final Button editOrgUnitButton = new Button(ViewConstants.ADD_LABEL);

    private final Button removeOrgUnitButton = new Button(ViewConstants.REMOVE_LABEL);

    private void addOrgUnitsWidget() {
        createOrgUnitTable();
        createButtonListener();
        setButtonsStyle();
        addVerticalSpace();
        addToPanel();
    }

    private void createButtonListener() {
        final AddOrgUnitToTheList listener = new AddOrgUnitToTheList(mainWindow, orgUnitTreeView);
        listener.using(orgUnitTable);
        editOrgUnitButton.addListener(listener);

        removeOrgUnitButton.addListener(new RemoveOrgUnitFromUserListener(orgUnitTable));
    }

    private void createOrgUnitTable() {
        orgUnitTable.setHeight(100, UNITS_PIXELS);
        orgUnitTable.setWidth(300, UNITS_PIXELS);
        orgUnitTable.setSelectable(true);
        orgUnitTable.setNullSelectionAllowed(true);
        orgUnitTable.setMultiSelect(true);
        orgUnitTable.setWriteThrough(false);

        orgUnitTable.setColumnHeaderMode(Table.COLUMN_HEADER_MODE_HIDDEN);
    }

    private void addVerticalSpace() {
        panel.addComponent(new Label("<br/>", Label.CONTENT_XHTML));
    }

    private VerticalLayout createOrgUnitWidgetWitButtons() {
        return createLayout(ViewConstants.ORGANIZATION_UNITS_LABEL, orgUnitTable, 120, (int) (0.5 * ROLE_LIST_HEIGHT),
            false, new Button[] { editOrgUnitButton, removeOrgUnitButton });
    }

    private void setButtonsStyle() {
        editOrgUnitButton.setStyleName(Reindeer.BUTTON_SMALL);
        removeOrgUnitButton.setStyleName(Reindeer.BUTTON_SMALL);
    }

    private void addToPanel() {
        panel.addComponent(createOrgUnitWidgetWitButtons());
    }

    private POJOContainer<ResourceRefDisplay> orgUnitContainer;

    private VerticalLayout footerLayout;

    private void bindUserOrgUnitsWithView() {
        final List<String> orgUnitIdsForSelectedUser = retrieveOrgUnitObjectIdsForSelectedUser();

        if (orgUnitIdsForSelectedUser.isEmpty() && orgUnitContainer != null) {
            orgUnitContainer.removeAllItems();
        }
        else {
            orgUnitContainer = new POJOContainer<ResourceRefDisplay>(ResourceRefDisplay.class, "objectId", "title");
            orgUnitTable.setContainerDataSource(orgUnitContainer);

            orgUnitTable.setVisibleColumns(new String[] { "title", "objectId" });
            orgUnitTable.setColumnHeaders(new String[] { "Title", "Object ID" });

            for (final String orgUnitId : orgUnitIdsForSelectedUser) {
                orgUnitContainer.addPOJO(new ResourceRefDisplay(orgUnitId, findTitleFor(orgUnitId)));
            }
        }
    }

    private String findTitleFor(final String orgUnitId) {
        try {
            return orgUnitService.findById(orgUnitId).getXLinkTitle();
        }
        catch (final EscidocClientException e) {
            ErrorMessage.show(mainWindow, "The organizational unit with the ID: " + orgUnitId + " does exist");
            return ViewConstants.EMPTY_STRING;
        }
    }

    // OrgUnit END

    private void addActiveStatusCheckBox() {
        activeStatus = new CheckBox();
        activeStatus.setWriteThrough(false);
        panel.addComponent(LayoutHelper.create(ViewConstants.ACTIVE_STATUS, activeStatus, LABEL_WIDTH, false));
    }

    private void addCreated() {
        createCreatedByLink();
        form.addComponent(LayoutHelper.create("Created", "by", createdOn, createdByLink, LABEL_WIDTH, LABEL_HEIGHT,
            false));
    }

    private void createCreatedByLink() {
        createdByLink = new Button();
        createdByLink.setStyleName(BaseTheme.BUTTON_LINK);
        createdByLinkListener = new LinkClickListener(app);
        createdByLink.addListener(createdByLinkListener);
    }

    private void addModified() {
        createModifiedByLink();
        form.addComponent(LayoutHelper.create("Modified", "by", modifiedOn, modifiedByLink, LABEL_WIDTH, LABEL_HEIGHT,
            false));
    }

    private void createModifiedByLink() {
        modifiedByLink = new Button();
        modifiedByLink.setStyleName(BaseTheme.BUTTON_LINK);
        modifiedByLinkListener = new LinkClickListener(app);
        modifiedByLink.addListener(modifiedByLinkListener);
    }

    private boolean isRetrieveUserPermitted(final String userId) {
        return pdpRequest.isPermitted(ActionIdConstants.RETRIEVE_USER_ACCOUNT, userId);
    }

    private void addObjectIdLabel() {
        panel.addComponent(LayoutHelper.create(ViewConstants.OBJECT_ID_LABEL, objIdField, LABEL_WIDTH, false));
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
        panel.addComponent(LayoutHelper.create(ViewConstants.LOGIN_NAME_LABEL, loginNameField, LABEL_WIDTH, false));
    }

    private void addNameField() {
        nameField = new TextField();
        nameField.setMaxLength(ViewConstants.MAX_TITLE_LENGTH);
        nameField.setWidth(ViewConstants.FIELD_WIDTH);
        nameField.setWriteThrough(false);
        panel.addComponent(LayoutHelper.create(ViewConstants.NAME_LABEL, nameField, LABEL_WIDTH, true));
    }

    private void createUpdatePasswordView() {
        passwordView = PasswordViewImpl.createView();
        passwordView.addPasswordField();
        passwordView.addRetypePasswordField();
        passwordView.addMinCharValidator();

        final UpdatePasswordOkListener updatePasswordOkListener =
            new UpdatePasswordOkListener(passwordView.getPasswordField(), passwordView.getRetypePasswordField());
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
        form.setWidth(530, UNITS_PIXELS);
        form.addComponent(createHeader());
    }

    private HorizontalLayout createHeader() {
        header.setMargin(true);
        header.setSpacing(true);
        if (isCreateNewUserAllowed()) {
            header.addComponent(newUserBtn);
        }
        header.addComponent(deleteUserBtn);
        header.setVisible(true);
        return header;
    }

    private void createAndAddRoleComponent() {
        initRoleTable();
        addRoleButton.setStyleName(Reindeer.BUTTON_SMALL);
        removeRoleButton.setStyleName(Reindeer.BUTTON_SMALL);
        final VerticalLayout rolesComponent =
            createLayout(ViewConstants.ROLES_LABEL, roleTable, 120, ROLE_LIST_HEIGHT, false, new Button[] {
                addRoleButton, removeRoleButton });
        panel.addComponent(rolesComponent);
    }

    private VerticalLayout createLayout(
        final String rolesLabel, final Table table, final int labelWidth, final int roleListHeight, final boolean b,
        final Button[] buttons) {

        final HorizontalLayout hLayout = new HorizontalLayout();
        hLayout.setHeight(roleListHeight + Constants.PX);
        hLayout.addComponent(new Label(" "));

        final Label textLabel = new Label(Constants.P_ALIGN_RIGHT + rolesLabel + Constants.P, Label.CONTENT_XHTML);
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
            LOG.error(ViewConstants.AN_UNEXPECTED_ERROR_OCCURED_SEE_LOG_FOR_DETAILS, e);
        }
        catch (final TransportException e) {
            ModalDialog.show(app.getMainWindow(), e);
            LOG.error(ViewConstants.AN_UNEXPECTED_ERROR_OCCURED_SEE_LOG_FOR_DETAILS, e);
        }
        catch (final EscidocClientException e) {
            ModalDialog.show(app.getMainWindow(), e);
            LOG.error(ViewConstants.AN_UNEXPECTED_ERROR_OCCURED_SEE_LOG_FOR_DETAILS, e);
        }
        return Collections.emptyList();
    }

    private void addFooter() {

        footer.setWidth(100, UNITS_PERCENTAGE);
        final HorizontalLayout hl = new HorizontalLayout();
        hl.addComponent(saveButton);
        hl.addComponent(cancelButton);

        footer.addComponent(hl);
        footer.setComponentAlignment(hl, Alignment.MIDDLE_RIGHT);

        form.addComponent(footer);
    }

    @Override
    public void buttonClick(final ClickEvent event) {
        final Button source = event.getButton();
        if (source.equals(cancelButton)) {
            discardFields();
            removeAllError();
        }
        else if (source.equals(saveButton) && isValid()) {
            updateUserAccount();
            commitFields();
            removeAllError();
            showMessage();
        }
    }

    private void updateView(final UserAccount userAccount) {
        final POJOItem<UserAccount> pojoItem =
            new POJOItem<UserAccount>(userAccount, new String[] { PropertyId.OBJECT_ID, PropertyId.NAME,
                PropertyId.CREATED_ON, PropertyId.CREATED_BY, PropertyId.LAST_MODIFICATION_DATE,
                PropertyId.MODIFIED_BY, PropertyId.LOGIN_NAME, PropertyId.ACTIVE });
        setSelected(pojoItem);
    }

    private void showMessage() {
        mainWindow.showNotification(new Notification("Info", "User Account is updated",
            Notification.TYPE_TRAY_NOTIFICATION));
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
        activeStatus.commit();
    }

    private void updateUserAccount() {
        try {
            final UserAccount updatedUserAccount =
                userService.update(getSelectedItemId(), (String) nameField.getValue());
            if (activeStatus.isModified()) {
                changeState(updatedUserAccount);
            }
            updateOrgUnit();
            updateView(userService.retrieve(getSelectedItemId()));
        }
        catch (final EscidocException e) {
            ModalDialog.show(app.getMainWindow(), e);
            LOG.error(ViewConstants.AN_UNEXPECTED_ERROR_OCCURED_SEE_LOG_FOR_DETAILS, e);
        }
        catch (final InternalClientException e) {
            ModalDialog.show(app.getMainWindow(), e);
            LOG.error(ViewConstants.AN_UNEXPECTED_ERROR_OCCURED_SEE_LOG_FOR_DETAILS, e);
        }
        catch (final TransportException e) {
            ModalDialog.show(app.getMainWindow(), e);
            LOG.error(ViewConstants.AN_UNEXPECTED_ERROR_OCCURED_SEE_LOG_FOR_DETAILS, e);
        }
        catch (final EscidocClientException e) {
            ModalDialog.show(app.getMainWindow(), e);
            LOG.error(ViewConstants.AN_UNEXPECTED_ERROR_OCCURED_SEE_LOG_FOR_DETAILS, e);
        }
    }

    private void updateOrgUnit() throws EscidocClientException {
        setOrgUnitsCommand.setSelectedUserId(getSelectedItemId());
        setOrgUnitsCommand.setSeletectedOrgUnit(getOrgUnitsFromTable());
        setOrgUnitsCommand.update(retrieveOrgUnitObjectIdsForSelectedUser());
    }

    public Set<ResourceRefDisplay> getOrgUnitsFromTable() {
        final Set<ResourceRefDisplay> selectedSet = new HashSet<ResourceRefDisplay>();

        final Collection<?> itemIds = orgUnitTable.getItemIds();

        for (final Object objectId : itemIds) {
            if (objectId instanceof ResourceRefDisplay) {
                selectedSet.add((ResourceRefDisplay) objectId);
            }
        }
        return selectedSet;
    }

    private boolean isValid() {
        boolean isValid = true;
        isValid = EmptyFieldValidator.isValid(nameField, "Please enter a " + ViewConstants.NAME_ID);
        isValid &= EmptyFieldValidator.isValid(loginNameField, "Please enter a " + ViewConstants.LOGIN_NAME_ID);
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
        setObjectId();
        bindNameField();
        bindLoginName();
        bindObjectId();
        bindActiveStatus();
        bindModifiedOn();
        bindModifiedBy();
        bindCreatedOn();
        bindCreatedBy();
        bindUpdatePassword();
        bindRolesWithView();
        bindUserOrgUnitsWithView();
        bindUserRightsWithView();
    }

    private void setObjectId() {
        userObjectId = (String) item.getItemProperty(PropertyId.OBJECT_ID).getValue();
    }

    private void bindNameField() {
        nameField.setPropertyDataSource(item.getItemProperty(ViewConstants.NAME_ID));
    }

    private void bindLoginName() {
        loginNameField.setPropertyDataSource(item.getItemProperty(PropertyId.LOGIN_NAME));
    }

    private void bindObjectId() {
        objIdField.setPropertyDataSource(item.getItemProperty(PropertyId.OBJECT_ID));
    }

    private void bindActiveStatus() {
        activeStatus.setPropertyDataSource(item.getItemProperty(PropertyId.ACTIVE));
    }

    private void bindCreatedOn() {
        createdOn.setCaption(Converter.dateTimeToString((org.joda.time.DateTime) item.getItemProperty(
            PropertyId.CREATED_ON).getValue()));
    }

    private void bindCreatedBy() {
        createdByLink.setCaption(getCreatorName());
        if (isRetrieveUserPermitted(getCreatorId())) {
            createdByLinkListener.setUser(getCreatorId());
        }
        else {
            createdByLink.setEnabled(false);
        }

    }

    private void bindModifiedOn() {
        modifiedOn.setCaption(Converter.dateTimeToString((org.joda.time.DateTime) item.getItemProperty(
            PropertyId.LAST_MODIFICATION_DATE).getValue()));
    }

    private String getCreatorId() {
        return getCreator().getObjid();
    }

    private String getCreatorName() {
        return getCreator().getXLinkTitle();
    }

    private UserAccountRef getCreator() {
        return (UserAccountRef) item.getItemProperty(PropertyId.CREATED_BY).getValue();
    }

    private void bindModifiedBy() {
        modifiedByLink.setCaption(getModifierName());

        if (isRetrieveUserPermitted(getModifierId())) {
            modifiedByLinkListener.setUser(getModifierId());
        }
        else {
            modifiedByLink.setEnabled(false);
        }
    }

    private String getModifierId() {
        return getModifier().getObjid();
    }

    private String getModifierName() {
        return getModifier().getXLinkTitle();
    }

    private UserAccountRef getModifier() {
        return (UserAccountRef) item.getItemProperty(PropertyId.MODIFIED_BY).getValue();
    }

    private void bindUserRightsWithView() {
        deleteUserBtn.setVisible(isDeleteUserAllowed());
        nameField.setReadOnly(isUpdateNotAllowed());
        if (isUpdateNotAllowed()) {
            panel.removeComponent(passwordView);
            panel.removeComponent(footerLayout);
        }
        activeStatus.setReadOnly(isDeactivateUserNotAllowed());
        addRoleButton.setVisible(isCreateGrantAllowed());
        removeRoleButton.setVisible(isRevokeGrantAllowed());
    }

    private boolean isRevokeGrantAllowed() {
        return pdpRequest.isPermitted(ActionIdConstants.REVOKE_GRANT, getSelectedItemId());
    }

    private boolean isCreateGrantAllowed() {
        return pdpRequest.isPermitted(ActionIdConstants.CREATE_GRANT, getSelectedItemId());
    }

    private boolean isDeactivateUserNotAllowed() {
        return !pdpRequest.isPermitted(ActionIdConstants.DEACTIVATE_USER_ACCOUNT, getSelectedItemId());
    }

    private boolean isUpdateNotAllowed() {
        return pdpRequest.isDenied(ActionIdConstants.UPDATE_USER_ACCOUNT, getSelectedItemId());
    }

    private boolean isDeleteUserAllowed() {
        return pdpRequest.isPermitted(ActionIdConstants.DELETE_USER_ACCOUNT, getSelectedItemId());
    }

    private boolean isCreateNewUserAllowed() {
        return pdpRequest.isPermitted(ActionIdConstants.CREATE_USER_ACCOUNT);
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
                new POJOContainer<Grant>(Grant.class, PropertyId.XLINK_TITLE, PropertyId.OBJECT_ID,
                    PropertyId.GRANT_ROLE_OBJECT_ID, PropertyId.ASSIGN_ON);
            roleTable.setContainerDataSource(grantContainer);
            roleTable.setVisibleColumns(new String[] { PropertyId.XLINK_TITLE });
            roleTable.setColumnHeaders(ViewConstants.ROLE_COLUMN_HEADERS);

            for (final Grant grant : userGrants) {
                final Reference assignedOn = grant.getGrantProperties().getAssignedOn();
                if (assignedOn == null) {
                    grant.getGrantProperties().setAssignedOn(new RoleRef("", ""));
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
        return (DateTime) item.getItemProperty(PropertyId.LAST_MODIFICATION_DATE).getValue();
    }

    public UserAccount deleteUser() throws EscidocClientException {
        return userService.delete(getSelectedItemId());
    }

    @SuppressWarnings("boxing")
    public void changeState(final UserAccount updatedUserAccount) throws EscidocClientException {

        final Object value = activeStatus.getPropertyDataSource().getValue();
        if (!(value instanceof Boolean)) {
            return;
        }

        if ((!(Boolean) activeStatus.getPropertyDataSource().getValue())) {
            userService.activate(updatedUserAccount);
        }
        else {
            userService.deactivate(updatedUserAccount);
        }
    }

    private class NewUserListener implements Button.ClickListener {
        private static final long serialVersionUID = -9112247524189044505L;

        @Override
        public void buttonClick(final ClickEvent event) {
            ((UserView) getParent().getParent()).showAddView();
        }
    }

    private class DeleteUserListener implements Button.ClickListener {

        private static final long serialVersionUID = 2287544338040780227L;

        @Override
        public void buttonClick(final ClickEvent event) {
            try {
                final UserAccount deletedUser = deleteUser();
                ((UserView) getParent().getParent()).remove(deletedUser);

            }
            catch (final InternalClientException e) {
                setComponentError(new SystemError(e.getMessage()));
                LOG.error(ViewConstants.AN_UNEXPECTED_ERROR_OCCURED_SEE_LOG_FOR_DETAILS, e);
            }
            catch (final TransportException e) {
                setComponentError(new SystemError(e.getMessage()));
                LOG.error(ViewConstants.AN_UNEXPECTED_ERROR_OCCURED_SEE_LOG_FOR_DETAILS, e);
            }
            catch (final EscidocException e) {
                LOG.error(ViewConstants.AN_UNEXPECTED_ERROR_OCCURED_SEE_LOG_FOR_DETAILS, e);
                setComponentError(new SystemError(e.getMessage()));
            }
            catch (final EscidocClientException e) {
                setComponentError(new SystemError(e.getMessage()));
                LOG.error("An unexpected error occured! See LOG for details.", e);
                ModalDialog.show(mainWindow, e);
            }
        }
    }
}