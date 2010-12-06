package de.escidoc.admintool.view.resource;

import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Window;

import de.escidoc.admintool.app.PropertyId;
import de.escidoc.admintool.service.ResourceService;
import de.escidoc.core.client.exceptions.EscidocClientException;

public class OpenResourceListener extends AbstractUpdateable
    implements ClickListener {

    public OpenResourceListener(final Window mainWindow,
        final ResourceService orgUnitService,
        final ResourceContainer resourceContainer) {
        super(mainWindow, orgUnitService, resourceContainer);
    }

    private static final long serialVersionUID = -4212838698528374884L;

    @Override
    public void buttonClick(final ClickEvent event) {
        onUpdate();
    }

    @Override
    public void updatePersistence() throws EscidocClientException {
        getOrgUnitService().open(getChildId(), "");
    }

    @Override
    public void updateItem() {
        getItem().getItemProperty(PropertyId.PUBLIC_STATUS).setValue("opened");
    }

    @Override
    public void updateResourceContainer() throws EscidocClientException {
        // TODO
        // getResourceContainer().getContainer().getItem(orgUnit);
    }

    @Override
    public void checkPostConditions() {
        // TODO
    }
}
