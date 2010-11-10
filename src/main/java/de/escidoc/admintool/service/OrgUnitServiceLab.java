package de.escidoc.admintool.service;

import java.util.Collection;

import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;
import de.escidoc.core.client.interfaces.HandlerServiceInterface;
import de.escidoc.core.client.interfaces.OrganizationalUnitHandlerClientInterface;
import de.escidoc.core.resources.Resource;
import de.escidoc.core.resources.oum.OrganizationalUnit;

public class OrgUnitServiceLab
    extends AbstractEscidocService<OrganizationalUnitHandlerClientInterface> {

    public OrgUnitServiceLab(final HandlerServiceInterface client) {
        super(client);
    }

    @Override
    public Resource create(final Resource resource) throws EscidocException,
        InternalClientException, TransportException {
        return getClient().create((OrganizationalUnit) resource);
    }

    @Override
    OrganizationalUnitHandlerClientInterface getClient() {
        return ((OrganizationalUnitHandlerClientInterface) client);
    }

    @Override
    Collection<? extends Resource> findPublicOrReleseadResourcesUsingOldFilter()
        throws EscidocException, InternalClientException, TransportException,
        EscidocClientException {
        return getClient()
            .retrieveOrganizationalUnits(withEmptyTaskParam())
            .getOrganizationalUnits();
    }

    @Override
    Collection<? extends Resource> findPublicOrReleasedResources()
        throws EscidocException, InternalClientException, TransportException {
        return getClient().retrieveOrganizationalUnitsAsList(withEmptyFilter());
    }

    @Override
    public Collection<? extends Resource> filterUsingInput(final String query)
        throws EscidocException, InternalClientException, TransportException {
        return getClient().retrieveOrganizationalUnitsAsList(
            userInputToFilter(query));
    }

    public void parent(final OrganizationalUnit orgUnitWithParent)
        throws InternalClientException, TransportException,
        EscidocClientException {

        getClient().update(orgUnitWithParent);
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
