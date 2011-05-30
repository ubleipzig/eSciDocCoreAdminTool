package de.escidoc.admintool.view.admintask;

import com.google.common.base.Preconditions;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import de.escidoc.admintool.service.ServiceContainer;
import de.escidoc.admintool.view.ViewConstants;

public abstract class AbstractAdminTaskView extends CustomComponent {

    private static final long serialVersionUID = 2712816510873244813L;

    protected ServiceContainer services;

    protected final Window mainWindow;

    final VerticalLayout mainLayout = new VerticalLayout();

    final CssLayout cssLayout = new CssLayout();

    public AbstractAdminTaskView(final ServiceContainer services, final Window mainWindow) {

        Preconditions.checkNotNull(services, "services is null: %s", services);
        Preconditions.checkNotNull(mainWindow, "mainWindow is null: %s", mainWindow);

        this.services = services;
        this.mainWindow = mainWindow;

        init();
    }

    private void init() {
        setCompositionRoot(mainLayout);
        cssLayout.setHeight(100, UNITS_PERCENTAGE);
        cssLayout.setMargin(true);
        cssLayout.setWidth(ViewConstants._100_PERCENT);
        mainLayout.addComponent(cssLayout);

        final HorizontalLayout texts = new HorizontalLayout();
        texts.setSpacing(true);
        texts.setWidth(ViewConstants._100_PERCENT);
        texts.setMargin(false, false, true, false);
        cssLayout.addComponent(texts);
    }

    public abstract void addView();
}