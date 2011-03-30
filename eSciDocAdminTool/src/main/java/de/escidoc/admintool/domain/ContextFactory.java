package de.escidoc.admintool.domain;

import javax.xml.parsers.ParserConfigurationException;

import de.escidoc.core.resources.om.context.AdminDescriptors;
import de.escidoc.core.resources.om.context.Context;
import de.escidoc.core.resources.om.context.ContextProperties;
import de.escidoc.core.resources.om.context.OrganizationalUnitRefs;

//TODO refactor this class, adapt to more appropriate pattern e.g. Builder
public class ContextFactory {

    // NOTE: PMD warns about non-transient non-static member.
    // This class is however not a bean. @See:
    // http://stackoverflow.com/questions/589855/java-pmd-warning-on-non-transient-class-member
    private Context context;

    private ContextProperties properties;

    public ContextFactory update(final Context context) {
        this.context = context;
        properties = context.getProperties();

        return this;
    }

    public ContextFactory create(
        final String name, final String description, final String contextType,
        final OrganizationalUnitRefs orgUnitRefs)
        throws ParserConfigurationException {
        context = new Context();

        properties = new ContextProperties();
        properties.setName(name);
        properties.setDescription(description);
        properties.setType(contextType);

        properties.setOrganizationalUnitRefs(orgUnitRefs);

        context.setProperties(properties);

        return this;
    }

    public ContextFactory adminDescriptors(
        final AdminDescriptors adminDescriptors) {
        if (isNotSet(adminDescriptors)) {
            return this;
        }
        context.setAdminDescriptors(adminDescriptors);
        return this;
    }

    private boolean isNotSet(final AdminDescriptors adminDescriptors) {
        return adminDescriptors == null;
    }

    public Context build() {
        context.setProperties(properties);
        return context;
    }

    public ContextFactory name(final String name) {
        properties.setName(name);
        return this;
    }

    public ContextFactory description(final String newDescription) {
        properties.setDescription(newDescription);
        return this;
    }

    public ContextFactory type(final String newType) {
        properties.setType(newType);
        return this;
    }

    public ContextFactory orgUnits(final OrganizationalUnitRefs orgUnitRefs) {
        properties.setOrganizationalUnitRefs(orgUnitRefs);
        return this;
    }
}