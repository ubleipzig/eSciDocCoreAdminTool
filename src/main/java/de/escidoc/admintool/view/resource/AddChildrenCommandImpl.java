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

import java.util.Collection;

import com.google.common.base.Preconditions;

import de.escidoc.admintool.service.internal.OrgUnitServiceLab;
import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;
import de.escidoc.core.resources.Resource;
import de.escidoc.core.resources.oum.OrganizationalUnit;

public final class AddChildrenCommandImpl implements AddChildrenCommand {
    private final OrgUnitServiceLab orgUnitService;

    private final ResourceContainer resourceContainer;

    public AddChildrenCommandImpl(final OrgUnitServiceLab orgUnitService, final ResourceContainer resourceContainer) {

        Preconditions.checkNotNull(orgUnitService, "orgUnitService is null: %s", orgUnitService);
        Preconditions.checkNotNull(resourceContainer, "resourceContainer is null: %s", resourceContainer);
        this.orgUnitService = orgUnitService;
        this.resourceContainer = resourceContainer;
    }

    @Override
    public void addChildrenFor(final Resource parent) throws EscidocClientException {
        if (parent == null) {
            throw new IllegalArgumentException("Parent can not be null.");
        }

        Collection<OrganizationalUnit> children = getChildren(parent);
        resourceContainer.addChildren(parent, children);
    }

    private Collection<OrganizationalUnit> getChildren(final Resource parent) throws EscidocException,
        InternalClientException, TransportException {
        return orgUnitService.retrieveChildren(parent.getObjid());
    }
}