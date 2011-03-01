package de.escidoc.admintool.view.user;

import java.util.List;
import java.util.Set;

import com.vaadin.ui.AbstractSelect;

import de.escidoc.admintool.view.resource.ResourceRefDisplay;
import de.escidoc.core.client.exceptions.EscidocClientException;

public interface SetOrgUnitsCommand {

    void setSelectedUserId(String objid);

    void setSeletectedOrgUnit(Set<ResourceRefDisplay> selectedOrgUnits);

    void execute(AbstractSelect orgUnitWidget) throws EscidocClientException;

    void update(List<String> oldOrgUnits) throws EscidocClientException;

}
