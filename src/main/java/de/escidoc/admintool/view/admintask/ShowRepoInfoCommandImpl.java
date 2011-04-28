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
import java.util.Map.Entry;

import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;

final class ShowRepoInfoCommandImpl implements RepositoryInfoView.ShowRepoInfoCommand {
    private final FormLayout formLayout = new FormLayout();

    private final RepositoryInfoView repositoryInfoView;

    /**
     * @param repositoryInfoView
     */
    ShowRepoInfoCommandImpl(final RepositoryInfoView repositoryInfoView) {
        this.repositoryInfoView = repositoryInfoView;
    }

    @Override
    public void execute(final Map<String, String> repoInfos) {
        repositoryInfoView.getViewLayout().addComponent(formLayout);
        formLayout.removeAllComponents();

        for (final Entry<String, String> entry : repoInfos.entrySet()) {
            formLayout.addComponent(createReadOnlyField(entry));
        }
    }

    private TextField createReadOnlyField(final Entry<String, String> entry) {
        final TextField textField = new TextField();
        textField.setCaption(entry.getKey());
        textField.setValue(entry.getValue());
        textField.setWidth(400, RepositoryInfoView.UNITS_PIXELS);
        textField.setReadOnly(true);
        return textField;
    }
}