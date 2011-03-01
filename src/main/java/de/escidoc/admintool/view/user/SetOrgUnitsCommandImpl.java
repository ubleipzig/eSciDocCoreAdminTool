package de.escidoc.admintool.view.user;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.Table;

import de.escidoc.admintool.app.AppConstants;
import de.escidoc.admintool.service.UserService;
import de.escidoc.admintool.view.resource.ResourceRefDisplay;
import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.resources.aa.useraccount.Attribute;
import de.escidoc.core.resources.aa.useraccount.Attributes;

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
    public void execute(final AbstractSelect select)
        throws EscidocClientException {
        Preconditions.checkNotNull(select, "orgUnitWidget is null: %s", select);
        Preconditions.checkNotNull(orgUnitIds, "orgUnitIds is null: %s",
            orgUnitIds);
        Preconditions.checkNotNull(userService, "userService is null: %s",
            userService);

        if (shouldSetOrgUnits(select)) {
            for (final ResourceRefDisplay resourceRefDisplay : orgUnitIds
                .toArray(new ResourceRefDisplay[orgUnitIds.size()])) {
                userService.assign(objectId, new Attribute(
                    AppConstants.DEFAULT_ORG_UNIT_ATTRIBUTE_NAME,
                    resourceRefDisplay.getObjectId()));
            }
        }
    }

    private boolean shouldSetOrgUnits(final AbstractSelect select) {
        return select.size() > 0;
    }

    @Override
    public void update(final List<String> oldOrgUnits, final Table orgUnitTable)
        throws EscidocClientException {

        final Set<String> newOrgUnits = new HashSet<String>();
        for (final ResourceRefDisplay resourceRefDisplay : orgUnitIds
            .toArray(new ResourceRefDisplay[orgUnitIds.size()])) {
            newOrgUnits.add(resourceRefDisplay.getObjectId());
        }

        final Set<String> old = new HashSet<String>(oldOrgUnits);

        final Set<String> toCreate = Sets.newHashSet(newOrgUnits);
        final Set<String> toRemove = Sets.newHashSet(oldOrgUnits);

        if (toCreate.equals(toRemove)) {
            return;
        }

        // toCreate=new-old;
        toCreate.removeAll(old);

        // toDelete=old-new;
        toRemove.removeAll(newOrgUnits);

        for (final String string : toCreate) {
            userService.assign(objectId, new Attribute(
                AppConstants.DEFAULT_ORG_UNIT_ATTRIBUTE_NAME, string));
        }

        foo(toRemove);
    }

    private void foo(final Set<String> toRemove) throws EscidocClientException {
        final Attributes allAttributes =
            userService.retrieveAttributes(objectId);

        for (final Attribute attribute : allAttributes) {
            if (isOrgUnit(attribute)) {
                final String value = attribute.getValue();
                if (toRemove.contains(value)) {
                    userService.removeAttribute(objectId, attribute);
                }

            }
        }
    }

    private boolean isOrgUnit(final Attribute attribute) {
        return "o".equals(attribute.getName());
    }
}
