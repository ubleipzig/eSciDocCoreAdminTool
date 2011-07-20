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
package de.escidoc.admintool.view.admintask.reindex;

import java.util.List;

import com.google.common.base.Preconditions;
import com.vaadin.Application;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Label;
import com.vaadin.ui.ProgressIndicator;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.Notification;

import de.escidoc.admintool.view.ModalDialog;
import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.resources.adm.AdminStatus;
import de.escidoc.core.resources.adm.MessagesStatus;

@SuppressWarnings("serial")
final class ReindexButtonListener implements ClickListener {

    private final VerticalLayout statusLayout = new VerticalLayout();

    private final ReindexResourceViewImpl reindexResourceViewImpl;

    private final Application application;

    private final CheckBox clearIndexBox;

    private final AbstractField indexNameSelect;

    private final Button reindexResourceBtn;

    private final ProgressIndicator progressIndicator;

    public ReindexButtonListener(final Application app, final ReindexResourceViewImpl reindexResourceViewImpl,
        final CheckBox clearIndexBox, final AbstractField indexNameSelect, final Button reindexResourceBtn,
        final ProgressIndicator progressIndicator) {
        Preconditions.checkNotNull(app, "app is null: %s", app);
        Preconditions.checkNotNull(reindexResourceViewImpl, "reindexResourceViewImpl is null: %s",
            reindexResourceViewImpl);
        Preconditions.checkNotNull(clearIndexBox, "clearIndexBox is null: %s", clearIndexBox);
        Preconditions.checkNotNull(indexNameSelect, "indexNameSelect is null: %s", indexNameSelect);
        Preconditions.checkNotNull(reindexResourceBtn, "reindexResourceBtn is null: %s", reindexResourceBtn);
        Preconditions.checkNotNull(progressIndicator, "progressIndicator is null: %s", progressIndicator);

        application = app;
        this.reindexResourceViewImpl = reindexResourceViewImpl;
        this.clearIndexBox = clearIndexBox;
        this.indexNameSelect = indexNameSelect;
        this.reindexResourceBtn = reindexResourceBtn;
        this.progressIndicator = progressIndicator;
    }

    @Override
    public void buttonClick(final ClickEvent event) {
        checkPreconditions();
        tryReindex();
    }

    private void makeReindexButtonInvisible() {
        reindexResourceBtn.setVisible(false);
    }

    private void checkPreconditions() {
        Preconditions.checkNotNull(getIndexName(), "indexName is null: %s", getIndexName());
        Preconditions.checkArgument(!getIndexName().isEmpty(), " indexName is empty", getIndexName());
    }

    private void tryReindex() {
        try {
            showReindexStatus(reindex());
            new AskStatusThread().start();
            progressIndicator.setEnabled(true);
            progressIndicator.setVisible(true);
            progressIndicator.setStyleName("big");
            progressIndicator.setValue(new Float(0f));
            makeReindexButtonInvisible();
        }
        catch (final EscidocClientException e) {
            ModalDialog.show(reindexResourceViewImpl.mainWindow, e);
        }
    }

    public class AskStatusThread extends Thread {

        private MessagesStatus reindexStatus;

        @Override
        public void run() {

            for (;;) {
                try {
                    reindexStatus = getReindexStatus();
                    if (reindexStatus.getStatusCode() == AdminStatus.STATUS_FINISHED) {
                        showFinishStatus(reindexStatus);
                        progressIndicator.setVisible(false);
                        progressIndicator.setEnabled(false);
                        reindexResourceBtn.setVisible(true);
                        progressIndicator.setValue(new Float(1f));
                        return;
                    }
                    showReindexStatus(reindexStatus);
                    Thread.sleep(1000);
                }
                catch (final InterruptedException e) {
                    e.printStackTrace();
                }
                catch (final EscidocClientException e) {
                    e.printStackTrace();
                }
                // All modifications to Vaadin components should be synchronized
                // over application instance. For normal requests this is done
                // by the servlet. Here we are changing the application state
                // via a separate thread.
                // Application application = reindexResourceViewImpl.getApplication();
                synchronized (application) {
                    updateProgressIndicator();
                }
            }
        }

        private void updateProgressIndicator() {
            if (reindexStatus.getStatusCode() == AdminStatus.STATUS_FINISHED) {
                progressIndicator.setEnabled(false);
                reindexResourceBtn.setVisible(true);
                progressIndicator.setValue(new Float(1f));
            }

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
            progressIndicator.setVisible(false);
            progressIndicator.setEnabled(false);
            reindexResourceBtn.setVisible(true);
            progressIndicator.setValue(new Float(1f));
            return;
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

    private void showInProgresStatus(final List<String> messageList) {
        Preconditions.checkNotNull(messageList, "messageList is null: %s", messageList);

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