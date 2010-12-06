package de.escidoc.admintool.view.resource;

import java.util.Collection;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import com.vaadin.ui.Field;
import com.vaadin.ui.Window;

import de.escidoc.admintool.service.ResourceService;
import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.resources.oum.OrganizationalUnit;

public class CreateOrgUnitBtnListener extends AbstractResourceBtnListener {

    private static final long serialVersionUID = 6514709536247207829L;

    private OrganizationalUnit build;

    private final ResourceContainer rContainer;

    private OrganizationalUnit created;

    protected CreateOrgUnitBtnListener(final Collection<Field> allFields,
        final Map<String, Field> fieldByName, final Window mainWindow,
        final ResourceService resourceService,
        final ResourceContainer rContainer) {
        super(allFields, fieldByName, mainWindow, resourceService);
        this.rContainer = rContainer;
    }

    @Override
    protected void updateModel() throws ParserConfigurationException {
        build =
            new de.escidoc.admintool.domain.OrgUnit.BuilderImpl(getTitle(),
                getDescription())
                .country(getCountry()).city(getCity())
                .alternative(getAlternative()).identifier(getIdentifier())
                .coordinates(getCoordinates()).type(getType()).build();

    }

    @Override
    protected void updatePersistence() throws EscidocClientException {
        created = (OrganizationalUnit) getData().resourceService.create(build);
    }

    @Override
    protected void updateResourceContainer() {
        rContainer.add(created);
    }

    @Override
    protected String getSucessMessage() {
        return "Succesfully created an organizational unit.";
    }
}