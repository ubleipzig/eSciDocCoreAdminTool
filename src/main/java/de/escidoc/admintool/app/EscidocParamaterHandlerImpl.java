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

import com.google.common.base.Preconditions;
import com.vaadin.terminal.ExternalResource;
import com.vaadin.terminal.ParameterHandler;

public class EscidocParamaterHandlerImpl implements ParameterHandler {

    private static final String EMPTY_TOKEN = AppConstants.EMPTY_STRING;

    private static final long serialVersionUID = 6392830954652643671L;

    private static final Logger LOG = LoggerFactory.getLogger(EscidocParamaterHandlerImpl.class);

    private final AdminToolApplication app;

    private final ParamaterDecoder paramDecoder;

    public EscidocParamaterHandlerImpl(final AdminToolApplication app) {
        Preconditions.checkNotNull(app, "app is null: %s", app);
        this.app = app;
        paramDecoder = new ParamaterDecoder(app);
    }

    @Override
    public void handleParameters(final Map<String, String[]> parameters) {
        if (isEscidocUrlExists(parameters) && tokenExist(parameters)) {
            LOG.debug("both escidocurl and token exists");
            app.setEscidocUri(parseEscidocUriFrom(parameters));
            showMainView(parameters);
        }
        if (tokenExist(parameters)) {
            LOG.debug("only token exists");
            showMainView(parameters);
        }
        else if (isEscidocUrlExists(parameters) && tokenDoesNotExist(parameters)) {
            LOG.debug("escidocurl exists but no token");
            app.setEscidocUri(parseEscidocUriFrom(parameters));
            app.loadProtectedResources(EMPTY_TOKEN);

        }
        else if (!isEscidocUrlExists(parameters) && tokenDoesNotExist(parameters)) {
            LOG.debug("nothing");
            app.showLandingView();
        }
    }

    private boolean tokenDoesNotExist(final Map<String, String[]> parameters) {
        return !tokenExist(parameters);
    }

    private void showMainView(final Map<String, String[]> parameters) {
        app.loadProtectedResources(paramDecoder.parseAndDecodeToken(parameters));
    }

    protected void showLoginView() {
        redirectTo(app.escidocLoginUrl + app.getURL());
    }

    private void redirectTo(final String url) {
        app.getMainWindow().open(new ExternalResource(url));
    }

    private String parseEscidocUriFrom(final Map<String, String[]> parameters) {
        return parameters.get(AppConstants.ESCIDOC_URL)[0];
    }

    private boolean isEscidocUrlExists(final Map<String, String[]> parameters) {
        return parameters.containsKey(AppConstants.ESCIDOC_URL);
    }

    private boolean tokenExist(final Map<String, String[]> parameters) {
        return parameters.containsKey(AppConstants.ESCIDOC_USER_HANDLE);
    }
}