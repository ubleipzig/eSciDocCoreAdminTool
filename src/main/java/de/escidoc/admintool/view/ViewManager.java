package de.escidoc.admintool.view;

import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;

public interface ViewManager {

    void showLandingView();

    void showMainView();

    void setMainView(ComponentContainer mainView);

    void setLandingView(ComponentContainer welcomePage);

    void setSecondComponent(Component component);

    void showView(Component filterResourceView);

}