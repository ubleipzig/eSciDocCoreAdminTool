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
package de.escidoc.admintool.view.user;

import com.google.common.base.Preconditions;
import com.vaadin.data.util.POJOItem;
import com.vaadin.event.ItemClickEvent;

import de.escidoc.admintool.app.AdminToolApplication;
import de.escidoc.admintool.app.PropertyId;
import de.escidoc.admintool.service.internal.UserService;
import de.escidoc.admintool.view.resource.AbstractResourceSelectListener;
import de.escidoc.admintool.view.resource.ResourceView;
import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.resources.aa.useraccount.UserAccount;

public class UserSelectListener extends AbstractResourceSelectListener {

    private static final long serialVersionUID = 7439976115422091225L;

    private final AdminToolApplication app;

    private final UserService userService;

    public UserSelectListener(final AdminToolApplication app, final UserService userService) {
        Preconditions.checkNotNull(app, "app is null: %s", app);
        Preconditions.checkNotNull(userService, "userService is null: %s", userService);
        this.app = app;
        this.userService = userService;
    }

    @Override
    public void itemClick(final ItemClickEvent event) {
        try {
            final POJOItem<UserAccount> retrievedItem = userAccountToItem(userService.retrieve(getSelectedObjectId(event)));
            final ResourceView view = getView();
            if (retrievedItem == null) {
                view.showAddView();
            }
            else {
                view.showEditView(retrievedItem);
            }
        }
        catch (final EscidocClientException e) {
            app.getMainWindow().showNotification(e.getMessage());
        }
    }

    private String getSelectedObjectId(final ItemClickEvent event) {
        return (String) event.getItem().getItemProperty(PropertyId.OBJECT_ID).getValue();
    }

    private POJOItem<UserAccount> userAccountToItem(final UserAccount userAccount) {
        return new POJOItem<UserAccount>(userAccount, new String[] { PropertyId.OBJECT_ID, PropertyId.NAME,
            PropertyId.CREATED_ON, PropertyId.CREATED_BY, PropertyId.LAST_MODIFICATION_DATE, PropertyId.MODIFIED_BY,
            PropertyId.LOGIN_NAME, PropertyId.ACTIVE });
    }

    @Override
    public ResourceView getView() {
        return app.getUserView();
    }
}