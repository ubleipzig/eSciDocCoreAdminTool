package de.escidoc.admintool.view.admintask;

import java.util.Collection;

import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Window;

import de.escidoc.admintool.service.AdminService;
import de.escidoc.admintool.view.ViewConstants;
import de.escidoc.core.resources.adm.LoadExamplesResult.Entry;

public class LoadExampleViewImpl extends CustomComponent
    implements LoadExampleView {

    private static final long serialVersionUID = -2478541354753165293L;

    private final Panel panel = new Panel();

    private final Button button = new Button(ViewConstants.LOAD_EXAMPLES);

    private OnLoadExampleClick listener;

    private final AdminService adminService;

    private final Window mainWindow;

    public LoadExampleViewImpl(final Window mainWindow,
        final AdminService adminService) {
        this.mainWindow = mainWindow;
        this.adminService = adminService;
        init();
    }

    private void init() {
        setCompositionRoot(panel);

        panel.addComponent(button);
        createLoadExampleButtonListener();
        button.addListener(listener);
    }

    interface ShowResultCommand {
        void execute(Collection<?> entry);
    }

    private void createLoadExampleButtonListener() {
        listener = new OnLoadExampleClick(new ShowResultCommand() {

            @Override
            public void execute(final Collection<?> entries) {
                for (final Object entry : entries) {
                    panel.addComponent(new Label(((Entry) entry).getObjid()));
                }
            }
        });
        listener.setAdminService(adminService);
        listener.setMainWindow(mainWindow);
    }
}