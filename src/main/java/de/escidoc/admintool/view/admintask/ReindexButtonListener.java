package de.escidoc.admintool.view.admintask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;

import de.escidoc.admintool.view.ModalDialog;
import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;
import de.escidoc.core.resources.adm.MessagesStatus;

final class ReindexButtonListener implements ClickListener {

    private static final Logger LOG = LoggerFactory
        .getLogger(ReindexButtonListener.class);

    private static final long serialVersionUID = -2927507839456545485L;

    private MessagesStatus status;

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

    @Override
    public void buttonClick(final ClickEvent event) {
        checkPreconditions();
        tryReindex();
        showMessage();
    }

    private void checkPreconditions() {
        final Boolean shouldClearIndex = shouldClearIndex();
        final String indexName = getIndexName();
        LOG.debug("Reindex clear?" + shouldClearIndex + " using " + indexName);

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
            ModalDialog.show(reindexResourceViewImpl.mainWindow, e);
        }
        catch (final InternalClientException e) {
            ModalDialog.show(reindexResourceViewImpl.mainWindow, e);
        }
        catch (final TransportException e) {
            ModalDialog.show(reindexResourceViewImpl.mainWindow, e);
        }
    }

    private void showMessage() {
        reindexResourceViewImpl.mainWindow.showNotification(status
            .getStatusMessage());
    }

    private Boolean shouldClearIndex() {
        return (Boolean) clearIndexBox.getValue();
    }

    private String getIndexName() {
        return (String) indexNameSelect.getValue();
    }
}