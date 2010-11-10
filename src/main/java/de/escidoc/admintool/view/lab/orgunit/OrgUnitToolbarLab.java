package de.escidoc.admintool.view.lab.orgunit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Window;

import de.escidoc.admintool.view.ErrorMessage;
import de.escidoc.admintool.view.ViewConstants;
import de.escidoc.admintool.view.context.PublicStatus;
import de.escidoc.core.client.exceptions.EscidocClientException;

public class OrgUnitToolbarLab extends CustomComponent {

    private final Logger log = LoggerFactory.getLogger(OrgUnitToolbarLab.class);

    private static final long serialVersionUID = 6227479076006945485L;

    private final HorizontalLayout header = new HorizontalLayout();

    private final Button newOrgUnitButton = new Button(ViewConstants.NEW,
        new NewOrgUnitListener());

    private final Button openOrgUnitButton = new Button(ViewConstants.OPEN,
        new OpenOrgUnitListener());

    private final Button closeOrgUnitButton = new Button(ViewConstants.CLOSE,
        new CloseOrgUnitListener());

    private final Button deleteOrgUnitButton = new Button(ViewConstants.DELETE,
        new DeleteOrgUnitListener());

    private final Window mainWindow;

    private OrgUnitViewLab orgUnitViewLab;

    public OrgUnitToolbarLab(final Window mainWindow) {
        this.mainWindow = mainWindow;
        init();
    }

    public void setOrgUnitView(final OrgUnitViewLab orgUnitViewLab) {
        this.orgUnitViewLab = orgUnitViewLab;
    }

    private void init() {
        header.setMargin(true);
        header.setSpacing(true);
        header.addComponent(newOrgUnitButton);
        header.addComponent(openOrgUnitButton);
        header.addComponent(closeOrgUnitButton);
        header.addComponent(deleteOrgUnitButton);
        header.setVisible(true);
        setCompositionRoot(header);
    }

    private class NewOrgUnitListener implements Button.ClickListener {

        private static final long serialVersionUID = -5497939033019573360L;

        @Override
        public void buttonClick(final ClickEvent event) {
            orgUnitViewLab.showAddView();
        }
    }

    private class OpenOrgUnitListener implements Button.ClickListener {

        private static final long serialVersionUID = -413173113376360998L;

        @Override
        public void buttonClick(final ClickEvent event) {
            mainWindow.addWindow(new OpenOrgUnitModalWindow(
                OrgUnitToolbarLab.this, orgUnitViewLab).getSubWindow());
        }
    }

    private class CloseOrgUnitListener implements Button.ClickListener {

        private static final long serialVersionUID = 4038394242582696771L;

        @Override
        public void buttonClick(final ClickEvent event) {
            mainWindow.addWindow(new CloseOrgUnitModalWindow(
                OrgUnitToolbarLab.this, orgUnitViewLab).getSubWindow());
        }
    }

    private class DeleteOrgUnitListener implements Button.ClickListener {

        private static final long serialVersionUID = -290305308440696149L;

        @Override
        public void buttonClick(final ClickEvent event) {
            try {
                orgUnitViewLab.deleteOrgUnit();
            }
            catch (final de.escidoc.core.client.exceptions.application.violated.OrganizationalUnitHasChildrenException e) {
                log
                    .debug(
                        "The Organizational Unit has children therefore can not be deleted.",
                        e);
                ErrorMessage.show(mainWindow,
                    "has children therefore can not be deleted.");
            }
            catch (final EscidocClientException e) {
                log.error("An unexpected error occured! See log for details.",
                    e);
                ErrorMessage.show(mainWindow, e);
            }
        }
    }

    public void changeState(final PublicStatus status) {
        switch (status) {
            case CREATED: {
                openOrgUnitButton.setVisible(true);
                closeOrgUnitButton.setVisible(false);
                deleteOrgUnitButton.setVisible(true);
                break;
            }
            case OPENED: {
                openOrgUnitButton.setVisible(false);
                closeOrgUnitButton.setVisible(true);
                deleteOrgUnitButton.setVisible(false);
                break;
            }
            case CLOSED: {
                openOrgUnitButton.setVisible(false);
                closeOrgUnitButton.setVisible(false);
                deleteOrgUnitButton.setVisible(false);
                break;
            }
        }
    }
}