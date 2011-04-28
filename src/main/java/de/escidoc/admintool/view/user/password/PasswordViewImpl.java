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

import com.vaadin.data.Validator;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.ui.AbstractTextField;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.themes.Reindeer;

import de.escidoc.admintool.view.ViewConstants;

public class PasswordViewImpl extends CustomComponent implements PasswordView {

    private static final long serialVersionUID = 4601029565488311333L;

    private static final boolean IS_NULL_ALLOWED = false;

    private static final int MAX_PASSWORD_LENGTH = 30;

    private static final int MIN_PASSWORD_LENGTH = 6;

    private final FormLayout layout = new FormLayout();

    private final PasswordField passwordField = new PasswordField(ViewConstants.PASSWORD_CAPTION);

    private final PasswordField retypePasswordField = new PasswordField(ViewConstants.RETYPE_PASSWORD_CAPTION);

    private HorizontalLayout footers;

    public PasswordViewImpl() {
        setCompositionRoot(layout);
    }

    public static PasswordView createView() {
        return new PasswordViewImpl();
    }

    @Override
    public void addPasswordField() {
        configure(passwordField);
        layout.addComponent(passwordField);
    }

    private void configure(final AbstractTextField passwordField) {
        passwordField.setWidth(ViewConstants.PASSWORD_FIELD_WIDTH, UNITS_PIXELS);
        passwordField.setImmediate(true);
        passwordField.setMaxLength(MAX_PASSWORD_LENGTH);
    }

    @Override
    public void addRetypePasswordField() {
        configure(retypePasswordField);
        layout.addComponent(retypePasswordField);
    }

    @Override
    public void setPassword(final String password) {
        passwordField.setValue(password);
    }

    @Override
    public void addMinCharValidator() {
        final Validator minCharValidator =
            new StringLengthValidator(ViewConstants.TOO_SHORT_PASSWORD_MESSAGE, MIN_PASSWORD_LENGTH,
                MAX_PASSWORD_LENGTH, IS_NULL_ALLOWED);
        passwordField.addValidator(minCharValidator);
    }

    @Override
    public void addOkButton(final ClickListener updatePasswordOkListener) {
        final Button saveBtn = new Button("Change Password", updatePasswordOkListener);
        saveBtn.setStyleName(Reindeer.BUTTON_SMALL);
        footers = new HorizontalLayout();
        footers.addComponent(saveBtn);
        layout.addComponent(footers);
    }

    @Override
    public PasswordField getPasswordField() {
        return passwordField;
    }

    @Override
    public PasswordField getRetypePasswordField() {
        return retypePasswordField;
    }

    @Override
    public void addCancelButton(final ClickListener cancelListener) {
        final Button cancelBtn = new Button(ViewConstants.CANCEL_LABEL, cancelListener);
        cancelBtn.setStyleName(Reindeer.BUTTON_SMALL);
        footers.addComponent(cancelBtn);
    }

    @Override
    public void resetFields() {
        passwordField.setValue(ViewConstants.EMPTY_STRING);
        retypePasswordField.setValue(ViewConstants.EMPTY_STRING);
    }

    @Override
    public void removeErrorMessages() {
        retypePasswordField.setComponentError(null);
        passwordField.setComponentError(null);
    }
}
