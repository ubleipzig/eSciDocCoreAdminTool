package de.escidoc.admintool.view.admintask;

import com.google.common.base.Preconditions;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Label;
import com.vaadin.ui.Window;

import de.escidoc.admintool.service.AdminService;
import de.escidoc.admintool.view.ModalDialog;
import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;
import de.escidoc.core.resources.adm.MessagesStatus;
import de.escidoc.core.resources.common.Result;

public class ShowPurgeStatusListener implements ClickListener {

    private static final long serialVersionUID = 7881290277636804150L;

    private final AdminService adminService;

    private final Window mainWindow;

    private final Label statusLabel;

    public ShowPurgeStatusListener(final AdminService adminService, final Window mainWindow, final Label statusLabel) {
        preconditions(adminService, mainWindow, statusLabel);
        this.adminService = adminService;
        this.mainWindow = mainWindow;
        this.statusLabel = statusLabel;
    }

    private void preconditions(final AdminService adminService, final Window mainWindow, final Label statusLabel) {
        Preconditions.checkNotNull(adminService, "adminService is null: %s", adminService);
        Preconditions.checkNotNull(mainWindow, "mainWindow is null: %s", mainWindow);
        Preconditions.checkNotNull(statusLabel, "adminService is null: %s", statusLabel);
    }

    @Override
    public void buttonClick(final ClickEvent event) {
        showStatus(getStatus());
    }

    private void showStatus(final MessagesStatus status) {
        statusLabel.setValue(toString(status));
    }

    private MessagesStatus getStatus() {
        try {
            return adminService.retrievePurgeStatus();
        }
        catch (final EscidocException e) {
            ModalDialog.show(mainWindow, e);
        }
        catch (final InternalClientException e) {
            ModalDialog.show(mainWindow, e);
        }
        catch (final TransportException e) {
            ModalDialog.show(mainWindow, e);
        }
        return new MessagesStatus(new Result(), MessagesStatus.STATUS_INVALID_RESULT);
    }

    private String toString(final MessagesStatus messagesStatus) {
        final StringBuilder builder = new StringBuilder();
        builder.append(messagesStatus.getStatusMessage());

        for (final String message : messagesStatus.getMessages()) {
            builder.append(message);
        }
        return builder.toString();
    }

}
