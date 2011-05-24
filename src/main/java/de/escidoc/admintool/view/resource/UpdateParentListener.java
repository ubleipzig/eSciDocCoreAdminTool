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

import de.escidoc.admintool.service.OrgUnitServiceLab;
import de.escidoc.admintool.view.ModalDialog;
import de.escidoc.core.client.exceptions.EscidocClientException;

final class UpdateParentListener extends AbstractAddOrChangeParentListener {

    protected static final long serialVersionUID = 7022982222058387053L;

    public UpdateParentListener(final AddOrEditParentModalWindow addOrEditParentModalWindow,
        final OrgUnitServiceLab orgUnitService) {

        Preconditions.checkNotNull(addOrEditParentModalWindow, "modalWindow is null: %s", addOrEditParentModalWindow);
        Preconditions.checkNotNull(orgUnitService, "orgUnitService is null: %s", orgUnitService);

        super.addOrEditParentModalWindow = addOrEditParentModalWindow;
        super.orgUnitService = orgUnitService;
        super.resourceContainer = addOrEditParentModalWindow.resourceContainer;
    }

    @Override
    protected void addOrUpdateParent() {
        updateParent();
    }

    private void updateParent() {
        try {
            selectedOrgUnit = getChild();
            selectedParent = getSelectedParent();
            updatePersistence();
            updateResourceContainer();
            updateItem();
            checkPostConditions(selectedOrgUnit);
        }
        catch (final EscidocClientException e) {
            ModalDialog.show(getMainWindow(), e);
        }
    }
}