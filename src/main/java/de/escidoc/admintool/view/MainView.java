package de.escidoc.admintool.view;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.SplitPanel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

import de.escidoc.admintool.app.AdminToolApplication;
import de.escidoc.admintool.view.factory.ToolbarFactory;
import de.escidoc.admintool.view.navigation.NavigationTree;
import de.escidoc.admintool.view.navigation.NavigationTreeImpl;

public class MainView extends CustomComponent {
    private static final long serialVersionUID = -5906063682647356346L;

    private final VerticalLayout appLayout = new VerticalLayout();

    private final SplitPanel horizontalSplit = new SplitPanel(
        SplitPanel.ORIENTATION_HORIZONTAL);

    private final Button logoutButton = new Button(ViewConstants.LOGOUT);

    private final ToolbarFactory factory = new ToolbarFactory();

    private final AdminToolApplication app;

    private NavigationTree navigation;

    private GridLayout toolbar;

    public MainView(final AdminToolApplication app) {
        this.app = app;
        init();
    }

    private void init() {
        setCompositionRoot(appLayout);
        makeFullSize();
        addToolbar();
        // createAndAddNavigationList();
        createAndAddNavigationTree();
    }

    private void makeFullSize() {
        setSizeFull();
        appLayout.setSizeFull();
    }

    private void addToolbar() {
        createLogOutButton();
        if (appLayout.getComponentIndex(toolbar) < 0) {
            toolbar = factory.createToolbar(new Button[] { logoutButton });
            appLayout.addComponent(toolbar);
        }
    }

    private void createLogOutButton() {
        logoutButton.setEnabled(true);
        logoutButton.setStyleName(Reindeer.BUTTON_SMALL);
        app.setLogoutURL(app.escidocLogoutUrl + app.getURL());
        logoutButton.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(final ClickEvent event) {
                app.close();
            }
        });
    }

    private boolean isLoggedIn() {
        return !(app.escidocLogoutUrl == null || app.escidocLogoutUrl.isEmpty());
    }

    private void createAndAddNavigationTree() {
        appLayout.addComponent(horizontalSplit);
        appLayout.setExpandRatio(horizontalSplit, 1);
        horizontalSplit.setSplitPosition(
            ViewConstants.SPLIT_POSITION_FROM_LEFT, SplitPanel.UNITS_PIXELS);
        horizontalSplit.addStyleName("small blue white");

        navigation = new NavigationTreeImpl(app);
        horizontalSplit.setFirstComponent(navigation);
    }

    public void setSecondComponent(final Component component) {
        horizontalSplit.setSecondComponent(component);
    }
}