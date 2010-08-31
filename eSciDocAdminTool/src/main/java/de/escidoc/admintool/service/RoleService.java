package de.escidoc.admintool.service;

import de.escidoc.core.client.Authentication;
import de.escidoc.core.client.RoleHandlerClient;
import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;
import de.escidoc.core.resources.aa.role.Role;

public class RoleService {

    private static final String ESCIDOC_SERVICE_ROOT_URI =
        "http://localhost:8080";

    private final Authentication authentification;

    private RoleHandlerClient roleClient;

    public RoleService(final Authentication authentification)
        throws EscidocException, InternalClientException, TransportException {
        this.authentification = authentification;
        createClient();
    }

    private void createClient() throws EscidocException,
        InternalClientException, TransportException {
        roleClient = new RoleHandlerClient();
        roleClient.setHandle(authentification.getHandle());
        roleClient.setServiceAddress(ESCIDOC_SERVICE_ROOT_URI);
    }

    public Role retrieve(final String roleObjectId) throws EscidocException,
        InternalClientException, TransportException {
        return roleClient.retrieve(roleObjectId);
    }
}
