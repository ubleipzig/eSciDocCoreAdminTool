package de.escidoc.admintool.view.lab.orgunit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Window;

import de.escidoc.admintool.view.ErrorMessage;
import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;

public class DeleteOrgUnitListener implements ClickListener {

    private static final Logger log = LoggerFactory
        .getLogger(DeleteOrgUnitListener.class);

    private static final long serialVersionUID = -290305308440696149L;

    private final OrgUnitViewLab orgUnitViewLab;

    public DeleteOrgUnitListener(final OrgUnitViewLab orgUnitViewLab,
        final Window mainWindow) {
        super();
        this.orgUnitViewLab = orgUnitViewLab;
        this.mainWindow = mainWindow;
    }

    private final Window mainWindow;

    @Override
    public void buttonClick(final ClickEvent event) {
        try {
            orgUnitViewLab.deleteOrgUnit();
        }
        catch (final EscidocException e) {
            log.error("An unexpected error occured! See log for details.", e);
            ErrorMessage.show(mainWindow, e);
        }
        catch (final InternalClientException e) {
            log.error("An unexpected error occured! See log for details.", e);
            ErrorMessage.show(mainWindow, e);
        }
        catch (final TransportException e) {
            log.error("An unexpected error occured! See log for details.", e);
            ErrorMessage.show(mainWindow, e);
        }
    }
}
