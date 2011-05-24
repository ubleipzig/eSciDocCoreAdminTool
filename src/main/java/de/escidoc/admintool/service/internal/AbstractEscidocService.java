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

import gov.loc.www.zing.srw.SearchRetrieveRequestType;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.apache.axis.types.NonNegativeInteger;

import com.google.common.base.Preconditions;

import de.escidoc.admintool.app.AppConstants;
import de.escidoc.admintool.service.LoginService;
import de.escidoc.core.client.Authentication;
import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;
import de.escidoc.core.client.interfaces.base.HandlerService;
import de.escidoc.core.resources.Resource;
import de.escidoc.core.resources.common.TaskParam;

public abstract class AbstractEscidocService<T extends HandlerService> implements ResourceService, LoginService {

    protected HandlerService client;

    private Authentication authentification;

    public AbstractEscidocService(final HandlerService client) {
        Preconditions.checkNotNull(client, "Client is null: %s", client);
        this.client = client;
    }

    // public AbstractEscidocService(final EscidocServiceLocation escidocServiceLocation) {
    // final ContextHandlerClientInterface client = new ContextHandlerClient(escidocServiceLocation.getUri());
    // client.setTransport(TransportProtocol.REST);
    // this.client = client;
    // }

    public AbstractEscidocService(final Authentication authentication) {
        this.authentification = authentication;
    }

    @Override
    public void login() throws InternalClientException {
        client.setHandle(authentification.getHandle());
    }

    abstract T getClient();

    @Override
    public void loginWith(final String handle) throws InternalClientException {
        client.setHandle(handle);
    }

    @Override
    public Set<Resource> findAll() throws EscidocClientException {
        return Collections.unmodifiableSet(new HashSet<Resource>(findPublicOrReleasedResources()));
    }

    protected TaskParam withEmptyTaskParam() {
        return new TaskParam();
    }

    protected SearchRetrieveRequestType withEmptyFilter() {
        final SearchRetrieveRequestType request = new SearchRetrieveRequestType();
        request.setMaximumRecords(new NonNegativeInteger(AppConstants.MAX_RESULT_SIZE));
        return request;
    }

    abstract Collection<? extends Resource> findPublicOrReleasedResources() throws EscidocException,
        InternalClientException, TransportException;

    @Override
    public abstract Collection<? extends Resource> filterUsingInput(String query) throws EscidocException,
        InternalClientException, TransportException;

    protected SearchRetrieveRequestType userInputToFilter(final String query) {
        final SearchRetrieveRequestType filter = new SearchRetrieveRequestType();
        filter.setQuery(query);
        return filter;
    }
}