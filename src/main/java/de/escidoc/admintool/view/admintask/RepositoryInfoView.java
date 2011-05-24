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

import java.util.Map;

import com.vaadin.ui.Button;

import de.escidoc.admintool.view.ViewConstants;

public class RepositoryInfoView extends AbstractCustomView {
    private static final long serialVersionUID = -7206908685980457887L;

    private final Button repoInfoButton = new Button(ViewConstants.SHOW_REPOSITORY_INFO);

    private final RepoInfoClickListener listener;

    public RepositoryInfoView(final RepoInfoClickListener listener) {
        super();
        this.listener = listener;
        init();
    }

    private void init() {
        addGetRepositoryInfoButton();
    }

    private void addGetRepositoryInfoButton() {
        repoInfoButton.setWidth(150, UNITS_PIXELS);
        getViewLayout().addComponent(repoInfoButton);
        addListener();
    }

    interface ShowRepoInfoCommand {
        void execute(Map<String, String> repoInfos);
    }

    private void addListener() {
        final ShowRepoInfoCommand command = new ShowRepoInfoCommandImpl(this);
        listener.setCommand(command);
        repoInfoButton.addListener(listener);
    }
}