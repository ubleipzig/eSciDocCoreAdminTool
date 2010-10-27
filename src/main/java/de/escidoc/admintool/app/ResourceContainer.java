package de.escidoc.admintool.app;

import java.util.Collection;

import org.joda.time.DateTime;

import com.google.common.base.Preconditions;
import com.vaadin.data.Item;
import com.vaadin.data.util.HierarchicalContainer;

import de.escidoc.admintool.service.EscidocService;
import de.escidoc.admintool.service.ResourceService;
import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;
import de.escidoc.core.resources.Resource;
import de.escidoc.core.resources.om.container.Container;
import de.escidoc.core.resources.oum.OrganizationalUnit;

public class ResourceContainer {

    private final static HierarchicalContainer container =
        new HierarchicalContainer();

    private final EscidocService EscidocService;

    public ResourceContainer(final EscidocService EscidocService) {
        Preconditions.checkNotNull(EscidocService,
            "EscidocService can not be null.");

        this.EscidocService = EscidocService;
    }

    public HierarchicalContainer create() throws EscidocClientException {

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
            addTopLevelresources();
        }
        return container;
    }

    private void addTopLevelresources() throws EscidocClientException {
        final Collection<Resource> topLevelresource = getAllRecources();

        buildTopLevelTree(topLevelresource);
    }

    private Collection<Resource> getAllRecources()
        throws EscidocClientException {
        return ((ResourceService) EscidocService).findAll();
    }

    private void buildTopLevelTree(final Collection<Resource> topLevelResources)
        throws EscidocException, InternalClientException, TransportException {

        assert (topLevelResources != null) : "top level org unit is null";
        for (final Resource resource : topLevelResources) {
            addToContainer(resource, null);
        }
    }

    private void buildTree(
        final Collection<OrganizationalUnit> topLevelresource,
        final OrganizationalUnit parent) throws EscidocException,
        InternalClientException, TransportException {
        assert (topLevelresource != null) : "top level org unit is null";
        assert (parent != null) : "parent org unit is null";

        for (final OrganizationalUnit resource : topLevelresource) {
            addToContainer(resource, parent);
        }
    }

    public void addToContainer(
        final Resource resource, final OrganizationalUnit parent)
        throws EscidocException, InternalClientException, TransportException {
        assert (resource != null) : "resource org unit is null";
        final Item item = container.addItem(resource);
        bind(item, resource);
        markAsLeafIfNecessary(resource);
    }

    private void markAsLeafIfNecessary(final Resource resource) {
        container.setChildrenAllowed(resource, false);
    }

    private void bind(final Item item, final Resource resource) {
        assert (item != null) : "item can not be null";
        assert (resource != null) : "root can not be null";
        item.getItemProperty(PropertyId.OBJECT_ID)
            .setValue(resource.getObjid());
        if (resource instanceof Container) {
            final Container container = (Container) resource;

            item.getItemProperty(PropertyId.NAME).setValue(
                container.getProperties().getName());
            item.getItemProperty(PropertyId.DESCRIPTION).setValue(
                container.getProperties().getDescription());
            item.getItemProperty(PropertyId.CREATED_ON).setValue(
                container.getProperties().getCreationDate());
            item.getItemProperty(PropertyId.CREATED_BY).setValue(
                container.getProperties().getCreatedBy().getXLinkTitle());
            item.getItemProperty(PropertyId.LAST_MODIFICATION_DATE).setValue(
                container.getLastModificationDate());
            item.getItemProperty(PropertyId.PUBLIC_STATUS).setValue(
                container.getProperties().getPublicStatus());
            item.getItemProperty(PropertyId.PUBLIC_STATUS_COMMENT).setValue(
                container.getProperties().getPublicStatusComment());
        }

    }

    public Item getItem(final OrganizationalUnit createdresource) {
        return container.getItem(createdresource);
    }

}
