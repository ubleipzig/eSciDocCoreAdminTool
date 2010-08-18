package de.escidoc.admintool.view.user.lab;

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
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;

import de.escidoc.admintool.app.AdminToolApplication;
import de.escidoc.admintool.service.UserService;
import de.escidoc.admintool.view.OrgUnitEditor;
import de.escidoc.admintool.view.ViewConstants;
import de.escidoc.admintool.view.validator.EmptyFieldValidator;
import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;
import de.escidoc.core.resources.aa.useraccount.UserAccount;
import de.escidoc.vaadin.utilities.LayoutHelper;

public class UserLabAddView extends CustomComponent implements ClickListener {
    private static final long serialVersionUID = 3007285643463919742L;

    private static final Logger log = LoggerFactory
        .getLogger(UserLabAddView.class);

    private final UserLabListView userLabList;

    private final UserService userService;

    private HorizontalLayout footer;

    private final Button save = new Button("Save", this);

    private final Button cancel = new Button("Cancel", this);

    private TextField nameField;

    private TextField loginNameField;

    private ObjectProperty nameProperty;

    private ObjectProperty loginNameProperty;

    private final ListSelect orgUnitList = new ListSelect();

    private final Button addOrgUnitButton = new Button(ViewConstants.ADD_LABEL);

    private final Button removeOrgUnitButton = new Button(
        ViewConstants.REMOVE_LABEL);

    public UserLabAddView(final AdminToolApplication app,
        final UserLabListView userLabList, final UserService userService) {
        this.userLabList = userLabList;
        this.userService = userService;
        init();
    }

    public void init() {
        Panel panel = new Panel();
        FormLayout form = new FormLayout();
        int labelWidth = 100;
        panel.setContent(form);
        form.setSpacing(false);
        panel.setCaption("Add a new User Account");
        panel.addComponent(LayoutHelper.create(ViewConstants.NAME_LABEL,
            nameField = new TextField(), labelWidth, true));
        nameProperty = new ObjectProperty("", String.class);
        nameField.setPropertyDataSource(nameProperty);
        nameField.setWidth("400px");

        panel.addComponent(LayoutHelper.create(ViewConstants.LOGIN_NAME_LABEL,
            loginNameField = new TextField(), labelWidth, true));

        loginNameProperty = new ObjectProperty("", String.class);
        loginNameField.setPropertyDataSource(loginNameProperty);
        loginNameField.setWidth("400px");

        orgUnitList.setRows(5);
        orgUnitList.setWidth("400px");
        orgUnitList.setNullSelectionAllowed(true);
        orgUnitList.setMultiSelect(true);
        orgUnitList.setImmediate(true);

        form.addComponent(LayoutHelper.create(
            ViewConstants.ORGANIZATION_UNITS_LABEL, new OrgUnitEditor(
                ViewConstants.ORGANIZATION_UNITS_LABEL, orgUnitList,
                addOrgUnitButton, removeOrgUnitButton), labelWidth, 140, false,
            new Button[] { addOrgUnitButton, removeOrgUnitButton }));

        panel.addComponent(addFooter());
        setCompositionRoot(panel);
    }

    private HorizontalLayout addFooter() {
        footer = new HorizontalLayout();
        footer.setSpacing(true);
        footer.addComponent(LayoutHelper.create(save));
        footer.addComponent(LayoutHelper.create(cancel));
        footer.setVisible(true);
        return footer;
    }

    public void buttonClick(final ClickEvent event) {
        final Button source = event.getButton();

        if (source == cancel) {
            nameField.setValue("");
            loginNameField.setValue("");
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
                nameField.setComponentError(null);
                loginNameField.setComponentError(null);

                try {
                    final UserAccount createdUserAccount =
                        userService.create((String) nameProperty.getValue(),
                            (String) loginNameProperty.getValue());
                    userLabList.addUser(createdUserAccount);
                    userLabList.select(createdUserAccount);
                    nameField.setValue("");
                    loginNameField.setValue("");
                }
                catch (final EscidocException e) {
                    String error =
                        "A user with login name "
                            + (String) nameProperty.getValue()
                            + " already exist.";

                    loginNameField.setComponentError(new UserError(error));
                    e.printStackTrace();
                    log.error(error, e);
                }
                catch (final InternalClientException e) {
                    log.error(
                        "An unexpected error occured! See log for details.", e);
                    e.printStackTrace();
                }
                catch (final TransportException e) {
                    log.error(
                        "An unexpected error occured! See log for details.", e);
                    e.printStackTrace();
                }
            }
        }
    }
}