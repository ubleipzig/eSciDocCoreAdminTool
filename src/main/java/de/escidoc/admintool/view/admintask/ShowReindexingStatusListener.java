package de.escidoc.admintool.view.admintask;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;

import de.escidoc.admintool.view.ModalDialog;
import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;
import de.escidoc.core.resources.adm.MessagesStatus;
import de.escidoc.core.resources.common.Result;

final class ShowReindexingStatusListener implements Button.ClickListener {
    private static final long serialVersionUID = 7983820676422127810L;

    private final ReindexResourceViewImpl reindexResourceViewImpl;

    ShowReindexingStatusListener(
        final ReindexResourceViewImpl reindexResourceViewImpl) {
        this.reindexResourceViewImpl = reindexResourceViewImpl;
    }

    @Override
    public void buttonClick(final ClickEvent event) {
        showStatus(getStatus());
    }

    private void showStatus(final MessagesStatus messageStatus) {
        reindexResourceViewImpl.statusLabel.setValue(toString(messageStatus));
    }

    private MessagesStatus getStatus() {
        try {
            return reindexResourceViewImpl.adminService.retrieveReindexStatus();
        }
        catch (final EscidocException e) {
            ModalDialog.show(reindexResourceViewImpl.mainWindow, e);
        }
        catch (final InternalClientException e) {
            ModalDialog.show(reindexResourceViewImpl.mainWindow, e);
        }
        catch (final TransportException e) {
            ModalDialog.show(reindexResourceViewImpl.mainWindow, e);
        }
        return new MessagesStatus(new Result(),
            MessagesStatus.STATUS_INVALID_RESULT);
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