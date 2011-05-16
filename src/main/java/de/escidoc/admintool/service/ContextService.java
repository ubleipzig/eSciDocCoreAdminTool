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

import gov.loc.www.zing.srw.SearchRetrieveRequestType;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.xml.parsers.ParserConfigurationException;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import de.escidoc.admintool.domain.ContextFactory;
import de.escidoc.core.client.ContextHandlerClient;
import de.escidoc.core.client.TransportProtocol;
import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;
import de.escidoc.core.client.interfaces.ContextHandlerClientInterface;
import de.escidoc.core.resources.common.TaskParam;
import de.escidoc.core.resources.om.context.AdminDescriptors;
import de.escidoc.core.resources.om.context.Context;
import de.escidoc.core.resources.om.context.OrganizationalUnitRefs;

public class ContextService {

    private static final Logger LOG = LoggerFactory.getLogger(ContextService.class);

    private ContextHandlerClientInterface client;

    private final Map<String, Context> contextById = new ConcurrentHashMap<String, Context>();

    private final Multimap<String, Context> contextByTitle = HashMultimap.create();

    private final String eSciDocUri;

    private final String handle;

    public ContextService(final String eSciDocUri, final String handle) throws InternalClientException {
        this.eSciDocUri = eSciDocUri;
        this.handle = handle;
        initClient();
    }

    private void initClient() throws InternalClientException {
        client = new ContextHandlerClient(eSciDocUri);
        client.setTransport(TransportProtocol.REST);
        client.setHandle(handle);
    }

    public Collection<Context> findAll() throws EscidocClientException {
        LOG.info("Retrieving Context from repository...");

        final Collection<Context> contexts = client.retrieveContextsAsList(new SearchRetrieveRequestType());

        if (contexts == null || contexts.isEmpty()) {
            return Collections.emptySet(); // NOPMD by CHH on 9/17/10 10:23 AM
        }

        for (final Context context : contexts) {
            // FIXME: createContextView only one cache/Map for both
            contextById.put(context.getObjid(), context);
            contextByTitle.put(context.getProperties().getName(), context);
        }

        LOG.info("Retrieval is finished, got: " + contexts.size() + " contexts");
        return contexts;
    }

    public Collection<Context> findByTitle(final String ctxTitle) throws EscidocClientException {
        if (contextByTitle.size() == 0) {
            findAll();
        }
        return contextByTitle.get(ctxTitle);
    }

    public Collection<Context> getCache() throws EscidocClientException {
        if (contextById.values().size() == 0) {
            findAll();
        }
        return Collections.unmodifiableCollection(contextById.values());
    }

    public Context update(
        final String objectId, final String newName, final String newDescription, final String newType,
        final OrganizationalUnitRefs orgUnitRefs, final AdminDescriptors newAdminDescriptors)
        throws EscidocClientException {

        assert !(objectId == null || newName == null || newDescription == null || newType == null) : "Neither objectId nor newName nor newDescription parameters can be null.";
        assert !newName.isEmpty() : "Name can not be empty.";
        assert !newDescription.isEmpty() : "newDescription can not be empty.";
        assert !newType.isEmpty() : "newType can not be empty.";
        assert orgUnitRefs != null : "organizationalUnitRefs can not be null.";

        final ContextBuilder builder = new ContextBuilder(getSelected(objectId));
        final Context updatedContext =
            builder
                .name(newName).description(newDescription).type(newType).orgUnits(orgUnitRefs)
                .adminDescriptors(newAdminDescriptors).build();

        final Context fromRepository = client.update(updatedContext);
        assert fromRepository != null : "update fails and return Null Pointer";

        contextById.put(fromRepository.getObjid(), fromRepository);

        return fromRepository;
    }

    public Context getSelected(final String objectId) {
        assertNotEmpty(objectId);
        final Context context = contextById.get(objectId);
        assert context != null : "context does not exist";
        return context;
    }

    private void assertNotEmpty(final String objectId) {
        assert !(objectId == null || objectId.isEmpty()) : "objectId must not be null or empty";
    }

    public Context open(final String objectId) throws EscidocException, InternalClientException, TransportException {

        assertNotEmpty(objectId);

        client.open(objectId, lastModificationDate(getSelected(objectId).getLastModificationDate()));
        final Context openedContext = client.retrieve(objectId);
        updateMap(objectId, openedContext);

        return openedContext;
    }

    public Context open(final String objectId, final String comment) throws EscidocException, InternalClientException,
        TransportException {
        assertNotEmpty(objectId);

        final TaskParam taskParam = new TaskParam();
        taskParam.setLastModificationDate(getSelected(objectId).getLastModificationDate());

        if (!comment.isEmpty()) {
            taskParam.setComment(comment);
        }
        client.open(objectId, taskParam);
        final Context openedContext = client.retrieve(objectId);
        updateMap(objectId, openedContext);
        return openedContext;
    }

    private TaskParam lastModificationDate(final DateTime lastModificationDate) { // NOPMD
                                                                                  // by
                                                                                  // CHH
                                                                                  // on
                                                                                  // 9/17/10
                                                                                  // 10:24
                                                                                  // AM
        final TaskParam taskParam = new TaskParam();
        taskParam.setLastModificationDate(lastModificationDate);
        return taskParam;
    }

    public Context close(final String objectId) throws EscidocException, InternalClientException, TransportException {
        assertNotEmpty(objectId);

        client.close(objectId, lastModificationDate(getSelected(objectId).getLastModificationDate()));
        final Context closedContext = client.retrieve(objectId);

        updateMap(objectId, closedContext);

        return closedContext;
    }

    private void updateMap(final String objectId, final Context updatedContext) {
        contextById.remove(objectId);
        contextById.put(objectId, updatedContext);
    }

    public void delete(final String objectId) throws EscidocClientException {
        client.delete(objectId);
        contextById.remove(objectId);
    }

    public Context close(final String objectId, final String comment) throws EscidocException, InternalClientException,
        TransportException {
        assertNotEmpty(objectId);

        final TaskParam taskParam = new TaskParam();
        taskParam.setLastModificationDate(getSelected(objectId).getLastModificationDate());
        taskParam.setComment(comment);

        client.close(objectId, taskParam);
        final Context closedContext = client.retrieve(objectId);
        updateMap(objectId, closedContext);
        return closedContext;
    }

    public Context create(
        final String name, final String description, final String contextType,
        final OrganizationalUnitRefs orgUnitRefs, final AdminDescriptors adminDescriptors)
        throws ParserConfigurationException, EscidocClientException {

        assert !(name == null || name.isEmpty()) : "name can not be null or empty";
        assert !(description == null || description.isEmpty()) : "description name can not be null or empty";
        assert !(contextType == null || contextType.isEmpty()) : "contextType name can not be null or empty";
        assert (orgUnitRefs != null) : "orgUnitRefs can not be null";
        assert (orgUnitRefs.size() > 0) : "orgUnitRefs can not be empty";

        final Context backedContext =
            createContextDTO(name, description, contextType, orgUnitRefs, adminDescriptors).build();
        final Context createdContext = client.create(backedContext);

        assert createdContext != null : "Got null reference from the server.";
        assert createdContext.getObjid() != null : "ObjectID can not be null.";
        final int sizeBefore = contextById.size();
        contextById.put(createdContext.getObjid(), createdContext);
        final int sizeAfter = contextById.size();
        assert sizeAfter > sizeBefore : "context is not added to map.";
        return createdContext;
    }

    private ContextFactory createContextDTO(
        final String name, final String description, final String contextType,
        final OrganizationalUnitRefs orgUnitRefs, final AdminDescriptors adminDescriptors)
        throws ParserConfigurationException {
        return new ContextFactory().create(name, description, contextType, orgUnitRefs).adminDescriptors(
            adminDescriptors);
    }
}