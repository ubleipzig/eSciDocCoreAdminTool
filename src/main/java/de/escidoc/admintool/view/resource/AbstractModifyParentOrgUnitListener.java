package de.escidoc.admintool.view.resource;

import com.google.common.base.Preconditions;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Window;

import de.escidoc.admintool.app.PropertyId;
import de.escidoc.admintool.service.OrgUnitServiceLab;
import de.escidoc.admintool.view.ModalDialog;
import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.resources.oum.OrganizationalUnit;
import de.escidoc.core.resources.oum.Parent;
import de.escidoc.core.resources.oum.Parents;

public abstract class AbstractModifyParentOrgUnitListener
    implements ModifyParentOrgUnitListener {

    protected OrgUnitServiceLab orgUnitService;

    protected ResourceContainer resourceContainer;

    protected Item item;

    protected OrganizationalUnit child;

    protected OrganizationalUnit selectedParent;

    protected Parents newParents;

    protected Property parentProperty;

    private Window mainWindow;

    @Override
    public void bind(final Item item) {
        this.item = item;
    }

    @Override
    public void buttonClick(final ClickEvent event) {
        onButtonClick();
    }

    protected abstract void onButtonClick();

    protected void updateParent() {
        try {
            child = getChild();
            selectedParent = getSelectedParent();

            updatePersistence();
            updateResourceContainer();
            updateItem();
            checkPostConditions(child);
        }
        catch (final EscidocClientException e) {
            ModalDialog.show(mainWindow, e);
        }
    }

    protected OrganizationalUnit getChild() throws EscidocClientException {
        return orgUnitService.findById(getChildId());
    }

    protected void updateItem() {
        final Parent parent = newParents.get(0);
        if (parent == null) {
            parentProperty.setValue("no parents");
        }
        else {
            parentProperty.setValue(parent.getXLinkTitle());
        }

        final Item updateItem = resourceContainer.getContainer().getItem(child);

        final Property itemProperty =
            updateItem.getItemProperty(PropertyId.PARENTS);
        itemProperty.setValue(newParents);
    }

    protected void updatePersistence() throws EscidocClientException {
        newParents =
            orgUnitService.updateParent(getChildId(), getSelectedParentId());
    }

    protected void updateResourceContainer() throws EscidocClientException {
        resourceContainer.updateParent(child, selectedParent);
    }

    protected OrganizationalUnit getSelectedParent()
        throws EscidocClientException {
        return orgUnitService.findById(getSelectedParentId());
    }

    protected String getChildId() {
        return (String) item.getItemProperty(PropertyId.OBJECT_ID).getValue();
    }

    protected abstract String getSelectedParentId();

    protected void checkPostConditions(final OrganizationalUnit child) {
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

    protected Property getParentProperty(final Item updateItem) {
        return updateItem.getItemProperty(PropertyId.PARENTS);
    }

    protected Item getItemFor(final OrganizationalUnit child) {
        return resourceContainer.getContainer().getItem(child);
    }

    @Override
    public void setParentProperty(final Property parentProperty) {
        this.parentProperty = parentProperty;
    }

}