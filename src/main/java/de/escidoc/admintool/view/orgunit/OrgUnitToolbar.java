package de.escidoc.admintool.view.orgunit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;

import de.escidoc.admintool.app.AdminToolApplication;
import de.escidoc.admintool.view.context.PublicStatus;

public class OrgUnitToolbar extends CustomComponent {
    private final Logger LOG = LoggerFactory.getLogger(OrgUnitToolbar.class);

    private static final long serialVersionUID = 6227479076006945485L;

    private final AdminToolApplication app;

    private final OrgUnitEditView orgUnitEditView;

    private final HorizontalLayout header = new HorizontalLayout();

    private final Button newOrgUnitButton = new Button("New",
        new NewOrgUnitListener());

    private final Button openOrgUnitButton = new Button("Open",
        new OpenOrgUnitListener());

    private final Button closeOrgUnitButton = new Button("Close",
        new CloseOrgUnitListener());

    private final Button deleteOrgUnitButton = new Button("Delete",
        new DeleteOrgUnitListener());

    public OrgUnitToolbar(final AdminToolApplication app,
        final OrgUnitEditView orgUnitEditView) {
        assert (app != null) : "AdminToolApplication can not be null.";
        assert (orgUnitEditView != null) : "orgUnitEditView can not be null.";
        this.app = app;
        this.orgUnitEditView = orgUnitEditView;
        init();
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

        @Override
        public void buttonClick(final ClickEvent event) {
            app.getOrgUnitView();
        }
    }

    private class OpenOrgUnitListener implements Button.ClickListener {

        @Override
        public void buttonClick(final ClickEvent event) {
            app.getMainWindow()
                .addWindow(
                    new OpenOrgUnitModalWindow(OrgUnitToolbar.this,
                        orgUnitEditView).getSubWindow());
        }
    }

    private class CloseOrgUnitListener implements Button.ClickListener {

        @Override
        public void buttonClick(final ClickEvent event) {
            app.getMainWindow().addWindow(
                new CloseOrgUnitModalWindow(OrgUnitToolbar.this,
                    orgUnitEditView).getSubWindow());
        }
    }

    private class DeleteOrgUnitListener implements Button.ClickListener {

        @Override
        public void buttonClick(final ClickEvent event) {
            orgUnitEditView.deleteOrgUnit();
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
