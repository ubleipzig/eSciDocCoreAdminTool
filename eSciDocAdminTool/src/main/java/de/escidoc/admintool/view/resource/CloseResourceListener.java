package de.escidoc.admintool.view.resource;

import com.google.common.base.Preconditions;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Window;

import de.escidoc.admintool.app.PropertyId;
import de.escidoc.admintool.service.ResourceService;
import de.escidoc.core.client.exceptions.EscidocClientException;

public class CloseResourceListener extends AbstractUpdateable
    implements ClickListener {
    private static final long serialVersionUID = -8186132990430106497L;

    private final ResourceToolbar resourceToolbar;

    public CloseResourceListener(final Window mainWindow,
        final ResourceService orgUnitService,
        final ResourceContainer resourceContainer,
        final ResourceToolbar resourceToolbar) {
        super(mainWindow, orgUnitService, resourceContainer);
        Preconditions.checkNotNull(resourceToolbar,
            "resourceToolbar is null: %s", resourceToolbar);
        this.resourceToolbar = resourceToolbar;
    }

    @Override
    public void buttonClick(final ClickEvent event) {
        onUpdate();
        resourceToolbar.bind(getItem());
    }

    @Override
    public void updatePersistence() throws EscidocClientException {
        getOrgUnitService().close(getChildId(), "");
    }

    @Override
    public void updateItem() {
        getItem().getItemProperty(PropertyId.PUBLIC_STATUS).setValue("closed");
    }

    @Override
    public void updateResourceContainer() throws EscidocClientException {
        // getResourceContainer().getContainer().getItem(orgUnit);
        // TODO
    }

    @Override
    public void checkPostConditions() {
        // TODO
    }

}
