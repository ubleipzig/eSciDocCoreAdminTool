package de.escidoc.admintool.view.orgunit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Button.ClickEvent;

import de.escidoc.admintool.app.AdminToolApplication;

public class OrgUnitToolbar extends CustomComponent {
    private final Logger log = LoggerFactory.getLogger(OrgUnitToolbar.class);

    private static final long serialVersionUID = 6227479076006945485L;

    private final AdminToolApplication app;

    private final OrgUnitEditView orgUnitEditView;

    private final HorizontalLayout header = new HorizontalLayout();

    private final Button newOrgUnitButton =
        new Button("New", new NewOrgUnitListener());

    private final Button openOrgUnitButton =
        new Button("Open", new OpenOrgUnitListener());

    private final Button closeOrgUnitButton =
        new Button("Close", new CloseOrgUnitListener());

    private final Button deleteOrgUnitButton =
        new Button("Delete", new DeleteOrgUnitListener());

    public OrgUnitToolbar(final AdminToolApplication app,
        final OrgUnitEditView orgUnitEditView) {
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
            app.getOrgUnitView().showOrganizationalUnitAddForm();
        }
    }

    private class OpenOrgUnitListener implements Button.ClickListener {

        @Override
        public void buttonClick(final ClickEvent event) {
            app.getMainWindow().addWindow(
                new OpenOrgUnitModalWindow(orgUnitEditView).getSubWindow());
        }
    }

    private class CloseOrgUnitListener implements Button.ClickListener {

        @Override
        public void buttonClick(final ClickEvent event) {
            app.getMainWindow().addWindow(
                new CloseOrgUnitModalWindow(orgUnitEditView).getSubWindow());
        }
    }

    private class DeleteOrgUnitListener implements Button.ClickListener {

        @Override
        public void buttonClick(final ClickEvent event) {
            orgUnitEditView.deleteOrgUnit();
        }
    }

}
