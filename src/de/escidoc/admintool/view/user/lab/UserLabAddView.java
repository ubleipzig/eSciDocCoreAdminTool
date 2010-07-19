package de.escidoc.admintool.view.user.lab;

import com.vaadin.terminal.UserError;
import com.vaadin.ui.Button;
import com.vaadin.ui.Form;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

import de.escidoc.admintool.app.AdminToolApplication;
import de.escidoc.admintool.service.UserService;
import de.escidoc.admintool.view.ViewConstants;
import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;
import de.escidoc.core.resources.aa.useraccount.UserAccount;

public class UserLabAddView extends Form implements ClickListener {

    private final AdminToolApplication app;

    private final UserLabListView userLabList;

    private final UserService userService;

    public UserLabAddView(final AdminToolApplication app,
        final UserLabListView userLabList, final UserService userService) {
        addStyleName("view");
        this.app = app;
        this.userLabList = userLabList;
        this.userService = userService;
        buildUI();
    }

    private void buildUI() {
        setCaption("Add a new User Account");
        addNameField().addLoginNameField();
        // .addGrantAddView();
        addFooter();
        setWriteThrough(false);
        setInvalidCommitted(false);
        // setValidationVisible(true);
    }

    private UserLabAddView addNameField() {
        final TextField nameField = new TextField(ViewConstants.NAME_LABEL);
        addField(ViewConstants.NAME_ID, nameField);
        nameField.setRequired(true);
        nameField.setRequiredError("Please enter a " + ViewConstants.NAME_ID);
        return this;
    }

    private UserLabAddView addLoginNameField() {
        final TextField loginNameField =
            new TextField(ViewConstants.LOGIN_NAME_LABEL);
        addField(ViewConstants.LOGIN_NAME_ID, loginNameField);
        loginNameField.setRequired(true);
        loginNameField.setRequiredError("Please enter a "
            + ViewConstants.LOGIN_NAME_ID);
        return this;
    }

    private UserLabAddView addGrantAddView() {
        getLayout().addComponent(new GrantAddView());
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
                        userService.create((String) getField(
                            ViewConstants.NAME_ID).getValue(),
                            (String) getField(ViewConstants.LOGIN_NAME_ID)
                                .getValue());
                    commit();
                    userLabList.addUser(createdUserAccount);
                    userLabList.select(createdUserAccount);

                    getField(ViewConstants.NAME_ID).setValue("");
                    getField(ViewConstants.LOGIN_NAME_ID).setValue("");
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
}