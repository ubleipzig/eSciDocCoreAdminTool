/**
 * CDDL HEADER START
 *
 * The contents of this file are subject to the terms of the
 * Common Development and Distribution License, Version 1.0 only
 * (the "License").  You may not use this file except in compliance
 * with the License.
 *
 * You can obtain a copy of the license at license/ESCIDOC.LICENSE
 * or https://www.escidoc.org/license/ESCIDOC.LICENSE .
 * See the License for the specific language governing permissions
 * and limitations under the License.
 *
 * When distributing Covered Code, include this CDDL HEADER in each
 * file and include the License file at license/ESCIDOC.LICENSE.
 * If applicable, add the following below this CDDL HEADER, with the
 * fields enclosed by brackets "[]" replaced with your own identifying
 * information: Portions Copyright [yyyy] [name of copyright owner]
 *
 * CDDL HEADER END
 *
 *
 *
 * Copyright 2011 Fachinformationszentrum Karlsruhe Gesellschaft
 * fuer wissenschaftlich-technische Information mbH and Max-Planck-
 * Gesellschaft zur Foerderung der Wissenschaft e.V.
 * All rights reserved.  Use is subject to license terms.
 */
package de.escidoc.admintool.view.resource;

import com.google.common.base.Preconditions;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Window;

import de.escidoc.admintool.app.PropertyId;
import de.escidoc.admintool.service.internal.OrgUnitServiceLab;
import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.resources.oum.OrganizationalUnit;
import de.escidoc.core.resources.oum.Parent;
import de.escidoc.core.resources.oum.Parents;

public abstract class AbstractAddOrChangeParentListener implements AddOrChangeParentListener {

    private static final long serialVersionUID = 7249780023759749046L;

    private Item item;

    private Property parentProperty;

    protected OrgUnitServiceLab orgUnitService;

    protected ResourceContainer resourceContainer;

    protected OrganizationalUnit selectedOrgUnit;

    protected OrganizationalUnit selectedParent;

    protected Parents newParents;

    private Window mainWindow;

    protected AddOrEditParentModalWindow addOrEditParentModalWindow;

    @Override
    public void buttonClick(final ClickEvent event) {
        onOkButtonClick();
    }

    protected void onOkButtonClick() {
        addOrUpdateParent();
        closeWindow();
    }

    protected abstract void addOrUpdateParent();

    private void closeWindow() {
        addOrEditParentModalWindow.closeWindow();
    }

    @Override
    public void bind(final Item item) {
        this.item = item;
    }

    @Override
    public void setParentProperty(final Property parentProperty) {
        this.parentProperty = parentProperty;
    }

    protected OrganizationalUnit getChild() throws EscidocClientException {
        return orgUnitService.findById(getChildId());
    }

    protected void updateItem() {
        final Parent parent = newParents.get(0);
        if (parent == null) {
            getParentProperty().setValue(new ResourceRefDisplay());
        }
        else {
            getParentProperty().setValue(new ResourceRefDisplay(parent.getObjid(), parent.getXLinkTitle()));
        }

        final Item updateItem = resourceContainer.getContainer().getItem(selectedOrgUnit);

        final Property itemProperty = updateItem.getItemProperty(PropertyId.PARENTS);
        itemProperty.setValue(newParents);
    }

    protected void updatePersistence() throws EscidocClientException {
        newParents = orgUnitService.updateParent(getChildId(), getSelectedParentId());
    }

    protected void updateResourceContainer() throws EscidocClientException {
        resourceContainer.updateParent(selectedOrgUnit, selectedParent);
    }

    protected OrganizationalUnit getSelectedParent() throws EscidocClientException {
        return orgUnitService.findById(getSelectedParentId());
    }

    protected String getChildId() {
        Preconditions.checkNotNull(item, "item is null: %s", item);
        return (String) item.getItemProperty(PropertyId.OBJECT_ID).getValue();
    }

    private String getSelectedParentId() {
        return addOrEditParentModalWindow.getSelectedParent();
    }

    protected void checkPostConditions(final OrganizationalUnit child) {
        final Item updateItem = getItemFor(child);
        Preconditions.checkNotNull(updateItem, "updateItem is null: %s", updateItem);

        final Property parentProperty = getParentProperty(updateItem);
        Preconditions.checkNotNull(parentProperty, "itemProperty is null: %s", parentProperty);

        final Object value = parentProperty.getValue();
        if (value instanceof Parents) {
            final Parents parents = (Parents) value;

            Preconditions.checkNotNull(parents, "parents is null: %s", parents);

            Preconditions.checkArgument(parents.size() <= 1, "Org unit has more than one parent.", parents.size());

            Preconditions.checkArgument(parents.size() == 1, "Parent in Item is not updated", parents.size());
        }
        else {
            throw new IllegalArgumentException("The value of ParentProperty is not instance of Parents");
        }
    }

    protected Property getParentProperty(final Item updateItem) {
        return updateItem.getItemProperty(PropertyId.PARENTS);
    }

    protected Item getItemFor(final OrganizationalUnit child) {
        return resourceContainer.getContainer().getItem(child);
    }

    public void setMainWindow(final Window mainWindow) {
        this.mainWindow = mainWindow;
    }

    public Window getMainWindow() {
        return mainWindow;
    }

    public Property getParentProperty() {
        return parentProperty;
    }

}