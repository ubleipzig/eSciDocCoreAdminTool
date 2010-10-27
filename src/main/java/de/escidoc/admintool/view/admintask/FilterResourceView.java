package de.escidoc.admintool.view.admintask;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.vaadin.ui.Button;
import com.vaadin.ui.Window;

import de.escidoc.admintool.service.AdminService;
import de.escidoc.admintool.view.ViewConstants;
import de.escidoc.core.resources.Resource;

public class FilterResourceView extends AbstractCustomView {

    private static final long serialVersionUID = -1412202753685048760L;

    static final Logger log = LoggerFactory.getLogger(FilterResourceView.class);

    private final Button filterResourceBtn = new Button(ViewConstants.RETRIEVE);

    final AdminService adminService;

    private final FilterResourceListener listener;

    final Window mainWindow;

    public FilterResourceView(final FilterResourceListener listener,
        final AdminService adminService, final Window mainWindow) {
        Preconditions.checkNotNull(listener, "Listener can not be null.");
        Preconditions.checkNotNull(adminService,
            "AdminService can not be null.");
        Preconditions.checkNotNull(mainWindow, "MainWindow can not be null.");

        this.listener = listener;
        this.adminService = adminService;
        this.mainWindow = mainWindow;
        init();
    }

    private void init() {
        addFilterButton();
    }

    private void addFilterButton() {
        getViewLayout().addComponent(filterResourceBtn);
        addListener();
    }

    interface ShowFilterResultCommand {
        void execute(Set<Resource> repoInfos);
    }

    private void addListener() {
        final ShowFilterResultCommand command =
            new ShowFilterResultCommandImpl(this);
        listener.setCommand(command);
        filterResourceBtn.addListener(listener);
    }
}
