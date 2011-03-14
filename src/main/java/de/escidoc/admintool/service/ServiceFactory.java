package de.escidoc.admintool.service;

import com.google.common.base.Preconditions;

import de.escidoc.admintool.view.EscidocServiceLocation;
import de.escidoc.core.client.AdminHandlerClient;
import de.escidoc.core.client.ContainerHandlerClient;
import de.escidoc.core.client.ContentModelHandlerClient;
import de.escidoc.core.client.ContextHandlerClient;
import de.escidoc.core.client.ItemHandlerClient;
import de.escidoc.core.client.OrganizationalUnitHandlerClient;
import de.escidoc.core.client.TransportProtocol;
import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;
import de.escidoc.core.client.exceptions.application.security.AuthenticationException;
import de.escidoc.core.client.interfaces.ContainerHandlerClientInterface;
import de.escidoc.core.client.interfaces.ContextHandlerClientInterface;
import de.escidoc.core.client.interfaces.HandlerServiceInterface;
import de.escidoc.core.client.interfaces.ItemHandlerClientInterface;
import de.escidoc.core.client.interfaces.OrganizationalUnitHandlerClientInterface;

public class ServiceFactory {

    private static final String SYSADMIN_PASSWORD_ESCIDEV_6 = "escidoc";

    private static final String SYSADMIN_PASSWORD_LOCAL = "eSciDoc";

    private static final String SYSADMIN_LOGIN_NAME = "sysadmin";

    private final String serviceUri;

    private final String token;

    private AdminServiceImpl adminService;

    private ContainerHandlerClientInterface client;

    private ItemHandlerClientInterface itemClient;

    public ServiceFactory(final EscidocServiceLocation escidocServiceLocation,
        final String token) {
        Preconditions.checkNotNull(escidocServiceLocation,
            "escidocServiceLocation can not be null: %s",
            escidocServiceLocation);
        Preconditions.checkNotNull(token, "token can not be null: %s", token);

        serviceUri = escidocServiceLocation.getUri();
        this.token = token;
    }

    public OrgUnitService createOrgService() throws InternalClientException {
        return new OrgUnitService(serviceUri, token);
    }

    public UserService createUserService() throws EscidocException,
        InternalClientException, TransportException {
        return new UserService(serviceUri, token);
    }

    public ContextService createContextService() throws EscidocException,
        TransportException, InternalClientException {
        return new ContextService(serviceUri, token);
    }

    public RoleService createRoleService() throws EscidocException,
        InternalClientException, TransportException {
        return new RoleService(serviceUri, token);
    }

    public EscidocService createContainerService()
        throws InternalClientException {
        client = new ContainerHandlerClient(serviceUri);
        client.setTransport(TransportProtocol.REST);
        final ContainerService containerService = new ContainerService(client);
        containerService.loginWith(token);
        return containerService;
    }

    public AdminService createAdminService() throws InternalClientException {
        adminService = new AdminServiceImpl(createAdminClient());
        adminService.loginWith(token);
        return adminService;
    }

    private HandlerServiceInterface createAdminClient()
        throws InternalClientException {
        final HandlerServiceInterface client =
            new AdminHandlerClient(serviceUri);
        client.setTransport(TransportProtocol.REST);
        return client;
    }

    public ItemService createItemService() throws InternalClientException {
        itemClient = new ItemHandlerClient(serviceUri);
        itemClient.setTransport(TransportProtocol.REST);
        final ItemService itemService = new ItemService(itemClient);
        itemService.loginWith(token);
        return itemService;
    }

    public ContextServiceLab createContextServiceLab()
        throws InternalClientException {
        final ContextHandlerClientInterface client =
            new ContextHandlerClient(serviceUri);
        client.setTransport(TransportProtocol.REST);
        final ContextServiceLab contextService = new ContextServiceLab(client);
        contextService.loginWith(token);
        return contextService;
    }

    public OrgUnitServiceLab createOrgUnitService()
        throws InternalClientException {
        final OrganizationalUnitHandlerClientInterface client =
            new OrganizationalUnitHandlerClient(serviceUri);
        client.setTransport(TransportProtocol.REST);
        final OrgUnitServiceLab orgUnitService = new OrgUnitServiceLab(client);
        orgUnitService.loginWith(token);
        return orgUnitService;
    }

    public ResourceService createContentModelService()
        throws InternalClientException {
        final ContentModelHandlerClient client =
            new ContentModelHandlerClient(serviceUri);
        client.setTransport(TransportProtocol.REST);

        final ResourceService service = new ContentModelService(client);
        service.loginWith(token);
        return service;
    }

    public PdpService createPdpService() throws AuthenticationException,
        TransportException {
        final PdpServiceImpl pdpService = new PdpServiceImpl(serviceUri);
        pdpService.loginWith(token);
        return pdpService;
    }
}