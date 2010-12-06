package de.escidoc.admintool.view.resource;

import com.vaadin.data.Property;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;

import de.escidoc.admintool.app.PropertyId;

public class PropertiesBinder implements FieldsBinder {

    private final PropertiesFieldsImpl propertiesFields;

    private Component toBeBind;

    /**
     * @param propertiesFieldsImpl
     */
    PropertiesBinder(final PropertiesFieldsImpl propertiesFieldsImpl) {
        propertiesFields = propertiesFieldsImpl;
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.escidoc.admintool.view.resource.IFieldsBinder#bindFields()
     */

    @Override
    public void bindFields() {
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

        bind(propertiesFields.statusField).with(
            getProperty(PropertyId.PUBLIC_STATUS));

        bind(propertiesFields.statusComment).with(
            getProperty(PropertyId.PUBLIC_STATUS_COMMENT));

    }

    private Property getProperty(final Object id) {
        return propertiesFields.item.getItemProperty(id);
    }

    private PropertiesBinder bind(final Component nameField) {
        toBeBind = nameField;
        return this;
    }

    private void with(final Property itemProperty) {
        if (toBeBind instanceof Label) {
            ((Label) toBeBind).setPropertyDataSource(itemProperty);
        }
        else {
            ((com.vaadin.ui.Field) toBeBind)
                .setPropertyDataSource(itemProperty);
        }
    }
}