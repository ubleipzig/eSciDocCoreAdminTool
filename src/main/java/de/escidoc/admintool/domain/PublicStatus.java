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
package de.escidoc.admintool.domain;

/**
 * @author ASP
 * 
 */
public enum PublicStatus {

    CREATED("Created"), OPENED("Opened"), CLOSED("Closed");

    private final String name;

    PublicStatus(final String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    public static PublicStatus from(final de.escidoc.core.resources.common.properties.PublicStatus fromCore) {
        return PublicStatus.valueOf(fromCore.toString());
    }

    public static final de.escidoc.core.resources.common.properties.PublicStatus to(final PublicStatus publicStatus) {
        return de.escidoc.core.resources.common.properties.PublicStatus.valueOf(publicStatus.name().toUpperCase());

    }
}
