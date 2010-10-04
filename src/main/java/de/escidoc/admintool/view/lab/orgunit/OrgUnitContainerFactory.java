package de.escidoc.admintool.view.lab.orgunit;

import java.util.Collection;
import java.util.Collections;

import com.vaadin.data.Item;
import com.vaadin.data.util.HierarchicalContainer;

import de.escidoc.admintool.app.PropertyId;
import de.escidoc.admintool.service.OrgUnitService;
import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;
import de.escidoc.core.resources.oum.OrganizationalUnit;
import de.escidoc.core.resources.oum.OrganizationalUnitList;

public class OrgUnitContainerFactory {

    private final static HierarchicalContainer container =
        new HierarchicalContainer();

    private final OrgUnitService orgUnitService;

    public OrgUnitContainerFactory(final OrgUnitService orgUnitService) {
        if (orgUnitService == null) {
            throw new IllegalArgumentException(
                "OrgUnitService can not be null.");
        }
        this.orgUnitService = orgUnitService;
    }

    public HierarchicalContainer create() throws EscidocException,
        InternalClientException, TransportException {
        if (container == null || container.getItemIds().isEmpty()) {
            container.addContainerProperty(PropertyId.NAME, String.class, null);
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
            addToContainer(orgUnit);
        }
    }

    private void buildTree(
        final Collection<OrganizationalUnit> topLevelOrgUnit,
        final OrganizationalUnit parent) throws EscidocException,
        InternalClientException, TransportException {
        assert (topLevelOrgUnit != null) : "top level org unit is null";
        assert (parent != null) : "parent org unit is null";

        for (final OrganizationalUnit orgUnit : topLevelOrgUnit) {
            addToContainer(orgUnit);
            setParentIfAny(parent, orgUnit);
        }
    }

    private void setParentIfAny(
        final OrganizationalUnit parent, final OrganizationalUnit orgUnit) {
        if (hasParent(parent)) {
            container.setParent(orgUnit, parent);
        }
    }

    private boolean hasParent(final OrganizationalUnit parent) {
        return parent != null;
    }

    private void addToContainer(final OrganizationalUnit orgUnit)
        throws EscidocException, InternalClientException, TransportException {
        assert (orgUnit != null) : "orgUnit org unit is null";

        final Item item = container.addItem(orgUnit);
        setNameAsCaption(item, orgUnit);
        markAsLeafIfNecessary(orgUnit);
    }

    private Collection<OrganizationalUnit> getChildren(
        final OrganizationalUnit root) throws EscidocException,
        InternalClientException, TransportException {
        return orgUnitService.retrieveChildren(root.getObjid());
    }

    private void setNameAsCaption(final Item item, final OrganizationalUnit root) {
        assert (item != null) : "item can not be null";
        assert (root != null) : "root can not be null";

        item.getItemProperty(PropertyId.NAME).setValue(
            root.getProperties().getName());
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
}