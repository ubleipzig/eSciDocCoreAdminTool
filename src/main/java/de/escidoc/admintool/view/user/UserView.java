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
import com.vaadin.data.Item;
import com.vaadin.ui.Label;
import com.vaadin.ui.SplitPanel;
import com.vaadin.ui.VerticalLayout;

import de.escidoc.admintool.app.AdminToolApplication;
import de.escidoc.admintool.view.ViewConstants;
import de.escidoc.admintool.view.resource.ResourceView;
import de.escidoc.core.resources.Resource;
import de.escidoc.core.resources.aa.useraccount.UserAccount;

@SuppressWarnings("serial")
public class UserView extends SplitPanel implements ResourceView {

    private final AdminToolApplication app;

    private final UserListView userList;

    private final UserEditView userEditView;

    private final VerticalLayout vLayout = new VerticalLayout();

    public UserView(final AdminToolApplication app, final UserListView userListView, final UserEditView userEditView) {
        Preconditions.checkNotNull(app, "app is null: %s", app);
        Preconditions.checkNotNull(userListView, "userListView is null: %s", userListView);
        Preconditions.checkNotNull(userEditView, "userLabEditView is null: %s", userEditView);
        this.app = app;
        this.userList = userListView;
        this.userEditView = userEditView;
    }

    public void init() {
        setSplitPosition(ViewConstants.SPLIT_POSITION_IN_PERCENT);
        setOrientation(ORIENTATION_HORIZONTAL);

        vLayout.setHeight(100, UNITS_PERCENTAGE);
        addHeader(vLayout);
        addListView(vLayout);
        setFirstComponent(vLayout);
    }

    private void addHeader(final VerticalLayout vLayout) {
        vLayout.addComponent(new Label("<b>User Accounts</b>", Label.CONTENT_XHTML));
    }

    private void addListView(final VerticalLayout vLayout) {
        userList.setSizeFull();
        vLayout.addComponent(userList);
        vLayout.setExpandRatio(userList, 1.0f);
    }

    public UserListView getUserList() {
        return userList;
    }

    public Item getSelectedItem() {
        return userList.getItem(userList.getValue());
    }

    @Override
    public void showAddView() {
        setSecondComponent(app.newUserAddView());
    }

    public Item toItem(final UserAccount user) {
        return userList.getContainerDataSource().getItem(user);
    }

    @Override
    public void showEditView(final Item item) {
        setSecondComponent(userEditView);
        userEditView.setSelected(item);
    }

    public void remove(final UserAccount deletedUser) {
        userList.remove(deletedUser);
        showAddView();
    }

    @Override
    public void selectInFolderView(final Resource resource) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}