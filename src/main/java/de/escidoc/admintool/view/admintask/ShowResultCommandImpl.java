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

import java.util.Collection;

import com.vaadin.ui.Label;

import de.escidoc.core.resources.adm.LoadExamplesResult.Entry;

final class ShowResultCommandImpl implements LoadExampleResourceViewImpl.ShowResultCommand {

    private final LoadExampleResourceViewImpl loadExampleResourceViewImpl;

    ShowResultCommandImpl(final LoadExampleResourceViewImpl loadExampleResourceViewImpl,
        final AddToContainer addExampleCommand) {
        this.loadExampleResourceViewImpl = loadExampleResourceViewImpl;
    }

    @Override
    public void execute(final Collection<?> entries) {
        for (final Object entry : entries) {
            if (entry instanceof Entry) {
                showLoadedExamplesResult((Entry) entry);
            }
        }
    }

    private void showLoadedExamplesResult(final Entry entry) {
        loadExampleResourceViewImpl.getViewLayout().addComponent(new Label(entry.getMessage()));
    }
}