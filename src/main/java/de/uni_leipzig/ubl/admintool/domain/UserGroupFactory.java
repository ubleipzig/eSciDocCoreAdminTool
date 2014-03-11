package de.uni_leipzig.ubl.admintool.domain;

import de.escidoc.core.resources.aa.usergroup.UserGroup;
import de.escidoc.core.resources.aa.usergroup.UserGroupProperties;

public class UserGroupFactory {

	private UserGroup userGroup;
	
	public UserGroupFactory update (final UserGroup userGroup, final String name, final String description, final String email) {
		this.userGroup = userGroup;
		
		final UserGroupProperties properties = userGroup.getProperties();
		properties.setDescription(description);
		properties.setName(name);
		properties.setEmail(email);
		
		userGroup.setProperties(properties);
		return this;
	}
	
	public UserGroup create (final String name, final String label, final String description, final String email) {
		assert name != null : "name must not be null";
		assert label != null : "label must not be null";
		
		userGroup = new UserGroup();
		
		final UserGroupProperties properties = new UserGroupProperties();
		properties.setName(name);
		properties.setLabel(label);
		if (description != null) {
			properties.setDescription(description);
		}
		if (email != null) {
			properties.setEmail(email);
		}
		userGroup.setProperties(properties);
		
		return userGroup;
	}
	
	public UserGroup build() {
		return userGroup;
	}
}
