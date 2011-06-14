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

import com.google.common.base.Preconditions;
import com.vaadin.terminal.ExternalResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Window;

public final class LoginButtonListener implements Button.ClickListener {

    private static final long serialVersionUID = -7439896206254776195L;

    private final Window mainWindow;

    private final String loginUrl;

    public LoginButtonListener(final Window mainWindow, final String loginUrl) {
        Preconditions.checkNotNull(mainWindow, "mainWindow is null: %s", mainWindow);
        Preconditions.checkNotNull(loginUrl, "loginUrl is null: %s", loginUrl);
        this.mainWindow = mainWindow;
        this.loginUrl = loginUrl;
    }

    @Override
    public void buttonClick(final ClickEvent event) {
        redirectTo(loginUrl);
    }

    private void redirectTo(final String url) {
        mainWindow.open(new ExternalResource(url));
    }
}