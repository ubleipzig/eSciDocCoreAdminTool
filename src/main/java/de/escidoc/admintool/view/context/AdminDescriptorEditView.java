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
package de.escidoc.admintool.view.context;

import com.vaadin.terminal.UserError;
import com.vaadin.ui.Accordion;
import com.vaadin.ui.Label;
import com.vaadin.ui.TabSheet.Tab;
import com.vaadin.ui.Window;

@SuppressWarnings("serial")
public class AdminDescriptorEditView extends AdminDescriptorView {

    public AdminDescriptorEditView(final Window mainWindow, final Accordion adminDescriptorAccordion,
        final String name, final String content) {
        super(mainWindow, adminDescriptorAccordion, name, content);
    }

    @Override
    protected void setWindowCaption() {
        setCaption(EDIT_ADMIN_DESCRIPTOR);
    }

    @Override
    protected void doSave() {
        final String adminDescriptorName = (String) adminDescNameField.getValue();
        if (isValid(adminDescriptorName)) {
            adminDescNameField.setComponentError(null);
            final String adminDescriptorContent = (String) adminDescContent.getValue();
            if (validate(adminDescriptorContent)) {
                getTabTitle().setCaption((String) adminDescNameField.getValue());
                adminDescriptorAccordion.replaceComponent(adminDescriptorAccordion.getSelectedTab(), new Label(content,
                    Label.CONTENT_PREFORMATTED));
                closeWindow();
            }
        }
        else {
            adminDescNameField.setComponentError(new UserError("Must not contain space"));
        }

    }

    private Tab getTabTitle() {
        return adminDescriptorAccordion.getTab(adminDescriptorAccordion.getSelectedTab());
    }
}