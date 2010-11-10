package de.escidoc.admintool.view.resource;

import com.vaadin.data.Property;
import com.vaadin.ui.Field;
import com.vaadin.ui.TextField;

import de.escidoc.admintool.app.PropertyId;

class FieldsBinder {

    /**
     * 
     */
    private final PropertiesFieldsImpl propertiesFields;

    /**
     * @param propertiesFieldsImpl
     */
    FieldsBinder(final PropertiesFieldsImpl propertiesFieldsImpl) {
        propertiesFields = propertiesFieldsImpl;
    }

    private Field toBeBind;

    void bindFields() {
        bind(propertiesFields.titleField).with(getProperty(PropertyId.NAME));

        bind(propertiesFields.descField).with(
            getProperty(PropertyId.DESCRIPTION));

        bind(propertiesFields.modifiedOn).with(
            getProperty(PropertyId.LAST_MODIFICATION_DATE));

        bind(propertiesFields.modifiedBy).with(
            getProperty(PropertyId.MODIFIED_BY));

        bind(propertiesFields.createdBy).with(
            getProperty(PropertyId.CREATED_BY));
        bind(propertiesFields.createdOn).with(
            getProperty(PropertyId.CREATED_ON));

        bind(propertiesFields.statusComment).with(
            getProperty(PropertyId.PUBLIC_STATUS_COMMENT));

        bind(propertiesFields.statusField).with(
            getProperty(PropertyId.PUBLIC_STATUS));
        bind(propertiesFields.statusComment).with(
            getProperty(PropertyId.PUBLIC_STATUS_COMMENT));

    }

    private Property getProperty(final Object id) {
        return propertiesFields.item.getItemProperty(id);
    }

    private FieldsBinder bind(final TextField nameField) {
        toBeBind = nameField;
        return this;
    }

    private void with(final Property itemProperty) {
        toBeBind.setPropertyDataSource(itemProperty);
    }
}