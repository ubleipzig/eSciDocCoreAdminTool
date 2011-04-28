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

import java.text.SimpleDateFormat;
import java.util.Date;

import com.vaadin.data.util.POJOItem;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Window;

import de.escidoc.admintool.view.util.Constants;
import de.escidoc.admintool.view.util.LayoutHelper;

/**
 * 
 * @author ASP
 * 
 */
public class HistoryDialog extends Window implements Button.ClickListener {
    private static final long serialVersionUID = 4138381496395357610L;

    private final FormLayout layout = new FormLayout();

    private final Window mainWindow;

    /**
     * Displays an history dialog to the customer.
     * 
     * @param mainWindow
     *            the main window of the application.
     * @param caption
     *            the headline.
     * @param pojo
     *            the data binding.
     * @param className
     *            the name of the class.
     */
    public HistoryDialog(final Window mainWindow, final String caption, final POJOItem<?> pojo, final String className) {
        this(mainWindow, caption, 600, 300, pojo, className);
    }

    /**
     * Displays an history dialog to the customer.
     * 
     * @param mainWindow
     *            the main window of the application.
     * @param caption
     *            the headline.
     * @param width
     *            the width of the window.
     * @param height
     *            the height of the window.
     * @param pojo
     *            the data binding.
     * @param className
     *            the name of the class.
     */
    public HistoryDialog(final Window mainWindow, final String caption, final int width, final int height,
        final POJOItem<?> pojo, final String className) {
        this.mainWindow = mainWindow;
        super.setWidth(width + "px");
        super.setHeight(height + "px");
        super.setCaption(caption);
        super.setModal(true);
        // created
        final SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        final AbstractComponent createdByField =
            LayoutHelper.createElement(className, pojo, true, Constants.CREATED_BY);

        Object creationDate = pojo.getItemProperty(Constants.CREATED_ON).getValue();

        if (creationDate == null) {
            creationDate = new Date();
        }

        layout.addComponent(LayoutHelper.create("Created:", " by ", new Label(sdf.format(creationDate)),
            createdByField, 100, 80, false));

        // modified
        final AbstractComponent modifiedByField =
            LayoutHelper.createElement(className, pojo, true, Constants.MODIFIED_BY);
        Object modificationDate = pojo.getItemProperty(Constants.MODIFIED_ON).getValue();
        if (modificationDate == null) {
            modificationDate = new Date();
        }
        layout.addComponent(LayoutHelper.create("Modified:", " by ", new Label(sdf.format(modificationDate)),
            modifiedByField, 100, 80, false));
        final Button button = new Button("OK", this);
        layout.addComponent(LayoutHelper.create("", button, 10, false));
        super.addComponent(layout);
    }

    @Override
    public void buttonClick(final ClickEvent event) {
        mainWindow.removeWindow(HistoryDialog.this);
    }

}
