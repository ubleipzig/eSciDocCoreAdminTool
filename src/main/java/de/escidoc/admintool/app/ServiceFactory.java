package de.escidoc.admintool.app;

import de.escidoc.admintool.service.ContextService;
import de.escidoc.admintool.service.OrgUnitService;
import de.escidoc.admintool.service.RoleService;
import de.escidoc.admintool.service.UserService;
import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;

public class ServiceFactory {

    private final String eSciDocUri;

    private final String authentication;

    public ServiceFactory(final String eSciDocUri, final String token) {
        this.eSciDocUri = eSciDocUri;
        authentication = token;
    }

    public OrgUnitService createOrgService() throws InternalClientException {
        return new OrgUnitService(eSciDocUri, authentication);
    }

    public UserService createUserService() throws EscidocException,
        InternalClientException, TransportException {
        return new UserService(eSciDocUri, authentication);
    }

    public ContextService createContextService() throws EscidocException,
        TransportException, InternalClientException {
        return new ContextService(eSciDocUri, authentication);
    }

    public RoleService createRoleService() throws EscidocException,
        InternalClientException, TransportException {
        return new RoleService(eSciDocUri, authentication);
    }
}