package de.escidoc.admintool.service;

import com.google.common.base.Preconditions;

import de.admintool.services.PdpServiceImpl;
import de.escidoc.core.client.AdminHandlerClient;
import de.escidoc.core.client.Authentication;
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

    private static final String SYSADMIN_PASSWORD = "eSciDoc";

    private static final String SYSADMIN_LOGIN_NAME = "sysadmin";

    private final String eSciDocUri;

    private final String token;

    private AdminServiceImpl adminService;

    private ContainerHandlerClientInterface client;

    private ItemHandlerClientInterface itemClient;

    public ServiceFactory(final String eSciDocUri, final String token) {
        Preconditions.checkNotNull(eSciDocUri,
            "eSciDocUri can not be null: %s", eSciDocUri);
        Preconditions.checkNotNull(token, "token can not be null: %s", token);

        this.eSciDocUri = eSciDocUri;
        this.token = token;
    }

    public OrgUnitService createOrgService() throws InternalClientException {
        return new OrgUnitService(eSciDocUri, token);
    }

    public UserService createUserService() throws EscidocException,
        InternalClientException, TransportException {
        return new UserService(eSciDocUri, token);
    }

    public ContextService createContextService() throws EscidocException,
        TransportException, InternalClientException {
        return new ContextService(eSciDocUri, token);
    }

    public RoleService createRoleService() throws EscidocException,
        InternalClientException, TransportException {
        return new RoleService(eSciDocUri, token);
    }

    public EscidocService createContainerService()
        throws InternalClientException {
        client = new ContainerHandlerClient(eSciDocUri);
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
            new AdminHandlerClient(eSciDocUri);
        client.setTransport(TransportProtocol.REST);
        return client;
    }

    public ItemService createItemService() throws InternalClientException {
        itemClient = new ItemHandlerClient(eSciDocUri);
        itemClient.setTransport(TransportProtocol.REST);
        final ItemService itemService = new ItemService(itemClient);
        itemService.loginWith(token);
        return itemService;
    }

    public ContextServiceLab createContextServiceLab()
        throws InternalClientException {
        final ContextHandlerClientInterface client =
            new ContextHandlerClient(eSciDocUri);
        client.setTransport(TransportProtocol.REST);
        final ContextServiceLab contextService = new ContextServiceLab(client);
        contextService.loginWith(token);
        return contextService;
    }

    public OrgUnitServiceLab createOrgUnitService()
        throws InternalClientException {
        final OrganizationalUnitHandlerClientInterface client =
            new OrganizationalUnitHandlerClient(eSciDocUri);
        client.setTransport(TransportProtocol.REST);
        final OrgUnitServiceLab orgUnitService = new OrgUnitServiceLab(client);
        orgUnitService.loginWith(token);
        return orgUnitService;
    }

    public ResourceService createContentModelService()
        throws InternalClientException {
        final ContentModelHandlerClient client =
            new ContentModelHandlerClient(eSciDocUri);
        client.setTransport(TransportProtocol.REST);

        final ResourceService service = new ContentModelService(client);
        service.loginWith(token);
        return service;
    }

    // TODO get SysAdmin LoginName from file/database
    public PdpService createPdpService() throws AuthenticationException,
        TransportException {
        return new PdpServiceImpl(new Authentication(eSciDocUri,
            SYSADMIN_LOGIN_NAME, SYSADMIN_PASSWORD));
    }
}