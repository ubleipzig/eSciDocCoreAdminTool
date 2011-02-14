package de.escidoc.admintool.view.user;

import java.util.Set;

import de.escidoc.admintool.view.resource.ResourceRefDisplay;
import de.escidoc.core.client.exceptions.EscidocClientException;

public interface SetOrgUnitsCommand {

    void setSelectedUserId(String objid);

    void setSeletectedOrgUnit(Set<ResourceRefDisplay> selectedOrgUnits);

    void execute(OrgUnitWidget orgUnitWidget) throws EscidocClientException;

}
