package de.escidoc.admintool.view.contentmodel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;

import de.escidoc.admintool.service.ContentModelService;
import de.escidoc.admintool.view.validator.EmptyFieldValidator;
import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.resources.Resource;
import de.escidoc.core.resources.cmm.ContentModel;

@SuppressWarnings("serial")
public final class UpdateContentModelListener implements Button.ClickListener {

    private static final Logger LOG = LoggerFactory.getLogger(UpdateContentModelListener.class);

    private final ContentModelService contentModelService;

    private final Window mainWindow;

    private final ContentModelEditView contentModelEditView;

    private ContentModel toBeUpdated;

    public UpdateContentModelListener(final ContentModelEditView contentModelEditView,
        final ContentModelService contentModelService, final Window mainWindow) {
        Preconditions.checkNotNull(contentModelEditView, "contentModelEditView is null: %s", contentModelEditView);
        Preconditions.checkNotNull(contentModelService, "contentModelService is null: %s", contentModelService);
        Preconditions.checkNotNull(mainWindow, "mainWindow is null: %s", mainWindow);
        this.contentModelEditView = contentModelEditView;
        this.contentModelService = contentModelService;
        this.mainWindow = mainWindow;
    }

    @Override
    public void buttonClick(final ClickEvent event) {

        boolean valid = true;
        valid = EmptyFieldValidator.isValid(getName(), "Name can not be empty.");
        valid &= EmptyFieldValidator.isValid(getDescription(), "Description can not be empty.");

        if (valid) {
            try {
                Preconditions.checkNotNull(toBeUpdated, "toBeUpdated is null: %s", toBeUpdated);
                toBeUpdated.getProperties().setName(getName().getValue().toString());
                toBeUpdated.getProperties().setDescription(getDescription().getValue().toString());
                contentModelService.update(toBeUpdated);

                getName().commit();

                removeAllErrorMessages();
                showSuccessMessage();
                updateEditView();
            }
            catch (final EscidocClientException e) {
                mainWindow.showNotification(new Window.Notification("Error", e.getMessage(),
                    Window.Notification.TYPE_ERROR_MESSAGE));
            }
        }
    }

    private void updateEditView() throws EscidocClientException {
        contentModelEditView.setContentModel(contentModelService.findById(toBeUpdated.getObjid()));
    }

    private void removeAllErrorMessages() {
        getName().setComponentError(null);
        getDescription().setComponentError(null);
    }

    private void showSuccessMessage() {
        mainWindow.showNotification(new Window.Notification("Info", "Succesfully Update Content Model",
            Window.Notification.TYPE_TRAY_NOTIFICATION));
    }

    private TextArea getDescription() {
        return contentModelEditView.getDescriptionField();
    }

    private TextField getName() {
        return contentModelEditView.getNameField();
    }

    public void setContentModel(final Resource toBeUpdated) {
        Preconditions.checkNotNull(toBeUpdated, "toBeUpdated is null: %s", toBeUpdated);
        this.toBeUpdated = (ContentModel) toBeUpdated;

    }
}