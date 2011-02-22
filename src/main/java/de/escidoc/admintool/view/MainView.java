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
import de.escidoc.admintool.domain.PdpRequest;
import de.escidoc.admintool.view.factory.ToolbarFactory;
import de.escidoc.admintool.view.navigation.NavigationTree;
import de.escidoc.admintool.view.navigation.NavigationTreeClickListener;

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

    private final PdpRequest pdpRequest;

    public MainView(final AdminToolApplication app, final PdpRequest pdpRequest) {
        this.app = app;
        this.pdpRequest = pdpRequest;
        init();
    }

    private void init() {
        setCompositionRoot(appLayout);
        makeFullSize();
        addToolbar();
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
            private static final long serialVersionUID = 6434716782391206321L;

            @Override
            public void buttonClick(final ClickEvent event) {
                app.close();
            }
        });
    }

    private void createAndAddNavigationTree() {
        appLayout.addComponent(horizontalSplit);
        appLayout.setExpandRatio(horizontalSplit, 1);

        horizontalSplit.setSplitPosition(
            ViewConstants.SPLIT_POSITION_FROM_LEFT, SplitPanel.UNITS_PIXELS);
        horizontalSplit.addStyleName(ViewConstants.THIN_SPLIT);

        navigation =
            NavigationTreeFactory.createViewFor(
                new NavigationTreeClickListener(app), pdpRequest);
        horizontalSplit.setFirstComponent(navigation);
    }

    public void setSecondComponent(final Component component) {
        horizontalSplit.setSecondComponent(component);
    }

}