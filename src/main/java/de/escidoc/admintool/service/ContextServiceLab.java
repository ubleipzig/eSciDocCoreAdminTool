package de.escidoc.admintool.service;

import java.util.Collection;

import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;
import de.escidoc.core.client.interfaces.ContextHandlerClientInterface;
import de.escidoc.core.client.interfaces.base.HandlerService;
import de.escidoc.core.resources.Resource;
import de.escidoc.core.resources.om.context.Context;

public class ContextServiceLab
    extends AbstractEscidocService<ContextHandlerClientInterface> {

    public ContextServiceLab(final HandlerService client) {
        super(client);
    }

    @Override
    ContextHandlerClientInterface getClient() {
        return ((ContextHandlerClientInterface) client);
    }

    @Override
    public Resource create(final Resource resource) throws EscidocException,
        InternalClientException, TransportException {
        return getClient().create((Context) resource);
    }

    @Override
    Collection<? extends Resource> findPublicOrReleasedResources()
        throws EscidocException, InternalClientException, TransportException {
        return getClient().retrieveContextsAsList(withEmptyFilter());
    }

    @Override
    public Collection<? extends Resource> filterUsingInput(final String query)
        throws EscidocException, InternalClientException, TransportException {
        return getClient().retrieveContextsAsList(userInputToFilter(query));
    }

    @Override
    public Resource findById(final String objid) throws EscidocClientException {
        return getClient().retrieve(objid);
    }

    @Override
    public void update(final Resource resource) throws EscidocClientException {
        throw new UnsupportedOperationException("Not yet implemented");

    }
}