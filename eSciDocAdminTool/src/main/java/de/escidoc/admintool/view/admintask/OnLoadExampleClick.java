package de.escidoc.admintool.view.admintask;

import java.util.List;

import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Window;

import de.escidoc.admintool.service.AdminService;
import de.escidoc.admintool.view.ModalDialog;
import de.escidoc.admintool.view.admintask.LoadExampleResourceViewImpl.ShowResultCommand;
import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;
import de.escidoc.core.resources.adm.LoadExamplesResult.Entry;

public class OnLoadExampleClick implements ClickListener {
    private static final long serialVersionUID = 3194220750169991701L;

    private AdminService adminService;

    private Window mainWindow;

    private final ShowResultCommand command;

    public OnLoadExampleClick(final ShowResultCommand command) {
        this.command = command;
    }

    @Override
    public void buttonClick(final ClickEvent event) {
        try {
            final List<Entry> loadedExamples = adminService.loadCommonExamples();
            command.execute(loadedExamples);
        }
        catch (final EscidocException e) {
            ModalDialog.show(mainWindow, e);
        }
        catch (final InternalClientException e) {
            ModalDialog.show(mainWindow, e);
        }
        catch (final TransportException e) {
            ModalDialog.show(mainWindow, e);
        }
    }

    public void setAdminService(final AdminService adminService) {
        this.adminService = adminService;
    }

    public void setMainWindow(final Window mainWindow) {
        this.mainWindow = mainWindow;
    }
}
