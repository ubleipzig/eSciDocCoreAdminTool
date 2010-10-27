package de.escidoc.admintool.view.admintask;

import com.google.common.base.Preconditions;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Window;

import de.escidoc.admintool.service.ServiceContainer;
import de.escidoc.admintool.view.ViewConstants;

public class AdminTaskViewImpl extends CustomComponent implements AdminTaskView {

    private static final long serialVersionUID = -8816703708910119233L;

    private final Panel panel = new Panel();

    private final Window mainWindow;

    private final ServiceContainer services;

    public AdminTaskViewImpl(final Window mainWindow,
        final ServiceContainer services) {
        Preconditions.checkNotNull(mainWindow, "mainWindow can not be null.");
        Preconditions.checkNotNull(services, "services can not be null.");
        this.mainWindow = mainWindow;
        this.services = services;
        init();
    }

    private void init() {
        initPanel();

        addLoadExampleView();
        addRepositoryInfoView();
        addFilterResourcesView();
    }

    private void initPanel() {
        setCompositionRoot(panel);
        setSizeFull();
        panel.setSizeFull();
        panel.setCaption(ViewConstants.ADMIN_TASK);
    }

    private void addLoadExampleView() {
        addLoadExampleButton();
    }

    private void addLoadExampleButton() {
        final Component loadExampleView =
            new LoadExampleViewImpl(mainWindow, services.getAdminService());
        panel.addComponent(loadExampleView);
    }

    private void addRepositoryInfoView() {
        final RepoInfoClickListener listener =
            new RepoInfoClickListener(mainWindow, services.getAdminService());
        final RepositoryInfoView repositoryInfoView =
            new RepositoryInfoView(listener);
        panel.addComponent(repositoryInfoView);
    }

    private void addFilterResourcesView() {
        final FilterResourceListener listener =
            new FilterResourceListener(mainWindow,
                services.getContainerService());
        final FilterResourceView filterResourceView =
            new FilterResourceView(listener, services.getAdminService(),
                mainWindow);
        panel.addComponent(filterResourceView);
    }
}