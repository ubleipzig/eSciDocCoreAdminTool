package de.escidoc.admintool.service;

import gov.loc.www.zing.srw.SearchRetrieveRequestType;

import java.util.Collection;

import com.google.common.base.Preconditions;

import de.escidoc.core.client.ContentModelHandlerClient;
import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;
import de.escidoc.core.client.interfaces.base.HandlerService;
import de.escidoc.core.resources.Resource;
import de.escidoc.core.resources.cmm.ContentModel;

public class ContentModelService extends AbstractEscidocService<ContentModelHandlerClient> {

    public ContentModelService(final HandlerService client) {
        super(client);
    }

    @Override
    public Resource create(final Resource resource) throws EscidocException, InternalClientException,
        TransportException {
        return getClient().create((ContentModel) resource);
    }

    @Override
    public Resource findById(final String objid) throws EscidocClientException {
        return getClient().retrieve(objid);
    }

    @Override
    public void update(final Resource resource) throws EscidocClientException {
        Preconditions.checkNotNull(resource, "resource is null: %s", resource);
        getClient().update((ContentModel) resource);
    }

    @Override
    ContentModelHandlerClient getClient() {
        return (ContentModelHandlerClient) client;
    }

    @Override
    Collection<? extends Resource> findPublicOrReleasedResources() throws EscidocException, InternalClientException,
        TransportException {
        return getClient().retrieveContentModelsAsList(new SearchRetrieveRequestType());
    }

    @Override
    public Collection<? extends Resource> filterUsingInput(final String query) throws EscidocException,
        InternalClientException, TransportException {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("ContentModelService [");
        if (getClient() != null) {
            builder.append("getClient()=").append(getClient());
        }
        builder.append("]");
        return builder.toString();
    }
}