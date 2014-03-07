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
	
	public UserGroupFactory create (final String name, final String label) {
		// TODO implement create user group
		return null;
	}
	
	public UserGroup build() {
		return userGroup;
	}
}
