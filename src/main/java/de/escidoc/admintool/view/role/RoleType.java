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
package de.escidoc.admintool.view.role;

// TODO retrieve predefined roles from repository.
public enum RoleType {

    SYSTEM_ADMINISTRATOR("escidoc:role-system-administrator", "System Administrator", false), SYSTEM_INSPECTOR(
        "escidoc:role-system-inspector", "System Inspector", false), AUTHOR("", "Author", true), ADMINISTRATOR(
        "escidoc:role-administrator", "Administrator", true), MD_EDITOR("", "MD-Editor", true), Moderator("",
        "Moderator", true), DEPOSITOR("", "Depositor", true), INSPECTOR("", "Inspector", true), COLLABOLATOR("",
        "Collaborator", true);

    private String objectId;

    private String name;

    private boolean canBeScoped;

    RoleType(final String objectId, final String name, final boolean canBeScoped) {
        this.objectId = objectId;
        this.name = name;
        this.canBeScoped = canBeScoped;
    }

    @Override
    public String toString() {
        return name;
    }

    public boolean canBeScoped() {
        return canBeScoped;
    }

    public String getObjectId() {
        return objectId;
    }
}