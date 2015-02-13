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
 * Copyright 2015 Leipzig University Library
 * 
 * This code is the result of the project
 * "Die Bibliothek der Milliarden Woerter". This project is funded by
 * the European Social Fund. "Die Bibliothek der Milliarden Woerter" is
 * a cooperation project between the Leipzig University Library, the
 * Natural Language Processing Group at the Institute of Computer
 * Science at Leipzig University, and the Image and Signal Processing
 * Group at the Institute of Computer Science at Leipzig University.
 * 
 * All rights reserved.  Use is subject to license terms.
 * 
 * @author Uwe Kretschmer <u.kretschmer@denk-selbst.de>
 * 
 */
package de.uni_leipzig.ubl.admintool.service.internal;

import gov.loc.www.zing.srw.SearchRetrieveRequestType;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
import de.escidoc.core.resources.aa.role.Role;
import de.escidoc.core.resources.aa.useraccount.Grant;
import de.escidoc.core.resources.aa.useraccount.GrantProperties;
import de.escidoc.core.resources.aa.usergroup.Selector;
import de.escidoc.core.resources.aa.usergroup.UserGroup;
import de.escidoc.core.resources.common.TaskParam;
import de.escidoc.core.resources.common.reference.ContextRef;
import de.escidoc.core.resources.common.reference.Reference;
import de.escidoc.core.resources.common.reference.RoleRef;
import de.uni_leipzig.ubl.admintool.domain.UserGroupFactory;

public class GroupService {
	
	private UserGroupHandlerClientInterface client;
	
    private final Map<String, UserGroup> userGroupById = new ConcurrentHashMap<String, UserGroup>();

    private final URL eSciDocUri;

    private final String handle;

    private Collection<UserGroup> userGroups;

    private UserGroup group;
    
    private GrantProperties grantProps;

    
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
		// set IDs of items to delete
		for (Selector selector : selectors) {
			taskParam.addResourceRef(selector.getObjid());
		}
		taskParam.setLastModificationDate(userGroup.getLastModificationDate());
		UserGroup updatedUserGroup = client.removeSelectors(userGroup.getObjid(), taskParam);
		return updatedUserGroup;
	}


	public GroupService assign(final UserGroup userGroup) {
		if (userGroup == null) {
			throw new IllegalArgumentException("UserGroup can not be null");
		}
		this.group = userGroup;
		return this;
	}
	
	public GroupService withRole(final Role selectedRole) {
		if (selectedRole == null) {
			throw new IllegalArgumentException("Role can not be null.");
		}
		if (group == null) {
			throw new IllegalArgumentException("You must sign a role to a group.");
		}
		grantProps = new GrantProperties();
		grantProps.setRole(new RoleRef(selectedRole.getObjid()));
		return this;
	}
	
	public GroupService onResources(final Set<ContextRef> selectedResources) {
		for (final Reference resourceRef : selectedResources) {
			grantProps.setAssignedOn(resourceRef);
		}
		return this;
	}
	
	public void execute() throws EscidocClientException {
		final Grant grant = new Grant();
		grant.setProperties(grantProps);
		client.createGrant(group.getObjid(), grant);
	}


	public void revokeGrant(final String groupId, final Grant grant, final String comment) throws EscidocClientException {
		final TaskParam taskParam = new TaskParam();
		taskParam.setLastModificationDate(grant.getLastModificationDate());
		taskParam.setComment(comment);
		client.revokeGrant(groupId, grant.getObjid(), taskParam);
	}

}
