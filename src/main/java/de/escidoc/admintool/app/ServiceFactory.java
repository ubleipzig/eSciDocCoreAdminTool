package de.escidoc.admintool.app;

import de.escidoc.admintool.service.AdminService;
import de.escidoc.admintool.service.AdminServiceImpl;
import de.escidoc.admintool.service.ContainerService;
import de.escidoc.admintool.service.ContextService;
import de.escidoc.admintool.service.EscidocService;
import de.escidoc.admintool.service.OrgUnitService;
import de.escidoc.admintool.service.RoleService;
import de.escidoc.admintool.service.UserService;
import de.escidoc.core.client.AdminHandlerClient;
import de.escidoc.core.client.ContainerHandlerClient;
import de.escidoc.core.client.TransportProtocol;
import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;
import de.escidoc.core.client.interfaces.ContainerHandlerClientInterface;
import de.escidoc.core.client.interfaces.HandlerServiceInterface;

public class ServiceFactory {

    private final String eSciDocUri;

    private final String authentication;

    public ServiceFactory(final String eSciDocUri, final String token) {
        this.eSciDocUri = eSciDocUri;
        authentication = token;
    }

    public OrgUnitService createOrgService() throws InternalClientException {
        final OrgUnitService orgUnitService =
            new OrgUnitService(eSciDocUri, authentication);
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

    private ContainerHandlerClientInterface client;

    private AdminServiceImpl adminService;

    public EscidocService createContainerService()
        throws InternalClientException {
        client = new ContainerHandlerClient(eSciDocUri);
        client.setTransport(TransportProtocol.REST);
        final ContainerService containerService = new ContainerService(client);
        containerService.loginWith(authentication);
        return containerService;
    }

    public AdminService createAdminService() throws InternalClientException {
        adminService = new AdminServiceImpl(createAdminClient());
        return adminService;
    }

    private HandlerServiceInterface createAdminClient()
        throws InternalClientException {
        final HandlerServiceInterface client =
            new AdminHandlerClient(eSciDocUri);
        client.setTransport(TransportProtocol.REST);
        return client;
    }
}