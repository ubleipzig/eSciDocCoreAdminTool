package de.uni_leipzig.ubl.admintool.view.group.selector;

import de.escidoc.core.resources.interfaces.XmlCompatibleEnum;

public enum InternalSelectorName implements XmlCompatibleEnum {
	
	USER_ACCOUNT("user-account"), USER_GROUP("user-group");
	
	private final String xmlName;
	
	/**
	 * 
	 * @param xmlName
	 */
	private InternalSelectorName(final String xmlName) {
		this.xmlName = xmlName;
	}

	@Override
	public String getXmlValue() {
		return xmlName;
	}

}
