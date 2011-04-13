package de.escidoc.admintool.view.admintask.filter;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.Notification;

import de.escidoc.admintool.service.AdminService;
import de.escidoc.admintool.view.ModalDialog;
import de.escidoc.admintool.view.ViewConstants;
import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;
import de.escidoc.core.resources.Resource;
import de.escidoc.core.resources.adm.AdminStatus;
import de.escidoc.core.resources.adm.MessagesStatus;

final class PurgeResourcesListener implements ClickListener {

    private static final Logger LOG = LoggerFactory
        .getLogger(PurgeResourcesListener.class);

    private final ShowFilterResultCommandImpl command;

    private final FormLayout formLayout;

    private final Window mainWindow;

    PurgeResourcesListener(final ShowFilterResultCommandImpl command,
        final FormLayout formLayout, final AdminService adminService,
        final Window mainWindow) {

        Preconditions.checkNotNull(command, "command is null: %s", command);
        Preconditions.checkNotNull(formLayout, "formLayout is null: %s",
            formLayout);
        Preconditions.checkNotNull(adminService, "adminService is null: %s",
            adminService);
        Preconditions.checkNotNull(mainWindow, "mainWindow is null: %s",
            mainWindow);
        this.command = command;
        this.formLayout = formLayout;
        this.mainWindow = mainWindow;
    }

    private static final long serialVersionUID = 978892007619016520L;

    private Set<Resource> selectedResources;

    @Override
    public void buttonClick(final ClickEvent event) {
        purgeSelectedResources();
    }

    @SuppressWarnings("unchecked")
    private void purgeSelectedResources() {
        final Object object = command.filteredList.getValue();
        if (object instanceof Set) {

            selectedResources = (Set<Resource>) object;
            if (selectedResources.isEmpty()) {
                return;
            }

            final Set<String> objectIds =
                new HashSet<String>(selectedResources.size());
            for (final Resource resource : selectedResources) {
                objectIds.add(resource.getObjid());
            }

            tryPurge(objectIds);
        }
    }

    private void tryPurge(final Set<String> objectIds) {
        try {
            showPurgeStatus(startPurging(objectIds));
        }
        catch (final EscidocException e) {
            LOG.warn("Unexpected error: " + e);
            ModalDialog.show(command.filterResourceView.mainWindow, e);
        }
        catch (final InternalClientException e) {
            LOG.warn("Unexpected error: " + e);
            ModalDialog.show(command.filterResourceView.mainWindow, e);
        }
        catch (final TransportException e) {
            LOG.warn("Unexpected error: " + e);
            ModalDialog.show(command.filterResourceView.mainWindow, e);
        }
    }

    private void showPurgeStatus(final MessagesStatus status)
        throws EscidocException, InternalClientException, TransportException {

        if (status.getStatusCode() == AdminStatus.STATUS_INVALID_RESULT) {
            showErrorMessage(status);
        }
        if (status.getStatusCode() == AdminStatus.STATUS_FINISHED) {
            removeResourcesFromContainer();
            showSuccessNotification(status);
        }
        else if (status.getStatusCode() == AdminStatus.STATUS_IN_PROGRESS) {
            showPurgeStatus(command.filterResourceView.adminService
                .retrievePurgeStatus());
        }
        else {
            showErrorMessage(status);
        }
    }

    private void showSuccessNotification(final MessagesStatus status) {
        mainWindow.showNotification(ViewConstants.INFO,
            status.getStatusMessage(), Notification.TYPE_TRAY_NOTIFICATION);
    }

    private MessagesStatus startPurging(final Set<String> objectIds)
        throws EscidocException, InternalClientException, TransportException {
        return command.filterResourceView.adminService.purge(objectIds);
    }

    private void removeResourcesFromContainer() {
        for (final Resource resource : selectedResources) {
            command.filteredResourcesContainer.removeItem(resource);
        }
    }

    private void showErrorMessage(final AdminStatus status) {
        mainWindow.showNotification(new Notification(status.getStatusMessage(),
            Notification.TYPE_ERROR_MESSAGE));
    }
}