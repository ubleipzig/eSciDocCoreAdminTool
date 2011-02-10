package de.escidoc.admintool.service;

import gov.loc.www.zing.srw.SearchRetrieveRequestType;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import de.escidoc.core.client.RoleHandlerClient;
import de.escidoc.core.client.TransportProtocol;
import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;
import de.escidoc.core.client.interfaces.RoleHandlerClientInterface;
import de.escidoc.core.resources.aa.role.Role;

public class RoleService {

    private RoleHandlerClientInterface client;

    private final Map<String, Role> roleById = new HashMap<String, Role>();

    private final String eSciDocUri;

    private final String handle;

    private Collection<Role> allRoles;

    public RoleService(final String eSciDocUri, final String handle)
        throws InternalClientException {
        this.eSciDocUri = eSciDocUri;
        this.handle = handle;
        initClient();
    }

    private void initClient() throws InternalClientException {
        client = new RoleHandlerClient(eSciDocUri);
        client.setTransport(TransportProtocol.REST);
        client.setHandle(handle);
    }

    public Role retrieve(final String roleObjectId) throws EscidocException,
        InternalClientException, TransportException {
        return client.retrieve(roleObjectId);
    }

    public Collection<Role> findAll() throws EscidocClientException {
        allRoles = client.retrieveRolesAsList(new SearchRetrieveRequestType());
        for (final Role r : allRoles) {
            roleById.put(r.getObjid(), r);
        }
        return allRoles;

    }
}