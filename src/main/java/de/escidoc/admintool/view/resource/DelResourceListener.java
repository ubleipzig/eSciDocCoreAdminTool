package de.escidoc.admintool.view.resource;

import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Window;

import de.escidoc.admintool.service.ResourceService;
import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.resources.Resource;

public class DelResourceListener extends AbstractUpdateable
    implements ClickListener {

    public DelResourceListener(final Window mainWindow,
        final ResourceService orgUnitService,
        final ResourceContainer resourceContainer) {
        super(mainWindow, orgUnitService, resourceContainer);
    }

    private static final long serialVersionUID = 63356969287971297L;

    private Resource orgUnit;

    @Override
    public void buttonClick(final ClickEvent event) {
        onUpdate();
    }

    @Override
    public void updatePersistence() throws EscidocClientException {
        orgUnit = getOrgUnit();
        getOrgUnitService().delete(getChildId());
    }

    @Override
    public void updateResourceContainer() throws EscidocClientException {
        getResourceContainer().remove(orgUnit);
    }

    @Override
    public void updateItem() {

    }

    @Override
    public void checkPostConditions() {

    }
}