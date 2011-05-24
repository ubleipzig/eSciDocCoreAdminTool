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

import com.google.common.base.Preconditions;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Window;

import de.escidoc.admintool.app.PropertyId;
import de.escidoc.admintool.domain.PublicStatus;
import de.escidoc.admintool.service.internal.ResourceService;
import de.escidoc.core.client.exceptions.EscidocClientException;

public class CloseResourceListener extends AbstractUpdateable implements ClickListener {
    private static final long serialVersionUID = -8186132990430106497L;

    private final ResourceToolbar resourceToolbar;

    public CloseResourceListener(final Window mainWindow, final ResourceService orgUnitService,
        final ResourceContainer resourceContainer, final ResourceToolbar resourceToolbar) {
        super(mainWindow, orgUnitService, resourceContainer);
        Preconditions.checkNotNull(resourceToolbar, "resourceToolbar is null: %s", resourceToolbar);
        this.resourceToolbar = resourceToolbar;
    }

    @Override
    public void buttonClick(final ClickEvent event) {
        onUpdate();
        resourceToolbar.bind(getItem());
    }

    @Override
    public void updatePersistence() throws EscidocClientException {
        getOrgUnitService().close(getChildId(), "");
    }

    @Override
    public void updateItem() {
        getItem().getItemProperty(PropertyId.PUBLIC_STATUS).setValue(PublicStatus.CLOSED);
    }

    @Override
    public void updateResourceContainer() throws EscidocClientException {
        // TODO
    }

    @Override
    public void checkPostConditions() {
        // TODO
    }

}
