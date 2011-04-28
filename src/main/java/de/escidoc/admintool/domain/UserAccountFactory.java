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

import de.escidoc.core.resources.aa.useraccount.UserAccount;
import de.escidoc.core.resources.aa.useraccount.UserAccountProperties;

public class UserAccountFactory {
    private UserAccount userAccount;

    public UserAccountFactory update(final UserAccount userAccount) {
        this.userAccount = userAccount;
        return this;
    }

    public UserAccountFactory name(final String name) {
        final UserAccountProperties properties = userAccount.getProperties();
        properties.setName(name);

        userAccount.setProperties(properties);
        return this;
    }

    public UserAccount build() {
        return userAccount;
    }

    public UserAccountFactory create(final String name, final String loginName) {
        assert name != null : "name must not be null";
        assert loginName != null : "loginName must not be null";

        userAccount = new UserAccount();

        final UserAccountProperties properties = new UserAccountProperties();
        properties.setName(name);
        properties.setLoginName(loginName);
        userAccount.setProperties(properties);

        return this;
    }
}