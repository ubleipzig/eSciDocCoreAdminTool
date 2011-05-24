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

import java.util.Collection;

import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;
import de.escidoc.core.client.interfaces.ContextHandlerClientInterface;
import de.escidoc.core.client.interfaces.base.HandlerService;
import de.escidoc.core.resources.Resource;
import de.escidoc.core.resources.om.context.Context;

public class ContextServiceLab extends AbstractEscidocService<ContextHandlerClientInterface> {

    public ContextServiceLab(final HandlerService client) {
        super(client);
    }

    @Override
    ContextHandlerClientInterface getClient() {
        return ((ContextHandlerClientInterface) client);
    }

    @Override
    public Resource create(final Resource resource) throws EscidocException, InternalClientException,
        TransportException {
        if (!(resource instanceof Context)) {
            throw new RuntimeException("Not instance of Context." + resource);
        }
        return getClient().create((Context) resource);
    }

    @Override
    Collection<? extends Resource> findPublicOrReleasedResources() throws EscidocException, InternalClientException,
        TransportException {
        return getClient().retrieveContextsAsList(withEmptyFilter());
    }

    @Override
    public Collection<? extends Resource> filterUsingInput(final String query) throws EscidocException,
        InternalClientException, TransportException {
        return getClient().retrieveContextsAsList(userInputToFilter(query));
    }

    @Override
    public Resource findById(final String objid) throws EscidocClientException {
        return getClient().retrieve(objid);
    }

    @Override
    public void update(final Resource resource) throws EscidocClientException {
        throw new UnsupportedOperationException("Not yet implemented");

    }
}