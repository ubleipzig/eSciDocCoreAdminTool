package de.escidoc.admintool.view.resource;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.ui.Field;
import com.vaadin.ui.Window;

import de.escidoc.admintool.service.ResourceService;
import de.escidoc.admintool.view.ViewConstants;
import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.resources.Resource;
import de.escidoc.core.resources.oum.OrganizationalUnit;

public class CreateOrgUnitBtnListener extends AbstractResourceBtnListener {

    private static final Logger LOG = LoggerFactory.getLogger(CreateOrgUnitBtnListener.class);

    private static final long serialVersionUID = 6514709536247207829L;

    private OrganizationalUnit build;

    private final ResourceContainer rContainer;

    private OrganizationalUnit created;

    private String parentId;

    protected CreateOrgUnitBtnListener(final Collection<Field> allFields, final Map<String, Field> fieldByName,
        final Window mainWindow, final ResourceView resourceView, final ResourceService resourceService,
        final ResourceContainer rContainer) {
        super(allFields, fieldByName, mainWindow, resourceView, resourceService);
        this.rContainer = rContainer;
    }

    @Override
    protected void updateModel() throws ParserConfigurationException {
        final Field parentField = getParentField();
        final Property property = parentField.getPropertyDataSource();
        final Object value = property.getValue();

        if (value instanceof ResourceRefDisplay) {
            final ResourceRefDisplay parentDisplay = (ResourceRefDisplay) value;
            parentId = parentDisplay.getObjectId();
            final Set<String> parents = new HashSet<String>();

            if (!parentId.isEmpty()) {
                parents.add(parentId);
                build =
                    new de.escidoc.admintool.domain.OrgUnit.BuilderImpl(getTitle(), getDescription())
                        .parents(parents).country(getCountry()).city(getCity()).alternative(getAlternative())
                        .identifier(getIdentifier()).coordinates(getCoordinates()).type(getType()).build();
            }
            else {
                build =
                    new de.escidoc.admintool.domain.OrgUnit.BuilderImpl(getTitle(), getDescription())
                        .country(getCountry()).city(getCity()).alternative(getAlternative())
                        .identifier(getIdentifier()).coordinates(getCoordinates()).type(getType()).build();
            }

        }
        else {
            LOG.error("Unknown type: " + value.getClass());
        }
    }

    @Override
    protected void updatePersistence() throws EscidocClientException {
        created = (OrganizationalUnit) getData().resourceService.create(build);
    }

    @Override
    protected void updateResourceContainer() {
        if (parentId.isEmpty()) {
            rContainer.add(created);
        }
        else {
            Resource parent;
            try {
                parent = getData().resourceService.findById(parentId);
                rContainer.addChild(parent, created);
            }
            catch (final EscidocClientException e) {
            }
        }
    }

    @Override
    protected String getSucessMessage() {
        return ViewConstants.ORGANIZATIONAL_UNIT_IS_CREATED;
    }

    @Override
    protected void showInEditView() {
        getResourceView().selectInFolderView(created);
        final Item item = rContainer.getItem(created);
        super.getResourceView().showEditView(item);
    }

    @Override
    protected void commitAllFields() {
        // Do nothing
    }
}