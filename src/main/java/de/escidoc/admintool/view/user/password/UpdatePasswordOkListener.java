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
package de.escidoc.admintool.view.user.password;

import com.google.common.base.Preconditions;
import com.vaadin.terminal.UserError;
import com.vaadin.ui.AbstractTextField;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Window;

import de.escidoc.admintool.view.ModalDialog;
import de.escidoc.admintool.view.ViewConstants;
import de.escidoc.core.client.exceptions.EscidocClientException;

public final class UpdatePasswordOkListener implements Button.ClickListener {
    private static final long serialVersionUID = 8604187353382368999L;

    private final AbstractTextField passwordField;

    private final AbstractTextField retypePasswordField;

    private Window mainWindow;

    private UpdatePasswordCommand command;

    // TODO make it builder
    public UpdatePasswordOkListener(final AbstractTextField passwordField, final AbstractTextField retypePasswordField) {
        this.passwordField = passwordField;
        this.retypePasswordField = retypePasswordField;
    }

    public void setMainWindow(final Window mainWindow) {
        this.mainWindow = mainWindow;
    }

    public void setCommand(final UpdatePasswordCommand command) {
        this.command = command;
    }

    @Override
    public void buttonClick(final ClickEvent event) {
        if (isLessThanSixChars(enteredPassword())) {
            focusOnPasswordField();
        }
        else if (isMatched(enteredRetypePassword(), enteredPassword())) {
            removeErrorMessage();
            updatePassword();
        }
        else {
            showErrorMessage();
            focusOnPasswordField();
        }
    }

    private void focusOnPasswordField() {
        passwordField.focus();
    }

    private String enteredPassword() {
        return (String) passwordField.getValue();
    }

    private String enteredRetypePassword() {
        return (String) retypePasswordField.getValue();
    }

    private boolean isLessThanSixChars(final String passwordValue) {
        return passwordValue.length() < 6;
    }

    private boolean isMatched(final String retypePasswordValue, final String passwordValue) {
        return retypePasswordValue.equals(passwordValue);
    }

    private void updatePassword() {
        Preconditions.checkNotNull(command, "command is null: %s", command);
        try {
            command.execute(enteredPassword());
            resetAllFields();
            mainWindow.showNotification(ViewConstants.PASSWORD_UPDATED_MESSAGE);
        }
        catch (final EscidocClientException e) {
            ModalDialog.show(mainWindow, e);
        }
    }

    private void removeErrorMessage() {
        retypePasswordField.setComponentError(null);
    }

    private void resetAllFields() {
        removeErrorMessage();
        passwordField.setValue("");
        retypePasswordField.setValue("");
    }

    private void showErrorMessage() {
        retypePasswordField.setComponentError(new UserError(ViewConstants.PASSWORDS_DID_NOT_MATCH_MESSAGE));
    }
}