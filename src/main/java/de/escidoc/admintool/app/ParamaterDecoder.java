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
package de.escidoc.admintool.app;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import biz.source_code.base64Coder.Base64Coder;

import com.google.common.base.Preconditions;
import com.vaadin.ui.Window.Notification;

import de.escidoc.admintool.view.ViewConstants;

public class ParamaterDecoder {

    private static final Logger LOG = LoggerFactory.getLogger(ParamaterDecoder.class);

    public ParamaterDecoder(final AdminToolApplication app) {
        Preconditions.checkNotNull(app, "app is null: %s", app);

        this.app = app;
    }

    private final AdminToolApplication app;

    public String parseAndDecodeToken(final Map<String, String[]> parameters) {
        return tryToDecode(parameters.get(AppConstants.ESCIDOC_USER_HANDLE)[0]);
    }

    private String tryToDecode(final String parameter) {
        try {
            return Base64Coder.decodeString(parameter);
        }
        catch (final IllegalArgumentException e) {
            Preconditions.checkNotNull(app.getMainWindow(), "MainWindow is null: %s", app.getMainWindow());
            LOG.error(ViewConstants.GENERAL_ERROR_MESSAGE, e);
            app.showLandingView();
            app.getMainWindow().showNotification(
                new Notification(ViewConstants.WRONG_TOKEN_MESSAGE, "Wrong token", Notification.TYPE_ERROR_MESSAGE));
        }
        return AppConstants.EMPTY_STRING;
    }
}
