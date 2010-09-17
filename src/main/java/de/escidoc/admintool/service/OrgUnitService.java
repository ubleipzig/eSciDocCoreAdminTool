package de.escidoc.admintool.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.escidoc.admintool.app.AdminToolContants;
import de.escidoc.admintool.exception.ResourceNotFoundException;
import de.escidoc.core.client.OrganizationalUnitHandlerClient;
import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;
import de.escidoc.core.resources.common.Filter;
import de.escidoc.core.resources.common.TaskParam;
import de.escidoc.core.resources.oum.OrganizationalUnit;
import de.escidoc.core.resources.oum.Predecessor;

public class OrgUnitService {

    private static final Logger log = LoggerFactory // NOPMD by CHH on 9/17/10
                                                    // 10:29 AM
        .getLogger(OrgUnitService.class);

    private final Map<String, String> objectIdByTitle = // NOPMD by CHH on
        new HashMap<String, String>(); // 9/17/10 10:29 AM

    // TODO re-factor orgUnit- and ContextService: @See ContextService.java
    private final OrganizationalUnitHandlerClient client;

    private Collection<OrganizationalUnit> orgUnits;

    public OrgUnitService(final OrganizationalUnitHandlerClient client) {
        this.client = client;
    }

    public OrgUnitService(final String handle) throws InternalClientException {
        client = createOuClient(handle);
    }

    private OrganizationalUnitHandlerClient createOuClient(final String handle)
        throws InternalClientException {
        final OrganizationalUnitHandlerClient client =
            new OrganizationalUnitHandlerClient();
        client.setHandle(handle);
        client.setServiceAddress(AdminToolContants.ESCIDOC_SERVICE_ROOT_URI);
        return client;
    }

    private final Map<String, OrganizationalUnit> orgUnitById =
        new HashMap<String, OrganizationalUnit>();

    public Map<String, OrganizationalUnit> getOrgUnitById() {
        return orgUnitById;
    }

    public OrganizationalUnit find(final String objectId) {
        return orgUnitById.get(objectId);
    }

    public String findOrgUnitTitleById(final String objectId)
        throws EscidocException, InternalClientException, TransportException {
        if (orgUnitById.isEmpty()) {
            findAll();
        }
        final OrganizationalUnit orgUnit = orgUnitById.get(objectId);
        if (orgUnit == null) {
            throw new ResourceNotFoundException(
                "Can not find resource with object ID: " + objectId);
        }
        return orgUnit.getProperties().getName();
    }

    public String getObjectIdByTitle(final Object title) {
        return objectIdByTitle.get(title);
    }

    public Collection<OrganizationalUnit> findAll() throws EscidocException,
        InternalClientException, TransportException {
        orgUnits =
            client
                .retrieveOrganizationalUnits(emptyFilter())
                .getOrganizationalUnits();

        for (final OrganizationalUnit orgUnit : orgUnits) {
            orgUnitById.put(orgUnit.getObjid(), orgUnit);
        }

        return orgUnits;
    }

    // FIXME duplicate method in ContextService
    private TaskParam emptyFilter() {
        final Collection<Filter> filters = TaskParam.filtersFactory();
        filters.add(getFilter(AdminToolContants.CREATED_BY_FILTER,
            AdminToolContants.SYSADMIN_OBJECT_ID, null));

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

    public OrganizationalUnit create(final OrganizationalUnit orgUnit)
        throws EscidocException, InternalClientException, TransportException {
        final OrganizationalUnit createdOrgUnit = client.create(orgUnit);
        assert createdOrgUnit != null : "Got null reference from the server.";
        assert createdOrgUnit.getObjid() != null : "ObjectID can not be null.";
        assert orgUnitById != null : "orgUnitById is null";
        log
            .debug("Succesfully stored a new Organizational Unit with the Object ID: "
                + createdOrgUnit.getObjid());
        final int sizeBefore = orgUnitById.size();
        orgUnitById.put(createdOrgUnit.getObjid(), createdOrgUnit);
        final int sizeAfter = orgUnitById.size();
        assert sizeAfter > sizeBefore : "user account is not added to map.";

        return createdOrgUnit;
    }

    public OrganizationalUnit retrieve(final String objid)
        throws EscidocException, InternalClientException, TransportException {
        return client.retrieve(objid);
    }

    public OrganizationalUnit update(final OrganizationalUnit orgUnit)
        throws EscidocException, InternalClientException, TransportException {
        final OrganizationalUnit old = orgUnit;
        final OrganizationalUnit updatedOrgUnit = client.update(orgUnit);
        orgUnitById.remove(old.getObjid());
        orgUnitById.put(updatedOrgUnit.getObjid(), updatedOrgUnit);
        return updatedOrgUnit;
    }

    public void delete(final OrganizationalUnit orgUnit)
        throws EscidocException, InternalClientException, TransportException {
        final OrganizationalUnit old = orgUnit;
        client.delete(orgUnit.getObjid());
        orgUnitById.remove(old.getObjid());
    }

    public Collection<OrganizationalUnit> getOrganizationalUnits()
        throws EscidocException, InternalClientException, TransportException {
        if (orgUnits == null) {
            return findAll();
        }
        return orgUnitById.values();
    }

    public Collection<String> getPredecessorsObjectId(
        final OrganizationalUnit orgUnit) {
        assert orgUnit != null : "Org Unit can not be null";
        if (orgUnit.getPredecessors() == null) {
            return Collections.emptyList();
        }
        return getPredecessorsByObjectId(orgUnit).keySet();
    }

    public Map<String, Predecessor> getPredecessorsByObjectId(
        final OrganizationalUnit orgUnit) {

        final Map<String, Predecessor> predecessorByObjectId =
            new ConcurrentHashMap<String, Predecessor>();

        final Iterator<Predecessor> iterator =
            orgUnit.getPredecessors().iterator();

        assert iterator != null : "iterator can not be null.";
        while (iterator.hasNext()) {
            final Predecessor predecessor = iterator.next();
            predecessorByObjectId.put(predecessor.getObjid(), predecessor);
        }
        return predecessorByObjectId;
    }

    public Collection<OrganizationalUnit> getOrgUnitsByIds(
        final List<String> objectIds) {
        if (objectIds == null || objectIds.isEmpty()) {
            return Collections.emptyList(); // NOPMD by CHH on 9/17/10 10:32 AM
        }

        final List<OrganizationalUnit> collected =
            new ArrayList<OrganizationalUnit>(objectIds.size());
        for (final String objectId : objectIds) {
            collected.add(orgUnitById.get(objectId));
        }
        return collected;
    }

    public OrganizationalUnit open(final String objectId, final String comment)
        throws EscidocException, InternalClientException, TransportException {
        assert !(objectId == null || objectId.isEmpty()) : "objectId must not be null or empty";

        final TaskParam taskParam = new TaskParam();
        taskParam.setLastModificationDate(find(objectId)
            .getLastModificationDate());

        if (!comment.isEmpty()) {
            taskParam.setComment(comment);
        }

        client.open(objectId, taskParam);
        final OrganizationalUnit openedOrgUnit = client.retrieve(objectId);
        updateMap(objectId, openedOrgUnit);
        return openedOrgUnit;
    }

    private void updateMap(
        final String objectId, final OrganizationalUnit updatedOrgUnit) {
        orgUnitById.remove(objectId);
        orgUnitById.put(objectId, updatedOrgUnit);
    }

    public OrganizationalUnit close(final String objectId, final String comment)
        throws EscidocException, InternalClientException, TransportException {
        assert !(objectId == null || objectId.isEmpty()) : "objectId must not be null or empty";

        final TaskParam taskParam = new TaskParam();
        taskParam.setLastModificationDate(find(objectId)
            .getLastModificationDate());
        taskParam.setComment(comment);

        client.close(objectId, taskParam);

        final OrganizationalUnit closedContext = client.retrieve(objectId);

        updateMap(objectId, closedContext);

        return closedContext;
    }
}