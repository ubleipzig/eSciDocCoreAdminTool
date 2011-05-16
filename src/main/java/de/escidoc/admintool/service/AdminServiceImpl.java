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
package de.escidoc.admintool.service;

import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;
import de.escidoc.core.client.interfaces.AdminHandlerClientInterface;
import de.escidoc.core.client.interfaces.base.HandlerService;
import de.escidoc.core.resources.adm.LoadExamplesResult.Entry;
import de.escidoc.core.resources.adm.MessagesStatus;
import de.escidoc.core.resources.common.MessagesResult;
import de.escidoc.core.resources.common.TaskParam;

public class AdminServiceImpl implements AdminService {

    private static final Logger LOG = LoggerFactory.getLogger(AdminServiceImpl.class);

    private final HandlerService client;

    public AdminServiceImpl(final HandlerService client) {
        this.client = client;
    }

    @Override
    public MessagesResult<Entry> loadCommonExamples() throws EscidocException, InternalClientException,
        TransportException {
        return getClient().loadExamples();
    }

    AdminHandlerClientInterface getClient() {
        return (AdminHandlerClientInterface) client;
    }

    @Override
    public void loginWith(final String handle) throws InternalClientException {
        client.setHandle(handle);
    }

    @Override
    public Map<String, String> getRepositoryInfo() throws EscidocException, InternalClientException, TransportException {
        return getClient().getRepositoryInfo();
    }

    @Override
    public MessagesStatus purge(final Set<String> list) throws EscidocException, InternalClientException,
        TransportException {
        return getClient().deleteObjects(usingTaskParam(list));
    }

    private TaskParam usingTaskParam(final Set<String> ids) {
        final TaskParam param = new TaskParam();
        param.setKeepInSync(true);
        for (final String id : ids) {
            param.addResourceRef(id);
        }
        return param;
    }

    @Override
    public MessagesStatus retrievePurgeStatus() throws EscidocException, InternalClientException, TransportException {
        return getClient().getPurgeStatus();
    }

    @Override
    public MessagesStatus reindexAll(final boolean shouldClearIndex) throws EscidocException, InternalClientException,
        TransportException {
        return getClient().reindexAll(shouldClearIndex);
    }

    @Override
    public MessagesStatus retrieveReindexStatus() throws EscidocException, InternalClientException, TransportException {
        return getClient().getReindexStatus();
    }

    @Override
    public MessagesStatus reindex(final Boolean shouldClearIndex, final String indexNamePrefix)
        throws EscidocException, InternalClientException, TransportException {
        Preconditions.checkNotNull(indexNamePrefix, "indexNamePrefix can not be null: %s", indexNamePrefix);
        Preconditions
            .checkArgument(!indexNamePrefix.isEmpty(), "indexNamePrefix can not be empty: %s", indexNamePrefix);
        return getClient().reindex(shouldClearIndex.booleanValue(), indexNamePrefix);
    }
}
