package de.escidoc.admintool.view.resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.data.Property;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

import de.escidoc.admintool.app.PropertyId;
import de.escidoc.admintool.view.ModalDialog;
import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.client.exceptions.application.invalid.InvalidStatusException;
import de.escidoc.core.resources.oum.OrganizationalUnit;

final class RemoveParentListener implements ClickListener {
    private static final long serialVersionUID = 1887055528394173137L;

    private static final Logger LOG = LoggerFactory
        .getLogger(RemoveParentListener.class);

    private final OrgUnitSpecificView orgUnitSpecificView;

    private Property parentProperty;

    private OrganizationalUnit child;

    RemoveParentListener(final OrgUnitSpecificView orgUnitSpecificView) {
        this.orgUnitSpecificView = orgUnitSpecificView;
    }

    @Override
    public void buttonClick(final ClickEvent event) {
        removeParent();
    }

    private void removeParent() {
        try {
            child = retrieveOrgUnit(getSelectedOrgUnitId());
            updatePersistence(child);
            updateResourceContainer(child);
            updateItem(child);
        }
        catch (final EscidocClientException e) {
            if (e instanceof InvalidStatusException) {
                ModalDialog.show(orgUnitSpecificView.mainWindow, "Parent of "
                    + child.getXLinkTitle() + " is not in status opened.");
            }
            else {
                LOG.warn("Internal server error.", e);
                ModalDialog.show(orgUnitSpecificView.mainWindow, e);
            }
        }
    }

    private void updateItem(final Object child) {
        parentProperty.setValue("no parents");
    }

    private void updateResourceContainer(final OrganizationalUnit child) {
        orgUnitSpecificView.resourceContainer.removeParent(child);
    }

    private OrganizationalUnit retrieveOrgUnit(final String objectId)
        throws EscidocClientException {
        return orgUnitSpecificView.orgUnitService.findById(objectId);
    }

    private String getSelectedOrgUnitId() {
        return (String) orgUnitSpecificView.item.getItemProperty(
            PropertyId.OBJECT_ID).getValue();
    }

    protected void updatePersistence(final OrganizationalUnit child)
        throws EscidocClientException {
        orgUnitSpecificView.orgUnitService.removeParent(child);
    }

    public void setParentProperty(final Property parentProperty) {
        this.parentProperty = parentProperty;
    }
}