package de.escidoc.admintool.view.admintask;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Window.Notification;

import de.escidoc.admintool.view.ModalDialog;
import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;
import de.escidoc.core.resources.Resource;
import de.escidoc.core.resources.adm.MessagesStatus;

final class PurgeResourcesListener implements ClickListener {

    private static final Logger LOG = LoggerFactory
        .getLogger(PurgeResourcesListener.class);

    private final ShowFilterResultCommandImpl command;

    PurgeResourcesListener(final ShowFilterResultCommandImpl command) {
        this.command = command;
    }

    private static final long serialVersionUID = 978892007619016520L;

    private Set<Resource> selectedResources;

    private MessagesStatus status;

    @Override
    public void buttonClick(final ClickEvent event) {
        getSelectedResources();
    }

    @SuppressWarnings("unchecked")
    private void getSelectedResources() {
        final Object object = command.filteredList.getValue();
        if (object instanceof Set) {

            selectedResources = (Set<Resource>) object;
            if (selectedResources.isEmpty()) {
                return;
            }

            final Set<String> objectIds = new HashSet<String>();
            for (final Resource resource : selectedResources) {
                LOG.debug("Purging: " + resource.getXLinkTitle());
                objectIds.add(resource.getObjid());
            }
            tryPurge(objectIds);
        }
    }

    private void tryPurge(final Set<String> objectIds) {
        try {
            startPurging(objectIds);
            pollStatus();
            if (status.getStatusCode() == MessagesStatus.STATUS_FINISHED) {
                removeResourcesFromContainer();
                // showMessage();
            }
            else {
                showErrorMessage();
            }
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

    private void startPurging(final Set<String> objectIds)
        throws EscidocException, InternalClientException, TransportException {
        status = command.filterResourceView.adminService.purge(objectIds);
    }

    private void pollStatus() throws EscidocException, InternalClientException,
        TransportException {
        while (status.getStatusCode() == MessagesStatus.STATUS_IN_PROGRESS) {
            retrievePurgeStatus();
        }
    }

    private void retrievePurgeStatus() throws EscidocException,
        InternalClientException, TransportException {
        status = command.filterResourceView.adminService.retrievePurgeStatus();
    }

    private void removeResourcesFromContainer() {
        for (final Resource resource : selectedResources) {
            command.filteredResourcesContainer.removeItem(resource);
        }
    }

    private void showErrorMessage() {
        command.filterResourceView.mainWindow
            .showNotification(new Notification(status.getStatusMessage(),
                Notification.TYPE_ERROR_MESSAGE));
    }
}