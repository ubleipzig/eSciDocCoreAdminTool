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

import java.util.HashMap;
import java.util.Map;

public enum ResourceType {

    ITEM("Item"), CONTAINER("Container"), CONTEXT("Context"), ORGANIZATIONAL_UNIT("Organizational Unit"), CONTENT_MODEL(
        "Content Model"), COMPONENT("Component"), CONTENT_RELATION("Content Relation"), USERACCOUNT("User Account"), USERGROUP(
        "User Group");

    @SuppressWarnings("serial")
    private static final Map<de.escidoc.core.resources.ResourceType, ResourceType> map =
        new HashMap<de.escidoc.core.resources.ResourceType, ResourceType>() {
            {
                put(de.escidoc.core.resources.ResourceType.ITEM, ITEM);
                put(de.escidoc.core.resources.ResourceType.CONTAINER, CONTAINER);
                put(de.escidoc.core.resources.ResourceType.CONTEXT, CONTEXT);
                put(de.escidoc.core.resources.ResourceType.ORGANIZATIONAL_UNIT, ORGANIZATIONAL_UNIT);
                put(de.escidoc.core.resources.ResourceType.CONTENT_MODEL, CONTENT_MODEL);
                put(de.escidoc.core.resources.ResourceType.COMPONENT, COMPONENT);
                put(de.escidoc.core.resources.ResourceType.CONTENT_RELATION, CONTENT_RELATION);
                put(de.escidoc.core.resources.ResourceType.USERACCOUNT, USERACCOUNT);
                put(de.escidoc.core.resources.ResourceType.USERGROUP, USERGROUP);
            }
        };

    private String label;

    ResourceType(final String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    @Override
    public String toString() {
        return label;
    }

    public static ResourceType convert(final de.escidoc.core.resources.ResourceType relationAttributeObjectType) {
        return map.get(relationAttributeObjectType);
    }
}
