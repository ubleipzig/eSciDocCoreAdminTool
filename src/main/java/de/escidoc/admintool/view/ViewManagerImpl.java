package de.escidoc.admintool.view;

import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Window;

public class ViewManagerImpl implements ViewManager {

    private final Window mainWindow;

    private ComponentContainer welcomePage;

    private ComponentContainer mainView;

    private ComponentContainer resoucesView;

    private ComponentContainer reindexView;

    public void setReindexView(final ComponentContainer reindexView) {
        this.reindexView = reindexView;
    }

    public ViewManagerImpl(final Window mainWindow) {
        this.mainWindow = mainWindow;
    }

    public void setMainView(final ComponentContainer mainView) {
        this.mainView = mainView;
    }

    @Override
    public void showLandingView() {
        mainWindow.setContent(welcomePage);
    }

    @Override
    public void showMainView() {
        mainWindow.setContent(mainView);
    }

    @Override
    public void setLandingView(final ComponentContainer welcomePage) {
        this.welcomePage = welcomePage;
    }

    @Override
    public void setSecondComponent(final Component component) {
        ((MainView) mainView).setSecondComponent(component);
    }

    @Override
    public void showReindexView(final ComponentContainer reindexView) {
        setSecondComponent(reindexView);
    }
}