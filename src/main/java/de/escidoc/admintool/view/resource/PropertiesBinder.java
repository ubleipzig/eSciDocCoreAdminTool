package de.escidoc.admintool.view.resource;

import org.joda.time.DateTime;

import com.vaadin.data.Property;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;

import de.escidoc.admintool.app.PropertyId;
import de.escidoc.admintool.view.util.Converter;

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

        bind(propertiesFields.objectId).with(getProperty(PropertyId.OBJECT_ID));
        bindModifienOn();

        bind(propertiesFields.modifiedBy).with(
            getProperty(PropertyId.MODIFIED_BY));

        bind(propertiesFields.createdBy).with(
            getProperty(PropertyId.CREATED_BY));

        bindCreatedOn();

        bind(propertiesFields.statusField).with(
            getProperty(PropertyId.PUBLIC_STATUS));

        bind(propertiesFields.statusComment).with(
            getProperty(PropertyId.PUBLIC_STATUS_COMMENT));

    }

    private void bindCreatedOn() {
        final Object value = getProperty(PropertyId.CREATED_ON).getValue();
        final DateTime dateTime = (DateTime) value;
        propertiesFields.createdOn.setCaption(Converter
            .dateTimeToString(dateTime));
    }

    private void bindModifienOn() {
        final Object value =
            getProperty(PropertyId.LAST_MODIFICATION_DATE).getValue();
        final DateTime dateTime = (DateTime) value;
        propertiesFields.modifiedOn.setCaption(Converter
            .dateTimeToString(dateTime));
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