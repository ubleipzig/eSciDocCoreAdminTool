package de.escidoc.admintool.view.user.lab;

import com.vaadin.data.Item;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.DefaultFieldFactory;
import com.vaadin.ui.Field;
import com.vaadin.ui.Form;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

import de.escidoc.admintool.app.AdminToolApplication;
import de.escidoc.admintool.service.UserService;
import de.escidoc.admintool.view.PopUpJodaDateField;
import de.escidoc.admintool.view.ViewConstants;
import de.escidoc.admintool.view.validator.StringValidator;
import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;
import de.escidoc.core.resources.aa.useraccount.UserAccount;

@SuppressWarnings("serial")
public class UserLabEditForm extends Form implements ClickListener {

    private final AdminToolApplication app;

    private final UserService userService;

    public UserLabEditForm(final AdminToolApplication app,
        final UserService userService) {
        this.app = app;
        this.userService = userService;
        buildUI();
    }

    private void buildUI() {
        addStyleName("view");
        setSizeFull();
        buildCustomFields();
        addFooter();
        setWriteThrough(false);
        setInvalidCommitted(false);
        setValidationVisible(true);

    }

    private void buildCustomFields() {
        setFormFieldFactory(new DefaultFieldFactory() {

            @Override
            public Field createField(
                final Item item, final Object propertyId,
                final Component uiContext) {

                final Field field =
                    super.createField(item, propertyId, uiContext);

                if (propertyId.equals(ViewConstants.NAME_ID)) {
                    final TextField tf = (TextField) field;
                    tf.setCaption(ViewConstants.NAME_LABEL);
                    tf.setRequired(true);
                    tf.setRequiredError("Name is required");
                    tf.addValidator(new StringValidator("Name is required"));
                    tf.setWidth("400px");
                }
                else if (propertyId.equals("properties.loginName")) {
                    final TextField tf = (TextField) field;
                    tf.setCaption(ViewConstants.LOGIN_NAME_LABEL);
                    tf.setRequired(true);
                    tf.setRequiredError(ViewConstants.LOGIN_NAME_ID
                        + "is required");
                    tf.addValidator(new StringValidator(
                        ViewConstants.LOGIN_NAME_LABEL + " is required"));
                    tf.setWidth("400px");
                }
                else if (propertyId.equals("objid")) {
                    final TextField tf = (TextField) field;
                    tf.setCaption(ViewConstants.OBJECT_ID_LABEL);
                    tf.setReadOnly(true);
                }
                else if (propertyId.equals("properties.active")) {
                    field.setReadOnly(false);
                    field.setCaption("Active status");
                }
                else if (propertyId.equals("properties.creationDate")) {
                    final PopUpJodaDateField popUpDateField =
                        new PopUpJodaDateField(ViewConstants.CREATED_ON_LABEL);
                    popUpDateField.setResolution(PopupDateField.RESOLUTION_DAY);
                    popUpDateField.setReadOnly(true);
                    return popUpDateField;
                }
                else if (propertyId.equals("properties.createdBy.objid")) {
                    final TextField tf =
                        new TextField(ViewConstants.CREATED_BY_LABEL);
                    tf.setReadOnly(true);
                    return tf;
                }
                else if (propertyId.equals("lastModificationDate")) {
                    final PopUpJodaDateField popUpDateField =
                        new PopUpJodaDateField(ViewConstants.MODIFIED_ON_LABEL);
                    popUpDateField.setResolution(PopupDateField.RESOLUTION_DAY);
                    popUpDateField.setReadOnly(true);
                    return popUpDateField;
                }
                else if (propertyId.equals("properties.createdBy.objid")) {
                    final PopUpJodaDateField popUpDateField =
                        new PopUpJodaDateField(ViewConstants.CREATED_ON_LABEL);
                    popUpDateField.setResolution(PopupDateField.RESOLUTION_DAY);
                    popUpDateField.setReadOnly(true);
                    return popUpDateField;
                }
                else if (propertyId.equals("properties.modifiedBy.objid")) {
                    final TextField tf =
                        new TextField(ViewConstants.MODIFIED_BY_LABEL);
                    tf.setReadOnly(true);
                    return tf;
                }
                field.setWidth("400px");
                return field;
            }
        });
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
        }
        else if (source == save) {
            if (!isValid()) {
                return;
            }
            try {
                // TODO refactor if-else. not OO
                if (getField("properties.active").isModified()) {
                    userService.update(getSelectedItemId(), (String) getField(
                        "properties.name").getValue(), (Boolean) getField(
                        "properties.active").getValue());
                }
                else {
                    userService.update(getSelectedItemId(), (String) getField(
                        "properties.name").getValue());
                }
                commit();
                setComponentError(null);
            }
            catch (final EscidocException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            catch (final InternalClientException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            catch (final TransportException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            catch (final EscidocClientException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public void setSelected(final Item item) {
        if (item != getItemDataSource()) {
            this.setItemDataSource(item);
        }
    }

    private String getSelectedItemId() {
        return (String) getItemDataSource().getItemProperty("objid").getValue();
    }

    public UserAccount deleteUser() throws EscidocException,
        InternalClientException, TransportException {
        return userService.delete(getSelectedItemId());
    }
}
