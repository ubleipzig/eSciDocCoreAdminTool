package de.escidoc.admintool.domain;

import java.util.Collection;
import java.util.Collections;

import org.joda.time.DateTime;

import com.google.common.base.Preconditions;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.util.HierarchicalContainer;
import com.vaadin.data.util.POJOContainer;

import de.escidoc.admintool.app.PropertyId;
import de.escidoc.admintool.view.ViewConstants;
import de.escidoc.core.resources.Resource;
import de.escidoc.core.resources.oum.OrganizationalUnit;
import de.escidoc.core.resources.oum.Parents;
import de.escidoc.core.resources.oum.Predecessors;

public class ContainerFactory implements ResourcesToContainer {

    public static Container toContainer(final Collection<Resource> topLevelOrgUnits) {
        Preconditions.checkNotNull(topLevelOrgUnits, "resources is null: %s", topLevelOrgUnits);

        if (topLevelOrgUnits.isEmpty()) {
            return createEmptyContainer();
        }
        else {
            return new POJOContainer<Resource>(topLevelOrgUnits, PropertyId.GENERIC_PROPERTIES);
        }
    }

    private static Container createEmptyContainer() {
        return new POJOContainer<Resource>(Resource.class, PropertyId.GENERIC_PROPERTIES);
    }

    private Collection<? extends Resource> topLevelResources;

    private final Container container = new HierarchicalContainer();

    public Container toHierarchicalContainer(final Collection<? extends Resource> topLevelResources) {
        Preconditions.checkNotNull(topLevelResources, "topLevelResources is null: %s", topLevelResources);
        this.topLevelResources = topLevelResources;

        if (topLevelResources.isEmpty()) {
            return createHierarchicalContainer();
        }
        else {
            createHierarchicalContainer();
            addContainerProperties();
            addTopLevel();
            return container;
        }
    }

    private Container createHierarchicalContainer() {
        addContainerProperties();
        return container;
    }

    private void addContainerProperties() {
        addGeneralProperties();
        if (isOrgUnit()) {
            addOrgUnitProperties();
        }
    }

    private void addGeneralProperties() {
        addObjectIdProperty(container);
        container.addContainerProperty(PropertyId.NAME, String.class, ViewConstants.EMPTY_STRING);
        container.addContainerProperty(PropertyId.DESCRIPTION, String.class, ViewConstants.EMPTY_STRING);
        container.addContainerProperty(PropertyId.CREATED_ON, DateTime.class, new DateTime());
        container.addContainerProperty(PropertyId.CREATED_BY, String.class, ViewConstants.EMPTY_STRING);
        container.addContainerProperty(PropertyId.LAST_MODIFICATION_DATE, DateTime.class, new DateTime());
        container.addContainerProperty(PropertyId.MODIFIED_BY, String.class, ViewConstants.EMPTY_STRING);
        container.addContainerProperty(PropertyId.PUBLIC_STATUS, String.class, ViewConstants.EMPTY_STRING);
        container.addContainerProperty(PropertyId.PUBLIC_STATUS_COMMENT, String.class, ViewConstants.EMPTY_STRING);
    }

    private void addObjectIdProperty(final Container container) {
        container.addContainerProperty(PropertyId.OBJECT_ID, String.class, ViewConstants.EMPTY_STRING);
    }

    private void addOrgUnitProperties() {
        container.addContainerProperty(PropertyId.PARENTS, Parents.class, Collections.EMPTY_SET);
        container.addContainerProperty(PropertyId.PREDECESSORS, Predecessors.class, Collections.EMPTY_SET);
    }

    private boolean isOrgUnit() {
        return true;
    }

    private void addTopLevel() {
        assert (topLevelResources != null) : "top level resources is null";

        for (final Resource topLevel : topLevelResources) {
            addToContainer(topLevel);
        }
    }

    public void addToContainer(final Resource topLevel) {
        Preconditions.checkNotNull(topLevel, "topLevel is null: %s", topLevel);

        final Item item = container.addItem(topLevel);
        markAsLeaf(topLevel);
        bind(item, topLevel);
    }

    private void bind(final Item item, final Resource resource) {

        assert (item != null) : "item can not be null";
        assert (resource != null) : "resource can not be null";

        final OrganizationalUnit orgUnit = (OrganizationalUnit) resource;

        item.getItemProperty(PropertyId.OBJECT_ID).setValue(resource.getObjid());
        item.getItemProperty(PropertyId.NAME).setValue(orgUnit.getProperties().getName());
        item.getItemProperty(PropertyId.DESCRIPTION).setValue(orgUnit.getProperties().getDescription());
        item.getItemProperty(PropertyId.CREATED_ON).setValue(orgUnit.getProperties().getCreationDate());
        item.getItemProperty(PropertyId.CREATED_BY).setValue(orgUnit.getProperties().getCreatedBy().getXLinkTitle());
        item.getItemProperty(PropertyId.LAST_MODIFICATION_DATE).setValue(orgUnit.getLastModificationDate());
        item.getItemProperty(PropertyId.MODIFIED_BY).setValue(orgUnit.getProperties().getModifiedBy().getXLinkTitle());
        item.getItemProperty(PropertyId.PUBLIC_STATUS).setValue(orgUnit.getProperties().getPublicStatus());
        item.getItemProperty(PropertyId.PUBLIC_STATUS_COMMENT).setValue(
            orgUnit.getProperties().getPublicStatusComment());
        item.getItemProperty(PropertyId.PARENTS).setValue(orgUnit.getParents());
        item.getItemProperty(PropertyId.PREDECESSORS).setValue(orgUnit.getPredecessors());
    }

    private void markAsLeaf(final Resource topLevel) {
        ((HierarchicalContainer) container).setChildrenAllowed(topLevel, hasChildren(topLevel));
    }

    private boolean hasChildren(final Resource topLevel) {
        return ((OrganizationalUnit) topLevel).getProperties().getHasChildren();
    }
}