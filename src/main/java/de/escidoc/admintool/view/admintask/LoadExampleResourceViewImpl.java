package de.escidoc.admintool.view.admintask;

import java.util.Collection;

import com.google.common.base.Preconditions;
import com.vaadin.ui.Button;
import com.vaadin.ui.Window;

import de.escidoc.admintool.service.AdminService;
import de.escidoc.admintool.view.ViewConstants;

public class LoadExampleResourceViewImpl extends AbstractCustomView
    implements LoadExampleView {

    private static final long serialVersionUID = -2478541354753165293L;

    private final Button button = new Button(ViewConstants.LOAD_EXAMPLES);

    private OnLoadExampleClick listener;

    private final AdminService adminService;

    private final Window mainWindow;

    public LoadExampleResourceViewImpl(final Window mainWindow,
        final AdminService adminService) {
        checkForNull(mainWindow, adminService);
        this.mainWindow = mainWindow;
        this.adminService = adminService;
        init();
    }

    private void checkForNull(
        final Window mainWindow, final AdminService adminService) {
        Preconditions.checkNotNull(mainWindow,
            "mainWindow can not be null: %s", mainWindow);
        Preconditions.checkNotNull(adminService,
            "adminService can not be null: %s", adminService);
    }

    private void init() {
        addLoadExampleButton();
        addListener();
    }

    private void addLoadExampleButton() {
        button.setWidth(150, UNITS_PIXELS);
        getViewLayout().addComponent(button);
    }

    private void addListener() {
        createLoadExampleButtonListener();
        button.addListener(listener);
    }

    interface ShowResultCommand {
        void execute(Collection<?> entry);
    }

    private void createLoadExampleButtonListener() {
        listener = new OnLoadExampleClick(new ShowResultCommandImpl(this));
        listener.setAdminService(adminService);
        listener.setMainWindow(mainWindow);
    }
}