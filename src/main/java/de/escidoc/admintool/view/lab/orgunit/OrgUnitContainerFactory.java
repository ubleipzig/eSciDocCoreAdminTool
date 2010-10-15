package de.escidoc.admintool.view.lab.orgunit;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.joda.time.DateTime;

import com.google.common.base.Preconditions;
import com.vaadin.data.Item;
import com.vaadin.data.util.HierarchicalContainer;

import de.escidoc.admintool.app.PropertyId;
import de.escidoc.admintool.service.OrgUnitService;
import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;
import de.escidoc.core.resources.oum.OrganizationalUnit;
import de.escidoc.core.resources.oum.OrganizationalUnitList;
import de.escidoc.core.resources.oum.Parent;
import de.escidoc.core.resources.oum.Parents;
import de.escidoc.core.resources.oum.Predecessors;

public class OrgUnitContainerFactory {

    private final static HierarchicalContainer container =
        new HierarchicalContainer();

    private final OrgUnitService orgUnitService;

    public OrgUnitContainerFactory(final OrgUnitService orgUnitService) {
        Preconditions.checkNotNull(orgUnitService,
            "orgUnitService can not be null.");
        this.orgUnitService = orgUnitService;
    }

    public HierarchicalContainer create() throws EscidocException,
        InternalClientException, TransportException {
        if (container == null || container.getItemIds().isEmpty()) {
            container.addContainerProperty(PropertyId.OBJECT_ID, String.class,
                null);
            container.addContainerProperty(PropertyId.NAME, String.class, null);
            container.addContainerProperty(PropertyId.DESCRIPTION,
                String.class, null);
            container.addContainerProperty(PropertyId.CREATED_ON,
                DateTime.class, null);
            container.addContainerProperty(PropertyId.CREATED_BY, String.class,
                null);
            container.addContainerProperty(PropertyId.LAST_MODIFICATION_DATE,
                DateTime.class, null);
            container.addContainerProperty(PropertyId.MODIFIED_BY,
                String.class, null);
            container.addContainerProperty(PropertyId.PUBLIC_STATUS,
                String.class, null);
            container.addContainerProperty(PropertyId.PUBLIC_STATUS_COMMENT,
                String.class, null);
            container.addContainerProperty(PropertyId.PARENTS, Parents.class,
                null);
            container.addContainerProperty(PropertyId.PREDECESSORS,
                Predecessors.class, null);
            addTopLevelOrgUnits();
        }

        return container;
    }

    public void addChildren(final OrganizationalUnit parent)
        throws EscidocException, InternalClientException, TransportException {
        if (parent == null) {
            throw new IllegalArgumentException("Parent can not be null.");
        }
        final Collection<OrganizationalUnit> children = getChildren(parent);
        buildTree(children, parent);
    }

    private void addTopLevelOrgUnits() throws EscidocException,
        InternalClientException, TransportException {
        final Collection<OrganizationalUnit> topLevelOrgUnit =
            getTopLevelOrgUnits();
        if (topLevelOrgUnit == null || topLevelOrgUnit.isEmpty()) {
            // TODO: tell user that no org unit stored yet.
        }
        buildTopLevelTree(topLevelOrgUnit);
    }

    private void buildTopLevelTree(
        final Collection<OrganizationalUnit> topLevelOrgUnit)
        throws EscidocException, InternalClientException, TransportException {
        assert (topLevelOrgUnit != null) : "top level org unit is null";
        for (final OrganizationalUnit orgUnit : topLevelOrgUnit) {
            addToContainer(orgUnit, null);
        }
    }

    private void buildTree(
        final Collection<OrganizationalUnit> topLevelOrgUnit,
        final OrganizationalUnit parent) throws EscidocException,
        InternalClientException, TransportException {
        assert (topLevelOrgUnit != null) : "top level org unit is null";
        assert (parent != null) : "parent org unit is null";

        for (final OrganizationalUnit orgUnit : topLevelOrgUnit) {
            addToContainer(orgUnit, parent);
        }
    }

    public void addToContainer(
        final OrganizationalUnit orgUnit, final OrganizationalUnit parent)
        throws EscidocException, InternalClientException, TransportException {
        assert (orgUnit != null) : "orgUnit org unit is null";
        final Item item = container.addItem(orgUnit);
        bind(item, orgUnit);
        markAsLeafIfNecessary(orgUnit);
        setParentIfAny(parent, orgUnit);
    }

    private void setParentIfAny(
        final OrganizationalUnit parent, final OrganizationalUnit orgUnit)
        throws EscidocException, InternalClientException, TransportException {
        if (parent != null) {
            container.setParent(orgUnit, parent);
        }
        else if (hasParent(orgUnit)) {
            final OrganizationalUnit p = getParent(orgUnit);
            container.setChildrenAllowed(p, true);
            container.setParent(orgUnit, p);
        }
    }

    private OrganizationalUnit getParent(final OrganizationalUnit createdOrgUnit)
        throws EscidocException, InternalClientException, TransportException {
        final List<Parent> parentRef =
            (List<Parent>) createdOrgUnit.getParents().getParentRef();
        return orgUnitService.find(parentRef.get(0).getObjid());
    }

    private boolean hasParent(final OrganizationalUnit createdOrgUnit) {
        return createdOrgUnit.getParents() != null
            && createdOrgUnit.getParents().getParentRef() != null
            && !createdOrgUnit.getParents().getParentRef().isEmpty();
    }

    private Collection<OrganizationalUnit> getChildren(
        final OrganizationalUnit root) throws EscidocException,
        InternalClientException, TransportException {
        return orgUnitService.retrieveChildren(root.getObjid());
    }

    private void bind(final Item item, final OrganizationalUnit orgUnit) {
        assert (item != null) : "item can not be null";
        assert (orgUnit != null) : "root can not be null";
        item.getItemProperty(PropertyId.OBJECT_ID).setValue(orgUnit.getObjid());
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

    private void markAsLeafIfNecessary(final OrganizationalUnit root)
        throws EscidocException, InternalClientException, TransportException {
        container.setChildrenAllowed(root, hasChildren(root));
    }

    private boolean hasChildren(final OrganizationalUnit root)
        throws EscidocException, InternalClientException, TransportException {
        return root.getProperties().getHasChildren()
            && getChildren(root) != null && !getChildren(root).isEmpty();
    }

    private Collection<OrganizationalUnit> getTopLevelOrgUnits()
        throws EscidocException, InternalClientException, TransportException {
        final OrganizationalUnitList list =
            orgUnitService.retrieveTopLevelOrgUnits();
        if (list == null) {
            return Collections.emptyList();
        }
        return list.getOrganizationalUnits();
    }

    public Item getItem(final OrganizationalUnit createdOrgUnit) {
        return container.getItem(createdOrgUnit);
    }
}