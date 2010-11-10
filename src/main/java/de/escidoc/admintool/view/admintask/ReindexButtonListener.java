package de.escidoc.admintool.view.admintask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Window.Notification;

import de.escidoc.admintool.view.ErrorMessage;
import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;
import de.escidoc.core.resources.adm.MessagesStatus;

final class ReindexButtonListener implements ClickListener {

    private static final Logger log = LoggerFactory
        .getLogger(ReindexButtonListener.class);

    private static final long serialVersionUID = -2927507839456545485L;

    private MessagesStatus status;

    private ShowStatusCommand showStatusCommand;

    private final ReindexResourceViewImpl reindexResourceViewImpl;

    private final CheckBox clearIndexBox;

    private final AbstractField indexNameSelect;

    /**
     * @param reindexResourceViewImpl
     */
    ReindexButtonListener(
        final ReindexResourceViewImpl reindexResourceViewImpl,
        final CheckBox clearIndexBox, final AbstractField indexNameSelect) {

        this.reindexResourceViewImpl = reindexResourceViewImpl;
        this.clearIndexBox = clearIndexBox;
        this.indexNameSelect = indexNameSelect;
    }

    void setCommand(final ShowStatusCommand showStatusCommand) {
        this.showStatusCommand = showStatusCommand;
    }

    @Override
    public void buttonClick(final ClickEvent event) {
        checkPreconditions();
        tryReindex();
        if (isFinished()) {
            showMessage();
        }
        else {
            showErrorMessage();
        }
    }

    private void checkPreconditions() {
        final Boolean shouldClearIndex = shouldClearIndex();
        final String indexName = getIndexName();
        log.debug("Reindex clear?" + shouldClearIndex + " using " + indexName);

        Preconditions.checkNotNull(indexName, "indexName is null: %s",
            indexName);
        Preconditions.checkArgument(!indexName.isEmpty(),
            " indexName is empty", indexName);
    }

    private void tryReindex() {
        try {
            status =
                reindexResourceViewImpl.adminService.reindex(
                    shouldClearIndex(), getIndexName());
        }
        catch (final EscidocException e) {
            ErrorMessage.show(reindexResourceViewImpl.mainWindow, e);
        }
        catch (final InternalClientException e) {
            ErrorMessage.show(reindexResourceViewImpl.mainWindow, e);
        }
        catch (final TransportException e) {
            ErrorMessage.show(reindexResourceViewImpl.mainWindow, e);
        }
    }

    private boolean isFinished() {
        return status.getStatusCode() == MessagesStatus.STATUS_FINISHED;
    }

    private void showMessage() {
        reindexResourceViewImpl.mainWindow.showNotification(status
            .getStatusMessage());
    }

    private void showErrorMessage() {
        reindexResourceViewImpl.mainWindow.showNotification(new Notification(
            status.getStatusMessage(), Notification.TYPE_ERROR_MESSAGE));
    }

    private void pollStatus() {
        final Thread thread = new Thread() {

            @Override
            public void run() {
                try {
                    MessagesStatus myStatus =
                        reindexResourceViewImpl.adminService
                            .retrieveReindexStatus();
                    for (; myStatus.getStatusCode() == MessagesStatus.STATUS_IN_PROGRESS;) {
                        myStatus =
                            reindexResourceViewImpl.adminService
                                .retrieveReindexStatus();
                        showStatusCommand.execute(myStatus);

                        sleep(1000);
                    }
                    showStatusCommand.execute(myStatus);

                }
                catch (final EscidocException e) {
                    throw new RuntimeException(e);
                }
                catch (final InternalClientException e) {
                    throw new RuntimeException(e);
                }
                catch (final TransportException e) {
                    throw new RuntimeException(e);
                }
                catch (final InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        thread.start();
    }

    // private void pollStatus() {
    // while (status.getStatusCode() == MessagesStatus.STATUS_IN_PROGRESS) {
    // retrieveReindexStatus();
    // showStatusToUser();
    // }
    // }

    private void showStatusToUser() {
        showStatusCommand.execute(status);
    }

    private void retrieveReindexStatus() {
        try {
            status =
                reindexResourceViewImpl.adminService.retrieveReindexStatus();
        }
        catch (final EscidocException e) {
            log.warn("Unexpected error: " + e);
            ErrorMessage.show(reindexResourceViewImpl.mainWindow, e);
        }
        catch (final InternalClientException e) {
            log.warn("Unexpected error: " + e);
            ErrorMessage.show(reindexResourceViewImpl.mainWindow, e);
        }
        catch (final TransportException e) {
            log.warn("Unexpected error: " + e);
            ErrorMessage.show(reindexResourceViewImpl.mainWindow, e);
        }
    }

    private Boolean shouldClearIndex() {
        return (Boolean) clearIndexBox.getValue();
    }

    private String getIndexName() {
        return (String) indexNameSelect.getValue();
    }
}