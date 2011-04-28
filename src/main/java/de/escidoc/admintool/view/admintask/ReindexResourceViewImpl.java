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
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Label;
import com.vaadin.ui.Window;

import de.escidoc.admintool.service.AdminService;
import de.escidoc.admintool.view.ViewConstants;
import de.escidoc.core.resources.adm.MessagesStatus;

public class ReindexResourceViewImpl extends AbstractCustomView implements ReindexResourceView {

    private static final long serialVersionUID = 2997054515640202370L;

    private final Button reindexResourceBtn = new Button(ViewConstants.REINDEX);

    private final ClickListener showStatusListener = new ShowReindexingStatusListener(this);

    private final Button showStatusButton = new Button(ViewConstants.SHOW_STATUS);

    private final CheckBox clearIndexBox = new CheckBox(ViewConstants.CLEAR_INDEX);

    private final ComboBox indexNameSelect = new ComboBox(ViewConstants.INDEX_NAME, ViewConstants.INDEX_NAMES);

    private final ReindexButtonListener listener = new ReindexButtonListener(this, clearIndexBox, indexNameSelect);

    final Label statusLabel = new Label(ViewConstants.STATUS);

    final AdminService adminService;

    final Window mainWindow;

    private ShowStatusCommand showStatusCommand;

    public ReindexResourceViewImpl(final AdminService adminService, final Window mainWindow) {
        preconditions(adminService, mainWindow);
        this.adminService = adminService;
        this.mainWindow = mainWindow;
        init();
    }

    private void preconditions(final AdminService adminService, final Window mainWindow) {
        Preconditions.checkNotNull(adminService, "adminService can not be null: %s", adminService);
        Preconditions.checkNotNull(adminService, "mainWindow can not be null: %s", mainWindow);
    }

    private void init() {
        createShowStatusCommand();
        addClearIndexBox();
        addIndexNameSelection();
        addReindexButton();
        addListener();
    }

    private void createShowStatusCommand() {
        showStatusCommand = new ShowStatusCommand() {

            @Override
            public void execute(final MessagesStatus status) {
                statusLabel.setValue(status.getStatusMessage());
            }
        };
    }

    private void addClearIndexBox() {
        setDefaultAsTrue();
        getViewLayout().addComponent(clearIndexBox);
    }

    // TODO refactor index name to ENUM
    private void addIndexNameSelection() {
        indexNameSelect.setNullSelectionAllowed(false);
        indexNameSelect.select(ViewConstants.REINDEX_ALL);
        getViewLayout().addComponent(indexNameSelect);
    }

    private void setDefaultAsTrue() {
        clearIndexBox.setValue(Boolean.TRUE);
    }

    private void addReindexButton() {
        reindexResourceBtn.setWidth(150, UNITS_PIXELS);
        getViewLayout().addComponent(reindexResourceBtn);
    }

    private void addListener() {
        reindexResourceBtn.addListener(listener);
    }
}