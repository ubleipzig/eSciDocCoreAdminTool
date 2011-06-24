/**
 * CDDL HEADER START
 * 
 * The contents of this file are subject to the terms of the Common Development
 * and Distribution License, Version 1.0 only (the "License"). You may not use
 * this file except in compliance with the License.
 * 
 * You can obtain a copy of the license at license/ESCIDOC.LICENSE or
 * https://www.escidoc.org/license/ESCIDOC.LICENSE . See the License for the
 * specific language governing permissions and limitations under the License.
 * 
 * When distributing Covered Code, include this CDDL HEADER in each file and
 * include the License file at license/ESCIDOC.LICENSE. If applicable, add the
 * following below this CDDL HEADER, with the fields enclosed by brackets "[]"
 * replaced with your own identifying information: Portions Copyright [yyyy]
 * [name of copyright owner]
 * 
 * CDDL HEADER END
 * 
 * 
 * 
 * Copyright 2011 Fachinformationszentrum Karlsruhe Gesellschaft fuer
 * wissenschaftlich-technische Information mbH and Max-Planck- Gesellschaft zur
 * Foerderung der Wissenschaft e.V. All rights reserved. Use is subject to
 * license terms.
 */
package de.escidoc.admintool.view.admintask.loadexamples;

import com.google.common.base.Preconditions;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Window;

import de.escidoc.admintool.service.AdminService;
import de.escidoc.admintool.view.ModalDialog;
import de.escidoc.admintool.view.admintask.loadexamples.LoadExampleResourceViewImpl.ShowResultCommand;
import de.escidoc.core.client.exceptions.EscidocClientException;

@SuppressWarnings("serial")
public class OnLoadExampleClick implements ClickListener {

    private final Button loadExampleButton;

    private final ShowResultCommand showResultCommand;

    private AdminService adminService;

    private Window mainWindow;

    public OnLoadExampleClick(Button loadExampleButton, final ShowResultCommand command) {
        Preconditions.checkNotNull(loadExampleButton, "loadExampleButton is null: %s", loadExampleButton);
        Preconditions.checkNotNull(command, "command is null: %s", command);
        this.loadExampleButton = loadExampleButton;
        this.showResultCommand = command;
    }

    @Override
    public void buttonClick(final ClickEvent event) {
        try {
            showResultCommand.execute(adminService.loadCommonExamples());
        }
        catch (final EscidocClientException e) {
            ModalDialog.show(mainWindow, e);
        }
    }

    public void setAdminService(final AdminService adminService) {
        this.adminService = adminService;
    }

    public void setMainWindow(final Window mainWindow) {
        this.mainWindow = mainWindow;
    }
}