package de.escidoc.admintool.domain;

import javax.xml.parsers.ParserConfigurationException;

import de.escidoc.core.resources.om.context.AdminDescriptors;
import de.escidoc.core.resources.om.context.Context;
import de.escidoc.core.resources.om.context.OrganizationalUnitRefs;
import de.escidoc.core.resources.om.context.Properties;

//TODO refactor this class, adapt to more appropriate pattern e.g. Builder
public class ContextFactory {
    private Context context;

    private Properties properties;

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

        properties = new Properties();
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
        return adminDescriptors == null || adminDescriptors.size() == 0;
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

        // final OrganizationalUnitRefs organizationalUnitRefs =
        // new OrganizationalUnitRefs();
        //
        // for (final OrganizationalUnit orgUnit : newOrgUnits) {
        // final ResourceRef organizationalUnitRef =
        // new ResourceRef(orgUnit.getObjid());
        // organizationalUnitRefs
        // .addOrganizationalUnitRef(organizationalUnitRef);
        // }

        return this;
    }
}