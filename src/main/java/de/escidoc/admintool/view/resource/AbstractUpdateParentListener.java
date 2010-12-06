package de.escidoc.admintool.view.resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Window;

import de.escidoc.admintool.app.PropertyId;
import de.escidoc.admintool.service.ResourceService;
import de.escidoc.admintool.view.ModalDialog;
import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.resources.oum.OrganizationalUnit;
import de.escidoc.core.resources.oum.Parent;
import de.escidoc.core.resources.oum.Parents;

public abstract class AbstractUpdateParentListener implements ClickListener {

    private static final long serialVersionUID = 6131626748789790237L;

    private static final Logger LOG = LoggerFactory
        .getLogger(AbstractUpdateParentListener.class);

    private final OrgUnitSpecificView orgUnitSpecificView;

    public AbstractUpdateParentListener(
        final OrgUnitSpecificView orgUnitSpecificView) {
        Preconditions.checkNotNull(orgUnitSpecificView,
            "orgUnitSpecificView is null: %s", orgUnitSpecificView);
        this.orgUnitSpecificView = orgUnitSpecificView;
    }

    @Override
    public void buttonClick(final ClickEvent event) {
        onClick();
    }

    protected void onClick() {
        try {
            final OrganizationalUnit child =
                retrieveOrgUnit(getSelectedOrgUnitId());
            updatePersistence(child);
            updateResourceContainer(child);
            updateItem(child);
            checkPostConditions(child);
        }
        catch (final EscidocClientException e) {
            LOG.warn("Internal server error.", e);
            ModalDialog.show(orgUnitSpecificView.mainWindow, e);
        }
    }

    protected String getSelectedOrgUnitId() {
        return (String) orgUnitSpecificView.item.getItemProperty(
            PropertyId.OBJECT_ID).getValue();
    }

    private void updatePersistence(final OrganizationalUnit child)
        throws EscidocClientException {
        final OrganizationalUnit updatedChild =
            orgUnitSpecificView.orgUnitService.updateParent(child,
                getSelectedParentId());
        Preconditions.checkNotNull(updatedChild, "updatedChild is null: %s",
            updatedChild);
    }

    private String getSelectedParentId() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    protected OrganizationalUnit retrieveOrgUnit(final String objectId)
        throws EscidocClientException {
        return orgUnitSpecificView.orgUnitService.findById(objectId);
    }

    private void updateResourceContainer(final OrganizationalUnit child)
        throws EscidocClientException {
        orgUnitSpecificView.resourceContainer.updateParent(child,
            getSelectedParent());
    }

    protected OrganizationalUnit getSelectedParent()
        throws EscidocClientException {
        return retrieveOrgUnit(getSelectedParentId());
    }

    private void updateItem(final Object child) {
        final Item updateItem =
            orgUnitSpecificView.resourceContainer.getContainer().getItem(child);
        final Property itemProperty =
            updateItem.getItemProperty(PropertyId.PARENTS);

        final Parents newParents = new Parents();
        newParents.add(new Parent(getSelectedParentId()));
        itemProperty.setValue(newParents);
    }

    private Item getItemFor(final OrganizationalUnit child) {
        return orgUnitSpecificView.resourceContainer.getContainer().getItem(
            child);
    }

    @SuppressWarnings("boxing")
    private void checkPostConditions(final OrganizationalUnit child) {
        final Item updateItem = getItemFor(child);
        Preconditions.checkNotNull(updateItem, "updateItem is null: %s",
            updateItem);

        final Property parentProperty = getParentProperty(updateItem);
        Preconditions.checkNotNull(parentProperty, "itemProperty is null: %s",
            parentProperty);

        final Object value = parentProperty.getValue();
        if (value instanceof Parents) {
            final Parents parents = (Parents) value;

            Preconditions.checkNotNull(parents, "parents is null: %s", parents);

            Preconditions.checkArgument(parents.size() <= 1,
                "Org unit has more than one parent.", parents.size());

            Preconditions.checkArgument(parents.size() == 1,
                "Parent in Item is not updated", parents.size());
        }
        else {
            throw new IllegalArgumentException(
                "The value of ParentProperty is not instance of Parents");
        }
    }

    private Property getParentProperty(final Item updateItem) {
        return updateItem.getItemProperty(PropertyId.PARENTS);
    }

    public Window getMainWindow() {
        return orgUnitSpecificView.mainWindow;
    }

    public ResourceService getService() {
        return orgUnitSpecificView.orgUnitService;
    }
}