package de.escidoc.admintool.view;

import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

import de.escidoc.admintool.app.AdminToolApplication;
import de.escidoc.admintool.app.AppConstants;
import de.escidoc.admintool.domain.PdpRequest;
import de.escidoc.admintool.view.factory.ToolbarFactory;
import de.escidoc.admintool.view.navigation.NavigationTree;
import de.escidoc.admintool.view.navigation.NavigationTreeClickListener;
import de.escidoc.core.resources.aa.useraccount.UserAccount;

public class MainView extends CustomComponent {

    private static final long serialVersionUID = -5906063682647356346L;

    private final VerticalLayout appLayout = new VerticalLayout();

    private final HorizontalSplitPanel horizontalSplit =
        new HorizontalSplitPanel();

    private final Button logoutButton = new Button(ViewConstants.LOGOUT);

    private final ToolbarFactory factory = new ToolbarFactory();

    final AdminToolApplication app;

    private final PdpRequest pdpRequest;

    private final UserAccount currentUser;

    private NavigationTree navigation;

    private GridLayout toolbar;

    private Button loginButton;

    public MainView(final AdminToolApplication app,
        final PdpRequest pdpRequest, final UserAccount currentUser) {
        this.app = app;
        this.pdpRequest = pdpRequest;
        this.currentUser = currentUser;
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
        return !currentUser.getObjid().equals(AppConstants.EMPTY_STRING);
    }

    private void show(final Button button) {
        toolbar = factory.createToolbar(new Button[] { button });
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
            new Button(ViewConstants.LOGIN_LABEL, new LoginButtonListener(
                app.getMainWindow(), app.escidocLoginUrl + app.getURL()));
        loginButton.setStyleName(Reindeer.BUTTON_SMALL);
    }

    private void createLogOutButton() {
        logoutButton.setStyleName(Reindeer.BUTTON_SMALL);
        final String logoutURL = app.escidocLogoutUrl + app.getURL();
        app.setLogoutURL(logoutURL);
        logoutButton.addListener(new Button.ClickListener() {
            private static final long serialVersionUID = 6434716782391206321L;

            @Override
            public void buttonClick(final ClickEvent event) {
                app.close();
            }
        });
    }

    private void createAndAddNavigationTree() {
        configureHorizontalSplit();
        createNavigationTree();
        horizontalSplit.setFirstComponent(navigation);
    }

    private void createNavigationTree() {
        navigation =
            NavigationTreeFactory.createViewFor(
                new NavigationTreeClickListener(app), pdpRequest);
    }

    private void configureHorizontalSplit() {
        appLayout.addComponent(horizontalSplit);
        appLayout.setExpandRatio(horizontalSplit, 1);
        horizontalSplit.setSplitPosition(
            ViewConstants.SPLIT_POSITION_FROM_LEFT, Sizeable.UNITS_PIXELS);
        horizontalSplit.addStyleName(ViewConstants.THIN_SPLIT);
    }

    public void setSecondComponent(final Component component) {
        horizontalSplit.setSecondComponent(component);
    }

}