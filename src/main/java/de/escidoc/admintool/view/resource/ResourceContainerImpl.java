package de.escidoc.admintool.view.resource;

import java.util.Collection;
import java.util.Collections;

import org.joda.time.DateTime;

import com.google.common.base.Preconditions;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.util.HierarchicalContainer;

import de.escidoc.admintool.app.PropertyId;
import de.escidoc.admintool.view.ViewConstants;
import de.escidoc.core.resources.Resource;
import de.escidoc.core.resources.oum.OrganizationalUnit;
import de.escidoc.core.resources.oum.Parents;
import de.escidoc.core.resources.oum.Predecessors;

public class ResourceContainerImpl implements ResourceContainer {

    private final Collection<? extends Resource> topLevelResources;

    private HierarchicalContainer container;

    public ResourceContainerImpl(
        final Collection<? extends Resource> topLevelResources) {
        Preconditions.checkNotNull(topLevelResources,
            "topLevelResources is null: %s", topLevelResources);

        this.topLevelResources = topLevelResources;
        toHierarchicalContainer();
    }

    private Container toHierarchicalContainer() {
        Preconditions.checkNotNull(topLevelResources,
            "topLevelResources is null: %s", topLevelResources);

        if (topLevelResources.isEmpty()) {
            return createHierarchicalContainer();
        }
        else {
            createHierarchicalContainer();
            addContainerProperties();
            addTopLevel();
            sortByLatestModificationDate();

            return container;
        }
    }

    private void sortByLatestModificationDate() {
        final boolean[] sort = new boolean[1];
        sort[0] = false;
        container.sort(new String[] { PropertyId.LAST_MODIFICATION_DATE },
            sort);
    }

    public void addChildren(
        final Resource parent, final Collection<OrganizationalUnit> children) {
        Preconditions.checkNotNull(parent, "parent is null: %s", parent);
        Preconditions.checkNotNull(children, "children is null: %s", children);

        for (final OrganizationalUnit child : children) {
            addChild(parent, child);
        }
    }

    public void addChild(final Resource parent, final Resource child) {
        final Item item = container.addItem(child);
        bind(item, child);
        markAsLeaf(child);
        setParentIfAny(parent, child);
    }

    private void setParentIfAny(final Resource parent, final Resource child) {
        if (parent != null) {
            container.setParent(child, parent);
        }
        else if (hasParent(child)) {
            throw new UnsupportedOperationException("Not yet implemented");
        }
    }

    private boolean hasParent(final Resource child) {
        if (child instanceof OrganizationalUnit) {
            return ((OrganizationalUnit) child).getParents() != null
                && ((OrganizationalUnit) child).getParents() != null
                && !((OrganizationalUnit) child).getParents().isEmpty();
        }
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private Container createHierarchicalContainer() {
        container = new HierarchicalContainer();
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
        container.addContainerProperty(PropertyId.OBJECT_ID, String.class,
            ViewConstants.EMPTY_STRING);
        container.addContainerProperty(PropertyId.NAME, String.class,
            ViewConstants.EMPTY_STRING);
        container.addContainerProperty(PropertyId.DESCRIPTION, String.class,
            ViewConstants.EMPTY_STRING);
        container.addContainerProperty(PropertyId.CREATED_ON, DateTime.class,
            new DateTime());
        container.addContainerProperty(PropertyId.CREATED_BY, String.class,
            ViewConstants.EMPTY_STRING);
        container.addContainerProperty(PropertyId.LAST_MODIFICATION_DATE,
            DateTime.class, new DateTime());
        container.addContainerProperty(PropertyId.MODIFIED_BY, String.class,
            ViewConstants.EMPTY_STRING);
        container.addContainerProperty(PropertyId.PUBLIC_STATUS, String.class,
            ViewConstants.EMPTY_STRING);
        container.addContainerProperty(PropertyId.PUBLIC_STATUS_COMMENT,
            String.class, ViewConstants.EMPTY_STRING);
    }

    private void addObjectIdProperty(final Container container) {
        container.addContainerProperty(PropertyId.OBJECT_ID, String.class,
            ViewConstants.EMPTY_STRING);
    }

    private void addOrgUnitProperties() {
        container.addContainerProperty(PropertyId.PARENTS, Parents.class,
            new Parents());

        container.addContainerProperty(PropertyId.PREDECESSORS,
            Predecessors.class, Collections.EMPTY_SET);
    }

    private boolean isOrgUnit() {
        return true;
    }

    private void addTopLevel() {
        assert (topLevelResources != null) : "top level resources is null";

        for (final Resource topLevel : topLevelResources) {
            add(topLevel);
        }
    }

    public void add(final Resource topLevel) {
        Preconditions.checkNotNull(topLevel, "topLevel is null: %s", topLevel);
        final Item item = container.addItem(topLevel);
        Preconditions.checkNotNull(item, "item is null: %s", item);

        markAsLeaf(topLevel);
        bind(item, topLevel);
    }

    private void bind(final Item item, final Resource resource) {
        Preconditions.checkNotNull(item, "item is null: %s", item);
        Preconditions.checkNotNull(resource, "resource is null: %s", resource);
        final OrganizationalUnit orgUnit = (OrganizationalUnit) resource;
        item.getItemProperty(PropertyId.OBJECT_ID)
            .setValue(resource.getObjid());
        item.getItemProperty(PropertyId.NAME).setValue(
            orgUnit.getProperties().getName());
        item.getItemProperty(PropertyId.DESCRIPTION).setValue(
            orgUnit.getProperties().getDescription());
        item.getItemProperty(PropertyId.CREATED_ON).setValue(
            orgUnit.getProperties().getCreationDate());
        item.getItemProperty(PropertyId.CREATED_BY).setValue(
            orgUnit.getProperties().getCreatedBy().getXLinkTitle());
        item.getItemProperty(PropertyId.LAST_MODIFICATION_DATE).setValue(
            orgUnit.getLastModificationDate());
        item.getItemProperty(PropertyId.MODIFIED_BY).setValue(
            orgUnit.getProperties().getModifiedBy().getXLinkTitle());
        item.getItemProperty(PropertyId.PUBLIC_STATUS).setValue(
            orgUnit.getProperties().getPublicStatus());
        item.getItemProperty(PropertyId.PUBLIC_STATUS_COMMENT).setValue(
            orgUnit.getProperties().getPublicStatusComment());
        item.getItemProperty(PropertyId.PARENTS).setValue(orgUnit.getParents());
        item.getItemProperty(PropertyId.PREDECESSORS).setValue(
            orgUnit.getPredecessors());
    }

    private void markAsLeaf(final Resource topLevel) {
        final boolean hasChildren = hasChildren(topLevel);
        container.setChildrenAllowed(topLevel, hasChildren);
    }

    private boolean hasChildren(final Resource resource) {
        return ((OrganizationalUnit) resource).getProperties().getHasChildren();
    }

    @Override
    public int size() {
        return container.size();
    }

    @Override
    public Container getContainer() {
        return container;
    }

    @Override
    public void updateParent(
        final OrganizationalUnit child, final OrganizationalUnit newParent) {

        preconditions(child);

        if (isNotNull(newParent)) {
            final Resource oldParent = (Resource) container.getParent(child);

            container.setChildrenAllowed(newParent, true);
            container.setParent(child, newParent);
            if (!container.hasChildren(oldParent)
                || container.getChildren(oldParent) == null) {
                container.setChildrenAllowed(oldParent, false);
            }

        }
        else if (hasParent(child)) {
            throw new UnsupportedOperationException("Not yet implemented");
        }
    }

    private void preconditions(final Object child) {
        Preconditions.checkNotNull(child, "child is null: %s", child);
    }

    private boolean isNotNull(final OrganizationalUnit parent) {
        return parent != null;
    }

    private boolean hasNoChildren(final Resource oldParent) {
        return !hasChildren(oldParent);
    }

    @Override
    public void removeParent(final OrganizationalUnit child) {
        Preconditions.checkNotNull(child, "child is null: %s", child);

        final OrganizationalUnit parent =
            (OrganizationalUnit) container.getParent(child);

        container.setParent(child, null);
        if (!container.hasChildren(parent)) {
            container.setChildrenAllowed(parent, false);
        }
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("ResourceContainerImpl [");
        if (topLevelResources != null) {
            builder
                .append("topLevelResources=").append(topLevelResources)
                .append(", ");
        }
        if (container != null) {
            builder.append("container=").append(container);
        }
        builder.append("]");
        return builder.toString();
    }

    @Override
    public void remove(final Resource resource) {
        Preconditions.checkNotNull(resource, "resource is null: %s", resource);

        final Object parent = container.getParent(resource);

        container.removeItem(resource);

        if (!container.hasChildren(parent)
            || container.getChildren(parent) == null) {
            container.setChildrenAllowed(parent, false);
        }
    }

}