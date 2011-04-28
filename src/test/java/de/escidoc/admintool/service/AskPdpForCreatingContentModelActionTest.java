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
package de.escidoc.admintool.service;

import static org.junit.Assert.assertTrue;

import java.net.URL;

import org.junit.Ignore;
import org.junit.Test;

import de.escidoc.admintool.app.AppConstants;
import de.escidoc.admintool.view.navigation.ActionIdConstants;
import de.escidoc.core.client.Authentication;

public class AskPdpForCreatingContentModelActionTest {

    private static final String LOCALHOST_8080 = "http://localhost:8080";

    private final PdpService service = new PdpServiceImpl(LOCALHOST_8080);

    @Ignore("need to adapted for CI: no running eSciDoc")
    @Test
    public void shouldReturnTrueForIsDenied() throws Exception {
        // When:
        final boolean isDenied =
            service
                .isAction(ActionIdConstants.CREATE_CONTENT_MODEL).forResource(AppConstants.EMPTY_STRING)
                .forUser(AppConstants.EMPTY_STRING).denied();
        // AssertThat:
        assertTrue(isDenied);
    }

    @Ignore("need to adapted for CI: no running eSciDoc")
    @Test
    public void shouldReturnTrueIfSysAdminAskIt() throws Exception {
        // Given:
        final Authentication authentication = new Authentication(new URL(LOCALHOST_8080), "sysadmin", "escidoc");
        final String handle = authentication.getHandle();
        service.loginWith(handle);

        // When:
        final boolean isDenied =
            service
                .isAction(ActionIdConstants.CREATE_CONTENT_MODEL).forResource(AppConstants.EMPTY_STRING)
                .forUser(AppConstants.EMPTY_STRING).denied();

        // AssertThat:
        assertTrue(isDenied);
    }
}
