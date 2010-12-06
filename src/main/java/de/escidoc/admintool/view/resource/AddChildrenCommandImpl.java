package de.escidoc.admintool.view.resource;

import com.google.common.base.Preconditions;
import de.escidoc.admintool.service.OrgUnitServiceLab;
import de.escidoc.admintool.view.resource.ResourceTreeView.AddChildrenCommand;
import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;
import de.escidoc.core.resources.Resource;
import de.escidoc.core.resources.oum.OrganizationalUnit;

import java.util.Collection;

public final class AddChildrenCommandImpl implements AddChildrenCommand {
    private final OrgUnitServiceLab orgUnitService;

    private final ResourceContainer resourceContainer;

    public AddChildrenCommandImpl(final OrgUnitServiceLab orgUnitService,
        final ResourceContainer resourceContainer) {

        Preconditions.checkNotNull(orgUnitService,
            "orgUnitService is null: %s", orgUnitService);
        Preconditions.checkNotNull(resourceContainer,
            "resourceContainer is null: %s", resourceContainer);
        this.orgUnitService = orgUnitService;
        this.resourceContainer = resourceContainer;
    }

    @Override
    public void addChildrenFor(final Resource parent)
        throws EscidocClientException {
        if (parent == null) {
            throw new IllegalArgumentException("Parent can not be null.");
        }

        resourceContainer.addChildren(parent, getChildren(parent));
    }

    private Collection<OrganizationalUnit> getChildren(final Resource parent)
        throws EscidocException, InternalClientException, TransportException {
        return orgUnitService.retrieveChildren(parent.getObjid());
    }
}