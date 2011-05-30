package de.escidoc.admintool.view.role;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;

import org.apache.commons.lang.NotImplementedException;

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

    private final RoleView roleView;

    ResourceTypeListener(final RoleView roleView) {
        this.roleView = roleView;
    }

    private static final long serialVersionUID = 2394096937007392588L;

    @Override
    public void valueChange(final ValueChangeEvent event) {
        try {
            onSelectedResourceType(event);
        }
        catch (final NotImplementedException e) {
            roleView.mainWindow.addWindow(new ErrorDialog(roleView.mainWindow, ViewConstants.ERROR_DIALOG_CAPTION, e
                .getMessage()));
        }
    }

    private void onSelectedResourceType(final ValueChangeEvent event) {
        final Object value = event.getProperty().getValue();
        if (value instanceof ResourceType) {
            final ResourceType type = (ResourceType) value;
            roleView.mainWindow.showNotification(type.toString());

            Component newComponent = null;
            switch (type) {
                case CONTEXT:
                    newComponent = roleView.resouceResult;
                    loadContextData();
                    break;
                case ORGANIZATIONAL_UNIT:
                    newComponent = roleView.resouceResult;
                    loadOrgUnitData();
                    break;
                default: {
                    clearResourceContainer();
                    throw new NotImplementedException("Scoping for " + type + " is not yet implemented");
                }
            }
            final Iterator<Component> it = roleView.resourceContainer.getComponentIterator();
            if (it.hasNext()) {
                roleView.resourceContainer.replaceComponent(it.next(), newComponent);
            }
            else {
                roleView.resourceContainer.addComponent(newComponent);
            }
        }
    }

    private void loadOrgUnitData() {
        final Set<Resource> organizationalUnits = getAllOrganizationalUnits();
        if (isNotEmpty(organizationalUnits)) {
            final POJOContainer<Resource> container =
                new POJOContainer<Resource>((Collection<Resource>) organizationalUnits, PropertyId.NAME);
            roleView.resouceResult.setContainerDataSource(container);
            roleView.resouceResult.setItemCaptionPropertyId(PropertyId.NAME);
        }

    }

    private boolean isNotEmpty(final Set<Resource> organizationalUnits) {
        return organizationalUnits != null && organizationalUnits.size() > 0;
    }

    private Set<Resource> getAllOrganizationalUnits() {

        try {
            final Set<Resource> all = roleView.serviceContainer.getOrgUnitService().findAll();
            return all;

        }
        catch (final EscidocClientException e) {
            roleView.mainWindow.addWindow(new ErrorDialog(roleView.mainWindow, ViewConstants.ERROR_DIALOG_CAPTION, e
                .getMessage()));
        }
        return Collections.emptySet();
    }

    private void clearResourceContainer() {
        roleView.resourceContainer.removeAllComponents();
    }

    private void loadContextData() {
        final POJOContainer<Resource> contextContainer =
            new POJOContainer<Resource>(findAllContexts(), PropertyId.NAME);
        roleView.resouceResult.setContainerDataSource(contextContainer);
        roleView.resouceResult.setItemCaptionPropertyId(PropertyId.NAME);
    }

    private Set<Resource> findAllContexts() {
        try {
            return roleView.serviceContainer.getContextService().findAll();
        }
        catch (final EscidocClientException e) {
            roleView.mainWindow.addWindow(new ErrorDialog(roleView.mainWindow, ViewConstants.ERROR_DIALOG_CAPTION, e
                .getMessage()));
        }
        return Collections.emptySet();

    }

}