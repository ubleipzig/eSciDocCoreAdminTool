package de.escidoc.admintool.view.user;

import com.vaadin.data.Item;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.DefaultFieldFactory;
import com.vaadin.ui.Field;
import com.vaadin.ui.Form;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.TextField;

import de.escidoc.admintool.service.UserService;
import de.escidoc.admintool.view.ViewConstants;
import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;

@SuppressWarnings("serial")
public class UserEditForm extends Form implements ClickListener {

    private final UserService service;

    public UserEditForm(final UserService userService) {
        service = userService;
        buildUI();
    }

    // TODO duplicate, create an abstract class
    private void buildUI() {
        addFooter();
        buildCustomFields();

        setWriteThrough(false);
        setInvalidCommitted(false);
        // TODO test if it works.
        setImmediate(true);
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
                    tf.setRequired(true);
                    tf.setRequiredError("Please enter a "
                        + ViewConstants.NAME_ID);

                    tf.setColumns(20);
                }
                else if (propertyId.equals(ViewConstants.CREATED_ON_ID)
                    || propertyId.equals(ViewConstants.MODIFIED_ON_ID)) {
                    final PopupDateField popUpDateField =
                        new PopupDateField(ViewConstants.MODIFIED_ON_LABEL);
                    popUpDateField.setResolution(PopupDateField.RESOLUTION_DAY);

                    return popUpDateField;
                }
                field.setWidth("400px");
                return field;
            }
        });
    }

    private HorizontalLayout footer;

    private final Button save = new Button("Save", (ClickListener) this);

    private final Button cancel = new Button("Cancel", (ClickListener) this);

    private final Button edit = new Button("Edit", (ClickListener) this);

    private void addFooter() {
        footer = new HorizontalLayout();
        footer.setSpacing(true);

        footer.addComponent(save);
        footer.addComponent(cancel);
        footer.addComponent(edit);
        footer.setVisible(false);

        setFooter(footer);
    }

    public void showFooter() {
        footer.setVisible(true);
    }

    public void setSelected(final Item item) {
        if (item != getItemDataSource()) {
            this.setItemDataSource(item);
        }
        showFooter();
        setReadOnly(true);
    }

    @Override
    public void setReadOnly(final boolean readOnly) {
        super.setReadOnly(readOnly);

        save.setVisible(!readOnly);
        cancel.setVisible(!readOnly);
        edit.setVisible(readOnly);

        getField(ViewConstants.LOGIN_NAME_ID).setReadOnly(true);
        getField(ViewConstants.OBJECT_ID).setReadOnly(true);
        getField(ViewConstants.CREATED_BY_ID).setReadOnly(true);
        getField(ViewConstants.MODIFIED_ON_ID).setReadOnly(true);
        getField(ViewConstants.CREATED_ON_ID).setReadOnly(true);
        getField(ViewConstants.MODIFIED_BY_ID).setReadOnly(true);
    }

    public void buttonClick(final ClickEvent event) {

        final Button source = event.getButton();
        if (source == edit) {
            setReadOnly(false);
        }
        else if (source == cancel) {
            discard();
            setReadOnly(true);
        }
        else if (source == save) {
            if (!isValid()) {
                return;
            }
            try {
                // TODO refactor if-else. not OO
                if (getField(ViewConstants.IS_ACTIVE_ID).isModified()) {
                    service.update(getSelectedItemId(), (String) getField(
                        ViewConstants.NAME_ID).getValue(), (Boolean) getField(
                        ViewConstants.IS_ACTIVE_ID).getValue());
                }
                else {
                    service.update(getSelectedItemId(), (String) getField(
                        ViewConstants.NAME_ID).getValue());
                }
                commit();
                setReadOnly(true);
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
            setReadOnly(true);
        }
    }

    private String getSelectedItemId() {
        return (String) getItemDataSource().getItemProperty(
            ViewConstants.OBJECT_ID).getValue();
    }
}