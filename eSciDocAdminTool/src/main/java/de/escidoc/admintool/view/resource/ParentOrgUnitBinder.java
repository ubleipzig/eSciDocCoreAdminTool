package de.escidoc.admintool.view.resource;

import com.vaadin.data.Property;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;
import com.vaadin.ui.Label;

public class ParentOrgUnitBinder implements FieldsBinder {

    private final OrgUnitSpecificView orgUnitSpecificView;

    private Component toBeBind;

    private final Property parentProperty;

    public ParentOrgUnitBinder(final OrgUnitSpecificView orgUnitSpecificView, final Property parentProperty) {
        this.orgUnitSpecificView = orgUnitSpecificView;
        this.parentProperty = parentProperty;
    }

    @Override
    public void bindFields() {
        bind(orgUnitSpecificView.parentsField).with(parentProperty);
    }

    private ParentOrgUnitBinder bind(final Component nameField) {
        toBeBind = nameField;
        return this;
    }

    private void with(final Property itemProperty) {
        if (toBeBind instanceof Label) {
            ((Label) toBeBind).setPropertyDataSource(itemProperty);
        }
        else if (toBeBind instanceof Field) {
            ((Field) toBeBind).setPropertyDataSource(itemProperty);
        }
    }
}