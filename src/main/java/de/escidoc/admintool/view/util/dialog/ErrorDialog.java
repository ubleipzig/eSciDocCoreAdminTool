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
package de.escidoc.admintool.view.util.dialog;

import com.google.common.base.Preconditions;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Window;

import de.escidoc.admintool.view.util.LayoutHelper;

/**
 * 
 * @author ASP
 * 
 */
public class ErrorDialog extends Window implements Button.ClickListener {
    private static final long serialVersionUID = 6255824594582824620L;

    private final FormLayout layout = new FormLayout();

    private final Window mainWindow;

    /**
     * Displays an error message dialog to the customer.
     * 
     * @param mainWindow
     *            the main window of the application.
     * @param caption
     *            the headline.
     * @param errorMessage
     *            the message, describing what went wrong.
     */
    public ErrorDialog(final Window mainWindow, final String caption, final String errorMessage) {
        this(mainWindow, caption, errorMessage, 600, 300);
    }

    /**
     * Displays an error message dialog to the customer.
     * 
     * @param mainWindow
     *            the main window of the application.
     * @param caption
     *            the headline.
     * @param errorMessage
     *            the message, describing what went wrong.
     * @param width
     *            the width of the window.
     * @param height
     *            the height of the window.
     */
    public ErrorDialog(final Window mainWindow, final String caption, final String errorMessage, final int width,
        final int height) {

        Preconditions.checkNotNull(mainWindow, "mainWindow can not be null: %s", mainWindow);
        Preconditions.checkNotNull(caption, "caption can not be null: %s", caption);
        Preconditions.checkNotNull(mainWindow, "errorMessage can not be null: %s", errorMessage);

        this.mainWindow = mainWindow;
        super.setWidth(width + "px");
        super.setHeight(height + "px");
        super.setCaption(caption);
        super.setModal(true);

        final Label errorMassageLabel = new Label(errorMessage);
        layout.addComponent(errorMassageLabel);
        layout.setExpandRatio(errorMassageLabel, 1);
        // layout.addComponent(LayoutHelper.createContextView("", new
        // Label(errorMessage),
        // 10, false));
        final Button button = new Button("OK");
        layout.addComponent(LayoutHelper.create("", button, 10, false));
        button.addListener(this);
        super.addComponent(layout);
    }

    @Override
    public void buttonClick(final ClickEvent event) {
        mainWindow.removeWindow(ErrorDialog.this);
    }
}
