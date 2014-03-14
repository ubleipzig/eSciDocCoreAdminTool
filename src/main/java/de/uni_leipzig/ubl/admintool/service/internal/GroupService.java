package de.uni_leipzig.ubl.admintool.service.internal;

import gov.loc.www.zing.srw.SearchRetrieveRequestType;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.axis.types.NonNegativeInteger;

import com.google.common.base.Preconditions;

import de.escidoc.admintool.app.AppConstants;
import de.escidoc.core.client.UserGroupHandlerClient;
import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;
import de.escidoc.core.client.interfaces.UserGroupHandlerClientInterface;
import de.escidoc.core.resources.aa.useraccount.Grant;
import de.escidoc.core.resources.aa.usergroup.Selector;
import de.escidoc.core.resources.aa.usergroup.UserGroup;
import de.escidoc.core.resources.common.TaskParam;
import de.uni_leipzig.ubl.admintool.domain.UserGroupFactory;

public class GroupService {
	
	private UserGroupHandlerClientInterface client;
	
    private final Map<String, UserGroup> userGroupById = new ConcurrentHashMap<String, UserGroup>();

    private final URL eSciDocUri;

    private final String handle;

    private Collection<UserGroup> userGroups;

    private UserGroup group;

    
    public GroupService(final String eSciDocUri, final String handle) throws InternalClientException, MalformedURLException {
        this.eSciDocUri = new URL(eSciDocUri);
        this.handle = handle;
        initClient();
    }
    
    
    private void initClient() throws InternalClientException {
    	client = new UserGroupHandlerClient(eSciDocUri);
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

	
	public UserGroup create(final String name, final String label, final String description, final String email) throws EscidocException, InternalClientException, TransportException {
		assert !(name == null || name.isEmpty()) : "name can not be null or empty";
		assert !(label == null || label.isEmpty()) : "label can not be null or empty";
		
		final UserGroup backedUserGroup = new UserGroupFactory().create(name, label, description, email);
		
		final UserGroup createdUserGroup = client.create(backedUserGroup);
		assert createdUserGroup != null : "Got null reference from the server.";
		assert createdUserGroup.getObjid() != null : "ObjectId can not be null.";
		assert userGroupById != null : "userGroupById is null";
		final int sizeBefore = userGroupById.size();
		userGroupById.put(createdUserGroup.getObjid(), createdUserGroup);
		final int sizeAfter = userGroupById.size();
		assert sizeAfter > sizeBefore : "user group is not added to map.";
		return createdUserGroup;
	}

	
	public UserGroup delete(final String objectId) throws EscidocClientException {
		client.delete(objectId);
		return userGroupById.remove(objectId);
	}


	public UserGroup addSelectors(final UserGroup userGroup, final List<Selector> selectors) throws EscidocException, InternalClientException, TransportException {
		final TaskParam taskParam = new TaskParam();
		taskParam.setSelectors(selectors);
		taskParam.setLastModificationDate(userGroup.getLastModificationDate());
		UserGroup updatedUserGroup = client.addSelectors(userGroup.getObjid(), taskParam);
		return updatedUserGroup;
	}
	
	public UserGroup removeSelectors(final UserGroup userGroup, final List<Selector> selectors) throws EscidocException, InternalClientException, TransportException {
		final TaskParam taskParam = new TaskParam();
		taskParam.setSelectors(selectors);
		taskParam.setLastModificationDate(userGroup.getLastModificationDate());
		UserGroup updatedUserGroup = client.removeSelectors(userGroup.getObjid(), taskParam);
		return updatedUserGroup;
	}

}
