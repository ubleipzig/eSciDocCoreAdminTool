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

import java.util.List;

import com.google.common.base.Preconditions;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.Notification;

import de.escidoc.admintool.view.ModalDialog;
import de.escidoc.admintool.view.ViewConstants;
import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.resources.adm.AdminStatus;
import de.escidoc.core.resources.adm.MessagesStatus;

@SuppressWarnings("serial")
final class ReindexButtonListener implements ClickListener {

    private final Button showStatusButton = new Button(ViewConstants.SHOW_STATUS);

    private final ReindexResourceViewImpl reindexResourceViewImpl;

    private final CheckBox clearIndexBox;

    private final AbstractField indexNameSelect;

    public ReindexButtonListener(final ReindexResourceViewImpl reindexResourceViewImpl, final CheckBox clearIndexBox,
        final AbstractField indexNameSelect) {
        Preconditions.checkNotNull(reindexResourceViewImpl, "reindexResourceViewImpl is null: %s",
            reindexResourceViewImpl);
        Preconditions.checkNotNull(clearIndexBox, "clearIndexBox is null: %s", clearIndexBox);
        Preconditions.checkNotNull(indexNameSelect, "indexNameSelect is null: %s", indexNameSelect);

        this.reindexResourceViewImpl = reindexResourceViewImpl;
        this.clearIndexBox = clearIndexBox;
        this.indexNameSelect = indexNameSelect;
    }

    @Override
    public void buttonClick(final ClickEvent event) {
        checkPreconditions();
        createShowStatusButton();
        tryReindex();
    }

    private void createShowStatusButton() {
        reindexResourceViewImpl.getViewLayout().addComponent(showStatusButton);
        showStatusButton.setVisible(false);
        showStatusButton.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(final ClickEvent event) {
                tryShowStatus();
            }

            private void tryShowStatus() {
                try {
                    showStatus();
                }
                catch (final EscidocClientException e) {
                    ModalDialog.show(reindexResourceViewImpl.mainWindow, e);
                }
            }
        });
    }

    private void checkPreconditions() {
        Preconditions.checkNotNull(getIndexName(), "indexName is null: %s", getIndexName());
        Preconditions.checkArgument(!getIndexName().isEmpty(), " indexName is empty", getIndexName());
    }

    private void tryReindex() {
        try {
            showReindexStatus(reindex());
        }
        catch (final EscidocClientException e) {
            ModalDialog.show(reindexResourceViewImpl.mainWindow, e);
        }
    }

    private MessagesStatus reindex() throws EscidocClientException {
        return reindexResourceViewImpl.adminService.reindex(shouldClearIndex(), getIndexName());
    }

    private void showReindexStatus(final MessagesStatus status) {
        if (status.getStatusCode() == AdminStatus.STATUS_INVALID_RESULT) {
            showErrorMessage(status);
        }
        if (status.getStatusCode() == AdminStatus.STATUS_FINISHED) {
            showFinishStatus(status);
        }
        else if (status.getStatusCode() == AdminStatus.STATUS_IN_PROGRESS) {
            showInProgresStatus(status.getMessages());
        }
        else {
            showErrorMessage(status);
        }
    }

    private void showFinishStatus(final MessagesStatus status) {
        statusLayout.removeAllComponents();
        statusLayout.addComponent(new Label(status.getStatusMessage()));
    }

    private void showStatus() throws EscidocClientException {
        showReindexStatus(getReindexStatus());
    }

    final VerticalLayout statusLayout = new VerticalLayout();

    private void showInProgresStatus(final List<String> messageList) {
        Preconditions.checkNotNull(messageList, "messageList is null: %s", messageList);
        showStatusButton.setVisible(true);

        if (reindexResourceViewImpl.getViewLayout().getComponentIndex(statusLayout) < 0) {
            reindexResourceViewImpl.getViewLayout().addComponent(statusLayout);
        }
        else {
            statusLayout.removeAllComponents();
        }

        for (final String message : messageList) {
            statusLayout.addComponent(new Label(message));
        }

    }

    private MessagesStatus getReindexStatus() throws EscidocClientException {
        return reindexResourceViewImpl.adminService.retrieveReindexStatus();
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