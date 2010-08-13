package de.escidoc.admintool.view.user.lab;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.data.Item;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;

import de.escidoc.admintool.app.AdminToolApplication;
import de.escidoc.admintool.service.UserService;
import de.escidoc.admintool.view.ViewConstants;
import de.escidoc.admintool.view.validator.EmptyFieldValidator;
import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;
import de.escidoc.core.resources.aa.useraccount.UserAccount;
import de.escidoc.vaadin.utilities.Converter;
import de.escidoc.vaadin.utilities.LayoutHelper;

@SuppressWarnings("serial")
public class UserLabEditForm extends CustomComponent implements ClickListener {
    private static final Logger log = LoggerFactory
        .getLogger(UserLabEditForm.class);

    private final AdminToolApplication app;

    private final UserService userService;

    private HorizontalLayout footer;

    private final Button save = new Button("Save", this);

    private final Button cancel = new Button("Cancel", this);

    private TextField nameField;

    private TextField loginNameField;

    private TextField objIdField;

    private Label modifiedOn;

    private Label modifiedBy;

    private Label createdOn;

    private Label createdBy;

    private CheckBox state;

    private Item item;

    public UserLabEditForm(final AdminToolApplication app,
        final UserService userService) {
        this.app = app;
        this.userService = userService;
        setSizeFull();
        init();
    }

    public void init() {
        Panel panel = new Panel();
        FormLayout form = new FormLayout();
        panel.setContent(form);
        form.setSpacing(false);

        panel.setCaption("Edit User Account");
        nameField = new TextField();
        nameField.setWidth("400px");
        nameField.setWriteThrough(false);
        int labelWidth = 100;
        int height = 15;
        panel.addComponent(LayoutHelper.create(ViewConstants.NAME_LABEL,
            nameField, labelWidth, true));
        loginNameField = new TextField();
        loginNameField.setWidth("400px");
        loginNameField.setWriteThrough(false);
        loginNameField.setReadOnly(true);
        panel.addComponent(LayoutHelper.create(ViewConstants.LOGIN_NAME_LABEL,
            loginNameField, labelWidth, false));

        objIdField = new TextField();
        objIdField.setReadOnly(true);
        panel.addComponent(LayoutHelper.create(ViewConstants.OBJECT_ID_LABEL,
            objIdField, labelWidth, false));

        modifiedOn = new Label();
        modifiedBy = new Label();
        panel.addComponent(LayoutHelper.create("Modified", "by", modifiedOn,
            modifiedBy, labelWidth, height, false));

        createdOn = new Label();
        createdBy = new Label();
        panel.addComponent(LayoutHelper.create("Created", "by", createdOn,
            createdBy, labelWidth, height, false));

        state = new CheckBox();
        state.setWriteThrough(false);
        panel.addComponent(LayoutHelper.create("Active status", state,
            labelWidth, false));

        panel.addComponent(addFooter());
        setCompositionRoot(panel);

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
            nameField.setValue("");
            loginNameField.setValue("");
        }
        else if (source == save) {
            try {
                boolean valid = true;
                valid =
                    EmptyFieldValidator.isValid(nameField, "Please enter a "
                        + ViewConstants.NAME_ID);
                valid &=
                    (EmptyFieldValidator.isValid(loginNameField,
                        "Please enter a " + ViewConstants.LOGIN_NAME_ID));
                if (valid) {
                    userService.update(getSelectedItemId(), (String) item
                        .getItemProperty(ViewConstants.NAME_ID).getValue());
                    if (state.isModified()) {
                        changeState();
                    }
                }
                nameField.setComponentError(null);
                loginNameField.setComponentError(null);
                nameField.commit();
                loginNameField.commit();
            }
            catch (final EscidocException e) {
                log.error("An unexpected error occured! See log for details.",
                    e);
                e.printStackTrace();
            }
            catch (final InternalClientException e) {
                log.error("An unexpected error occured! See log for details.",
                    e);
                e.printStackTrace();
            }
            catch (final TransportException e) {
                log.error("An unexpected error occured! See log for details.",
                    e);
                e.printStackTrace();
            }
            catch (EscidocClientException e) {
                log.error("An unexpected error occured! See log for details.",
                    e);
                e.printStackTrace();
            }
        }
    }

    public void setSelected(final Item item) {
        this.item = item;
        if (item != null) {
            nameField.setPropertyDataSource(item
                .getItemProperty(ViewConstants.NAME_ID));
            loginNameField.setPropertyDataSource(item
                .getItemProperty("properties.loginName"));
            objIdField.setPropertyDataSource(item.getItemProperty("objid"));
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
        }
    }

    private String getSelectedItemId() {
        if (item == null)
            return "";
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
}
