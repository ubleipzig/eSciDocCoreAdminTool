/**
 * CDDL HEADER START
 *
 * The contents of this file are subject to the terms of the
 * Common Development and Distribution License, Version 1.0 only
 * (the "License").  You may not use this file except in compliance
 * with the License.
 *
 * You can obtain a copy of the license at license/ESCIDOC.LICENSE
 * or https://www.escidoc.org/license/ESCIDOC.LICENSE .
 * See the License for the specific language governing permissions
 * and limitations under the License.
 *
 * When distributing Covered Code, include this CDDL HEADER in each
 * file and include the License file at license/ESCIDOC.LICENSE.
 * If applicable, add the following below this CDDL HEADER, with the
 * fields enclosed by brackets "[]" replaced with your own identifying
 * information: Portions Copyright [yyyy] [name of copyright owner]
 *
 * CDDL HEADER END
 *
 *
 *
 * Copyright 2011 Fachinformationszentrum Karlsruhe Gesellschaft
 * fuer wissenschaftlich-technische Information mbH and Max-Planck-
 * Gesellschaft zur Foerderung der Wissenschaft e.V.
 * All rights reserved.  Use is subject to license terms.
 */
package de.escidoc.admintool.service.internal;

import java.net.MalformedURLException;
import java.net.URL;

import com.google.common.base.Preconditions;

import de.escidoc.admintool.service.AdminService;
import de.escidoc.admintool.service.EscidocService;
import de.escidoc.admintool.service.PdpService;
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
import de.escidoc.core.client.interfaces.ItemHandlerClientInterface;
import de.escidoc.core.client.interfaces.OrganizationalUnitHandlerClientInterface;
import de.escidoc.core.client.interfaces.base.HandlerService;

public class ServiceFactory {

    private final String serviceUri;

    private final String token;

    private AdminServiceImpl adminService;

    private ContainerHandlerClientInterface client;

    private ItemHandlerClientInterface itemClient;

    public ServiceFactory(final EscidocServiceLocation escidocServiceLocation, final String token) {
        Preconditions.checkNotNull(escidocServiceLocation, "escidocServiceLocation can not be null: %s",
            escidocServiceLocation);
        Preconditions.checkNotNull(token, "token can not be null: %s", token);

        serviceUri = escidocServiceLocation.getUri();
        this.token = token;
    }

    public OrgUnitService createOrgService() throws InternalClientException {
        return new OrgUnitService(serviceUri, token);
    }

    public UserService createUserService() throws EscidocException, InternalClientException, TransportException {
        return new UserService(serviceUri, token);
    }

    public ContextService createContextService() throws EscidocException, TransportException, InternalClientException {
        return new ContextService(serviceUri, token);
    }

    public RoleService createRoleService() throws EscidocException, InternalClientException, TransportException {
        return new RoleService(serviceUri, token);
    }

    public EscidocService createContainerService() throws InternalClientException {
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

    private HandlerService createAdminClient() throws InternalClientException {
        final HandlerService client = new AdminHandlerClient(serviceUri);
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

    public ContextServiceLab createContextServiceLab() throws InternalClientException {
        final ContextHandlerClientInterface client = new ContextHandlerClient(serviceUri);
        client.setTransport(TransportProtocol.REST);
        final ContextServiceLab contextService = new ContextServiceLab(client);
        contextService.loginWith(token);
        return contextService;
    }

    public OrgUnitServiceLab createOrgUnitService() throws InternalClientException {
        final OrganizationalUnitHandlerClientInterface client = new OrganizationalUnitHandlerClient(serviceUri);
        client.setTransport(TransportProtocol.REST);
        final OrgUnitServiceLab orgUnitService = new OrgUnitServiceLab(client);
        orgUnitService.loginWith(token);
        return orgUnitService;
    }

    public ResourceService createContentModelService() throws InternalClientException {
        final ContentModelHandlerClient client = new ContentModelHandlerClient(serviceUri);
        client.setTransport(TransportProtocol.REST);

        final ResourceService service = new ContentModelService(client);
        service.loginWith(token);
        return service;
    }

    public PdpService createPdpService() throws AuthenticationException, TransportException, MalformedURLException {
        final PdpServiceImpl pdpService = new PdpServiceImpl(new URL(serviceUri));
        pdpService.loginWith(token);
        return pdpService;
    }
}