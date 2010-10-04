package de.escidoc.admintool.view.lab.orgunit;

import de.escidoc.admintool.app.Command;
import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.resources.oum.OrganizationalUnit;

public class AddChildrenCommand implements Command {

    private final OrgUnitContainerFactory container;

    private OrganizationalUnit parent;

    public AddChildrenCommand(final OrgUnitContainerFactory container) {
        this.container = container;
    }

    public void setParent(final OrganizationalUnit selectedOrgUnit) {
        parent = selectedOrgUnit;
    }

    @Override
    public void execute() throws EscidocClientException {
        container.addChildren(parent);
    }
}