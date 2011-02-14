package de.escidoc.admintool.view.resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

import de.escidoc.admintool.app.PropertyId;
import de.escidoc.admintool.view.ModalDialog;
import de.escidoc.admintool.view.ViewConstants;
import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.client.exceptions.application.invalid.InvalidStatusException;
import de.escidoc.core.resources.oum.OrganizationalUnit;
import de.escidoc.core.resources.oum.Parent;
import de.escidoc.core.resources.oum.Parents;

final class RemoveParentListener implements ClickListener {

    private static final long serialVersionUID = 1887055528394173137L;

    private static final Logger LOG = LoggerFactory
        .getLogger(RemoveParentListener.class);

    private final OrgUnitSpecificView orgUnitSpecificView;

    private Property parentProperty;

    private OrganizationalUnit selectedOrgUnit;

    RemoveParentListener(final OrgUnitSpecificView orgUnitSpecificView) {
        this.orgUnitSpecificView = orgUnitSpecificView;
    }

    @Override
    public void buttonClick(final ClickEvent event) {
        if (getSelectedOrgUnitId().equals(ViewConstants.EMPTY_STRING)) {
            return;
        }
        removeParent();
    }

    private void removeParent() {
        try {
            retrieveOrgUnit(getSelectedOrgUnitId());
            updatePersistence();
            updateResourceContainer();
            updateItem();
            updateView();
        }
        catch (final EscidocClientException e) {
            if (e instanceof InvalidStatusException) {
                ModalDialog.show(orgUnitSpecificView.mainWindow, "Parent of "
                    + selectedOrgUnit.getXLinkTitle()
                    + " is not in status opened.");
            }
            else {
                LOG.warn("Internal server error.", e);
                ModalDialog.show(orgUnitSpecificView.mainWindow, e);
            }
        }
    }

    private void updateItem() {
        final Parent parent = null;
        if (parent == null) {
            // getParentProperty().setValue("no parents");
            getParentProperty().setValue(new ResourceRefDisplay());
        }

        final Item updateItem = orgUnitSpecificView.item;

        final Property itemProperty =
            updateItem.getItemProperty(PropertyId.PARENTS);
        itemProperty.setValue(new Parents());
    }

    private Property getParentProperty() {
        return parentProperty;
    }

    protected void updatePersistence() throws EscidocClientException {
        orgUnitSpecificView.orgUnitService.removeParent(selectedOrgUnit);
    }

    private void updateResourceContainer() {
        orgUnitSpecificView.resourceContainer.removeParent(selectedOrgUnit);
    }

    private void updateView() {
        parentProperty.setValue(new ResourceRefDisplay());
    }

    private OrganizationalUnit retrieveOrgUnit(final String objectId)
        throws EscidocClientException {
        return selectedOrgUnit =
            orgUnitSpecificView.orgUnitService.findById(objectId);
    }

    private String getSelectedOrgUnitId() {
        if (orgUnitSpecificView.item == null) {
            return ViewConstants.EMPTY_STRING;
        }
        return (String) orgUnitSpecificView.item.getItemProperty(
            PropertyId.OBJECT_ID).getValue();
    }

    public void setParentProperty(final Property parentProperty) {
        this.parentProperty = parentProperty;
    }
}