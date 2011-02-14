package de.escidoc.admintool.view.user;

import java.util.Set;

import com.google.common.base.Preconditions;

import de.escidoc.admintool.app.AppConstants;
import de.escidoc.admintool.service.UserService;
import de.escidoc.admintool.view.resource.ResourceRefDisplay;
import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.resources.aa.useraccount.Attribute;

public class SetOrgUnitsCommandImpl implements SetOrgUnitsCommand {

    private final UserService userService;

    private String objectId;

    private Set<ResourceRefDisplay> orgUnitIds;

    public SetOrgUnitsCommandImpl(final UserService userService) {
        this.userService = userService;
    }

    @Override
    public void setSelectedUserId(final String objid) {
        objectId = objid;
    }

    @Override
    public void setSeletectedOrgUnit(final Set<ResourceRefDisplay> orgUnitIds) {
        this.orgUnitIds = orgUnitIds;
    }

    @Override
    public void execute(final OrgUnitWidget orgUnitWidget)
        throws EscidocClientException {
        Preconditions.checkNotNull(orgUnitWidget, "orgUnitWidget is null: %s",
            orgUnitWidget);
        Preconditions.checkNotNull(orgUnitIds, "orgUnitIds is null: %s",
            orgUnitIds);
        Preconditions.checkNotNull(userService, "userService is null: %s",
            userService);
        if (shouldSetOrgUnits(orgUnitWidget)) {
            for (final ResourceRefDisplay resourceRefDisplay : orgUnitIds
                .toArray(new ResourceRefDisplay[orgUnitIds.size()])) {
                userService.assign(objectId, new Attribute(
                    AppConstants.DEFAULT_ORG_UNIT_ATTRIBUTE_NAME,
                    resourceRefDisplay.getObjectId()));
            }
        }
    }

    private boolean shouldSetOrgUnits(final OrgUnitWidget orgUnitWidget) {
        return orgUnitWidget.getOrgUnitsFromTable().size() > 0;
    }
}
