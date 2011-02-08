package de.escidoc.admintool.view.resource;

import com.google.common.base.Preconditions;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Window;

import de.escidoc.admintool.app.PropertyId;
import de.escidoc.admintool.service.ResourceService;
import de.escidoc.core.client.exceptions.EscidocClientException;

public class OpenResourceListener extends AbstractUpdateable
    implements ClickListener {

    private final ResourceToolbar resourceToolbar;

    public OpenResourceListener(final Window mainWindow,
        final ResourceService orgUnitService,
        final ResourceContainer resourceContainer,
        final ResourceToolbar resourceToolbar) {
        super(mainWindow, orgUnitService, resourceContainer);
        Preconditions.checkNotNull(resourceToolbar,
            "resourceToolbar is null: %s", resourceToolbar);
        this.resourceToolbar = resourceToolbar;
    }

    private static final long serialVersionUID = -4212838698528374884L;

    @Override
    public void buttonClick(final ClickEvent event) {
        onUpdate();
        resourceToolbar.bind(getItem());
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
