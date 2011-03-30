package de.escidoc.admintool.service;

import de.escidoc.core.resources.om.context.AdminDescriptors;
import de.escidoc.core.resources.om.context.Context;
import de.escidoc.core.resources.om.context.OrganizationalUnitRefs;

public class ContextBuilder {

    private final Context context;

    public ContextBuilder(final Context context) {
        this.context = context;
    }

    public ContextBuilder name(final String newName) {
        context.getProperties().setName(newName);
        return this;
    }

    public ContextBuilder description(final String newDescription) {
        context.getProperties().setDescription(newDescription);
        return this;
    }

    public ContextBuilder type(final String newType) {
        context.getProperties().setType(newType);
        return this;
    }

    public ContextBuilder orgUnits(final OrganizationalUnitRefs orgUnitRefs) {
        context.getProperties().setOrganizationalUnitRefs(orgUnitRefs);
        return this;
    }

    public ContextBuilder adminDescriptors(
        final AdminDescriptors newAdminDescriptors) {
        if (isNotSet(newAdminDescriptors)) {
            return this;
        }
        context.setAdminDescriptors(newAdminDescriptors);
        return this;
    }

    private boolean isNotSet(final AdminDescriptors adminDescriptors) {
        return adminDescriptors == null;
    }

    public Context build() {
        return context;
    }
}