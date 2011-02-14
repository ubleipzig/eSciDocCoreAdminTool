package de.escidoc.admintool.view.resource;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;

public class MetadataBinder implements FieldsBinder {

    private Component toBeBind;

    private Item item;

    @Override
    public void bindFields() {
        // TODO

    }

    private Property getProperty(final Object id) {
        return item.getItemProperty(id);
    }

    private MetadataBinder bind(final Component nameField) {
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
