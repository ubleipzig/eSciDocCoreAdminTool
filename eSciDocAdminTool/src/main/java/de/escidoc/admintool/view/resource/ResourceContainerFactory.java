package de.escidoc.admintool.view.resource;

import java.util.Collection;

import com.google.common.base.Preconditions;

import de.escidoc.admintool.service.OrgUnitServiceLab;
import de.escidoc.admintool.service.ResourceService;
import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.resources.oum.OrganizationalUnit;

public class ResourceContainerFactory {

    private final OrgUnitServiceLab resourceService;

    public ResourceContainerFactory(final ResourceService resourceService) {
        Preconditions.checkNotNull(resourceService, "resourceService is null: %s", resourceService);
        this.resourceService = (OrgUnitServiceLab) resourceService;
    }

    public ResourceContainer getResourceContainer() throws EscidocClientException {
        return createResourceContainer();
    }

    private ResourceContainer createResourceContainer() throws EscidocClientException {
        return new ResourceContainerImpl(getTopLevelOrgUnits());
    }

    private Collection<OrganizationalUnit> getTopLevelOrgUnits() throws EscidocClientException {
        return resourceService.getTopLevelOrgUnits();
    }
}
