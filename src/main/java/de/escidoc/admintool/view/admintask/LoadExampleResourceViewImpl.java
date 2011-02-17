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

    private final AddToContainer addExampleCommand;

    public LoadExampleResourceViewImpl(final Window mainWindow,
        final AdminService adminService, final AddToContainer addExampleCommand) {
        checkPreconditions(mainWindow, adminService, addExampleCommand);
        this.mainWindow = mainWindow;
        this.adminService = adminService;
        this.addExampleCommand = addExampleCommand;
        init();
    }

    private void checkPreconditions(
        final Window mainWindow, final AdminService adminService,
        final AddToContainer addExampleCommand) {
        Preconditions.checkNotNull(mainWindow,
            "mainWindow can not be null: %s", mainWindow);
        Preconditions.checkNotNull(adminService,
            "adminService can not be null: %s", adminService);
        Preconditions.checkNotNull(addExampleCommand,
            "addExampleCommand is null: %s", addExampleCommand);
    }

    private void init() {
        addLoadExampleButton();
        createLoadExampleButtonListener();
        addListener();
    }

    private void addLoadExampleButton() {
        button.setWidth(150, UNITS_PIXELS);
        getViewLayout().addComponent(button);
    }

    private void addListener() {
        button.addListener(listener);
    }

    interface ShowResultCommand {
        void execute(Collection<?> entry);
    }

    private void createLoadExampleButtonListener() {
        listener =
            new OnLoadExampleClick(new ShowResultCommandImpl(this,
                addExampleCommand));
        listener.setAdminService(adminService);
        listener.setMainWindow(mainWindow);
    }
}