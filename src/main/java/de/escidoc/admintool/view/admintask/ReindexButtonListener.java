/**
 * CDDL HEADER START
 *
 * The contents of this file are subject to the terms of the
 * Common Development and Distribution License, Version 1.0 only
 * (the "License").  You may not use this file except in compliance
 * with the License.
 *
 * You can obtain a copy of the license at license/ESCIDOC.LICENSE
 * or https://www.escidoc.org/license/ESCIDOC.LICENSE .
 * See the License for the specific language governing permissions
 * and limitations under the License.
 *
 * When distributing Covered Code, include this CDDL HEADER in each
 * file and include the License file at license/ESCIDOC.LICENSE.
 * If applicable, add the following below this CDDL HEADER, with the
 * fields enclosed by brackets "[]" replaced with your own identifying
 * information: Portions Copyright [yyyy] [name of copyright owner]
 *
 * CDDL HEADER END
 *
 *
 *
 * Copyright 2011 Fachinformationszentrum Karlsruhe Gesellschaft
 * fuer wissenschaftlich-technische Information mbH and Max-Planck-
 * Gesellschaft zur Foerderung der Wissenschaft e.V.
 * All rights reserved.  Use is subject to license terms.
 */
package de.escidoc.admintool.view.admintask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.Notification;

import de.escidoc.admintool.view.ModalDialog;
import de.escidoc.admintool.view.ViewConstants;
import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;
import de.escidoc.core.resources.adm.AdminStatus;
import de.escidoc.core.resources.adm.MessagesStatus;

final class ReindexButtonListener implements ClickListener {

    private static final Logger LOG = LoggerFactory.getLogger(ReindexButtonListener.class);

    private static final long serialVersionUID = -2927507839456545485L;

    private final ReindexResourceViewImpl reindexResourceViewImpl;

    private final CheckBox clearIndexBox;

    private final AbstractField indexNameSelect;

    ReindexButtonListener(final ReindexResourceViewImpl reindexResourceViewImpl, final CheckBox clearIndexBox,
        final AbstractField indexNameSelect) {

        this.reindexResourceViewImpl = reindexResourceViewImpl;
        this.clearIndexBox = clearIndexBox;
        this.indexNameSelect = indexNameSelect;
    }

    @Override
    public void buttonClick(final ClickEvent event) {
        checkPreconditions();
        tryReindex();
    }

    private void checkPreconditions() {
        final Boolean shouldClearIndex = shouldClearIndex();
        final String indexName = getIndexName();
        LOG.debug("Reindex clear?" + shouldClearIndex + " using " + indexName);

        Preconditions.checkNotNull(indexName, "indexName is null: %s", indexName);
        Preconditions.checkArgument(!indexName.isEmpty(), " indexName is empty", indexName);
    }

    private void tryReindex() {
        try {
            showReindexStatus(reindexResourceViewImpl.adminService.reindex(shouldClearIndex(), getIndexName()));
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

    private void showReindexStatus(final MessagesStatus status) throws EscidocException, InternalClientException,
        TransportException {

        if (status.getStatusCode() == AdminStatus.STATUS_INVALID_RESULT) {
            showErrorMessage(status);
        }
        if (status.getStatusCode() == AdminStatus.STATUS_FINISHED) {
            showSuccessNotification(status);
        }
        else if (status.getStatusCode() == AdminStatus.STATUS_IN_PROGRESS) {
            showReindexStatus(getReindexStatus());
        }
        else {
            showErrorMessage(status);
        }
    }

    private MessagesStatus getReindexStatus() throws EscidocException, InternalClientException, TransportException {
        return reindexResourceViewImpl.adminService.retrieveReindexStatus();
    }

    private void showSuccessNotification(final MessagesStatus status) {
        getMainWindow().showNotification(ViewConstants.INFO, status.getStatusMessage(),
            Notification.TYPE_TRAY_NOTIFICATION);
    }

    private void showErrorMessage(final MessagesStatus status) {
        getMainWindow().showNotification(new Notification(status.getStatusMessage(), Notification.TYPE_ERROR_MESSAGE));
    }

    private Window getMainWindow() {
        return reindexResourceViewImpl.mainWindow;
    }

    private Boolean shouldClearIndex() {
        return (Boolean) clearIndexBox.getValue();
    }

    private String getIndexName() {
        return (String) indexNameSelect.getValue();
    }
}