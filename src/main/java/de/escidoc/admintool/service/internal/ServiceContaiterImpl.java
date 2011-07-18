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
package de.escidoc.admintool.service.internal;

import java.util.HashMap;
import java.util.Map;

import de.escidoc.admintool.service.AdminService;
import de.escidoc.admintool.service.EscidocService;
import de.escidoc.admintool.service.ServiceContainer;

public class ServiceContaiterImpl implements ServiceContainer {

    private final Map<Class<? extends EscidocService>, EscidocService> services =
        new HashMap<Class<? extends EscidocService>, EscidocService>();

    @Override
    public void add(final EscidocService service) {
        services.put(service.getClass(), service);
    }

    @Override
    public AdminService getAdminService() {
        return (AdminService) services.get(AdminServiceImpl.class);
    }

    @Override
    public ResourceService getContainerService() {
        return (ResourceService) services.get(ContainerService.class);
    }

    @Override
    public ResourceService getItemService() {
        return (ResourceService) services.get(ItemService.class);
    }

    @Override
    public ResourceService getContextService() {
        return (ResourceService) services.get(ContextServiceLab.class);
    }

    @Override
    public ResourceService getOrgUnitService() {
        return (ResourceService) services.get(OrgUnitServiceLab.class);
    }

    @Override
    public ResourceService getContentModelService() {
        return (ResourceService) services.get(ContentModelService.class);
    }

    @Override
    public ResourceService getContentRelationService() {
        return (ResourceService) services.get(ContentRelationService.class);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("ServiceContaiterImpl [");
        if (services != null) {
            builder.append("services=").append(services).append(", ");
        }
        if (getAdminService() != null) {
            builder.append("getAdminService()=").append(getAdminService()).append(", ");
        }
        if (getContainerService() != null) {
            builder.append("getContainerService()=").append(getContainerService()).append(", ");
        }
        if (getItemService() != null) {
            builder.append("getItemService()=").append(getItemService()).append(", ");
        }
        if (getContextService() != null) {
            builder.append("getContextService()=").append(getContextService()).append(", ");
        }
        if (getOrgUnitService() != null) {
            builder.append("getOrgUnitService()=").append(getOrgUnitService()).append(", ");
        }
        if (getContentModelService() != null) {
            builder.append("getContentModelService()=").append(getContentModelService());
        }
        builder.append("]");
        return builder.toString();
    }

}