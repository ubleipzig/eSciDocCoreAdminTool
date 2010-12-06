package de.escidoc.admintool.view.lab.orgunit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Window;

import de.escidoc.admintool.view.ModalDialog;
import de.escidoc.core.client.exceptions.EscidocClientException;

public class DeleteOrgUnitListener implements ClickListener {

    private static final Logger LOG = LoggerFactory
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
        catch (final EscidocClientException e) {
            LOG.error("An unexpected error occured! See LOG for details.", e);
            ModalDialog.show(mainWindow, e);
        }
    }
}
