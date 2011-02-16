package de.escidoc.admintool.view.admintask;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Window;

import de.escidoc.admintool.service.AdminService;
import de.escidoc.admintool.view.ModalDialog;
import de.escidoc.admintool.view.admintask.RepositoryInfoView.ShowRepoInfoCommand;
import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;

public class RepoInfoClickListener implements ClickListener {

    private static final long serialVersionUID = 3487193092482071418L;

    private static final Logger LOG = LoggerFactory
        .getLogger(RepoInfoClickListener.class);

    private final AdminService adminService;

    private final Window mainWindow;

    private ShowRepoInfoCommand command;

    public RepoInfoClickListener(final Window mainWindow,
        final AdminService adminService) {
        this.mainWindow = mainWindow;
        this.adminService = adminService;
    }

    @Override
    public void buttonClick(final ClickEvent event) {
        final Map<String, String> repositoriesInfo = retrieveRepoInfo();// retrieveRepoInfoMock();

        for (final Entry<String, String> entry : repositoriesInfo.entrySet()) {
            LOG.debug("key: " + entry.getKey() + "/value: " + entry.getValue());
        }

        command.execute(repositoriesInfo);
    }

    private Map<String, String> retrieveRepoInfoMock() {
        final Map<String, String> repositoriesInfo =
            new HashMap<String, String>();
        repositoriesInfo.put("organizational-unit",
            "http://www.escidoc.de/schemas/organizationalunit/0.8");
        repositoriesInfo.put("container",
            "http://www.escidoc.de/schemas/container/0.8");
        repositoriesInfo.put("context",
            "http://www.escidoc.de/schemas/context/0.7");

        return repositoriesInfo;
    }

    private Map<String, String> retrieveRepoInfo() {
        try {
            return adminService.getRepositoryInfo();
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
        return Collections.emptyMap();
    }

    public void setCommand(final ShowRepoInfoCommand command) {
        this.command = command;
    }
}
