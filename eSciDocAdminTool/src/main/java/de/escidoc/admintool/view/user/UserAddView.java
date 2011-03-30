package de.escidoc.admintool.view.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.data.util.POJOItem;
import com.vaadin.terminal.UserError;
import com.vaadin.ui.AbstractTextField;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.Notification;
import com.vaadin.ui.themes.Reindeer;

import de.escidoc.admintool.app.AdminToolApplication;
import de.escidoc.admintool.service.UserService;
import de.escidoc.admintool.view.ErrorMessage;
import de.escidoc.admintool.view.ViewConstants;
import de.escidoc.admintool.view.resource.ResourceTreeView;
import de.escidoc.admintool.view.user.password.PasswordView;
import de.escidoc.admintool.view.user.password.PasswordViewImpl;
import de.escidoc.admintool.view.user.password.UpdatePasswordCommand;
import de.escidoc.admintool.view.user.password.UpdatePasswordCommandImpl;
import de.escidoc.admintool.view.util.Constants;
import de.escidoc.admintool.view.util.LayoutHelper;
import de.escidoc.admintool.view.validator.EmptyFieldValidator;
import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;
import de.escidoc.core.client.exceptions.application.security.AuthorizationException;
import de.escidoc.core.client.exceptions.application.violated.UniqueConstraintViolationException;
import de.escidoc.core.resources.aa.useraccount.UserAccount;

public class UserAddView extends CustomComponent implements ClickListener {
    private static final int LEFT_MARGIN = 111;

    private static final long serialVersionUID = 3007285643463919742L;

    private static final Logger LOG = LoggerFactory
        .getLogger(UserAddView.class);

    private final Panel panel = new Panel();

    private final FormLayout form = new FormLayout();

    private final HorizontalLayout footer = new HorizontalLayout();

    private final Button saveButton =
        new Button(ViewConstants.SAVE_LABEL, this);

    private final Button cancelButton = new Button(ViewConstants.CANCEL, this);

    private final UserListView userListView;

    private final UserService userService;

    final AdminToolApplication app;

    private final ResourceTreeView resourceTreeView;

    private TextField nameField;

    private TextField loginNameField;

    private ObjectProperty<String> nameProperty;

    private ObjectProperty<String> loginNameProperty;

    public UserAddView(final AdminToolApplication app,
        final UserListView userListView, final UserService userService,
        final ResourceTreeView resourceTreeView) {
        this.app = app;
        this.userListView = userListView;
        this.userService = userService;
        this.resourceTreeView = resourceTreeView;
    }

    public void init() {
        configureLayout();
        setOrgUnitsCommand = new SetOrgUnitsCommandImpl(userService);
        addNameField();
        addLoginField();
        addOrgUnitWidget();
        addPasswordFields();
        addSpace();
        addFooter();
    }

    private void configureLayout() {
        setCompositionRoot(panel);

        panel.setStyleName(Reindeer.PANEL_LIGHT);
        panel.setCaption(ViewConstants.USER_ADD_VIEW_CAPTION);
        panel.setContent(form);

        form.setSpacing(false);
        // form.setWidth(75, UNITS_PERCENTAGE);
        form.setWidth(520, UNITS_PIXELS);
    }

    private void addSpace() {
        form.addComponent(new Label("<br/>", Label.CONTENT_XHTML));
        form.addComponent(new Label("<br/>", Label.CONTENT_XHTML));
    }

    private void addPasswordFields() {
        createPasswordField();

        panel.addComponent(createLayout(ViewConstants.PASSWORD_CAPTION,
            passwordView.getPasswordField(), LEFT_MARGIN, true));

        panel.addComponent(createLayout(ViewConstants.RETYPE_PASSWORD_CAPTION,
            passwordView.getRetypePasswordField(), LEFT_MARGIN, true));
    }

    private Component createLayout(
        final String nameLabel, final AbstractTextField textField,
        final int leftMargin, final boolean required) {

        final HorizontalLayout hor = new HorizontalLayout();
        hor.setHeight(37, UNITS_PIXELS);
        hor.addComponent(new Label(" "));

        final String text = Constants.P_ALIGN_RIGHT + nameLabel + Constants.P;
        Label l;
        hor.addComponent(l = new Label(text, Label.CONTENT_XHTML));
        l.setWidth(leftMargin + Constants.PX);

        if (required) {
            hor
                .addComponent(new Label(
                    "&nbsp;<span style=\"color:red; position:relative; top:13px;\">*</span>",
                    Label.CONTENT_XHTML));
        }
        else {
            hor.addComponent(new Label("&nbsp;&nbsp;", Label.CONTENT_XHTML));
        }
        hor.addComponent(textField);
        hor.setComponentAlignment(textField, Alignment.BOTTOM_RIGHT);
        hor.addComponent(new Label(" "));
        hor.setSpacing(false);
        return hor;
    }

    // START: ORG UNIT
    // TODO refactor to a new class
    final OrgUnitWidget orgUnitWidget = new OrgUnitWidgetImpl();

    Window modalWindow;

    private void addOrgUnitWidget() {
        orgUnitWidget.addTable();
        createModalWindow();

        orgUnitWidget.withAddButton().addListener(
            new ShowOrgUnitsSelectionWidget(this));
        orgUnitWidget.withRemoveButton().withListener(
            new RemoveSeletecteOrgUnitListener(this));
        panel.addComponent(new Label(" &nbsp; ", Label.CONTENT_XHTML));
        panel.addComponent(createLayout(orgUnitWidget.build()));
    }

    private Component createLayout(final VerticalLayout build) {
        final HorizontalLayout hLayout = new HorizontalLayout();

        final Label textLabel =
            new Label(Constants.P_ALIGN_RIGHT
                + ViewConstants.ORGANIZATION_UNITS_LABEL + Constants.P,
                Label.CONTENT_XHTML);
        hLayout.addComponent(textLabel);
        textLabel.setSizeUndefined();
        textLabel.setWidth(111 + Constants.PX);

        hLayout.addComponent(textLabel);
        hLayout.addComponent(build);
        return hLayout;
    }

    private void createModalWindow() {
        configure();
        addOrgUnitTreeView();
        addButtons();
    }

    private final HorizontalLayout buttons = new HorizontalLayout();

    private void addButtons() {
        modalWindow.addComponent(buttons);

        addOkButton();
        addCancelButton();
    }

    private void addCancelButton() {
        final Button cancelButton = new Button(ViewConstants.CANCEL);
        cancelButton.addListener(new CloseOrgUnitSelectionWidget(this));
        buttons.addComponent(cancelButton);
    }

    private void addOkButton() {
        final Button okButton = new Button(ViewConstants.OK_LABEL);
        okButton.addListener(new AddOrgUnitsToTable(app.getMainWindow(),
            modalWindow, resourceTreeView, orgUnitWidget.getTable()));
        buttons.addComponent(okButton);
    }

    void showModalWindow() {
        app.getMainWindow().addWindow(modalWindow);
    }

    private void addOrgUnitTreeView() {
        resourceTreeView.multiSelect();
        modalWindow.addComponent(resourceTreeView);
    }

    private void configure() {
        modalWindow = new Window();
        modalWindow.setModal(true);
        modalWindow.setCaption(ViewConstants.SELECT_ORGANIZATIONAL_UNIT);
        modalWindow.setHeight(ViewConstants.MODAL_DIALOG_HEIGHT);
        modalWindow.setWidth(ViewConstants.MODAL_DIALOG_WIDTH);
    }

    private PasswordView passwordView;

    private void createPasswordField() {
        passwordView = PasswordViewImpl.createView();
        passwordView.getPasswordField().setCaption("");
        passwordView.addPasswordField();

        passwordView.addRetypePasswordField();
        passwordView.getRetypePasswordField().setCaption("");

        passwordView.addMinCharValidator();
    }

    private void addNameField() {
        panel.addComponent(LayoutHelper.create(ViewConstants.NAME_LABEL,
            nameField = new TextField(), LEFT_MARGIN, true));
        nameField.setMaxLength(ViewConstants.MAX_TITLE_LENGTH);
        nameProperty =
            new ObjectProperty<String>(ViewConstants.EMPTY_STRING, String.class);
        nameField.setPropertyDataSource(nameProperty);
        nameField.setWidth(ViewConstants.FIELD_WIDTH);
    }

    private void addLoginField() {
        panel.addComponent(LayoutHelper.create(ViewConstants.LOGIN_NAME_LABEL,
            loginNameField = new TextField(), LEFT_MARGIN, true));
        loginNameProperty =
            new ObjectProperty<String>(ViewConstants.EMPTY_STRING, String.class);
        loginNameField.setPropertyDataSource(loginNameProperty);
        loginNameField.setWidth(ViewConstants.FIELD_WIDTH);
    }

    private void addFooter() {
        // footer.setSpacing(true);
        // footer.setMargin(true);
        // footer.setVisible(true);
        //
        // footer.addComponent(saveButton);
        // footer.addComponent(cancelButton);
        //
        // final VerticalLayout verticalLayout = new VerticalLayout();
        // verticalLayout.addComponent(footer);
        // verticalLayout.setComponentAlignment(footer, Alignment.MIDDLE_RIGHT);
        //
        // panel.addComponent(verticalLayout);
        footer.setWidth(100, UNITS_PERCENTAGE);

        final HorizontalLayout hLayout = new HorizontalLayout();
        hLayout.addComponent(saveButton);
        hLayout.addComponent(cancelButton);

        footer.addComponent(hLayout);
        footer.setComponentAlignment(hLayout, Alignment.MIDDLE_RIGHT);

        form.addComponent(footer);
    }

    @Override
    public void buttonClick(final ClickEvent event) {
        final Button source = event.getButton();
        if (source == cancelButton) {
            resetFields();
        }
        else if (source == saveButton) {
            validateAndSave();
        }
    }

    private void validateAndSave() {
        boolean valid = true;
        valid = isNameFieldValid();
        valid &= isLoginNameFieldValid();
        valid &= isPasswordFieldValid();

        if (valid) {
            trySaveAndUpdateView();
        }
    }

    private void trySaveAndUpdateView() {
        try {
            final UserAccount createdUserAccount = createUserAccount();
            setPasswordFor(createdUserAccount);
            setOrgUnitsFor(createdUserAccount);

            final POJOItem<UserAccount> item =
                userListView.addUser(createdUserAccount);
            Preconditions.checkNotNull(item,
                "Add new user to the list failed: %s", item);
            resetFields();
            app.showUser(createdUserAccount);
            showMessage();
        }
        catch (final EscidocException e) {
            if (e instanceof AuthorizationException) {
                ErrorMessage.show(app.getMainWindow(), "Not Authorized", e);
            }
            else if (e instanceof UniqueConstraintViolationException) {
                loginNameField.setComponentError(new UserError(e.getMessage()));
            }
        }
        catch (final InternalClientException e) {
            ErrorMessage.show(app.getMainWindow(), "Not Authorized", e);
            LOG.error("An unexpected error occured! See LOG for details.", e);
        }
        catch (final TransportException e) {
            ErrorMessage.show(app.getMainWindow(), "Not Authorized", e);
            LOG.error("An unexpected error occured! See LOG for details.", e);
        }
        catch (final EscidocClientException e) {
            ErrorMessage.show(app.getMainWindow(), "Not Authorized", e);
            LOG.error("An unexpected error occured! See LOG for details.", e);
        }
    }

    private void showMessage() {
        app.getMainWindow().showNotification(
            new Notification("Info", "User Account is created",
                Notification.TYPE_TRAY_NOTIFICATION));
    }

    // START:reference selected org units to created user.
    private SetOrgUnitsCommand setOrgUnitsCommand;

    private void initSetOrgUnitsCommand(final UserAccount createdUserAccount) {
        Preconditions.checkNotNull(createdUserAccount,
            "createdUserAccount is null: %s", createdUserAccount);

        setOrgUnitsCommand.setSelectedUserId(createdUserAccount.getObjid());
        setOrgUnitsCommand.setSeletectedOrgUnit(orgUnitWidget
            .getOrgUnitsFromTable());
    }

    private void setOrgUnitsFor(final UserAccount createdUserAccount)
        throws EscidocClientException {
        Preconditions.checkNotNull(createdUserAccount,
            "createdUserAccount is null: %s", createdUserAccount);

        initSetOrgUnitsCommand(createdUserAccount);
        setOrgUnitsCommand.execute(orgUnitWidget.getTable());
    }

    // END:reference selected org units to created user.

    private boolean isPasswordFieldValid() {
        if (isEmpty(enteredPassword())) {
            showErrorMessageForEmptyPassword();
            return false;
        }
        if (isLessThanSixChars(enteredPassword())) {
            passwordView.getPasswordField().setComponentError(null);
            focusOnPasswordField();
            return false;
        }
        else if (isMatched(enteredRetypePassword(), enteredPassword())) {
            removeErrorMessages();
            return true;
        }
        else {
            showErrorMessage();
            focusOnPasswordField();
            return false;
        }
    }

    private void showErrorMessageForEmptyPassword() {
        passwordView.getPasswordField().setComponentError(
            new UserError("Password can not be empty."));
    }

    private boolean isEmpty(final String enteredPassword) {
        return enteredPassword == null || enteredPassword.isEmpty();
    }

    private void showErrorMessage() {
        passwordView.getPasswordField().setComponentError(null);
        passwordView.getRetypePasswordField().setComponentError(
            new UserError(ViewConstants.PASSWORDS_DID_NOT_MATCH_MESSAGE));
    }

    private void removeErrorMessages() {
        passwordView.getPasswordField().setComponentError(null);

        passwordView.getRetypePasswordField().setComponentError(null);
    }

    private String enteredRetypePassword() {
        return (String) passwordView.getRetypePasswordField().getValue();
    }

    private void focusOnPasswordField() {
        passwordView.getPasswordField().focus();
    }

    private boolean isLessThanSixChars(final String passwordValue) {
        return passwordValue.length() < 6;
    }

    private boolean isMatched(
        final String retypePasswordValue, final String passwordValue) {
        return retypePasswordValue.equals(passwordValue);
    }

    private boolean isNameFieldValid() {
        return EmptyFieldValidator.isValid(nameField, "Please enter a "
            + ViewConstants.NAME_ID);
    }

    private boolean isLoginNameFieldValid() {
        return (EmptyFieldValidator.isValid(loginNameField, "Please enter a "
            + ViewConstants.LOGIN_NAME_ID));
    }

    private UpdatePasswordCommand updatePasswordCommand;

    private void setPasswordFor(final UserAccount createdUserAccount)
        throws EscidocClientException {
        initUpdatePasswordCommand(createdUserAccount);
        updatePasswordCommand.execute(enteredPassword());
    }

    private void initUpdatePasswordCommand(final UserAccount createdUserAccount) {
        Preconditions.checkNotNull(userService, "userService is null: %s",
            userService);
        updatePasswordCommand = new UpdatePasswordCommandImpl(userService);

        Preconditions.checkNotNull(createdUserAccount,
            "createdUserAccount is null: %s", createdUserAccount);
        updatePasswordCommand.setSelectedUserId(createdUserAccount.getObjid());
        updatePasswordCommand.setLastModificationDate(createdUserAccount
            .getLastModificationDate());
    }

    private String enteredPassword() {
        return (String) passwordView.getPasswordField().getValue();
    }

    private UserAccount createUserAccount() throws EscidocException,
        InternalClientException, TransportException {
        final UserAccount createdUserAccount =
            userService.create(nameProperty.getValue(),
                loginNameProperty.getValue());
        return createdUserAccount;
    }

    private void resetFields() {
        nameField.setComponentError(null);
        loginNameField.setComponentError(null);
        nameField.setValue(ViewConstants.EMPTY_STRING);
        loginNameField.setValue(ViewConstants.EMPTY_STRING);
    }
}