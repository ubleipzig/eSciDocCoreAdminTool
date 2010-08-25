package de.escidoc.admintool.service;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.xml.parsers.ParserConfigurationException;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.escidoc.admintool.domain.ContextFactory;
import de.escidoc.core.client.Authentication;
import de.escidoc.core.client.ContextHandlerClient;
import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;
import de.escidoc.core.resources.common.Filter;
import de.escidoc.core.resources.common.TaskParam;
import de.escidoc.core.resources.om.context.AdminDescriptors;
import de.escidoc.core.resources.om.context.Context;
import de.escidoc.core.resources.om.context.OrganizationalUnitRefs;

public class ContextService {
    private static final Logger log =
        LoggerFactory.getLogger(ContextService.class);

    // TODO separate Model/Cache and Service
    // TODO use blackboard / Event Bus to publish AddContextEvent,
    // EditContextEvent,DeleteContextEvent. Let the View subscribe to these
    // events and update itself with the changes.

    private static final String ESCIDOC_SERVICE_ROOT_URI =
        "http://localhost:8080";

    private final ContextHandlerClient client;

    private final Map<String, Context> contextById =
        new ConcurrentHashMap<String, Context>();

    public ContextService(final Authentication authentication)
        throws EscidocException, TransportException, InternalClientException {
        client = initClient(authentication.getHandle());
    }

    private ContextHandlerClient initClient(final String handle)
        throws EscidocException, TransportException, InternalClientException {
        final ContextHandlerClient client = new ContextHandlerClient();
        client.setHandle(handle);
        client.setServiceAddress(ESCIDOC_SERVICE_ROOT_URI);
        return client;
    }

    @SuppressWarnings("deprecation")
    public Collection<Context> all() throws EscidocException,
        InternalClientException, TransportException {
        log.info("Retrieving Context from repository...");
        final Collection<Context> contexts =
            client.retrieveContexts(createdBySysAdmin()).getContexts();
        if (contexts == null || contexts.size() == 0) {
            return Collections.emptySet();
        }

        for (final Context context : contexts) {
            contextById.put(context.getObjid(), context);
        }
        log
            .info("Retrieval is finished, got: " + contexts.size()
                + " contexts");
        return contexts;
    }

    private TaskParam createdBySysAdmin() {
        final Collection<Filter> filters = TaskParam.filtersFactory();
        filters.add(getFilter(
            "http://escidoc.de/core/01/structural-relations/created-by",
            "escidoc:exuser1", null));

        final TaskParam filterParam = new TaskParam();
        filterParam.setFilters(filters);
        return filterParam;
    }

    // FIXME duplicate method in ContextService
    private Filter getFilter(
        final String name, final String value, final Collection<String> ids) {

        final Filter filter = new Filter();
        filter.setName(name);
        filter.setValue(value);
        filter.setIds(ids);
        return filter;
    }

    public Context update(
        final String objectId, final String newName,
        final String newDescription, final String newType,
        final OrganizationalUnitRefs organizationalUnitRefs,
        final AdminDescriptors newAdminDescriptors) throws EscidocException,
        InternalClientException, TransportException {

        assert !(objectId == null || newName == null || newDescription == null || newType == null) : "Neither objectId nor newName nor newDescription parameters can be null.";
        assert !newName.isEmpty() : "Name can not be empty.";
        assert !newDescription.isEmpty() : "newDescription can not be empty.";
        assert !newType.isEmpty() : "newType can not be empty.";
        assert organizationalUnitRefs != null : "organizationalUnitRefs can not be null.";

        final Context updatedContext =
            new ContextFactory()
                .update(getSelected(objectId)).name(newName).description(
                    newDescription).type(newType).orgUnits(
                    organizationalUnitRefs).adminDescriptors(
                    newAdminDescriptors).build();

        final Context fromRepository = client.update(updatedContext);
        assert fromRepository != null : "update fails and return Null Pointer";

        contextById.put(fromRepository.getObjid(), fromRepository);

        return fromRepository;
    }

    public Context getSelected(final String objectId) {
        assert !(objectId == null || objectId.isEmpty()) : "objectId must not be null or empty";
        final Context context = contextById.get(objectId);
        assert context != null : "context does not exist";
        return context;
    }

    public Context open(final String objectId) throws EscidocException,
        InternalClientException, TransportException {

        assert !(objectId == null || objectId.isEmpty()) : "objectId must not be null or empty";

        client.open(objectId, lastModificationDate(getSelected(objectId)
            .getLastModificationDate()));
        final Context openedContext = client.retrieve(objectId);
        updateMap(objectId, openedContext);

        return openedContext;
    }

    public Context open(final String objectId, final String comment)
        throws EscidocException, InternalClientException, TransportException {
        assert !(objectId == null || objectId.isEmpty()) : "objectId must not be null or empty";

        final TaskParam taskParam = new TaskParam();
        taskParam.setLastModificationDate(getSelected(objectId)
            .getLastModificationDate());

        if (!comment.isEmpty()) {
            taskParam.setComment(comment);
        }
        client.open(objectId, taskParam);
        final Context openedContext = client.retrieve(objectId);
        updateMap(objectId, openedContext);
        return openedContext;
    }

    private TaskParam lastModificationDate(final DateTime lastModificationDate) {
        final TaskParam taskParam = new TaskParam();
        taskParam.setLastModificationDate(lastModificationDate);
        return taskParam;
    }

    public Context close(final String objectId) throws EscidocException,
        InternalClientException, TransportException {
        assert !(objectId == null || objectId.isEmpty()) : "objectId must not be null or empty";

        client.close(objectId, lastModificationDate(getSelected(objectId)
            .getLastModificationDate()));
        final Context closedContext = client.retrieve(objectId);

        updateMap(objectId, closedContext);

        return closedContext;
    }

    private void updateMap(final String objectId, final Context updatedContext) {
        contextById.remove(objectId);
        contextById.put(objectId, updatedContext);
    }

    public void delete(final String objectId) throws EscidocException,
        InternalClientException, TransportException {
        client.delete(objectId);
        contextById.remove(objectId);
    }

    public Context close(final String objectId, final String comment)
        throws EscidocException, InternalClientException, TransportException {
        assert !(objectId == null || objectId.isEmpty()) : "objectId must not be null or empty";

        final TaskParam taskParam = new TaskParam();
        taskParam.setLastModificationDate(getSelected(objectId)
            .getLastModificationDate());
        taskParam.setComment(comment);

        client.close(objectId, taskParam);
        final Context closedContext = client.retrieve(objectId);
        updateMap(objectId, closedContext);
        return closedContext;
    }

    public Context create(
        final String name, final String description, final String contextType,
        final OrganizationalUnitRefs orgUnitRefs,
        final AdminDescriptors adminDescriptors) throws EscidocException,
        InternalClientException, TransportException,
        ParserConfigurationException {

        assert !(name == null || name.isEmpty()) : "name can not be null or empty";
        assert !(description == null || description.isEmpty()) : "description name can not be null or empty";
        assert !(contextType == null || contextType.isEmpty()) : "contextType name can not be null or empty";
        assert (orgUnitRefs != null) : "orgUnitRefs can not be null";
        assert (orgUnitRefs.size() > 0) : "orgUnitRefs can not be empty";

        final Context backedContext =
            new ContextFactory().create(name, description, contextType,
                orgUnitRefs).adminDescriptors(adminDescriptors).build();
        final Context createdContext = client.create(backedContext);
        assert createdContext != null : "Got null reference from the server.";
        assert createdContext.getObjid() != null : "ObjectID can not be null.";
        final int sizeBefore = contextById.size();
        contextById.put(createdContext.getObjid(), createdContext);
        final int sizeAfter = contextById.size();
        assert sizeAfter > sizeBefore : "context is not added to map.";
        return createdContext;
    }
}