package de.escidoc.admintool.view.lab.orgunit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;

import de.escidoc.admintool.view.context.PublicStatus;

public class OrgUnitToolbarLab extends CustomComponent {

    private final Logger log = LoggerFactory.getLogger(OrgUnitToolbarLab.class);

    private static final long serialVersionUID = 6227479076006945485L;

    private final HorizontalLayout header = new HorizontalLayout();

    private final Button newOrgUnitButton = new Button("New",
        new NewOrgUnitListener());

    private final Button openOrgUnitButton = new Button("Open",
        new OpenOrgUnitListener());

    private final Button closeOrgUnitButton = new Button("Close",
        new CloseOrgUnitListener());

    private final Button deleteOrgUnitButton = new Button("Delete",
        new DeleteOrgUnitListener());

    public OrgUnitToolbarLab() {
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
        }
    }

    private class OpenOrgUnitListener implements Button.ClickListener {

        @Override
        public void buttonClick(final ClickEvent event) {
        }
    }

    private class CloseOrgUnitListener implements Button.ClickListener {

        @Override
        public void buttonClick(final ClickEvent event) {
        }
    }

    private class DeleteOrgUnitListener implements Button.ClickListener {

        @Override
        public void buttonClick(final ClickEvent event) {
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
