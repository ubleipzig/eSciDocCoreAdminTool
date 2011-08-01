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
import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

import de.escidoc.admintool.app.AdminToolApplication;
import de.escidoc.admintool.app.AppConstants;
import de.escidoc.admintool.domain.PdpRequest;
import de.escidoc.admintool.view.factory.ToolbarFactory;
import de.escidoc.admintool.view.navigation.ExpandCollapseCommand;
import de.escidoc.admintool.view.navigation.ExpandCollapseCommandImpl;
import de.escidoc.admintool.view.navigation.NavigationTree;
import de.escidoc.admintool.view.navigation.NavigationTreeClickListener;
import de.escidoc.core.resources.aa.useraccount.UserAccount;

@SuppressWarnings("serial")
public class MainView extends CustomComponent {

    private final VerticalLayout appLayout = new VerticalLayout();

    private final HorizontalSplitPanel horizontalSplit = new HorizontalSplitPanel();

    private final Button logoutButton = new Button(ViewConstants.LOGOUT);

    private final ToolbarFactory factory = new ToolbarFactory();

    private final AdminToolApplication app;

    private final PdpRequest pdpRequest;

    private final UserAccount currentUser;

    private NavigationTree navigation;

    private GridLayout toolbar;

    private Button loginButton;

    private final EscidocServiceLocation location;

    public MainView(final AdminToolApplication app, final PdpRequest pdpRequest, final UserAccount currentUser,
        final EscidocServiceLocation location) {
        Preconditions.checkNotNull(app, "app is null: %s", app);
        Preconditions.checkNotNull(pdpRequest, "pdpRequest is null: %s", pdpRequest);
        Preconditions.checkNotNull(currentUser, "currentUser is null: %s", currentUser);
        Preconditions.checkNotNull(location, "location is null: %s", location);
        this.app = app;
        this.pdpRequest = pdpRequest;
        this.currentUser = currentUser;
        this.location = location;
    }

    public void init() {
        setCompositionRoot(appLayout);
        makeFullSize();
        createButtons();
        addToolbar();
        createAndAddNavigationTree();
    }

    private void addToolbar() {
        if (addToolbarIsNeeded()) {
            addButtonToToolbar();
            appLayout.addComponent(toolbar);
        }
    }

    private boolean addToolbarIsNeeded() {
        return appLayout.getComponentIndex(toolbar) < 0;
    }

    private void addButtonToToolbar() {
        if (isUserLoggedIn()) {
            show(logoutButton);
        }
        else {
            show(loginButton);
        }
    }

    private boolean isUserLoggedIn() {
        return currentUser.getObjid() != null;
    }

    private void show(final Button button) {
        final VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setSizeUndefined();
        final Label box = new Label("<b>Version 1.1.1-RC2-SNAPSHOT</b>", Label.CONTENT_XHTML);

        verticalLayout.addComponent(box);
        verticalLayout.setSpacing(true);
        final HorizontalLayout layout = new HorizontalLayout();
        layout.setSpacing(true);
        layout.setMargin(true);
        layout.addComponent(new Label("<b>User: " + currentUser.getProperties().getLoginName() + "</b>",
            Label.CONTENT_XHTML));
        layout.addComponent(new Label("|", Label.CONTENT_XHTML));
        layout.addComponent(button);

        verticalLayout.addComponent(layout);
        toolbar = factory.createToolbar(verticalLayout);
    }

    private void makeFullSize() {
        setSizeFull();
        appLayout.setSizeFull();
    }

    private void createButtons() {
        createLogInButton();
        createLogOutButton();
    }

    private void createLogInButton() {
        loginButton =
            new Button(ViewConstants.LOGIN_LABEL, new LoginButtonListener(app.getMainWindow(), location.getLoginUri()));
        loginButton.setStyleName(Reindeer.BUTTON_SMALL);
    }

    private void createLogOutButton() {
        logoutButton.setStyleName(Reindeer.BUTTON_SMALL);
        app.setLogoutURL(buildLogOutUrl());
        final LogoutButtonListener logoutButtonListener = new LogoutButtonListener(app);
        logoutButton.addListener(logoutButtonListener);
    }

    private String buildLogOutUrl() {
        final StringBuilder builder = new StringBuilder();
        builder.append(location.getLogoutUri());
        builder.append(app.getURL());
        builder.append(AppConstants.ESCIDOC_URL_PARAMETER);
        builder.append(location.getUri());
        return builder.toString();
    }

    private void createAndAddNavigationTree() {
        configureHorizontalSplit();
        createNavigationTree();
        horizontalSplit.setFirstComponent(navigation);
    }

    private void createNavigationTree() {
        final NavigationTreeClickListener treeClickListener = new NavigationTreeClickListener(app);
        navigation = NavigationTreeFactory.createViewFor(treeClickListener, pdpRequest);
        final ExpandCollapseCommand command = new ExpandCollapseCommandImpl(navigation);
        treeClickListener.setCommand(command);
    }

    private void configureHorizontalSplit() {
        appLayout.addComponent(horizontalSplit);
        appLayout.setExpandRatio(horizontalSplit, 1);
        horizontalSplit.setSplitPosition(ViewConstants.SPLIT_POSITION_FROM_LEFT, Sizeable.UNITS_PIXELS);
        horizontalSplit.addStyleName(ViewConstants.THIN_SPLIT);
    }

    public void setSecondComponent(final Component component) {
        horizontalSplit.setSecondComponent(component);
    }

    public NavigationTree getNavigationTree() {
        return navigation;
    }
}