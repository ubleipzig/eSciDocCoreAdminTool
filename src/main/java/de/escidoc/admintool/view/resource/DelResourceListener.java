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

import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Window;

import de.escidoc.admintool.app.AdminToolApplication;
import de.escidoc.admintool.service.internal.ResourceService;
import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.resources.Resource;

public class DelResourceListener extends AbstractUpdateable implements ClickListener {

    private final AdminToolApplication app;

    public DelResourceListener(final AdminToolApplication app, final Window mainWindow,
        final ResourceService resourceService, final ResourceContainer resourceContainer) {
        super(mainWindow, resourceService, resourceContainer);
        this.app = app;
    }

    private static final long serialVersionUID = 63356969287971297L;

    private Resource orgUnit;

    @Override
    public void buttonClick(final ClickEvent event) {
        onUpdate();
        app.showResourceView();
    }

    @Override
    public void updatePersistence() throws EscidocClientException {
        orgUnit = getOrgUnit();
        getOrgUnitService().delete(getChildId());
    }

    @Override
    public void updateResourceContainer() throws EscidocClientException {
        getResourceContainer().remove(orgUnit);
    }

    @Override
    public void updateItem() {
        // Do Nothing
    }

    @Override
    public void checkPostConditions() {
        // Do Nothing
    }
}