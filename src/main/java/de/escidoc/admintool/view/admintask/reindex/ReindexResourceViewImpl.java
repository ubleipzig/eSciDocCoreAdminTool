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

import com.google.common.base.Preconditions;

import com.vaadin.Application;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.ProgressIndicator;
import com.vaadin.ui.Window;

import de.escidoc.admintool.service.AdminService;
import de.escidoc.admintool.view.ViewConstants;
import de.escidoc.admintool.view.admintask.AbstractCustomView;

@SuppressWarnings("serial")
public class ReindexResourceViewImpl extends AbstractCustomView implements ReindexResourceView {

    private final Button reindexResourceBtn = new Button(ViewConstants.REINDEX);

    private final CheckBox clearIndexBox = new CheckBox(ViewConstants.CLEAR_INDEX);

    private final ComboBox indexNameSelect = new ComboBox(ViewConstants.INDEX_NAME, IndexName.all());

    private final ProgressIndicator progressIndicator = new ProgressIndicator(new Float(0f));

    private final Application app;

    final AdminService adminService;

    final Window mainWindow;

    public ReindexResourceViewImpl(final AdminService adminService, final Window mainWindow, final Application app) {
        Preconditions.checkNotNull(adminService, "adminService is null: %s", adminService);
        Preconditions.checkNotNull(mainWindow, "mainWindow is null: %s", mainWindow);
        Preconditions.checkNotNull(app, "app is null: %s", app);
        this.adminService = adminService;
        this.mainWindow = mainWindow;
        this.app = app;
    }

    public void init() {
        addClearIndexBox();
        addIndexNameSelection();
        addReindexButton();
        buildProgressIndicator();
        addReindexButtonListener();
    }

    private void addClearIndexBox() {
        setDefaultAsTrue();
        getViewLayout().addComponent(clearIndexBox);
    }

    private void addIndexNameSelection() {
        indexNameSelect.setNullSelectionAllowed(false);
        indexNameSelect.select(IndexName.REINDEX_ALL.asInternalName());
        getViewLayout().addComponent(indexNameSelect);
    }

    private void setDefaultAsTrue() {
        clearIndexBox.setValue(Boolean.TRUE);
    }

    private void addReindexButton() {
        reindexResourceBtn.setWidth(150, UNITS_PIXELS);
        getViewLayout().addComponent(reindexResourceBtn);
    }

    private void addReindexButtonListener() {
        reindexResourceBtn.addListener(new ReindexButtonListener(app, this, clearIndexBox, indexNameSelect,
            reindexResourceBtn, progressIndicator));
    }

    private void buildProgressIndicator() {
        progressIndicator.setImmediate(true);
        progressIndicator.setEnabled(false);
        progressIndicator.setVisible(false);
        getViewLayout().addComponent(progressIndicator);
    }
}