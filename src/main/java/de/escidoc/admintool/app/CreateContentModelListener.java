package de.escidoc.admintool.app;

import java.util.Collection;
import java.util.Map;

import com.google.common.base.Preconditions;
import com.vaadin.data.Item;
import com.vaadin.terminal.UserError;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Field;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.Notification;

import de.escidoc.admintool.service.ResourceService;
import de.escidoc.admintool.view.ModalDialog;
import de.escidoc.admintool.view.resource.ResourceBtnListener;
import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.resources.Resource;
import de.escidoc.core.resources.cmm.ContentModel;

public class CreateContentModelListener implements ResourceBtnListener {

    private static final long serialVersionUID = 1978422911316028641L;

    private final Collection<Field> allFields;

    private final ResourceService contentModelService;

    private ContentModel build;

    private final Map<String, Field> fieldByName;

    private final Window mainWindow;

    private final ContentModelContainerImpl container;

    public CreateContentModelListener(final Collection<Field> allFields, final ResourceService contentModelService,
        final Map<String, Field> fieldByName, final Window mainWindow, final ContentModelContainerImpl container) {
        Preconditions.checkNotNull(mainWindow, "mainWindow is null: %s", mainWindow);
        Preconditions.checkNotNull(fieldByName, "fieldByName is null: %s", fieldByName);
        Preconditions.checkNotNull(contentModelService, "contentModelService is null: %s", contentModelService);
        Preconditions.checkNotNull(allFields, "allFields is null: %s", allFields);
        Preconditions.checkNotNull(container, "container is null: %s", container);

        this.mainWindow = mainWindow;
        this.contentModelService = contentModelService;

        this.allFields = allFields;
        this.fieldByName = fieldByName;
        this.container = container;
    }

    @Override
    public void buttonClick(final ClickEvent event) {
        if (validateAllFields() && saveToReposity()) {
            removeValidationErrors();
            commitAllFields();
        }
    }

    private boolean saveToReposity() {
        createModel();
        return updatePersistence();
    }

    private boolean updatePersistence() {
        try {
            final Resource created = contentModelService.create(build);
            if (created != null && created.getObjid() != null) {
                mainWindow.showNotification(new Window.Notification("Info", "A new Content Model with the ID "
                    + created.getObjid() + " is created.", Notification.TYPE_TRAY_NOTIFICATION));
                container.add(created);
                return created != null && created.getObjid() != null;
            }
            return false;
        }
        catch (final EscidocClientException e) {
            ModalDialog.show(mainWindow, e);
            return false;
        }
    }

    private void createModel() {
        build = new ContentModelBuilder(getTitle()).description(getDescription()).build();
    }

    private String getDescription() {
        return (String) fieldByName.get("description").getValue();
    }

    protected String getTitle() {
        return (String) fieldByName.get("title").getValue();
    }

    private boolean validateAllFields() {
        for (final Field field : allFields) {
            try {
                field.validate();
            }
            catch (final Exception e) {
                ((AbstractComponent) field).setComponentError(new UserError(field.getCaption() + " is required"));
                return false;
            }
        }
        return true;
    }

    private void removeValidationErrors() {
        for (final Field field : allFields) {
            ((AbstractComponent) field).setComponentError(null);
        }
    }

    private void commitAllFields() {
        // do nothing
    }

    @Override
    public void bind(final Item item) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
