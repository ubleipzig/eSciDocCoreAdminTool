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
package de.escidoc.admintool.view.start;

import com.google.common.base.Preconditions;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import de.escidoc.admintool.app.AdminToolApplication;
import de.escidoc.admintool.view.ViewConstants;

@SuppressWarnings("serial")
public class LandingView extends CustomComponent {

    private final VerticalLayout viewLayout = new VerticalLayout();

    private final Panel panel = new Panel();

    private final FormLayout formLayout = new FormLayout();

    private final TextField escidocServiceUrl = new TextField(ViewConstants.ESCIDOC_URL_TEXTFIELD);

    private final HorizontalLayout footer = new HorizontalLayout();

    private final AdminToolApplication app;

    private Button startButton;

    public LandingView(final AdminToolApplication app) {
        Preconditions.checkNotNull(app, "app is null: %s", app);
        this.app = app;
    }

    public void init() {
        setStyleName(ViewConstants.LANDING_VIEW_STYLE_NAME);
        setCompositionRoot(viewLayout);
        setSizeFull();
        viewLayout.setSizeFull();
        viewLayout.setStyleName(ViewConstants.LANDING_VIEW_STYLE_NAME);

        viewLayout.addComponent(panel);
        viewLayout.setComponentAlignment(panel, Alignment.MIDDLE_CENTER);

        panel.setWidth(ViewConstants.LOGIN_WINDOW_WIDTH);
        panel.setCaption(ViewConstants.WELCOMING_MESSAGE);

        addEscidocUrlField();
        addFooters();

        panel.addComponent(formLayout);
    }

    private void addFooters() {
        footer.setWidth(100, UNITS_PERCENTAGE);
        footer.setMargin(true);
        formLayout.addComponent(footer);
        addStartButton();
    }

    private void addEscidocUrlField() {
        escidocServiceUrl.setWidth(265, UNITS_PIXELS);
        escidocServiceUrl.setImmediate(true);
        escidocServiceUrl.focus();
        escidocServiceUrl.setRequired(true);
        escidocServiceUrl.setRequiredError(ViewConstants.E_SCI_DOC_LOCATION_CAN_NOT_BE_EMPTY);
        setInputPrompt();
        formLayout.addComponent(escidocServiceUrl);
    }

    private void setInputPrompt() {
        escidocServiceUrl.setInputPrompt(ViewConstants.HTTP);
    }

    private void addStartButton() {
        startButton = new Button(ViewConstants.OK_LABEL);
        final StartButtonListenerImpl listener = new StartButtonListenerImpl(escidocServiceUrl, app);
        startButton.addListener(listener);
        footer.addComponent(startButton);
        footer.setComponentAlignment(startButton, Alignment.MIDDLE_RIGHT);
    }
}