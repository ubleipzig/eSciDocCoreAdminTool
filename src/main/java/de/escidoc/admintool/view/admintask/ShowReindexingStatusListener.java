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

import com.google.common.base.Preconditions;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Label;

import de.escidoc.admintool.view.ModalDialog;
import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;
import de.escidoc.core.resources.adm.MessagesStatus;
import de.escidoc.core.resources.common.Result;

final class ShowReindexingStatusListener implements Button.ClickListener {
    private static final long serialVersionUID = 7983820676422127810L;

    private final ReindexResourceViewImpl reindexResourceViewImpl;

    private final Label statusLabel;

    ShowReindexingStatusListener(final ReindexResourceViewImpl reindexResourceViewImpl, final Label statusLabel) {
        Preconditions.checkNotNull(reindexResourceViewImpl, "reindexResourceViewImpl is null: %s",
            reindexResourceViewImpl);
        Preconditions.checkNotNull(statusLabel, "statusLabel is null: %s", statusLabel);
        this.reindexResourceViewImpl = reindexResourceViewImpl;
        this.statusLabel = statusLabel;
    }

    @Override
    public void buttonClick(final ClickEvent event) {
        showStatus(getStatus());
    }

    private void showStatus(final MessagesStatus messageStatus) {
        statusLabel.setValue(toString(messageStatus));
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