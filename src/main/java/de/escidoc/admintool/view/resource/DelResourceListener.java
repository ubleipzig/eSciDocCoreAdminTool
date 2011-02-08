package de.escidoc.admintool.view.resource;

import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Window;

import de.escidoc.admintool.app.AdminToolApplication;
import de.escidoc.admintool.service.ResourceService;
import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.resources.Resource;

public class DelResourceListener extends AbstractUpdateable
    implements ClickListener {

    private final AdminToolApplication app;

    public DelResourceListener(final AdminToolApplication app,
        final Window mainWindow, final ResourceService resourceService,
        final ResourceContainer resourceContainer) {
        super(mainWindow, resourceService, resourceContainer);
        this.app = app;
    }

    private static final long serialVersionUID = 63356969287971297L;

    private Resource orgUnit;

    @Override
    public void buttonClick(final ClickEvent event) {
        onUpdate();
        app.showResourceView();
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
        // Do Nothing
    }

    @Override
    public void checkPostConditions() {
        // Do Nothing
    }
}