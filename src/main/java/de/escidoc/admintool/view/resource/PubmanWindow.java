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
package de.escidoc.admintool.view.resource;

import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;

import de.escidoc.admintool.view.ViewConstants;

public class PubmanWindow extends Window {

    private final class CancelButtonListener implements ClickListener {
        private static final long serialVersionUID = -1211409730229979129L;

        @Override
        public void buttonClick(final ClickEvent event) {
            closeWindow();
        }
    }

    private static final long serialVersionUID = -4691275024835350852L;

    private static final String MODAL_DIALOG_WIDTH = "460px";

    private static final String MODAL_DIALOG_HEIGHT = "300px";

    private final Button okButton = new Button(ViewConstants.OK_LABEL);

    private final Button cancelBtn = new Button(ViewConstants.CANCEL_LABEL);

    String selectedParent;

    Window mainWindow;

    CssLayout pubman = new CssLayout();

    protected final TextField alternativeField = new TextField(ViewConstants.ALTERNATIVE_LABEL);

    protected final TextField identifierField = new TextField(ViewConstants.IDENTIFIER_LABEL);

    protected final TextField orgTypeField = new TextField(ViewConstants.ORGANIZATION_TYPE);

    private final TextField cityField = new TextField(ViewConstants.CITY_LABEL);

    private final TextField countryField = new TextField(ViewConstants.COUNTRY_LABEL);

    protected final TextField coordinatesField = new TextField(ViewConstants.COORDINATES_LABEL);

    FormLayout fl = new FormLayout();

    public PubmanWindow(final Window mainWindow) {
        this.mainWindow = mainWindow;
        init();
    }

    private void init() {
        setContent(fl);
        fl.setMargin(true);
        configureWindow();
        configureCountryField();
        addButtons();
    }

    private final HorizontalLayout buttons = new HorizontalLayout();

    private void addButtons() {
        fl.addComponent(buttons);
        fl.setComponentAlignment(buttons, Alignment.MIDDLE_RIGHT);
        addOkButton();
        addCancelButton();
    }

    private void addCancelButton() {
        cancelBtn.addListener(new CancelButtonListener());
        buttons.addComponent(cancelBtn);
    }

    void closeWindow() {
        mainWindow.removeWindow(this);
    }

    private void addOkButton() {
        buttons.addComponent(okButton);
    }

    private void configureWindow() {
        setModal(true);
        setCaption("PubMan Metadata");
        setHeight(MODAL_DIALOG_HEIGHT);
        setWidth(MODAL_DIALOG_WIDTH);
    }

    private void configureCountryField() {
        alternativeField.setWidth(300, Sizeable.UNITS_PIXELS);
        identifierField.setWidth(300, Sizeable.UNITS_PIXELS);
        orgTypeField.setWidth(300, Sizeable.UNITS_PIXELS);
        coordinatesField.setWidth(300, Sizeable.UNITS_PIXELS);
        cityField.setWidth(300, Sizeable.UNITS_PIXELS);
        countryField.setWidth(300, Sizeable.UNITS_PIXELS);

        fl.addComponent(alternativeField);
        fl.addComponent(identifierField);
        fl.addComponent(orgTypeField);
        fl.addComponent(coordinatesField);
        fl.addComponent(cityField);
        fl.addComponent(countryField);
    }

    public void setSelected(final String selectedParent) {
        this.selectedParent = selectedParent;
    }
}