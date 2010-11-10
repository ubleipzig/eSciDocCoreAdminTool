package de.escidoc.admintool.service;

import java.util.Collection;

import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;
import de.escidoc.core.client.interfaces.ContainerHandlerClientInterface;
import de.escidoc.core.client.interfaces.HandlerServiceInterface;
import de.escidoc.core.resources.Resource;

public class ContainerService
    extends AbstractEscidocService<ContainerHandlerClientInterface> {

    public ContainerService(final HandlerServiceInterface client) {
        super(client);
    }

    @Override
    Collection<? extends Resource> findPublicOrReleseadResourcesUsingOldFilter()
        throws EscidocClientException {
        return getClient()
            .retrieveContainers(withEmptyTaskParam()).getContainers();
    }

    @Override
    Collection<? extends Resource> findPublicOrReleasedResources()
        throws EscidocException, InternalClientException, TransportException {
        return getClient().retrieveContainersAsList(withEmptyFilter());
    }

    @Override
    ContainerHandlerClientInterface getClient() {
        return (ContainerHandlerClientInterface) client;
    }

    @Override
    public Resource create(final Resource resource) throws EscidocException,
        InternalClientException, TransportException {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Collection<? extends Resource> filterUsingInput(final String query)
        throws EscidocException, InternalClientException, TransportException {
        return getClient().retrieveContainersAsList(userInputToFilter(query));
    }
}