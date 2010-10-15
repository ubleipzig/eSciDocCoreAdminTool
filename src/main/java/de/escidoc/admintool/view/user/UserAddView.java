package de.escidoc.admintool.view.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.data.util.ObjectProperty;
import com.vaadin.terminal.UserError;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.Reindeer;

import de.escidoc.admintool.app.AdminToolApplication;
import de.escidoc.admintool.service.OrgUnitService;
import de.escidoc.admintool.service.UserService;
import de.escidoc.admintool.view.ViewConstants;
import de.escidoc.admintool.view.validator.EmptyFieldValidator;
import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;
import de.escidoc.core.resources.aa.useraccount.UserAccount;
import de.escidoc.vaadin.utilities.LayoutHelper;

public class UserAddView extends CustomComponent implements ClickListener {

    private static final long serialVersionUID = 3007285643463919742L;

    private static final Logger log = LoggerFactory
        .getLogger(UserAddView.class);

    private static final String USER_ADD_VIEW_CAPTION =
        "Add a new User Account";

    private static final String FIELD_WIDTH = "400px";

    private static final int LABEL_WIDTH = 100;

    private final Panel panel = new Panel();

    private final FormLayout form = new FormLayout();

    private final HorizontalLayout footer = new HorizontalLayout();

    private final Button save = new Button("Save", this);

    private final Button cancel = new Button("Cancel", this);

    private final UserListView userListView;

    private final UserService userService;

    private final OrgUnitService orgUnitService;

    private TextField nameField;

    private TextField loginNameField;

    private ObjectProperty nameProperty;

    private ObjectProperty loginNameProperty;

    public UserAddView(final AdminToolApplication app,
        final UserListView userListView, final UserService userService,
        final OrgUnitService orgUnitService) {
        this.userListView = userListView;
        this.userService = userService;
        this.orgUnitService = orgUnitService;
        init();
    }

    public void init() {
        setCompositionRoot(panel);
        setStyleName("view");
        panel.setStyleName(Reindeer.PANEL_LIGHT);
        panel.setContent(form);
        form.setSpacing(false);
        panel.setCaption(USER_ADD_VIEW_CAPTION);

        // Name
        panel.addComponent(LayoutHelper.create(ViewConstants.NAME_LABEL,
            nameField = new TextField(), LABEL_WIDTH, true));
        nameProperty = new ObjectProperty("", String.class);
        nameField.setPropertyDataSource(nameProperty);
        nameField.setWidth(FIELD_WIDTH);

        // Login
        panel.addComponent(LayoutHelper.create(ViewConstants.LOGIN_NAME_LABEL,
            loginNameField = new TextField(), LABEL_WIDTH, true));
        loginNameProperty = new ObjectProperty("", String.class);
        loginNameField.setPropertyDataSource(loginNameProperty);
        loginNameField.setWidth(FIELD_WIDTH);
        panel.addComponent(addFooter());
    }

    private HorizontalLayout addFooter() {
        footer.setSpacing(true);
        footer.addComponent(LayoutHelper.create(save));
        footer.addComponent(LayoutHelper.create(cancel));
        footer.setVisible(true);
        return footer;
    }

    @Override
    public void buttonClick(final ClickEvent event) {
        final Button source = event.getButton();
        if (source == cancel) {
            resetFields();
        }
        else if (source == save) {
            boolean valid = true;
            valid =
                EmptyFieldValidator.isValid(nameField, "Please enter a "
                    + ViewConstants.NAME_ID);
            valid &=
                (EmptyFieldValidator.isValid(loginNameField, "Please enter a "
                    + ViewConstants.LOGIN_NAME_ID));

            if (valid) {
                try {
                    final UserAccount createdUserAccount =
                        userService.create((String) nameProperty.getValue(),
                            (String) loginNameProperty.getValue());

                    userListView.addUser(createdUserAccount);
                    userListView.select(createdUserAccount);

                    resetFields();
                }
                catch (final EscidocException e) {
                    final String error =
                        "A user with login name "
                            + (String) nameProperty.getValue()
                            + " already exist.";
                    loginNameField.setComponentError(new UserError(error));
                    ;
                    log.error(error, e);
                }
                catch (final InternalClientException e) {
                    log.error(
                        "An unexpected error occured! See log for details.", e);
                    ;
                }
                catch (final TransportException e) {
                    log.error(
                        "An unexpected error occured! See log for details.", e);
                    ;
                }
            }
        }
    }

    private void resetFields() {
        nameField.setComponentError(null);
        loginNameField.setComponentError(null);
        nameField.setValue("");
        loginNameField.setValue("");
    }
}