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
package de.escidoc.admintool.view.navigation;

import com.google.common.base.Preconditions;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;

import de.escidoc.admintool.app.AdminToolApplication;
import de.escidoc.admintool.view.ViewConstants;

public class NavigationTreeClickListener implements ItemClickListener {

    private static final long serialVersionUID = 3387642828574003867L;

    private final AdminToolApplication app;

    private ExpandCollapseCommand command;

    public NavigationTreeClickListener(final AdminToolApplication app) {
        Preconditions.checkNotNull(app, "app is null: %s", app);
        this.app = app;
    }

    public void setCommand(final ExpandCollapseCommand command) {
        Preconditions.checkNotNull(command, "command is null: %s", command);
        this.command = command;
    }

    @Override
    public void itemClick(final ItemClickEvent event) {
        Preconditions.checkNotNull(event, "event is null: %s", event);
        showClickedView(event.getItemId());
    }

    private void showClickedView(final Object itemId) {
        if (isNullAndNotString(itemId)) {
            return;
        }
        else if (ViewConstants.CONTEXTS.equals(itemId)) {
            app.showContextView();
        }
        else if (ViewConstants.ORG_UNITS.equals(itemId)) {
            app.showResourceView();
        }
        else if (ViewConstants.CONTENT_MODELS.equals(itemId)) {
            app.showContentModelView();
        }
        else if (ViewConstants.USERS.equals(itemId)) {
            app.showUserView();
        }
        else if (ViewConstants.ROLE.equals(itemId)) {
            app.showRoleView();
        }
        else if (ViewConstants.LOAD_EXAMPLES_TITLE.equals(itemId)) {
            app.showLoadExampleView();
        }
        else if (ViewConstants.REINDEX.equals(itemId)) {
            app.showReindexView();
        }
        else if (ViewConstants.SHOW_REPOSITORY_INFO.equals(itemId)) {
            app.showRepoInfoView();
        }
        else if (ViewConstants.FILTERING_RESOURCES_TITLE.equals(itemId)) {
            app.showFilterResourceView();
        }
        else if (ViewConstants.RESOURCES.equals(itemId) || ViewConstants.ADMIN_TASKS_LABEL.equals(itemId)) {
            command.execute(itemId);
        }
        else {
            throw new IllegalArgumentException("Unknown type.");
        }
    }

    private boolean isNullAndNotString(final Object itemId) {
        return (itemId == null) || !(itemId instanceof String);
    }
}