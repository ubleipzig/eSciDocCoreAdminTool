package de.escidoc.admintool.view.user;

import java.io.Serializable;

import com.vaadin.terminal.UserError;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Form;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;

import de.escidoc.admintool.app.AdminToolApplication;
import de.escidoc.admintool.service.UserService;
import de.escidoc.admintool.view.ViewConstants;
import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;
import de.escidoc.core.resources.aa.useraccount.UserAccount;

@SuppressWarnings("serial")
public class UserAddForm extends Form implements ClickListener, Serializable {

    private final UserService service;

    private final UserList userList;

    private final AdminToolApplication app;

    public UserAddForm(final UserService userService, final UserList userList,
        final AdminToolApplication app) {
        assert userService != null : "userService must not be null.";
        assert userList != null : "userService must not be null.";
        assert app != null : "app must not be null.";

        service = userService;
        this.userList = userList;
        this.app = app;
        buildUI();
    }

    public void buildUI() {
        setCaption("Add a new User");
        addNameField().addLoginNameField();
        addFooter();
        setWriteThrough(false);
        setInvalidCommitted(false);
        // TODO test if it works.
        // this.setImmediate(true);
    }

    private UserAddForm addNameField() {
        final TextField nameField = new TextField(ViewConstants.NAME_ID);
        addField(ViewConstants.NAME_ID, nameField);
        nameField.setRequired(true);
        nameField.setRequiredError("Please enter a " + ViewConstants.NAME_ID);
        return this;
    }

    private UserAddForm addLoginNameField() {
        final TextField loginNameField =
            new TextField(ViewConstants.LOGIN_NAME_ID);
        addField(ViewConstants.LOGIN_NAME_ID, loginNameField);
        loginNameField.setRequired(true);
        loginNameField.setRequiredError("Please enter a "
            + ViewConstants.LOGIN_NAME_ID);
        return this;
    }

    private HorizontalLayout footer;

    private final Button save = new Button("Save", (ClickListener) this);

    private final Button cancel = new Button("Cancel", (ClickListener) this);

    private void addFooter() {
        footer = new HorizontalLayout();
        footer.setSpacing(true);
        footer.addComponent(save);
        footer.addComponent(cancel);
        footer.setVisible(true);
        setFooter(footer);
    }

    public void buttonClick(final ClickEvent event) {
        final Button source = event.getButton();

        if (source == cancel) {
            discard();
            // TODO fix these, this.discard should actually do the trick.
            getField(ViewConstants.NAME_ID).setValue("");
            getField(ViewConstants.LOGIN_NAME_ID).setValue("");
            setVisible(false);
            setReadOnly(true);
        }
        else if (source == save) {
            setValidationVisible(true);
            validate();
            if (isValid()) {
                try {
                    final UserAccount createdUserAccount =
                        service.create((String) getField(ViewConstants.NAME_ID)
                            .getValue(), (String) getField(
                            ViewConstants.LOGIN_NAME_ID).getValue());
                    commit();
                    userList.addUser(createdUserAccount);
                    getField(ViewConstants.NAME_ID).setValue("");
                    getField(ViewConstants.LOGIN_NAME_ID).setValue("");
                    app.showUsersView();
                }
                // TODO log exception
                // TODO report appropriate error message to the user.
                catch (final EscidocException e) {
                    ((TextField) getField(ViewConstants.LOGIN_NAME_ID))
                        .setComponentError(new UserError(
                            "A user with login name "
                                + getField(ViewConstants.LOGIN_NAME_ID)
                                    .getValue() + " already exist."));
                    e.printStackTrace();
                }
                catch (final InternalClientException e) {
                    e.printStackTrace();
                }
                catch (final TransportException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    @Override
    public void setReadOnly(final boolean readOnly) {
        super.setReadOnly(readOnly);
        save.setVisible(!readOnly);
        cancel.setVisible(!readOnly);
    }
}