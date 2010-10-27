package de.escidoc.admintool.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.escidoc.core.client.AdminHandlerClient;
import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;
import de.escidoc.core.client.interfaces.HandlerServiceInterface;
import de.escidoc.core.resources.adm.LoadExamplesResult.Entry;
import de.escidoc.core.resources.adm.MessagesStatus;

public class AdminServiceMock implements AdminService {

    private final HandlerServiceInterface client;

    public AdminServiceMock(final HandlerServiceInterface client) {
        this.client = new AdminHandlerClient();
    }

    @Override
    public List<Entry> loadCommonExamples() throws EscidocException,
        InternalClientException, TransportException {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void loginWith(final String handle) throws InternalClientException {

    }

    private final Map<String, String> map = new HashMap<String, String>();

    @Override
    public Map<String, String> getRepositoryInfo() throws EscidocException,
        InternalClientException, TransportException {
        map.put("organizational-unit",
            "http://www.escidoc.de/schemas/organizationalunit/0.8");
        return map;
    }

    @Override
    public MessagesStatus purge(final Set<String> list)
        throws EscidocException, InternalClientException, TransportException {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public MessagesStatus retrievePurgeStatus() throws EscidocException,
        InternalClientException, TransportException {
        throw new UnsupportedOperationException("Not yet implemented");

    }
}