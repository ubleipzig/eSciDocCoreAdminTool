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

import de.escidoc.admintool.service.OrgUnitServiceLab;
import de.escidoc.admintool.service.ResourceService;
import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.resources.oum.OrganizationalUnit;

public class ResourceContainerFactory {

    private final OrgUnitServiceLab resourceService;

    public ResourceContainerFactory(final ResourceService resourceService) {
        Preconditions.checkNotNull(resourceService, "resourceService is null: %s", resourceService);
        if (resourceService instanceof OrgUnitServiceLab) {
            throw new RuntimeException("Not instance of OrgUnitServiceLab." + resourceService);
        }
        this.resourceService = (OrgUnitServiceLab) resourceService;
    }

    public ResourceContainer getResourceContainer() throws EscidocClientException {
        return createResourceContainer();
    }

    private ResourceContainer createResourceContainer() throws EscidocClientException {
        return new ResourceContainerImpl(getTopLevelOrgUnits());
    }

    private Collection<OrganizationalUnit> getTopLevelOrgUnits() throws EscidocClientException {
        return resourceService.getTopLevelOrgUnits();
    }
}
