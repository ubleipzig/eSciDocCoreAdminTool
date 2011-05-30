package de.escidoc.admintool.view.resource;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.vaadin.ui.Field;
import com.vaadin.ui.Window;

import de.escidoc.admintool.app.PropertyId;
import de.escidoc.admintool.domain.OrgUnitBuilder;
import de.escidoc.admintool.service.ResourceService;
import de.escidoc.admintool.view.ViewConstants;
import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.resources.oum.OrganizationalUnit;

public class UpdateOrgUnitBtnListener extends AbstractResourceBtnListener {

    private static final long serialVersionUID = 4095932748716005999L;

    public UpdateOrgUnitBtnListener(final Collection<Field> allFields, final Map<String, Field> fieldByName,
        final Window mainWindow, final ResourceView resourceView, final ResourceService resourceService) {
        super(allFields, fieldByName, mainWindow, resourceView, resourceService);
    }

    @Override
    protected void updateModel() throws ParserConfigurationException, SAXException, IOException, EscidocClientException {

        getData().toBeUpdated =
            createBuilder()
                .with(getTitle(), getDescription()).country(getCountry()).city(getCity()).alternative(getAlternative())
                .identifier(getIdentifier()).coordinates(getCoordinates()).type(getType()).build();

    }

    private OrgUnitBuilder createBuilder() throws EscidocClientException {
        return new OrgUnitBuilder(getOldOrgUnit());
    }

    private OrganizationalUnit getOldOrgUnit() throws EscidocClientException {
        return (OrganizationalUnit) getData().resourceService.findById((String) getData().item.getItemProperty(
            PropertyId.OBJECT_ID).getValue());
    }

    @Override
    protected void updatePersistence() throws EscidocClientException {
        getData().resourceService.update(getData().toBeUpdated);
    }

    @Override
    protected String getSucessMessage() {
        return ViewConstants.SUCCESFULLY_UPDATED_ORGANIZATIONAL_UNIT;
    }

    @Override
    protected void updateResourceContainer() {
        // do nothing
    }

    @Override
    protected void showInEditView() {
        // do nothing
    }

    @Override
    protected void commitAllFields() {
        for (final Field field : getData().allFields) {
            field.commit();
        }
    }
}