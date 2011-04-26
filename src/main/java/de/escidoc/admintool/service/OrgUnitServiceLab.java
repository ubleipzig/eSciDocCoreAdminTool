package de.escidoc.admintool.service;

import gov.loc.www.zing.srw.SearchRetrieveRequestType;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.google.common.base.Preconditions;

import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;
import de.escidoc.core.client.interfaces.OrganizationalUnitHandlerClientInterface;
import de.escidoc.core.client.interfaces.base.HandlerService;
import de.escidoc.core.resources.Resource;
import de.escidoc.core.resources.common.Result;
import de.escidoc.core.resources.common.TaskParam;
import de.escidoc.core.resources.oum.OrganizationalUnit;
import de.escidoc.core.resources.oum.Parent;
import de.escidoc.core.resources.oum.Parents;

public class OrgUnitServiceLab extends AbstractEscidocService<OrganizationalUnitHandlerClientInterface> {

    public OrgUnitServiceLab(final HandlerService client) {
        super(client);
    }

    @Override
    public OrganizationalUnitHandlerClientInterface getClient() {
        return (OrganizationalUnitHandlerClientInterface) client;
    }

    @Override
    public Resource create(final Resource resource) throws EscidocException, InternalClientException,
        TransportException {
        return getClient().create((OrganizationalUnit) resource);
    }

    @Override
    public Collection<? extends Resource> findPublicOrReleasedResources() throws EscidocException,
        InternalClientException, TransportException {
        return getClient().retrieveOrganizationalUnitsAsList(withEmptyFilter());
    }

    @Override
    public Collection<? extends Resource> filterUsingInput(final String query) throws EscidocException,
        InternalClientException, TransportException {
        return getClient().retrieveOrganizationalUnitsAsList(userInputToFilter(query));
    }

    public void parent(final OrganizationalUnit orgUnitWithParent) throws InternalClientException, TransportException,
        EscidocClientException {

        getClient().update(orgUnitWithParent);
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public Collection<OrganizationalUnit> getTopLevelOrgUnits() throws EscidocException, InternalClientException,
        TransportException {
        final SearchRetrieveRequestType searchRequest = new SearchRetrieveRequestType();
        searchRequest.setQuery("\"top-level-organizational-units\"=true");
        return getClient().retrieveOrganizationalUnitsAsList(searchRequest);
    }

    public Collection<OrganizationalUnit> retrieveChildren(final String objid) throws EscidocException,
        InternalClientException, TransportException {
        Preconditions.checkNotNull(objid, "objid is null: %s", objid);
        Preconditions.checkArgument(!objid.isEmpty(), "objid is empty", objid);

        final List<OrganizationalUnit> children = getClient().retrieveChildObjectsAsList(objid);

        if (children == null) {
            return Collections.emptySet();
        }

        return children;
    }

    @Override
    public OrganizationalUnit findById(final String objid) throws EscidocClientException {
        return getClient().retrieve(objid);
    }

    public Parents updateParent(final OrganizationalUnit child, final OrganizationalUnit parent)
        throws EscidocClientException {

        final Parents parents = new Parents();
        parents.add(new Parent(parent.getObjid()));

        return getClient().updateParents(child, parents);
    }

    public OrganizationalUnit updateParent(final OrganizationalUnit child, final String parentObjectId)
        throws EscidocClientException {

        Preconditions.checkNotNull(child, "child is null: %s", child);
        Preconditions.checkNotNull(parentObjectId, "parentObjectId is null: %s", parentObjectId);
        Preconditions.checkArgument(!parentObjectId.isEmpty(), "parentObjectId is empty", parentObjectId);

        final Parents parents = new Parents();
        parents.add(new Parent(parentObjectId));

        child.setParents(parents);

        return getClient().update(child);

    }

    public OrganizationalUnit removeParent(final OrganizationalUnit child) throws EscidocClientException {

        Preconditions.checkNotNull(child, "child is null: %s", child);

        final Parents parents = new Parents();
        child.setParents(parents);

        return getClient().update(child);
    }

    public Parents updateParent(final String childId, final String parentObjectId) throws EscidocClientException {

        Preconditions.checkNotNull(childId, "child is null: %s", childId);
        Preconditions.checkArgument(!childId.isEmpty(), "childId is empty", childId);

        Preconditions.checkNotNull(parentObjectId, "parentObjectId is null: %s", parentObjectId);
        Preconditions.checkArgument(!parentObjectId.isEmpty(), "parentObjectId is empty", parentObjectId);

        final Parents parents = new Parents();
        parents.add(new Parent(parentObjectId));

        return getClient().updateParents(findById(childId), parents);
    }

    @Override
    public void update(final Resource resource) throws EscidocClientException {
        getClient().update((OrganizationalUnit) resource);
    }

    public void delete(final String objectId) throws EscidocClientException {
        getClient().delete(objectId);
    }

    public Object open(final String objectId, final String comment) throws EscidocClientException {
        Preconditions.checkArgument(objectId != null && !objectId.isEmpty(), "objectId must not be null or empty");
        return getClient().open(objectId, createCommentForStatus(objectId, comment));
    }

    public Result close(final String objectId, final String comment) throws EscidocClientException {
        Preconditions.checkArgument(objectId != null && !objectId.isEmpty(), "objectId must not be null or empty");
        return getClient().close(objectId, createCommentForStatus(objectId, comment));
    }

    private TaskParam createCommentForStatus(final String objectId, final String comment) throws EscidocClientException {
        final TaskParam taskParam = new TaskParam();
        taskParam.setLastModificationDate(findById(objectId).getLastModificationDate());

        if (!comment.isEmpty()) {
            taskParam.setComment(comment);
        }
        return taskParam;
    }
}