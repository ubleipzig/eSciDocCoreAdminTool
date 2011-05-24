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
/**
 * 
 */
package de.escidoc.admintool.view.context.listener;

import com.google.common.base.Preconditions;
import com.vaadin.ui.Accordion;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.TabSheet.Tab;
import com.vaadin.ui.Window;

import de.escidoc.admintool.view.context.AdminDescriptorEditView;

/**
 * @author ASP
 * 
 */
public class EditAdminDescriptorListener implements Button.ClickListener {
    private static final long serialVersionUID = 2603421967112465661L;

    private final Accordion adminDescriptorAccordion;

    private final Window mainWindow;

    public EditAdminDescriptorListener(final Window mainWindow, final Accordion adminDescriptorAccordion) {
        Preconditions.checkNotNull(mainWindow, "mainWindow can not be null: %s", mainWindow);
        Preconditions.checkNotNull(adminDescriptorAccordion, "adminDescriptorAccordion can not be null: %s",
            adminDescriptorAccordion);

        this.mainWindow = mainWindow;
        this.adminDescriptorAccordion = adminDescriptorAccordion;
    }

    public void buttonClick(final ClickEvent event) {
        final Component selectedTab = adminDescriptorAccordion.getSelectedTab();
        if (selectedTab == null) {
            return;
        }

        mainWindow.addWindow(new AdminDescriptorEditView(mainWindow, adminDescriptorAccordion,
            getName(adminDescriptorAccordion.getTab(selectedTab)), getContent(selectedTab)));
    }

    private String getName(final Tab tab) {
        String name = "";
        if (tab != null) {
            name = tab.getCaption();
        }
        return name;
    }

    private String getContent(final Component selectedTab) {
        final String content = (String) ((Label) selectedTab).getValue();
        return content;
    }
}
