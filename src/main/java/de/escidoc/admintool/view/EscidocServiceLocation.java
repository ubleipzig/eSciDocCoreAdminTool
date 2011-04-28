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
package de.escidoc.admintool.view;

import java.net.URL;

import com.google.common.base.Preconditions;

import de.escidoc.admintool.app.AppConstants;

public class EscidocServiceLocation {

    private final String escidocUri;

    private URL appUri;

    public EscidocServiceLocation(final String eSciDocUri) {
        Preconditions.checkNotNull(eSciDocUri, "eSciDocUri is null");
        escidocUri = eSciDocUri;
    }

    public EscidocServiceLocation(final String escidocUri, final URL appUri) {
        Preconditions.checkNotNull(escidocUri, "eSciDocUri is null: %s", escidocUri);
        Preconditions.checkNotNull(appUri, "appUri is null: %s", appUri);
        this.escidocUri = escidocUri;
        this.appUri = appUri;
    }

    public String getUri() {
        return escidocUri;
    }

    public String getLoginUri() {
        return escidocUri + AppConstants.LOGIN_TARGET + appUri;
    }

    public String getLogoutUri() {
        return escidocUri + AppConstants.LOGOUT_TARGET;
    }
}