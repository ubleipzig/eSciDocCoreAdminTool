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

public class MainView extends CustomComponent {

    private static final long serialVersionUID = -5906063682647356346L;

    private final VerticalLayout appLayout = new VerticalLayout();

    private final HorizontalSplitPanel horizontalSplit =
        new HorizontalSplitPanel();

    private final Button logoutButton = new Button(ViewConstants.LOGOUT);

    private final ToolbarFactory factory = new ToolbarFactory();

    private final AdminToolApplication app;

    private final PdpRequest pdpRequest;

    private final UserAccount currentUser;

    private NavigationTree navigation;

    private GridLayout toolbar;

    private Button loginButton;

    private final EscidocServiceLocation location;

    public MainView(final AdminToolApplication app,
        final PdpRequest pdpRequest, final UserAccount currentUser,
        final EscidocServiceLocation location) {
        Preconditions.checkNotNull(app, "app is null: %s", app);
        Preconditions.checkNotNull(pdpRequest, "pdpRequest is null: %s",
            pdpRequest);
        Preconditions.checkNotNull(currentUser, "currentUser is null: %s",
            currentUser);
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
        return !currentUser.getObjid().equals(AppConstants.EMPTY_STRING);
    }

    private void show(final Button logOutButton) {
        final HorizontalLayout layout = new HorizontalLayout();
        layout.setSpacing(true);
        layout.setMargin(true);
        layout.addComponent(new Label("<b>User: "
            + currentUser.getProperties().getLoginName() + "</b>",
            Label.CONTENT_XHTML));
        layout.addComponent(new Label("|", Label.CONTENT_XHTML));
        layout.addComponent(logOutButton);
        toolbar = factory.createToolbar(layout);
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
                app.getMainWindow(), location.getLoginUri()));
        loginButton.setStyleName(Reindeer.BUTTON_SMALL);
    }

    private void createLogOutButton() {
        logoutButton.setStyleName(Reindeer.BUTTON_SMALL);
        app.setLogoutURL(location.getLogoutUri() + app.getURL());
        final LogoutButtonListener logoutButtonListener =
            new LogoutButtonListener(app);
        logoutButton.addListener(logoutButtonListener);
    }

    private void createAndAddNavigationTree() {
        configureHorizontalSplit();
        createNavigationTree();
        horizontalSplit.setFirstComponent(navigation);
    }

    private void createNavigationTree() {
        final NavigationTreeClickListener treeClickListener =
            new NavigationTreeClickListener(app);
        navigation =
            NavigationTreeFactory.createViewFor(treeClickListener, pdpRequest);
        final ExpandCollapseCommand command =
            new ExpandCollapseCommandImpl(navigation);
        treeClickListener.setCommand(command);
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

    public NavigationTree getNavigationTree() {
        return navigation;
    }
}