package de.uni_leipzig.ubl.admintool.service.internal;

import gov.loc.www.zing.srw.SearchRetrieveRequestType;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.axis.types.NonNegativeInteger;

import com.google.common.base.Preconditions;

import de.escidoc.admintool.app.AppConstants;
import de.escidoc.core.client.TransportProtocol;
import de.escidoc.core.client.UserGroupHandlerClient;
import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;
import de.escidoc.core.client.interfaces.UserGroupHandlerClientInterface;
import de.escidoc.core.resources.aa.useraccount.Grant;
import de.escidoc.core.resources.aa.usergroup.UserGroup;
import de.escidoc.core.resources.common.TaskParam;
import de.uni_leipzig.ubl.admintool.domain.UserGroupFactory;

public class GroupService {
	
	private UserGroupHandlerClientInterface client;
	
    private final Map<String, UserGroup> userGroupById = new ConcurrentHashMap<String, UserGroup>();

    private final String eSciDocUri;

    private final String handle;

    private Collection<UserGroup> userGroups;

    private UserGroup group;

    public GroupService(final String eSciDocUri, final String handle) throws InternalClientException {
        this.eSciDocUri = eSciDocUri;
        this.handle = handle;
        initClient();
    }
    
    private void initClient() throws InternalClientException {
    	client = new UserGroupHandlerClient(eSciDocUri);
    	client.setTransport(TransportProtocol.REST);
    	client.setHandle(handle);
    }
    
    public Collection<UserGroup> findAll() throws EscidocClientException {
    	userGroups = client.retrieveUserGroupsAsList(withEmptyFilter());
    	putInMap();
    	return userGroups;
    }
    
    private SearchRetrieveRequestType withEmptyFilter() {
    	final SearchRetrieveRequestType request = new SearchRetrieveRequestType();
    	request.setMaximumRecords(new NonNegativeInteger(AppConstants.MAX_RESULT_SIZE));
    	return request;
    }
    
    private void putInMap() {
    	for (UserGroup group : userGroups) {
			userGroupById.put(group.getObjid(), group);
		}
    }
    
    public UserGroup retrieve(final String groupObjectId) throws EscidocClientException {
    	Preconditions.checkNotNull(groupObjectId, "groupObjectId is null: %s", groupObjectId);
    	return client.retrieve(groupObjectId);    			
    }
    
    public UserGroup update (final String objid, final String newName, final String newDescription, final String newEmail) throws EscidocClientException {
    	assert !(newName == null || newName.isEmpty()) : "name must not be null or empty";
    	
    	final UserGroup userGroup = retrieve(objid);
    	final UserGroup updatedUserGroup = new UserGroupFactory().update(userGroup, newName, newDescription, newEmail).build();
    	
    	return client.update(updatedUserGroup);
    }
    
    public UserGroup getGroupById(final String groupObjectId) throws EscidocClientException {
    	if (userGroups == null) {
    		findAll();
    	}
    	
    	return userGroupById.get(groupObjectId);
    	
    }
    
    public Collection<Grant> retrieveCurrentGrants(final String groupObjectId) throws EscidocException, InternalClientException, TransportException {
    	return getRestClient().retrieveCurrentGrants(groupObjectId);
    }
    
    private UserGroupHandlerClientInterface getRestClient() {
    	client.setTransport(TransportProtocol.REST);
    	return client;
    }

	public void activate(final UserGroup updatedUserGroup) throws EscidocException, InternalClientException, TransportException {
		final TaskParam taskParam = new TaskParam();
		taskParam.setLastModificationDate(updatedUserGroup.getLastModificationDate());
		client.activate(updatedUserGroup.getObjid(), taskParam);
	}
	
	public void deactivate(final UserGroup updatedUserGroup) throws EscidocException, InternalClientException, TransportException {
		final TaskParam taskParam = new TaskParam();
		taskParam.setLastModificationDate(updatedUserGroup.getLastModificationDate());
		client.deactivate(updatedUserGroup.getObjid(), taskParam);
	}
	
}
