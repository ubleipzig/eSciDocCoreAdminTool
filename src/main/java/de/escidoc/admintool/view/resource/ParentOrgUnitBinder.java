package de.escidoc.admintool.view.resource;

import com.vaadin.data.Property;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;

import de.escidoc.core.resources.oum.Parents;

public class ParentOrgUnitBinder implements FieldsBinder {

    private final OrgUnitSpecificView orgUnitSpecificView;

    private Component toBeBind;

    private final Property parentProperty;

    public ParentOrgUnitBinder(final OrgUnitSpecificView orgUnitSpecificView,
        final Property parentProperty) {
        this.orgUnitSpecificView = orgUnitSpecificView;
        this.parentProperty = parentProperty;
    }

    @Override
    public void bindFields() {
        bind(orgUnitSpecificView.parentsValue).with(parentProperty);
    }

    private ParentOrgUnitBinder bind(final Component nameField) {
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

    private Parents getParentsFromItem(final Object propertyId) {
        return (Parents) orgUnitSpecificView.item
            .getItemProperty(propertyId).getValue();
    }

    private String getXLinkTitle(final Parents parents) {
        if (parents == null || parents.isEmpty()) {
            return "no parents";
        }
        return parents.get(0).getXLinkTitle();
    }
}