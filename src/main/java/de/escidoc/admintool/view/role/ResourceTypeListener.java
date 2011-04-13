package de.escidoc.admintool.view.role;

import java.util.Collections;
import java.util.Iterator;
import java.util.Set;

import org.apache.commons.lang.NotImplementedException;

import com.google.common.base.Preconditions;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.POJOContainer;
import com.vaadin.ui.Component;

import de.escidoc.admintool.app.PropertyId;
import de.escidoc.admintool.view.ViewConstants;
import de.escidoc.admintool.view.admintask.ResourceType;
import de.escidoc.admintool.view.util.dialog.ErrorDialog;
import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.resources.Resource;

class ResourceTypeListener implements ValueChangeListener {

    private static final long serialVersionUID = 2394096937007392588L;

    private final RoleView roleView;

    ResourceTypeListener(final RoleView roleView) {
        Preconditions.checkNotNull(roleView, "roleView is null: %s", roleView);
        this.roleView = roleView;
    }

    @Override
    public void valueChange(final ValueChangeEvent event) {
        try {
            onSelectedResourceType(event);
        }
        catch (final NotImplementedException e) {
            roleView.mainWindow.addWindow(new ErrorDialog(roleView.mainWindow,
                ViewConstants.ERROR_DIALOG_CAPTION, e.getMessage()));
        }
    }

    private void onSelectedResourceType(final ValueChangeEvent event) {
        final Object value = event.getProperty().getValue();
        if (value instanceof ResourceType) {
            final ResourceType type = (ResourceType) value;

            Component newComponent = null;
            switch (type) {
                case CONTEXT:
                    newComponent = assignComponent();
                    loadContextData();
                    break;
                case ORGANIZATIONAL_UNIT:
                    newComponent = assignComponent();
                    loadOrgUnitData();
                    break;
                case CONTAINER:
                    newComponent = assignComponent();
                    loadContainerData();
                    break;
                default: {
                    clearResourceContainer();
                    throw new NotImplementedException("Scoping for " + type
                        + " is not yet implemented");
                }
            }
            final Iterator<Component> it =
                roleView.resourceContainer.getComponentIterator();
            if (it.hasNext()) {
                roleView.resourceContainer.replaceComponent(it.next(),
                    newComponent);
            }
            else {
                roleView.resourceContainer.addComponent(newComponent);
            }
        }
    }

    private void loadItemData() {
        final POJOContainer<Resource> itemContainer =
            new POJOContainer<Resource>(Resource.class,
                "metadataRecords.escidoc");
        for (final Resource orgUnit : findAllItems()) {
            itemContainer.addItem(orgUnit);
        }
        roleView.resouceResult.setContainerDataSource(itemContainer);
        roleView.resouceResult
            .setItemCaptionPropertyId("metadataRecords.escidoc");
    }

    private Set<Resource> findAllItems() {
        try {
            return roleView.serviceContainer.getItemService().findAll();
        }
        catch (final EscidocClientException e) {
            handleError(e);
        }
        return Collections.emptySet();

    }

    private POJOContainer<Resource> newPojoContainer() {
        return new POJOContainer<Resource>(Resource.class, PropertyId.NAME);
    }

    private Component assignComponent() {
        Component newComponent;
        newComponent = roleView.resouceResult;
        return newComponent;
    }

    private void loadContainerData() {
        final POJOContainer<Resource> containerContainer = newPojoContainer();
        for (final Resource orgUnit : findAllContainers()) {
            containerContainer.addItem(orgUnit);
        }
        configureList(containerContainer);
    }

    private Set<Resource> findAllContainers() {
        try {
            return roleView.serviceContainer.getContainerService().findAll();
        }
        catch (final EscidocClientException e) {
            handleError(e);
        }
        return Collections.emptySet();
    }

    private void handleError(final EscidocClientException e) {
        roleView.mainWindow.addWindow(new ErrorDialog(roleView.mainWindow,
            ViewConstants.ERROR_DIALOG_CAPTION, e.getMessage()));
    }

    private void loadOrgUnitData() {
        final POJOContainer<Resource> orgUnitContainer = newPojoContainer();
        for (final Resource orgUnit : findAllOrgUnits()) {
            orgUnitContainer.addItem(orgUnit);
        }
        configureList(orgUnitContainer);
    }

    private Set<Resource> findAllOrgUnits() {
        try {
            return roleView.serviceContainer.getOrgUnitService().findAll();
        }
        catch (final EscidocClientException e) {
            handleError(e);
        }
        return Collections.emptySet();
    }

    private void clearResourceContainer() {
        roleView.resourceContainer.removeAllComponents();
    }

    private void loadContextData() {
        final POJOContainer<Resource> contextContainer = newPojoContainer();
        for (final Resource context : findAllContexts()) {
            contextContainer.addItem(context);
        }
        configureList(contextContainer);
    }

    private void configureList(final POJOContainer<Resource> contextContainer) {
        roleView.resouceResult.setContainerDataSource(contextContainer);
        roleView.resouceResult.setItemCaptionPropertyId(PropertyId.NAME);
    }

    private Set<Resource> findAllContexts() {
        try {
            return roleView.serviceContainer.getContextService().findAll();
        }
        catch (final EscidocClientException e) {
            handleError(e);
        }
        return Collections.emptySet();
    }
}