package de.escidoc.admintool.service;

import java.util.Collection;

import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;
import de.escidoc.core.client.interfaces.ItemHandlerClientInterface;
import de.escidoc.core.client.interfaces.base.HandlerService;
import de.escidoc.core.resources.Resource;
import de.escidoc.core.resources.om.item.Item;

public class ItemService extends AbstractEscidocService<ItemHandlerClientInterface> {

    public ItemService(final HandlerService client) {
        super(client);
    }

    @Override
    protected final ItemHandlerClientInterface getClient() {
        return ((ItemHandlerClientInterface) client);
    }

    @Override
    protected final Collection<? extends Resource> findPublicOrReleasedResources() throws EscidocException,
        InternalClientException, TransportException {
        return getClient().retrieveItemsAsList(withEmptyFilter());
    }

    @Override
    public Resource create(final Resource resource) throws EscidocException, InternalClientException,
        TransportException {
        return getClient().create((Item) resource);
    }

    @Override
    public Collection<? extends Resource> filterUsingInput(final String query) throws EscidocException,
        InternalClientException, TransportException {
        return getClient().retrieveItemsAsList(userInputToFilter(query));
    }

    @Override
    public Resource findById(final String objid) throws EscidocClientException {
        return getClient().retrieve(objid);
    }

    @Override
    public void update(final Resource resource) throws EscidocClientException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Not yet implemented");

    }
}